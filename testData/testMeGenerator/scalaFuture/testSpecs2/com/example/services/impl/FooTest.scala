package com.example.services.impl

import org.specs2.mutable.Specification
import scala.concurrent.duration._
import scala.concurrent.Await

/** created by TestMe integration test on MMXVI */
class FooTest extends Specification {
  val foo = new Foo()

  "Foo" should {

    "look Into The Future" in {
      val result = foo.lookIntoTheFuture(scala.concurrent.Future.successful(1))
      Await.result(result, 10.seconds) === 1.1f
    }

    "find Me A Better Future" in {
      val result = foo.findMeABetterFuture(Some("hopes"))
      Await.result(result, 10.seconds) === "replaceMeWithExpectedResult"
    }

  }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme