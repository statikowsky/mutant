# Abominable pasture

This is a sample web application for Mutant!

## Live reload

When running AbominablePasture add `-javaagent:path_to_springloaded-1.2.5.RELEASE.jar -noverify`

Now whenever you recompile your project should reload in browser.  
To recompile on save run: `./gradlew classes -t`  

TODO:
- [ ] plug in `mutant-flyway` for migrations
- [ ] plug in `mutant-jooq` as sql dsl
- [ ] add creatures administration
- [ ] add caretakers administration