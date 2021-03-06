package org.ensime.jerk

import java.io.File
import scala.util._

import spray.json._
import org.ensime.json._
import shapeless._

import org.ensime.api._

import pimpathon.file._

private object JerkConversions extends DefaultJsonProtocol with FamilyFormats {
  // wtf?? why is this needed, why does it even work? Miles??
  implicit val symbolFormat = SymbolJsonFormat

  implicit object FileFormat extends JsonFormat[File] {
    def read(j: JsValue): File = j match {
      case JsString(path) => file(path)
      case other => unexpectedJson[File](other)
    }
    def write(f: File): JsValue = JsString(f.getPath)
  }

  // keeps the JSON a little bit cleaner
  implicit object DebugThreadIdFormat extends JsonFormat[DebugThreadId] {
    def read(j: JsValue): DebugThreadId = j match {
      case JsNumber(id) => new DebugThreadId(id.longValue)
      case other => unexpectedJson[DebugThreadId](other)
    }
    def write(dtid: DebugThreadId): JsValue = JsNumber(dtid.id)
  }

  // some of the case classes use the keyword `type`, so we need a better default
  override implicit def coproductHint[T: Typeable]: CoproductHint[T] = new FlatCoproductHint[T]("typehint")

  val RpcRequestEnvelopeFormat = JsonFormat[RpcRequestEnvelope]
  val EnsimeServerMessageFormat = JsonFormat[EnsimeServerMessage]
}

object JerkFormats {
  implicit val RpcRequestEnvelopeFormat = JerkConversions.RpcRequestEnvelopeFormat
  implicit val EnsimeServerMessageFormat = JerkConversions.EnsimeServerMessageFormat
}
