# gag (gradle-android-git)

[![Build Status](https://travis-ci.org/ksoichiro/gradle-android-git.svg?branch=master)](https://travis-ci.org/ksoichiro/gradle-android-git)

This is a Gradle plugin to manage Android dependency using Git.
Usually, libraries are versioned with group, name and version numbers and you can download from Maven Central repository or jcenter.  
But sometimes you might want to use not versioned library.  
This project enables you to manage those libraries
in your Android app build process.

## With this plugin...

* You don't have to upload your library jars/aars to external Maven repository servers

* You will be noticed when to update the dependencies

    * Like CocoaPod, Bundler, you can specify library's version with Git commit hash or tags.
    * When you have to update, your build will fails.
    * If you want to upgrade, just execute 1 command.

* You can develop Android library with other apps

    * Like CocoaPod, you can choose dependency's origin from remote repositories or local path.

## Usage

To use this plugin:

1. Create your `gagconfig.gradle`, `gag.gradle` and execute `update` (see "Setting up gagconfig.gradle/gag.gradle" section below)
1. Edit your `build.gradle` (see "Setting up build.gradle" section below)
1. Build your app (see "Build your app" section below)

### Setting up gagconfig.gradle/gag.gradle

At first, write `gagconfig.gradle` (or whatever you like) like following contents.  
This includes dependencies definitions.  
You must add this file to your VCS.  
If you want to update dependencies or add new dependency, only this file should be updated.

```groovy
git {
    directory = ".gag"
    gitDependencies {
        // Use older version by commit hash (Detached HEAD)
        repo location: 'https://github.com/ksoichiro/AndroidFormEnhancer.git', name: 'afe', libraryProject: 'androidformenhancer', groupId: 'com.github.ksoichiro', artifactId: 'androidformenhancer', commit: '5a9492f45fd0f97289001a7398d04c59b846af40'

        // Use older version by tag (Detached HEAD)
        repo location: 'https://github.com/ksoichiro/SimpleAlertDialog-for-Android.git', name: 'sad', libraryProject: 'simplealertdialog', groupId: 'com.github.ksoichiro', artifactId: 'simplealertdialog', tag: 'v1.1.1'

        // Closure style configuration
        repo {
            location = 'https://github.com/JakeWharton/ActionBarSherlock.git'
            name = 'abs'
            libraryProject = 'actionbarsherlock'
            groupId = 'com.actionbarsherlock'
            artifactId = 'actionbarsherlock'
            commit = '4a79d536af872339899a90d6dc743aa57745474b'
            // If the project does not contain gradle wrapper,
            // gag will generate with `gradleVersion` version.
            gradleVersion = '1.6'
        }
    }
}
```

Then, create `gag.gradle` (or whatever you like).  
This script uses `gagconfig.gradle`.  

```groovy
buildscript {
    repositories {
        mavenCentral()
        // Sonatype repository
        // maven {
        //     url uri('https://oss.sonatype.org/service/local/repositories/releases/content/')
        // }
    }
    dependencies {
        classpath 'com.github.ksoichiro:gradle-android-git:0.1.+'
    }
}

apply plugin: 'gag'
apply from: 'gagconfig.gradle'

defaultTasks 'update'
```

After that, execute this:

```sh
$ ./gradlew -b gag.gradle
or
$ ./gradlew -b gag.gradle update
```

This will clone all the repositories, check out, assemble and upload archive to `.gag/.repo`.

`.gag` directory is just a working directory, so you must add `.gag/` to your `.gitignore`.

### Setting up build.gradle

Your `build.gradle` doesn't look so different.  
Just edit your `repositories` and `dependencies` to use `.gag/.repo` contents.

```groovy
buildscript {
    repositories {
        mavenCentral()
        maven {
            url uri('../repo')
        }
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:0.12.+'
        classpath 'com.github.ksoichiro:gradle-android-git:0.1.+'
    }
}

apply plugin: 'com.android.application'
apply plugin: 'gag'
apply from: 'gagconfig.gradle'

android {
    compileSdkVersion 19
    buildToolsVersion "19.1.0"
    defaultConfig {
        minSdkVersion 8
    }
}

// Add repository generated by gag.
repositories {
    maven {
        url uri('.gag/.repo')
    }
}

dependencies {
    compile 'com.android.support:support-v4:20.0.+'

    // You can write dependencies in a groovy way
    for (r in project.git.gitDependencies.repos) {
        compile "${r.groupId}:${r.artifactId}:${r.resolvedVersion}@aar"
    }

    // Of course, you can write in a normal way
    // compile 'com.github.ksoichiro:androidformenhancer:5a9492f45fd0f97289001a7398d04c59b846af40@aar'
    // compile 'com.github.ksoichiro:simplealertdialog:v1.1.1@aar'
    // compile 'com.actionbarsherlock:actionbarsherlock:4a79d536af872339899a90d6dc743aa57745474b@aar'
}
```

### Build your app

Build your app as you always do.  
For example:

```sh
$ ./gradlew installDebug
```

If somebody in your team updated dependencies with new commit hash or tags, your build will fail because Gradle can't resolve dependencies.  
If you see that kind of errors, just `update`:

```sh
$ ./gradlew -b gag.gradle
or
$ ./gradlew -b gag.gradle update
```

## Configuration

### git

`git` closure is the main configuration.  
This defines dependencies (and other meta data in the future).

### gitDependencies

`gitDependencies` includes Git repositories definition.  
This is also a closure.

### repo

`repo` is the Git repository configuration.  
Each parameters will be passed to git command, so local path and remote URL will be resolved.

#### location

Repository location.  
This can be a local file path, remote git URL, or whatever `git` can recognize.

#### name

Name of the dependency.  
The dependency is cloned with this name.

#### libraryProject

The name of the Android library sub-project in the repository.

#### groupId

Maven `groupId`.  
This is not the public `groupId`.  
You can use anything if it doesn't conflict with your other dependencies.

#### artifactId

Maven `artifactId`.
Its meaning is the same as the `groupId`.  
It's not necessary to match to the public `artifactId`.

#### branch

Default value is `master`.

#### commit / tag

Target version identified with commit or tag.  

#### gradleVersion

Gradle wrapper version to build Android library project.  
This parameter is only used when the target dependency repository doesn't include gradle wrapper.

To generate wrapper, gag uses host project's gradle wrapper.

## Samples

See `samples/consumer` directory.  
This is an Android app project configured with Gradle.

## License

Copyright (c) 2014 Soichiro Kashima  
Licensed under MIT license.  
See the bundled [LICENSE](https://github.com/ksoichiro/gradle-android-git/blob/master/LICENSE) file for details.
