package enum

import shapeless.{:+:, Witness, CNil, Generic, Coproduct}

import scala.annotation.implicitNotFound

/** A typeclass giving the values of an enumeration */
@implicitNotFound("Unable to find values of ${A}. Make sure it is a sealed trait and is only extended by case objects.")
trait Values[A] {
  /** The values of the `A` enumeration */
  val values: Set[A]
}

 /*
  * The companion object contains the machinery to automatically derive the values of a sealed trait
  * extended by case objects only.
  *
  * Basically, the derivation process is the following:
  *  - we are given a sort of list containing the types of the case objects that extend a sealed trait `A` ;
  *  - we inductively traverse this structure using two implicit definitions: one for the empty list case and
  *    one for the “cons” case ;
  *  - we accumulate the traversed case objects in a `List` ;
  *  - that’s it: the resulting `List` contains all the possible values of type `A`.
  *
  * The first step is the hard part of this process and is actually achieved by shapeless (using macros).
  */
object Values {

  @inline def apply[A](implicit values: Values[A]): Values[A] = values

  trait Derived[A] extends Values[A]

  /**
    * {{{
    *   sealed trait Foo
    *   object Foo {
    *     case object Bar extends Foo
    *     case object Baz extends Foo
    *   }
    *   Values.derived[Foo].values == Set(Foo.Bar, Foo.Baz)
    * }}}
    *
    * @return All the possible values of `A`
    */
  @inline implicit def derived[A](implicit derived: Derived[A]): Values[A] = derived

  object Derived {
     /*
      * Derives a `Values[A]` instance given a representation `Repr` of type `A` in terms of `Coproduct`, and a given
      * a `ValuesAux[A, Repr]` instance.
      *
      * @tparam Repr Type of the representation of `A` as a `Coproduct`.
      *              A `Coproduct` is recursively defined as either `CNil` or `H :+: T`, where `T` is a
      *              subtype of `Coproduct`.
      *              For instance, a sealed trait that is extended by only one case object `Foo` can have the following
      *              representation: `Foo.type :+: CNil`.
      * @param gen Isomorphism between `A` and `Repr`. Shapeless is able to provide such an implicit value for any
      *            sealed type
      */
    implicit def generic[A, Repr <: Coproduct](implicit gen: Generic.Aux[A, Repr], v: ValuesAux[A, Repr]): Derived[A] =
      new Derived[A] {
        val values = v.values.toSet
      }

     /*
      * An intermediate data structure that carries both the type `A` and its representation `Repr`.
      *
      * @tparam Repr Phantom type describing the structure of `A`
      */
    case class ValuesAux[A, Repr](values: List[A])

     /*
      * To sum up: we are given the types of the possible `A` values in a `Coproduct` structure and we want to
      * define how to traverse this structure to accumulate all the values in a `List`.
      */
    object ValuesAux {

       /*
        * Base case: no values
        */
      implicit def cnil[A]: ValuesAux[A, CNil] = ValuesAux[A, CNil](Nil)

       /*
        * Induction case: append a value of type `L` to the `R` previous values
        * @param l Singleton of type `L`
        */
      implicit def ccons[A, L <: A, R <: Coproduct](implicit l: Witness.Aux[L], r: ValuesAux[A, R]): ValuesAux[A, L :+: R] =
        ValuesAux[A, L :+: R](l.value :: r.values)

    }

  }

}
