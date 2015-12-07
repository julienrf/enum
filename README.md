# Enum

Yet another enumeration toolbox for Scala.

Two artifacts are available: `enum-values` and `enum-labels`. As their names suggest, the former deals with the values
of an enumeration and the latter deals with the names of an enumeration.

## Installation

Add the following dependencies to your build, according to the artifacts you are interested in:

~~~ scala
libraryDependencies += "org.julienrf" %% "enum-values" % "1.0"
~~~

~~~ scala
libraryDependencies += "org.julienrf" %% "enum-labels" % "1.0"
~~~

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

## Changelog

- 1.0
    - First release

## License

This content is released under the [MIT License](http://opensource.org/licenses/mit-license.php).