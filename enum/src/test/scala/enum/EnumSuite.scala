package enum

import org.scalatest.FunSuite

class EnumSuite extends FunSuite {
  import EnumSuite.{Foo, TwoLevels}

  test("Enum[Foo]") {
    val enum = Enum[Foo]
    assert(enum.values == Set(Foo.Bar, Foo.Baz))
    assert(enum.labels == Set("Bar", "Baz"))
    assert(enum.encode(Foo.Bar) == "Bar")
    assert(enum.decode("Baz") == Right(Foo.Baz))
    assert(enum.decode("invalid") == Left(DecodingFailure[Foo](Set("Bar", "Baz"))))
    assert(enum.decodeOpt("Bar") == Some(Foo.Bar))
    assert(enum.decodeOpt("invalid") == None)
  }

  test("Enum[TwoLevels]") {
    assert(Enum[TwoLevels].labels == Set("FirstValue", "SecondValue", "ThirdValue"))
  }

}

object EnumSuite {
  sealed trait Foo
  object Foo {
    case object Bar extends Foo
    case object Baz extends Foo

    implicit val enum: Enum[Foo] = Enum.derived
  }

  sealed trait TwoLevels
  case object FirstValue extends TwoLevels
  sealed trait SecondLevel extends TwoLevels
  case object SecondValue extends SecondLevel
  case object ThirdValue extends SecondLevel
  implicit val twoLevelsEnum: Enum[TwoLevels] = Enum.derived
}