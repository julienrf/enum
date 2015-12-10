package julienrf.enum

/**
  * {{{
  *   sealed trait Foo
  *   object Foo {
  *     case object Bar extends Foo
  *     case object Baz extends Foo
  *
  *     val enum: Enum[Foo] = Enum[Foo]
  *   }
  *
  *   assert(Foo.enum.values == Set(Foo.Bar, Foo.Baz))
  *   assert(Foo.enum.labels == Set("Bar", "Baz"))
  *   assert(Foo.enum.encode(Foo.Bar) == "Bar")
  *   assert(Foo.enum.decode("Baz") == Right(Foo.Baz))
  *   assert(Foo.enum.decode("invalid") == Left(DecodingFailure[Foo](Set("Bar", "Baz"))))
  * }}}
  *
  * @tparam A Type of the enumeration values
  */
trait Enum[A] {

  /** All the values of the enumeration */
  val values: Set[A]

  /** All the labels of the enumeration */
  val labels: Set[String]

  /** @return The label of the given value `a` */
  def encode(a: A): String

  /** @return The value corresponding to the given `label` */
  def decode(label: String): Either[DecodingFailure[A], A]

  /** Convenient shorthand for `decode` that returns an `Option[A]` */
  final def decodeOpt(s: String): Option[A] =
    decode(s).right.toOption

}

case class DecodingFailure[A](validValues: Set[String])

object Enum {

  @inline def apply[A](implicit codec: Enum[A]): Enum[A] = codec

  implicit def fromValuesAndEncoder[A](implicit _values: Values[A], encoder: Encoder[A]): Enum[A] =
    new Enum[A] {

      val values = _values.values

      val labels = values.map(encoder.encode)

      val decoder: Map[String, A] =
        values.map(value => (encoder.encode(value), value)).toMap

      def decode(s: String) =
        decoder.get(s).map(Right(_)).getOrElse(Left(DecodingFailure[A](labels)))

      def encode(a: A) =
        encoder.encode(a)
    }

}