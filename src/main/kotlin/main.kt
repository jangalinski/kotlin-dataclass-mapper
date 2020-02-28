package com.github.jangalinski.kotlin

fun main() {

  // Sample of usage mapList with targetParameterSupplier
  val listB = mutableListOf<B>()
  for (i in 0 until 10) {
    listB.add(B(1, 2, C("a", "b")))
  }

  val listA = listB.mapList<B, A>(mapOf(
    "c1" to object : targetParameterSupplier<B, String> {
      override fun invoke(inType: B): String? = inType.c?.c1
    },
    "c2" to object : targetParameterSupplier<B, String> {
      override fun invoke(inType: B): String? = inType.c?.c2
    }
  ))

  print(listA)
}

data class A(
  val a: Int,
  val b: Int,
  val c1: String? = null,
  val c2: String? = null
)

data class B(
  val a: Int,
  val b: Int,
  val c: C? = null
)

data class C(
  val c1: String,
  val c2: String
)
