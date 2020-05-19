# Marvel Characters
Marvel characters it's an app built to present info related to marvel charaters available at https://developer.marvel.com/docs.

# Goals
- Study Kotlin and apply some architecture concepts in this project.

# Architecture
This project is built using a MVVM architecture with multi module structure. The diagram below represents the structure of the modules.

![image](https://user-images.githubusercontent.com/5900020/82272449-098f2c00-9951-11ea-9bcf-13ea55b1d6f3.png)


# Stack
  - MVVM Architecture without databinding
  - Coroutines
  - Kotlin
  - Room
  - Koin
  - Retrofit
  - Paging
  
 # Testing
  - AndroidX Test
  - Mockk
  - Junit
  - Espresso
  - Robot Pattern
  - Orchestrator

# How To Run?
You need to follow the steps described steps in order to the application doesn't crash during runtime.
* Import the project to AndroidStudio
* Copy api.properties.clone as api.properties
* Put your Marvel Credentials on the file api.properties.
  * [How can I get a Marvel key?](https://developer.marvel.com)
* Build the application

# How To run tests?
* Instrumented: `./gradlew connectedAndroidTest`
* Unit: `./gradlew test`

# Contact:
soutosss@gmail.com
