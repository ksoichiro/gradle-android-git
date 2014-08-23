# gag (gradle-android-git)

This is a Gradle plugin to manage Android dependency using Git.
Usually, libraries are versioned with group, name and version numbers and you can download from Maven Central repository or jcenter.  
But sometimes you might want to use not versioned library.  
This project enables you to manage those libraries
in your Android app build process.

## With this plugin...

* You don't have to upload your library jars/aars to external Maven repository servers

* You don't have to care about upgrade of dependencies

    * Like CocoaPod, Bundler, you can specify library's version with Git commit hash or tags.
    * When you have to update, plugin output warnings to persist you to upgrade dependencies.
    * If you want to upgrade, just execute 1 command.

* You can develop Android library with other apps

    * Like CocoaPod, you can choose dependency's origin from remote repositories or local path. So you don't have to create archive to test your library in your app.

## Usage

This plugin works in your local environment, but not released to `mavenCentral` nor `jcenter` yet.  
Please build this plugin and `uploadArchive` to your local repository.

To use this plugin:

1. Edit your build.gradle (see "Setting up build.gradle" section below)
1. Edit your settings.gradle (see "Setting up build.gradle" section below)
1. Build your app

### Setting up build.gradle

```groovy
buildscript {
    repositories {
        mavenCentral()
        maven {
            url url('/path/to/your/local/repo')
        }
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:0.12.+'
        classpath 'com.github.ksoichiro:gradle-android-git:0.1.+'
    }
}

apply plugin: 'com.android.application'
apply plugin: 'gag'

// To integrate to your build, write this:
preBuild.dependsOn {
    update
}

git {
    dependencies {
        // Define your dependencies
        repo location: 'https://github.com/ksoichiro/AndroidFormEnhancer.git', name: 'afe', commit: '5a9492f45fd0f97289001a7398d04c59b846af40'
        repo location: 'https://github.com/ksoichiro/SimpleAlertDialog-for-Android.git', name: 'sad', tag: 'v1.1.1'
    }
}

android {
    compileSdkVersion 19
    buildToolsVersion "19.1.0"
    defaultConfig {
        minSdkVersion 8
    }
    :
}

dependencies {
    compile 'com.android.support:support-v4:20.0.+'

    // Add your dependencies as sub-projects
    compile project(':library:afe:androidformenhancer')
    compile project(':library:sad:simplealertdialog')
}

```

Note that the cloned repository(sub-project root) and the library you want to compile don't always match.

For example, the library `AndroidFormEnhancer`'s root directory does not contain main library and the sub-directory `androidformenhancer` does.  
So you must specify subdirectory.

```
    // This is wrong
    compile project(':library:afe')
    // This is OK!
    compile project(':library:afe:androidformenhancer')
```

### Setting up settings.gradle

```groovy
// Include sub-projects cloned by this plugin.
include ':library:afe:androidformenhancer'
include ':library:sad:simplealertdialog'
include ':'
```

### Build your app

Build your app as you always do.  
The plugin automatically clone Git repositories and checkout specified version.  
If you configured your .gradle files correctly, dependencies are available as sub-projects (`library` directory).  
For example:

```sh
$ ./gradlew installDebug
```

If you don't want to integrate updating dependencies,  
remove `preBuild dependsOn...` from `build.gradle`  
and execute following command manually before building your app:

```sh
$ ./gradlew update
```

## License

Copyright (c) 2014 Soichiro Kashima  
Licensed under MIT license.  
See the bundled [LICENSE](https://github.com/ksoichiro/gradle-android-git/blob/master/LICENSE) file for details.
