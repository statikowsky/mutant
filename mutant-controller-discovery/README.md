#Controller discovery

This module allows mutant to discover controllers.

Your controllers need to be in `controllers` package.

To discover controllers you need to call `discoverControllers()` in your
mutant config:
```Kotlin
mutant {
    discoverControllers()
}
```

This is the basic controller form:
```kotlin

object ControllerName {

   val get = act { "Hello"}
}

```

Each controller action needs to be a property with a `act { }` action.  
Controller discovery currently understands two types of controllers: singular and plural.  
By default all controllers are of _plural_ type.

You can mark your controller as _singular_ or _plural_ by using the  
`@Singular` or `@Plural` annotation.


```kotlin
@Singular object Codex {
    val get = act { "Welcome to Codex! "}
}
```

These actions will be recognized and mapped as routes for _singular_ controllers:  
```
get    -> GET    /controllerName  
create -> POST   /controllerName  
delete -> DELETE /controllerName  
update -> UPDATE /controllerName  
```

And for _plural_:
```
index  -> GET    /controllerName
get    -> GET    /controllerName/:id
create -> POST   /controllerName
delete -> DELETE /controllerName/:id
update -> PUT    /controllerName/:id
```

You can add new actions to your controller but you must annotate the
action with the supported http method:
```kotlin

@Post createHugeTrap = act { "Whoa there! " }

```

When using controller discovery you can still use custom routes in  
your `mutant { }` config.

TODO:
- [ ] add @Path annotation for path override
- [ ] discover class controllers
- [ ] bind args in class controller methods