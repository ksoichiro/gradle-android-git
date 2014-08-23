package com.github.ksoichiro.gag

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class UpdateTask extends DefaultTask {
    @TaskAction
    def exec() {
        println "Dependencies managed by gag:"
        project.git.dependencies.repos.each() { repo ->
            println "dependency:"
            println "  location: ${repo.location}"
            println "  name: ${repo.name}"
            println "  commit: ${repo.commit}"
            println "  tag: ${repo.tag}"
            def repoDir = project.file('library/' + repo.name)
            if (repoDir.exists()) {
                // TODO Already cloned, so we discard changes and update
            } else {
                // This is the first time, so we clone
                println "  Initialize dependency '${repo.name}' from '${repo.location}'..."
                def cmd = "git clone ${repo.location} ./library/${repo.name}"
                def proc = cmd.execute()
                proc.waitFor()
                // println "return code: ${proc.exitValue()}"
                // println "stderr: ${proc.err.text}"
                // println "stdout: ${proc.in.text}"
                if (proc.exitValue() == 0) {
                    // TODO Check out the appropriate version specified with commit or tag
                    println "  Completed successfully."
                    println "  To use this dependency, you need some actions:"
                    println "  * Add following lines to your `settings.gradle` in the root project directory."
                    println ""
                    println "      include ':library:${repo.name}'"
                    println ""
                    println "  * Add the dependency description to your `build.gradle`."
                    println ""
                    println "      dependencies {"
                    println "          compile project(':library:${repo.name}')"
                    println "      }"
                    println ""
                } else {
                    println "Failed to initialize. Check your configuration and network connection."
                    println "${proc.err.text}"
                    // TODO Exit with error
                }
            }
        }
        println "Done. Please put `library/` to your .gitignore."
    }
}
