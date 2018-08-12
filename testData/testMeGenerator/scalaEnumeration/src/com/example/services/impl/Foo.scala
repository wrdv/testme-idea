package com.example.services.impl

class Foo(weekDay: WeekDay){
  //  <caret>
  def beamMeUp(planet:Planet.Value,shoppingDay:WeekDay, optWeekDay:Option[WeekDay]): String ={
    "I'm traveling to "+planet+" on "+weekDay+ " do some shopping on " +shoppingDay+"and maybe will return on "+optWeekDay
  }
}