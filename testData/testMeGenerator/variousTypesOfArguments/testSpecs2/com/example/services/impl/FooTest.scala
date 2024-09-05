package com.example.services.impl

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito

/** created by TestMe integration test on MMXVI */
class FooTest extends Specification with Mockito {
  isolated
  val fooFighter: com.example.warriers.FooFighter = mock[com.example.warriers.FooFighter]
  val innerStaticClass: com.example.services.impl.Foo.InnerStaticClass = mock[com.example.services.impl.Foo.InnerStaticClass]
  val foo = new Foo(fooFighter, 64.toByte, 1, 12345l, 1.1f, 100d, true, 'c', 21.toShort, innerStaticClass)

  "Foo" should {

    "various Args" in {
      fooFighter.fight(any) returns "fightResponse"
      val result = foo.variousArgs(new com.example.foes.Fire(), "foeName", 64.toByte, 1, 12345l, 1.1f, 100d, true, 'c', 21.toShort, BigDecimal(2.44).bigDecimal, BigInt(123123).bigInteger, new java.util.GregorianCalendar(2016, java.util.Calendar.JANUARY, 11, 22, 45).getTime, java.time.LocalDateTime.of(2016, java.time.Month.JANUARY, 11, 22, 45, 55).toInstant(java.time.ZoneOffset.UTC))
      result === "replaceMeWithExpectedResult"
    }

  }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme