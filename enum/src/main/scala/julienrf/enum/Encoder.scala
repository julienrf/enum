package julienrf.enum

import shapeless.labelled._
import shapeless.{:+:, Witness, CNil, LabelledGeneric, Coproduct}

import scala.annotation.implicitNotFound

/** A typeclass defining how to encode enumeration values as strings */
@implicitNotFound("Unable to find a way to encode ${A} values as strings. Make sure it is a sealed trait and is only extended by case objects.")
trait Encoder[A] {
  def encode(a: A): String
}

object Encoder {

  trait Derived[A] extends Encoder[A]

  /**
    * @return A generated encoder for `A` values
    */
  @inline def apply[A](implicit derived: Derived[A]): Encoder[A] = derived

  /**
    * @param gen Isomorphism between `A` and `Repr`. Compared to `Generic`, `LabelledGeneric` yields `Repr` types
    *            also containing names: `FieldType['Foo.type, Foo.type] :+: CNil`.
    */
  implicit def generic[A, Repr <: Coproduct](implicit gen: LabelledGeneric.Aux[A, Repr], e: EncoderAux[A, Repr]): Derived[A] =
    new Derived[A]{ def encode(a: A) = e.encode(a) }

  case class EncoderAux[A, Repr](encode: Map[A, String])

  implicit def cnil[A]: EncoderAux[A, CNil] = EncoderAux(Map.empty)

  implicit def ccons[A, K <: Symbol, L <: A, R <: Coproduct](implicit
    l: Witness.Aux[L],
    k: Witness.Aux[K],
    r: EncoderAux[A, R]
  ): EncoderAux[A, FieldType[K, L] :+: R] =
    EncoderAux[A, FieldType[K, L] :+: R](r.encode + (l.value -> k.value.name))

}
