# gag (gradle-android-git)

Work in progress. Just thinking about what I want to do...

This is a Gradle plugin to manage Android dependency using Git.  
Usually, libraries are versioned with group, name and version numbers and you can download from Maven Central repository or jcenter.  
But sometimes you might want to use not versioned library.  
This project enables you to manage those libraries
in your Android app build process.

## With this plugin...

* You don't have to create your library jars/aars

* You don't have to care about upgrade of dependencies

    * Like CocoaPod, Bundler, you can specify library's version with Git commit hash or tags.
    * When you have to update, plugin output warnings to persist you to upgrade dependencies.
    * If you want to upgrade, just execute 1 command.

* You can develop Android library with other apps

    * Like CocoaPod, you can choose dependency's origin from remote repositories or local path. So you don't have to create archive to test your library in your app.

## Usage

This is just an image.

```groovy
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:0.12.+'
        classpath 'com.github.ksoichiro:gag:0.0.1'    }}

:

apply plugin: 'com.github.ksoichiro.gag'

git {
    dependencies {
        repo location: 'https://github.com/ksoichiro/AndroidFormEnhancer.git', name: 'afe', commit: 'a9d4496adee3aa79e118c8db9ddd4a0fff1c03d9'        repo location: 'https://github.com/ksoichiro/SimpleAlertDialog-for-Android.git', name: 'sad', tag: 'v1.1.1'
    }
}

```

## Credits

### grgit

This project uses (will use) [ajoberstar/grgit](https://github.com/ajoberstar/grgit).

## License

MIT license.
