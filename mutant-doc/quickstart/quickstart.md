# Quickstart

_This documentation is WIP_

Usually when starting your Mutant project you would first pick the modules you need. 
But when you just want to get started quickly there is the `mutant-starter-pack`. 
It's a _mostly_ sane collection of Mutant modules.

In this guide we will take a quick but very light on details look
at some features of Mutant and it's modules. 
If you ever want a more detailed overview follow the doc links to find more info!

All the examples below use `mutant-starter-pack`

#### Your first Mutant application

```kotlin
import org.droba.mutantStarterPack.mutant

fun main (args : Array<String>) {
  mutant {
    get("/") { "Hello World!" }
  }
}
```

Visit `localhost:8080` and you should be greeted with `Hello World!`

#### Routes

We can register routes inside `mutant { }` by using `get`, `post`, `put`, `delete`, `patch`, `head`, `options` and `trace`
functions:
```kotlin
//verb    path   action

  get     ("/") { "fetch"  }
  post    ("/") { "create" }
  put     ("/") { "update" }
  delete  ("/") { "delete" }
  patch   ("/") { "patch"  }
```

See [Routing]() docs for more information.

#### Request info

You can access request information trough the `req` prop.
Let's read and display the `User-Agent` header from the request:

```kotlin
  get ("/user-agent") {
    req.headers["User-Agent"] 
  }
```
Visit `localhost:8080/user-agent` and you should see your user agent string!

Some of the info you can get from `req`:
```kotlin
  req.params      // get url params  ( ?id=slimyJelly )
  req.pathParams  // get path params ( /creature/undeadJelly )
  req.body        // get req body
  req.headers     // get req headers
  req.cookies     // get cookies
  req.session     // get session
```
See [MutantRequest]() docs for more information.

#### Working with JSON

`mutant-starter-pack` uses `mutant-gson-json-renderer` for easy json serialization
and deserialization. 
Let's write a route that reads json content from request body and returns json:
```kotlin
  data class User(val name: String, val surname: String)

  get ("/json") {
    val user = req into User::class  // deserialize into User 
    json(user)                       // emit User as json
  }

  // there is also an alternate way to achieve the same result 
  get ("/json_alt") {
    val user = req.into<User>()     // deserialize into User 
    json(user)                      // emit User as json
  }
```

If you need to quickly construct a json object:
```kotlin
  get ("/json2") {
    jsonObject(
    	"name"    to "Rick",
        "surname" to "Sanchez"	
    )
  }
```
This would return a json object `{ name : "Rick", surname : "Sanchez" }`

See [mutant-gson-json-renderer]() docs for more information.

#### Rendering views

##### Rendering views with kotlinx.html

You can use [`kotlinx.html`](https://github.com/Kotlin/kotlinx.html) to write your views in Kotlin!
`kotlinx.html` is a Kotlin DSL for building HTML. 
You can check out how such views are organized in [`mutant-abominable-pasture`](https://github.com/statikowsky/mutant/tree/master/mutant-abominable-pasture) example project.

##### Rendering views with handlebars

`mutant-starter-pack` uses `mutant-handlebars-template-renderer` mutant module for template rendering.
This renderer uses `handlebars.java` for template rendering. This module expects your template files to be located
in the root of your project inside `/templates` directory.

Let's create `user.hbs` template to our `/templates` directory:
```handlebars
   <div>
     <h1>Hello there {{this}}</h1>
   </div>
```

And now let's render our view:
```kotlin
   get ("/user") {
     view("user", "Rick Sanchez") 
   }
```
If you now visit `localhost:8080/user` you should see `Hello there Rick Sanchez`!

See [mutant-handlebars-template-renderer]() docs for more information.

#### Organizing controllers and controller discovery

##### Organizing controllers manually

Instead of writing your actions when defining you routes e.g.
```kotlin
   get ("/controllerOrg") { "Let's organize!" }
```

You can create separate classes or objects that will hold your actions
and then bind them to your routes:

`controllers/UserController.kt`
```kotlin
   object UserController {
     val get     = act { "Sry no users here yet!" }
     val create  = act { "Nothing to update yet!" }
     val summary = act { "Nothing to do here :(!" }
   }
```

We can use the `act` helper so we do not have to specify the signature of our lambda. 

`AppStart.kt`
```kotlin
   mutant {
     get(  "/user/:id",     UserController::get)
     post( "/user",         UserController::create)
     get(  "/user/summary", UserController::summary)     
   }
```

##### Using controller discovery

If you are ok with abiding with a few conventions you can use `ControllerDiscovery` from `mutant-controller-discovery` module.
It is provided by default in `mutant-starter-pack`. It allows us to write and wire up controllers
easily.
To enable controller discovery in your project you need to call `DiscoverControllers()`
in mutant setup:

`AppStart.kt`
```kotlin
   mutant {
     discoverControllers()
   }
```

`controllers/UserController.kt`
```kotlin
   object UserController {
     val get          = act { "Sry no users here yet!" } // will map to GET  /user/:id
     val create       = act { "Nothing to update yet!" } // will map to POST /user
     val @Get summary = act { "Nothing to do here :(!" } // will map to GET  /user/summary
   }
```

See [mutant-controller-discovery]() docs for more information.


#### Database

`mutant-starter-pack` includes multiple db related packages:  
 - `mutant-hikari-cp` (database pool)
 - `mutant-jooq` (typesafe SQL dsl api)
 - `mutant-flyway` (migrations)


