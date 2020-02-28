package com.github.jangalinski.kotlin

inline fun <reified I : Any, reified O : Any> map(
  data: I,
  parameterSuppliers: Map<String, targetParameterSupplier<I, Any>>? = null
): O = applyParameterSuppliers<I, O>(parameterSuppliers).invoke(data)


inline fun <reified I : Any, reified O : Any> Set<I>.mapSet(
  parameterSuppliers: Map<String, targetParameterSupplier<I, Any>>? = null
): Set<O> = DataClassMapper.setMapper<I, O> {
  applyParameterSuppliers<I, O>(parameterSuppliers).invoke(it)
}.invoke(this)

inline fun <reified I : Any, reified O : Any> List<I>.mapList(
  parameterSuppliers: Map<String, targetParameterSupplier<I, Any>>? = null
): List<O> = DataClassMapper.listMapper<I, O> {
  applyParameterSuppliers<I, O>(parameterSuppliers).invoke(it)
}.invoke(this)

@PublishedApi
internal inline fun <reified I : Any, reified O : Any> applyParameterSuppliers(
  parameterSuppliers: Map<String, targetParameterSupplier<I, Any>>?
): DataClassMapper<I, O> {
  val mapper = DataClassMapper<I, O>()
  parameterSuppliers?.let { it ->
    it.forEach { entry ->
      mapper.targetParameterSupplier(entry.key, entry.value)
    }
  }
  return mapper
}
