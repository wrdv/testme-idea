package com.example.services.impl

import com.example.scala.{DayInAWeek, SolarSystemPlanets}

class Foo(fromPlanet:SolarSystemPlanets.Planet){
  //  <caret>
  def beamMeUp(toPlanet:SolarSystemPlanets.Planet, onDay:Option[DayInAWeek.EnumVal]): String ={
    s"preparing to beam you up from $fromPlanet to $toPlanet on $onDay"
  }
}