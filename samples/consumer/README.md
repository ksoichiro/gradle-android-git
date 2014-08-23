# consumer

Sample Android app as a 'gag' plugin consumer.

## Prerequisites

In root directory, you must create plugin archive.

```sh
$ ./gradlew uploadArchives
```

This will export plugin archive to `samples/repo` which is ignored by git.

In this sample project, `samples/repo` directory is registered as a maven repository:

```groovy
buildscript {
    repositories {
        mavenCentral()
        maven {
            url uri('../repo')
        }
    }
}
```


## Build

Just assemble the entire project!

```sh
$ ./gradlew assemble
```

