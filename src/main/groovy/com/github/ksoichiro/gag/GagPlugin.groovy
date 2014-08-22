package com.github.ksoichiro.gag

import org.gradle.api.Plugin
import org.gradle.api.Project


class GagPlugin implements Plugin<Project> {
    void apply(Project target) {
        target.task('update', type: UpdateTask)
        target.task('joke', type: JokeTask)
    }
}
