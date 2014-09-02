package com.github.ksoichiro.gag

class Repo {
    String location
    String name
    String libraryProject
    String groupId
    String artifactId
    String branch = "master"
    String commit
    String tag
    String gradleVersion
    String resolvedVersion

    void resolveVersion() {
        if (commit == null) {
            resolvedVersion = tag
        } else {
            resolvedVersion = commit
        }
    }
}
