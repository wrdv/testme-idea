package com.example.common

import java.util.logging.Logger

/**
  * Date: 13/07/2018
  *
  * @author Yaron Yamin
  */
trait LogSupport {
  lazy val logger: Logger =  Logger.getLogger(this.getClass.getCanonicalName)
}
