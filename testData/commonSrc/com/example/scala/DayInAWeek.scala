package com.example.scala

/**
  * Date: 17/08/2018
  *
  * @author Yaron Yamin
  */
object DayInAWeek {
  sealed trait EnumVal
  case object Mon extends EnumVal
  case object Tue extends EnumVal
  case object Wed extends EnumVal
  case object Thu extends EnumVal
  case object Fri extends EnumVal
  val daysOfWeek = Seq(Mon, Tue, Wed, Thu, Fri)
}