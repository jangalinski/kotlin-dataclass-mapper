package com.github.jangalinski.kotlin

fun main() {

  // Sample of usage mapList with targetParameterSupplier
  val listB = mutableListOf<B>()
  for (i in 0 until 10) {
    listB.add(B(1, 2, C("a", "b")))
  }

  val listA = listB.mapList<B, A>(mapOf(
    A::c1 to object : targetParameterSupplier<B, String> {
      override fun invoke(p1: B): String? = p1.c?.c1
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
