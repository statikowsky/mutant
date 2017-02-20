# Mutant springloaded support

![Reload example](https://raw.githubusercontent.com/statikowsky/mutant/master/mutant-doc/images/liveReload.gif)

In your halo config call `registerReloadNotifier()`  
Your pages will need a bit of js to listen to reload events and trigger reload.  
If you are using kotlinx.html this module has a helper that will add the  
required script. Just call `reloadScript()` while building your view.

Add `springloaded.jar` to your project.

Run your project with  
`-javaagent:/path/to/springloaded.jar -noverify`

Now whenever you save and compile your mutant project should refresh itself!

TODO:
- [ ] make reload more robust

