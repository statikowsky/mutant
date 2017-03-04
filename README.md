<img src="https://raw.githubusercontent.com/statikowsky/mutant/master/mutant-doc/images/little_mutant_logo.png" height="80">

Mutant is a _very WIP_ Kotlin web kit aiming to be developer friendly.  
Fun and frictionless is the guiding mantra.

A tiny "Hello world" example:  
`Start.kt`
```kotlin
import org.droba.mutantStarterPack.mutant

fun main (args : Array<String>) {
  mutant {
      get("/") { "Hello World!" }
  }
}
```

Visit [docs](https://statikowsky.github.io/mutant/) or `mutant-abominable-pasture` example project to find out more.

### Features

- simple DSL for routes
- live reload with [springloaded module](https://github.com/statikowsky/mutant/tree/master/mutant-springloaded-support)
- easy request and response handling
- work with JSON easily
- opt in auto wired controllers with param and model binding
- modular, pick what you need throw away the rest
- middleware

#### On the way

- db modules - hikari as pool, jooq as sql dsl and flyway for migrations 
- kotlin.x module - better integration with kotlinx.html (form helpers etc.)
- eye module - simple req and db perf monitoring with app diagnostics
- websockets support
- jobs module - job runner
- streaming support

#### License   

MIT
