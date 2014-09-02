package com.github.ksoichiro.gag

import org.gradle.api.Project

class Dependencies {
    Project project
    List<Repo> repos = []

    Dependencies(Project project) {
        this.project = project
    }

    void repo(Map<String, ?> map) {
        def r = new Repo()
        project.configure(r) {
            location = map["location"]
            name = map["name"]
            libraryProject = map["libraryProject"]
            groupId = map["groupId"]
            artifactId = map["artifactId"]
            branch = map["branch"]
            commit = map["commit"]
            tag = map["tag"]
            gradleVersion = map["gradleVersion"]
        }
        r.resolveVersion()
        repos.add(r)
    }

    void repo(Closure closure) {
        def r = new Repo()
        project.configure(r, closure)
        r.resolveVersion()
        repos.add(r)
    }
}
