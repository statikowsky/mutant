# Mutant

Mutant is a _very WIP_ Kotlin web kit aiming to be developer friendly,  
fun and frictionless is the guiding mantra.

```kotlin
mutant {
	get("/") { "Hello world!" }
}
```

Visit [docs](https://statikowsky.github.io/mutant/) for more information.


#### Todo

There is a TODO for each module.  

Global TODO:
- [ ] extract old mutant jooq code into `mutant-jooq` module
- [ ] extract old mutant hikari code into `mutant-hikari` module
- [ ] extract old mutant flyway code into `mutant-flyway` module
- [ ] see if old mutant diagnostics are salvageable or just write new for `mutant-eye`
- [ ] websocket support
- [ ] stream support
- [ ] route to controller 'n reverse
- [ ] simple job runner
- [ ] kotlinx form gen
- [ ] write a bunch of docs


#### License   

MIT
