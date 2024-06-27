# CurtainGradle

Gradle toolchain for minecraft plugin development.

# Usage

You can use it via jitpack:

[![](https://jitpack.io/v/TCBuildMC/CurtainGradle.svg)](https://jitpack.io/#TCBuildMC/CurtainGradle)

```gradle
buildscript {
    repositories {
        maven {
            url = "https://jitpack.io/"
        }
    }

    dependencies {
        classpath "com.github.TCBuildMC.CurtainGradle:io.github.tcbuildmc.curtaingradle.gradle.plugin:<commit>"
    }
}

apply plugin: "io.github.tcbuildmc.curtaingradle"
```
