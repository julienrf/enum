# Enum

Yet another enumeration toolbox for Scala, powered by [shapeless](https://github.com/milessabin/shapeless).

## Installation

Several artifacts are available:
 - `enum-values` [![Maven Central](https://img.shields.io/maven-central/v/org.julienrf/enum-values_2.11.svg)](https://maven-badges.herokuapp.com/maven-central/org.julienrf/enum-values_2.11): finds the set of values of an enumeration ;
 - `enum-labels` [![Maven Central](https://img.shields.io/maven-central/v/org.julienrf/enum-labels_2.11.svg)](https://maven-badges.herokuapp.com/maven-central/org.julienrf/enum-labels_2.11): finds the set of value names of an enumeration ;
 - `enum` [![Maven Central](https://img.shields.io/maven-central/v/org.julienrf/enum_2.11.svg)](https://maven-badges.herokuapp.com/maven-central/org.julienrf/enum_2.11): finds the values and labels of an enumeration and a mapping to go from a value to its label and _vice versa_.

The artifacts are built for Scala 2.11 and Scala.js 0.6.

## Usage

Just define your enumeration as a sealed trait extended by case objects.

For instance:

~~~ scala
sealed trait Foo
object Foo {
  case object Bar extends Foo
  case object Baz extends Foo
}
~~~

### `enum-values`

Use `Values[A]` to automatically gather all the `A` enumeration values as a `Set[A]`:

~~~ scala
import julienrf.enum.Values

val fooValues: Values[Foo] = Values[Foo]

fooValues.values == Set(Foo.Bar, Foo.Baz)
~~~

### `enum-labels`

Use `Labels[A]` to automatically gather all the `A` enumeration labels as a `Set[String]`:

~~~ scala
import julienrf.enum.Labels

val fooLabels: Labels[Foo] = Labels[Foo]

fooLabels.labels == Set("Bar", "Baz")
~~~

### `enum`

~~~ scala
import julienrf.enum.{Enum, DecodingFailure}

val enum: Enum[Foo] = Enum[Foo]

enum.values == Set(Foo.Bar, Foo.Baz)
enum.labels == Set("Bar", "Baz")
enum.encode(Foo.Bar) == "Bar"
enum.decode("Baz") == Right(Foo.Baz)
enum.decode("invalid") == Left(DecodingFailure[Foo](Set("Bar", "Baz")))
enum.decodeOpt("Baz") == Some(Foo.Baz)
~~~

## Changelog

- 2.2
    - Update shapeless to 2.3.0 and Scala.js to 0.6.7
- 2.1
    - Lessen derivation implicit priority
- 2.0
    - Added support for implicit definitions in enumerations companion objects
- 1.1
    - Added `Enum[A]`
- 1.0
    - First release

## License

This content is released under the [MIT License](http://opensource.org/licenses/mit-license.php).
