package com.github.ksoichiro.gag

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class UpdateTask extends DefaultTask {
    @TaskAction
    def exec() {
        def cmd = "git status"
        def proc = cmd.execute()
        proc.waitFor()
        println "return code: ${proc.exitValue()}"
        println "stderr: ${proc.err.text}"
        println "stdout: ${proc.in.text}"
    }
}
