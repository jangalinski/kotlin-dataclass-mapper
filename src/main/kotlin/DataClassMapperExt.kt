package com.github.jangalinski.kotlin

import kotlin.reflect.KProperty1

inline fun <reified I : Any, reified O : Any> map(
  data: I,
  parameterSuppliers: Map<KProperty1<*, *>, TargetParameterSupplier<I, Any>>? = null,
  subMappers: Map<KProperty1<Any, Any>, Mapper<Any, Any>>? = null
): O = DataClassMapper<I, O>()
  .applySubMappers(subMappers)
  .applyParameterSuppliers(parameterSuppliers)
  .invoke(data)


inline fun <reified I : Any, reified O : Any> Set<I>.mapSet(
  parameterSuppliers: Map<KProperty1<*, *>, TargetParameterSupplier<I, Any>>? = null,
  subMappers: Map<KProperty1<Any, Any>, Mapper<Any, Any>>? = null
): Set<O> = DataClassMapper.setMapper<I, O> {
  DataClassMapper<I, O>()
    .applySubMappers(subMappers)
    .applyParameterSuppliers(parameterSuppliers)
    .invoke(it)
}.invoke(this)

inline fun <reified I : Any, reified O : Any> List<I>.mapList(
  parameterSuppliers: Map<KProperty1<*, *>, TargetParameterSupplier<I, Any>>? = null,
  subMappers: Map<KProperty1<Any, Any>, Mapper<Any, Any>>? = null
): List<O> = DataClassMapper.listMapper<I, O> {
  DataClassMapper<I, O>()
    .applySubMappers(subMappers)
    .applyParameterSuppliers(parameterSuppliers)
    .invoke(it)
}.invoke(this)

@PublishedApi
internal inline fun <reified I : Any, reified O : Any> DataClassMapper<I, O>.applySubMappers(
  subMappers: Map<KProperty1<Any, Any>, Mapper<Any, Any>>?
): DataClassMapper<I, O> {
  subMappers?.let { it ->
    it.forEach { entry ->
      register(entry.key, entry.value)
    }
  }
  return this
}

@PublishedApi
internal inline fun <reified I : Any, reified O : Any> DataClassMapper<I, O>.applyParameterSuppliers(
  parameterSuppliers: Map<KProperty1<*, *>, TargetParameterSupplier<I, Any>>?
): DataClassMapper<I, O> {
  parameterSuppliers?.let { it ->
    it.forEach { entry ->
      targetParameterSupplier(entry.key, entry.value)
    }
  }
  return this
}
