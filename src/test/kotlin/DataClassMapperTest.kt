package com.github.jangalinski.kotlin

import com.github.jangalinski.kotlin.DataClassMapper.Companion.setMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test


internal class DataClassMapperTest {
  data class FooIn(val name: String)
  data class BarIn(val name: String, val foos: Set<FooIn>)
  data class BazIn(val foo: FooIn)

  data class FooOut(val name: String)
  data class BarOut(val name: String, val foos: Set<FooOut>)
  data class BazOut(val foo: FooOut)


  val fooMapper = DataClassMapper<FooIn, FooOut>()
  val bazMapper = DataClassMapper<BazIn, BazOut>()
    .register("foo", fooMapper)
  val barMapper = DataClassMapper<BarIn,BarOut>()
    .register("foos", setMapper(fooMapper))

  @Test
  fun `simple mapping foo`() {
    assertThat(fooMapper(FooIn("kermit")))
      .isEqualTo(FooOut("kermit"))
  }

  @Test
  fun `transitive mapping baz(foo)`() {
    assertThat(bazMapper(BazIn(FooIn("piggy"))))
      .isEqualTo(BazOut(FooOut("piggy")))
  }

  @Test
  fun `set mapping bar`() {
    val inValue = BarIn("kermit", setOf(FooIn("piggy")))

    val outValue = barMapper(inValue)

    assertThat(outValue).isEqualTo(BarOut("kermit", setOf(FooOut("piggy"))))
  }
}
