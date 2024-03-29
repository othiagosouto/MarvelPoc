# Marvel Characters

[![Codacy Badge](https://app.codacy.com/project/badge/Grade/1cf3763119e849af8a12f681635b76f1)](https://www.codacy.com/gh/othiagosouto/MarvelPoc/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=othiagosouto/MarvelPoc&amp;utm_campaign=Badge_Grade)
![Main Workflow](https://github.com/othiagosouto/MarvelPoc/actions/workflows/main.yml/badge.svg?branch=main)

Marvel characters it's an app built to present info related to marvel characters available at [Marvel website](https://developer.marvel.com/docs).

## Current objective

-   Refactor the app to use compose
-   Replace the directs calls to MarvelApi to use a BFF as middle
-   Refactor the koin usage to Hilt
-   Create design system structure

## Goals

-   Study Kotlin and apply some architecture concepts in this project.

## Architecture

This project is built using a MVVM architecture with multi module structure. The diagram below represents the structure of the modules.

![image](https://user-images.githubusercontent.com/5900020/82272449-098f2c00-9951-11ea-9bcf-13ea55b1d6f3.png)

## Stack

-   MVVM Architecture without databinding
-   Coroutines
-   Kotlin
-   Room
-   Koin
-   Ktor
-   Paging
-   Github actions

## Testing

-   AndroidX Test
-   Mockk
-   Junit
-   Espresso
-   Robot Pattern
-   Orchestrator

## Quick Start

### Run project

You need to follow the steps described steps in order to the application doesn't crash during runtime.

-   This app uses a private BFF hosted on digital ocean-
-   Checkout the code and build the application

### Running tests

-   Instrumented: `./gradlew connectedAndroidTest`
-   Unit: `./gradlew test`

## Contact

[Email: thiagosouto@duck.com](mailto:thiagosouto@duck.com)

[Twitter](https://twitter.com/othiagosouto)

[Medium](https://othiagosouto.medium.com/)
