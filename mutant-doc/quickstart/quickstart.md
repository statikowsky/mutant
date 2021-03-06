# Quickstart

_This documentation is WIP_

To get started quick with Mutant use `mutant-starter-pack` a _mostly_ sane
collection of Mutant modules. Once you are in no rush you can pick and choose
the modules you _really_ need.

We will now make a quick overview of modules included in `mutant-starter-pack`. 

Remember to add `mutant-starter-pack` dependency in your build system of choice.
In case you are using `gradle` add:

` gradle placeholder `

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

See [Mutant builder]() docs for more information. 

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
  req.formParams  // get form params ( x-www-form-urlencoded ) 
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
     val get     : Action = { "Sry no users here yet!" }
     val create  : Action = { "Nothing to update yet!" }
     val summary : Action = { "Nothing to do here :(!" }
   }
```

Note that the lambdas are of type `Action`. `Action` is a typealias for `M.() -> Any`.  

You can then reference your actions in Mutant setup:
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
     // default routes
     //
     // get will map to GET `/user/:id`
     // create will map to POST `/user`

     val get    : Action = act { "Sry no users here yet!" } 
     val create : Action = act { "Nothing to update yet!" }

     // non-default routes need to be annotated with http methods

     @Get val summary : Action = { "Nothing to do here :(!" } 
     
     // all non-default routes get automatic paths from field name, 
     // use @Path annotation to override

     @Get @Path("/dancers")
     val listDancers : Action = { userService.dancers }

     // use a lambda with an M receiver and parameters that represent the
     // values you need in your request
     //
     // in this example you path param `:shirt` would be bound to `shirt`
     // and request body would be bound to `detailsDto`
     //
     // check controller discovery docs for more info 

     @Post @Path("/details/:shirt/nonsensical/")
     val nonsensical M.(String, DetailsDto) -> Any = {
     	shirt, detailsDto -> "What are you doing?"
     }
   }
```

See [mutant-controller-discovery]() docs for more information.

#### Database

`mutant-starter-pack` includes multiple db related packages:  
 - `mutant-hikari-cp` (database pool)
 - `mutant-jooq` (typesafe SQL dsl api)
 - `mutant-flyway` (migrations)


