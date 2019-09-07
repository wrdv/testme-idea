package com.example.services.impl

import org.specs2.mutable.Specification
import scala.concurrent.duration._
import scala.concurrent.Await

/** created by TestMe integration test on MMXVI */
class FooTest extends Specification {
  val foo = new Foo()

  "Foo" should {

    "find Me A Better Future" in {
      val result = foo.findMeABetterFuture(Some("hopes"))
      Await.result(result, 10.seconds) === "replaceMeWithExpectedResult"
    }

    "look Into The Future" in {
      val result = foo.lookIntoTheFuture(scala.concurrent.Future.successful(1))
      Await.result(result, 10.seconds) === 1.1f
    }

  }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme