package com.example.dependencies

import scala.concurrent.Future

/**
  * Date: 16/07/2018
  *
  * @author Yaron Yamin
  */
class TimeMachine {
  def lookInto():Future[Double]=Future.successful(12.343)
}
