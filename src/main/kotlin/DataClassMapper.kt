package com.github.jangalinski.kotlin

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

typealias Mapper<IN, OUT> = (IN) -> OUT
typealias SetMapper<IN, OUT> = (Set<IN>) -> Set<OUT>
typealias FieldMapping<T> = (T) -> Any?


class DataClassMapper<I : Any, O : Any>(private val inType: KClass<I>, private val outType: KClass<O>) : Mapper<I, O> {

  companion object {
    inline operator fun <reified I : Any, reified O : Any> invoke() = DataClassMapper(I::class, O::class)
  }

  private val outConstructor: KFunction<O> = outType.primaryConstructor!!
  private val inPropertiesByName = inType.memberProperties.associateBy { it.name }

  private fun argFor(parameter: KParameter, data: I): Any? = inPropertiesByName[parameter.name]?.get(data)

  override fun invoke(data: I): O = with(outConstructor) {
    callBy(parameters.associateWith { parameter -> argFor(parameter, data) })
  }

//
//  fun <T : Any, R : Any> createMapper(inClass: KClass<T>, outClass: KClass<R>, mapping: Map<String, FieldMapping<T>>): Mapper<T, R> {
//    val outConstructor = outClass.primaryConstructor!!
//    val inPropertiesByName = inClass.memberProperties.associateBy { it.name }
//
//    fun argFor(parameter: KParameter, data: T): Any? = mapping[parameter.name]
//      ?.invoke(data)
//      ?: inPropertiesByName[parameter.name]?.get(data)
//
//    fun transform(data: T): R = with(outConstructor) {
//      callBy(parameters.associateWith { parameter -> argFor(parameter, data) })
//    }
//
//    return { transform(it) }
//  }

  override fun toString(): String {
    return "DataClassMapper(inType=$inType, outType=$outType, outConstructor=$outConstructor, inPropertiesByName=$inPropertiesByName)"
  }


}
