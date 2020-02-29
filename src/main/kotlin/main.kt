package com.github.jangalinski.kotlin

fun main() {

  // Sample of usage mapList with targetParameterSupplier
  val listB = mutableListOf<B>()
  for (i in 0 until 10) {
    listB.add(B(1, 2, listOf(C("a"), C("b"), C("c"))))
  }

  val listA = listB.mapList<B, A>()

  print(listA)
}

data class A(
  val a: Int,
  val b: Int,
  val c: List<C>
)

data class B(
  val a: Int,
  val b: Int,
  val c: List<C>
)

data class C(
  val c1: String
)
