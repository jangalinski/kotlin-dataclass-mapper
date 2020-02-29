package com.github.jangalinski.kotlin

import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

typealias Mapper<I, O> = (I) -> O
typealias TargetParameterSupplier<T, O> = (T) -> O?

/**
 * Mapper that can convert one data class into another data class.
 *
 * @param <I> inType (convert from)
 * @param <O> outType (convert to)
 */
class DataClassMapper<I : Any, O : Any>(
  private val inType: KClass<I>,
  private val outType: KClass<O>
) : Mapper<I, O> {

  val fieldMappers = mutableMapOf<String, Mapper<Any, Any>>()
  val targetParameterProviders = mutableMapOf<String, TargetParameterSupplier<I, Any>>()

  private val outConstructor = outType.primaryConstructor!!
  private val inPropertiesByName by lazy { inType.memberProperties.associateBy { it.name } }

  private fun argFor(parameter: KParameter, data: I): Any? {
    // get value from input data or apply a default value to the target class
    val value = inPropertiesByName[parameter.name]?.get(data)
      ?: return targetParameterProviders[parameter.name]?.invoke(data)

    // if a special mapper is registered, use it, otherwise keep value
    return fieldMappers[parameter.name]?.invoke(value) ?: value
  }

  inline fun <reified C : Any, reified S : Any, reified T : Any> register(
    property: KProperty1<C, S>,
    crossinline mapper: Mapper<S, T>
  ): DataClassMapper<I, O> = apply {
    this.fieldMappers[property.name] = object : Mapper<Any, Any> {
      override fun invoke(data: Any): Any = mapper.invoke(data as S)
    }
  }

  inline fun <T : Any> targetParameterSupplier(
    property: KProperty1<*, Any?>,
    crossinline mapper: TargetParameterSupplier<I, T>
  ): DataClassMapper<I, O> = apply {
    this.targetParameterProviders[property.name] = object : TargetParameterSupplier<I, Any> {
      override fun invoke(inType: I): Any? = mapper.invoke(inType)
    }
  }

  override fun invoke(data: I): O = with(outConstructor) {
    callBy(parameters.associateWith { argFor(it, data) })
  }

  override fun toString(): String = "DataClassMapper($inType -> $outType)"

  companion object {
    inline operator fun <reified I : Any, reified O : Any> invoke() =
      DataClassMapper(I::class, O::class)

    fun <I : Any, O : Any> setMapper(mapper: Mapper<I, O>) = object : Mapper<Set<I>, Set<O>> {
      override fun invoke(data: Set<I>): Set<O> = data.map(mapper).toSet()
    }

    fun <I : Any, O : Any> listMapper(mapper: Mapper<I, O>) = object : Mapper<List<I>, List<O>> {
      override fun invoke(data: List<I>): List<O> = data.map(mapper)
    }
  }
}
