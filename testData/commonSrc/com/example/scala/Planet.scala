package com.example.scala

/**
  * Created with IntelliJ IDEA.
  * User: yaron.yamin
  * Date: 8/9/2018
  * Time: 4:26 PM
  */
//Reference: https://www.scala-lang.org/api/2.12.6/scala/Enumeration.html
object Planet extends Enumeration {
  protected case class Val(mass: Double, radius: Double) extends super.Val {
    def surfaceGravity: Double = Planet.G * mass / (radius * radius)
    def surfaceWeight(otherMass: Double): Double = otherMass * surfaceGravity
  }
  implicit def valueToPlanetVal(x: Value): Val = x.asInstanceOf[Val]

  val G: Double = 6.67300E-11
  val Mercury = Val(3.303e+23, 2.4397e6)
  val Venus   = Val(4.869e+24, 6.0518e6)
  val Earth   = Val(5.976e+24, 6.37814e6)
  val Mars    = Val(6.421e+23, 3.3972e6)
  val Jupiter = Val(1.9e+27, 7.1492e7)
  val Saturn  = Val(5.688e+26, 6.0268e7)
  val Uranus  = Val(8.686e+25, 2.5559e7)
  val Neptune = Val(1.024e+26, 2.4746e7)
}