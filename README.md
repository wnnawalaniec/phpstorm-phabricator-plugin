# Developing
If someone wish to contribute feel free to. There is basic docker-compose file with Phabricator. I created also some
basic shell scripts to populate Phabricator with projects and tasks - checkout `/bin`

# Building 
To build plugin you need JDK, I tested it only with JDK 11. Java must be in PATH.
If you have installed Java, just run this command:
```
./gradlew buildPlugin
```

After task is done, you can install plugin manually by:
 1. open PhpStorm
 2. go into _File->Settings->Plugins_
 3. click gear icon "⚙️" 
 4. select option _Install Plugin from Disk..._
 5. select file `build/libs/phabricator-plugin-(...).jar` from project director
 6. done