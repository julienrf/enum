package julienrf.enum

import org.scalatest.FunSuite

class EnumSuite extends FunSuite {
  import EnumSuite.Foo

  test("Enum[Foo]") {
    assert(Foo.enum.values == Set(Foo.Bar, Foo.Baz))
    assert(Foo.enum.labels == Set("Bar", "Baz"))
    assert(Foo.enum.encode(Foo.Bar) == "Bar")
    assert(Foo.enum.decode("Baz") == Right(Foo.Baz))
    assert(Foo.enum.decode("invalid") == Left(DecodingFailure[Foo](Set("Bar", "Baz"))))
    assert(Foo.enum.decodeOpt("Bar") == Some(Foo.Bar))
    assert(Foo.enum.decodeOpt("invalid") == None)
  }

}

object EnumSuite {
  sealed trait Foo
  object Foo {
    case object Bar extends Foo
    case object Baz extends Foo

    val enum: Enum[Foo] = Enum[Foo]
  }
}