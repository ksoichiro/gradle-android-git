package com.github.ksoichiro.gag

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class UpdateTask extends DefaultTask {
    @TaskAction
    def exec() {
        def baseDir = project.git.directory
        println "Dependencies managed by gag:"
        project.git.dependencies.repos.each() { Repo repo ->
            println "dependency:"
            println "  location: ${repo.location}"
            println "  name: ${repo.name}"
            println "  commit: ${repo.commit}"
            println "  tag: ${repo.tag}"
            def repoDir = project.file(baseDir + '/' + repo.name)
            if (!repoDir.exists()) {
                // This is the first time, so we clone
                println "  Initialize dependency '${repo.name}' from '${repo.location}'..."
                def cmd = "git clone ${repo.location} ${baseDir}/${repo.name}"
                def proc = cmd.execute()
                proc.waitFor()
                if (proc.exitValue() == 0) {
                    println "  Completed successfully."
                    println "  To use this dependency, you need some actions:"
                    println "  * Add following lines to your `settings.gradle` in the root project directory."
                    println ""
                    println "      include ':${baseDir}:${repo.name}'"
                    println ""
                    println "  * Add the dependency description to your `build.gradle`."
                    println ""
                    println "      dependencies {"
                    println "          compile project(':${baseDir}:${repo.name}')"
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
        println "Done. Please put `${baseDir}/` to your .gitignore."
    }

    def checkout(Repo repo) {
        def version = repo.commit == null ? repo.tag : repo.commit;
        def wd = new File("${project.git.directory}/${repo.name}")

        def proc = execProc("git checkout master", wd)
        proc.waitFor()

        proc = execProc("git fetch", wd)
        proc.waitFor()

        proc = execProc("git pull origin master", wd)
        proc.waitFor()

        if (version != null) {
            println "Switch to ${version}..."
            proc = execProc("git checkout -f ${version}", wd)
            proc.waitFor()
        }
    }

    static def execProc(String cmd, File cwd) {
        cmd.execute([], cwd)
    }
}
