package com.example.services.impl

/** created by TestMe integration test on MMXVI */
class FooTest extends Specification {
  val foo = new Foo(com.example.scala.SolarSystemPlanets.MERCURY)

  "Foo" should {

    "beam Me Up" in {
      val result = foo.beamMeUp(com.example.scala.SolarSystemPlanets.MERCURY, Some(com.example.scala.DayInAWeek.Mon))
      result === "replaceMeWithExpectedResult"
    }

  }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme