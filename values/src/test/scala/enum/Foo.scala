package enum

sealed trait Foo

object Foo {
  case object Bar extends Foo
  case object Baz extends Foo

  implicit val enum: Values[Foo] = Values.derived
}