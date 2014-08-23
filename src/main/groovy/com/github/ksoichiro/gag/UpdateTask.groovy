package com.github.ksoichiro.gag

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class UpdateTask extends DefaultTask {
    @TaskAction
    def exec() {
        println "Dependencies managed by gag:"
        project.git.dependencies.repos.each() { Repo repo ->
            println "dependency:"
            println "  location: ${repo.location}"
            println "  name: ${repo.name}"
            println "  commit: ${repo.commit}"
            println "  tag: ${repo.tag}"
            def repoDir = project.file('library/' + repo.name)
            if (!repoDir.exists()) {
                // This is the first time, so we clone
                println "  Initialize dependency '${repo.name}' from '${repo.location}'..."
                def cmd = "git clone ${repo.location} ./library/${repo.name}"
                def proc = cmd.execute()
                proc.waitFor()
                if (proc.exitValue() == 0) {
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
            checkout repo
        }
        println "Done. Please put `library/` to your .gitignore."
    }

    static def checkout(Repo repo) {
        def version = repo.commit == null ? repo.tag : repo.commit;
        def cmd = "cd library/${repo.name} && git checkout master"
        def proc = cmd.execute()
        proc.waitFor()

        cmd = "cd library/${repo.name} && git fetch"
        proc = cmd.execute()
        proc.waitFor()

        cmd = "cd library/${repo.name} && git checkout -f master"
        proc = cmd.execute()
        proc.waitFor()

        cmd = "cd library/${repo.name} && git pull origin master"
        proc = cmd.execute()
        proc.waitFor()

        if (version != null) {
            cmd = "cd library/${repo.name} && git checkout ${version}"
            proc = cmd.execute()
            proc.waitFor()
        }
    }
}
