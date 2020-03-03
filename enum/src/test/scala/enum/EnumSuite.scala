package enum

import org.scalatest.funsuite.AnyFunSuite

class EnumSuite extends AnyFunSuite {
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