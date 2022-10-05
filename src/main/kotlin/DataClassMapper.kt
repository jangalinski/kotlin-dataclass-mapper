package com.github.jangalinski.kotlin

import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

typealias Mapper<I, O> = (I) -> O
typealias targetParameterSupplier<I,O> = (I) -> O

/**
 * Mapper that can convert one data class into another data class.
 *
 * @param <I> inType (convert from)
 * @param <O> outType (convert to)
 */
class DataClassMapper<I : Any, O : Any>(private val inType: KClass<I>, private val outType: KClass<O>) : Mapper<I, O> {

  companion object {
    inline operator fun <reified I : Any, reified O : Any> invoke() = DataClassMapper(I::class, O::class)

    fun <I : Any, O : Any> setMapper(mapper: Mapper<I, O>) = object : Mapper<Set<I>, Set<O>> {
      override fun invoke(data: Set<I>): Set<O> = data.map(mapper).toSet()
    }
  }

  val fieldMappers = mutableMapOf<String, Mapper<Any, Any>>()
  private val targetParameterProviders = mutableMapOf<String, targetParameterSupplier<Any,Any>>()

  private val outConstructor = outType.primaryConstructor!!
  private val inPropertiesByName by lazy { inType.memberProperties.associateBy { it.name } }

  private fun argFor(parameter: KParameter, data: I): Any? {
    // get value from input data or apply a default value to the target class
    val value = inPropertiesByName[parameter.name]?.get(data) ?: return targetParameterProviders[parameter.name]?.invoke(data)

    // if a special mapper is registered, use it, otherwise keep value
    return fieldMappers[parameter.name]?.invoke(value) ?: value
  }

  inline fun <reified S : Any, reified T : Any> register(parameterName: String, crossinline mapper: Mapper<S, T>): DataClassMapper<I, O> = apply {
    this.fieldMappers[parameterName] = object : Mapper<Any, Any> {
      override fun invoke(data: Any): Any = mapper.invoke(data as S)
    }
  }

  inline fun <reified C : Any, reified S : Any, reified T : Any> register(property: KProperty1<C, S>, crossinline mapper: Mapper<S, T>): DataClassMapper<I, O> = apply {
    this.fieldMappers[property.name] = object : Mapper<Any, Any> {
      override fun invoke(data: Any): Any = mapper.invoke(data as S)
    }
  }

  fun <T : Any> targetParameterSupplier(parameterName: String, mapper: targetParameterSupplier<I,T>): DataClassMapper<I, O> = apply {
    this.targetParameterProviders[parameterName] = object : targetParameterSupplier<Any,Any> {
      override fun invoke(i: Any): Any = mapper.invoke(i as I)
    }
  }

  fun <S : Any, T : Any> targetParameterSupplier(property: KProperty1<S, Any?>, mapper: targetParameterSupplier<I,T>): DataClassMapper<I, O> = apply {
    this.targetParameterProviders[property.name] = object : targetParameterSupplier<Any,Any> {
      override fun invoke(i: Any): Any = mapper.invoke(i as I)
    }
  }

  override fun invoke(data: I): O = with(outConstructor) {
    callBy(parameters.associateWith { argFor(it, data) })
  }

  override fun toString(): String = "DataClassMapper($inType -> $outType)"

}
