package com.example.services.impl

import com.example.foes.Fire
import com.example.services.impl.Foo.InnerStaticClass
import com.example.warriers.FooFighter
import org.specs2.mutable.Specification
import org.specs2.mock.Mockito

/** created by TestMe integration test on MMXVI */
class FooTest extends Specification with Mockito {
  isolated
  val fooFighter: FooFighter = mock[FooFighter]
  val innerStaticClass: InnerStaticClass = mock[InnerStaticClass]
  val foo = new Foo(fooFighter, 64.toByte, 1, 12345l, 1.1f, 100d, true, 'c', 21.toShort, innerStaticClass)

  "Foo" should {

    "various Args" in {
      val result = foo.variousArgs(new Fire(), "foeName", 64.toByte, 1, 12345l, 1.1f, 100d, true, 'c', 21.toShort, BigDecimal(2.44).bigDecimal, BigInt(123123).bigInteger, new java.util.GregorianCalendar(2016, java.util.Calendar.JANUARY, 11, 22, 45).getTime, java.time.LocalDateTime.of(2016, java.time.Month.JANUARY, 11, 22, 45, 55).toInstant(java.time.ZoneOffset.UTC))
      result === "replaceMeWithExpectedResult"
    }

  }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme