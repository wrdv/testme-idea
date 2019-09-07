package com.example.services.impl

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import com.example.warriers.FooFighter

import scala.concurrent.duration._
import scala.concurrent.Await

/** created by TestMe integration test on MMXVI */
class FooTest extends Specification with Mockito {
  isolated
  val fooFighter: FooFighter = mock[FooFighter]
  val foo = new Foo(fooFighter)

  "Foo" should {

    "fight" in {
      fooFighter.fight(any) returns "fightResponse"
      val result = foo.fight(Some("optFire"))
      Await.result(result, 10.seconds) === "replaceMeWithExpectedResult"
    }

    "fight With Map" in {
      fooFighter.fight(any) returns "fightResponse"
      val result = foo.fightWithMap(Some("optFire"))
      Await.result(result, 10.seconds) === Some("Option")
    }

  }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme