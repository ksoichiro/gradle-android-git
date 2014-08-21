package com.github.ksoichiro.gag

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class JokeTask extends DefaultTask {
    @TaskAction
    def whatever() {
        println 'This is a joke task. But "gag" is a serious plugin ;)'
    }
}