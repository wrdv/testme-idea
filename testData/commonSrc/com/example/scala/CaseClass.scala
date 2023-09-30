package com.example.scala

case class CaseClass(i:Int, made:Boolean, my:Double, `case`:String, myName: Option[String], aNestedClass: Option[Nested] = None){
  def this(str:String)= this(1,true,2.23,str)
}

case class Nested(arg:Option[String])