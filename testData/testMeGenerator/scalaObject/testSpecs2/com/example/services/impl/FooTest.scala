package com.example.services.impl

import org.specs2.mutable.Specification
import scala.concurrent.duration._
import scala.concurrent.Await

/** created by TestMe integration test on MMXVI */
class FooTest extends Specification {

  "Foo" should {

    "fight" in {
      val result = Foo.fight(Some("optFire"))
      Await.result(result, 10.seconds) === "replaceMeWithExpectedResult"
    }

    "fight With Map" in {
      val result = Foo.fightWithMap(Some("optFire"))
      Await.result(result, 10.seconds) === Some("Option")
    }

  }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme