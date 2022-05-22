# Marvel Characters
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/1cf3763119e849af8a12f681635b76f1)](https://www.codacy.com/gh/othiagosouto/MarvelPoc/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=othiagosouto/MarvelPoc&amp;utm_campaign=Badge_Grade)
![Main Workflow](https://github.com/othiagosouto/marvelpoc/workflows/Main/badge.svg)

Marvel characters it's an app built to present info related to marvel charaters available at https://developer.marvel.com/docs.

# Current objective
- Refactor the app to use compose
- Replace the directs calls to MarvelApi to use a BFF as middle
- Refactor the koin usage to Hilt
- Create design system structure

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
  - Github actions
  
 # Testing
  - AndroidX Test
  - Mockk
  - Junit
  - Espresso
  - Robot Pattern
  - Orchestrator

# How To Run?
You need to follow the steps described steps in order to the application doesn't crash during runtime.
* This app uses a private BFF hosted on digital ocean
* Build the application

# How To run tests?
* Instrumented: `./gradlew connectedAndroidTest`
* Unit: `./gradlew test`

# Contact:
thiagosouto@duck.com
