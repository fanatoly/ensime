package org.ensime.json

import org.scalatest._
import spray.json._

trait SprayJsonTestSupport {
  this: Matchers =>

  def roundtrip[T: JsonFormat](value: T, via: Option[String] = None): Unit = {
    val json = value.toJson

    via match {
      case None =>
        println(s"check and add the following assertion: $value = ${json.prettyPrint}")
      case Some(expected) => json shouldBe expected.parseJson
    }

    val recovered = json.convertTo[T]
    recovered shouldBe value
  }

}
