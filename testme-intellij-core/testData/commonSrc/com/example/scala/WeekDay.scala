package com.example.scala

/**
  * Created with IntelliJ IDEA.
  * User: yaron.yamin
  * Date: 8/9/2018
  * Time: 4:24 PM
  */
//Reference: https://www.scala-lang.org/api/2.12.6/scala/Enumeration.html
object WeekDay extends Enumeration {
  type WeekDay = Value
  val Mon, Tue, Wed, Thu, Fri, Sat, Sun = Value
}
