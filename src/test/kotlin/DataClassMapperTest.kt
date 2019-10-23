package com.github.jangalinski.kotlin

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test


internal class DataClassMapperTest {
  object In {
    data class Foo(val name: String)
    data class Bar(val name: String, val foos:Set<Foo>)
  }

  object Out {
    data class Foo(val name: String)
    data class Bar(val name: String, val foos: Set<Foo>)
  }

  @Test
  fun `map foo`() {
    val mapper = DataClassMapper<In.Foo, Out.Foo> ()

    val inFoo = In.Foo("kermit")
    val outFoo = Out.Foo("kermit")

    assertThat(mapper(inFoo)).isEqualTo(outFoo)
  }
}
//
//package de.eosts.fx.kermit.forderungsdaten.domain.mapper
//
//import kotlin.reflect.KClass
//import kotlin.reflect.KParameter
//import kotlin.reflect.full.memberProperties
//import kotlin.reflect.full.primaryConstructor
//
//
//typealias Mapper<T, R> = (T) -> R
//typealias SetMapper<T, R> = (Set<T>) -> Set<R>
//typealias FieldMapping<T> = (T) -> Any?
//
//object DataClassMapper {
//  inline fun <reified T : Any, reified R : Any> single(mapping: Map<String, FieldMapping<T>> = mapOf()): Mapper<T, R> = createMapper(T::class, R::class, mapping)
//
//  inline fun <reified T : Any, reified R : Any> set(mapping: Map<String, FieldMapping<T>> = mapOf()): SetMapper<T, R> = set(single(mapping))
//  inline fun <reified T : Any, reified R : Any> set(crossinline single: Mapper<T, R>): SetMapper<T, R> = { it.map(single).toSet() }
//
//}
//
//data class X(val n:String) {
//  companion object {
//    inline fun xx() = ""
//  }
//}
//
//fun <T : Any, R : Any> createMapper(inClass: KClass<T>, outClass: KClass<R>, mapping: Map<String, FieldMapping<T>>): Mapper<T, R> {
//  val outConstructor = outClass.primaryConstructor!!
//  val inPropertiesByName = inClass.memberProperties.associateBy { it.name }
//
//  fun argFor(parameter: KParameter, data: T): Any? = mapping[parameter.name]
//    ?.invoke(data)
//    ?: inPropertiesByName[parameter.name]?.get(data)
//
//  fun transform(data: T): R = with(outConstructor) {
//    callBy(parameters.associateWith { parameter -> argFor(parameter, data) })
//  }
//
//  return { transform(it) }
//}
//
//
//
//package de.eosts.fx.kermit.forderungsdaten.domain.mapper
//
//import org.assertj.core.api.Assertions.assertThat
//import org.junit.Test
//
//class DataClassMapperTest {
//
//  object In {
//    data class Foo(val name: String)
//    data class Bar(val name: String, val foos:Set<Foo>)
//  }
//
//  object Out {
//    data class Foo(val name: String)
//    data class Bar(val name: String, val foos: Set<Foo>)
//  }
//
//  val fooMapper = DataClassMapper.single<In.Foo, Out.Foo>()
//  val fooSetMapper = DataClassMapper.set(fooMapper)
//
//
//
//
//  @Test
//  fun `can map simple foo`() {
//    assertThat(fooMapper(In.Foo("kermit"))).isEqualTo(Out.Foo("kermit"))
//  }
//
//  @Test
//  fun `can map set of foos`() {
//    assertThat(fooSetMapper(setOf(In.Foo("piggy")))).isEqualTo(setOf(Out.Foo("piggy")))
//  }
//
//  @Test
//  fun `can map bar with set of foos`() {
//
//    val barMapper = DataClassMapper.single<In.Bar,Out.Bar>(mapOf(
//
//    ))
//  }
//}
