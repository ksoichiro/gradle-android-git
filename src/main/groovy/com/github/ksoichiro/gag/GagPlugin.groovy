package com.github.ksoichiro.gag

import org.gradle.api.Plugin
import org.gradle.api.Project


class GagPlugin implements Plugin<Project> {
    void apply(Project target) {
        target.extensions.create("git", GagPluginExtension, target)
        target.git.extensions.create("gitDependencies", Dependencies, target)

        target.task('update', type: UpdateTask)

        target.task('listConfig') << {
            target.git.gitDependencies.repos.each() { repo ->
                println "dependency:"
                println "  location: ${repo.location}"
                println "  name: ${repo.name}"
                println "  libraryProject: ${repo.libraryProject}"
                println "  groupId: ${repo.groupId}"
                println "  artifactId: ${repo.artifactId}"
                println "  branch: ${repo.branch}"
                println "  commit: ${repo.commit}"
                println "  tag: ${repo.tag}"
                println "  gradleVersion: ${repo.gradleVersion}"
            }
        }
    }
}
