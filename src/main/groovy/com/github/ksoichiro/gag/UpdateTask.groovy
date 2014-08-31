package com.github.ksoichiro.gag

import org.gradle.api.DefaultTask
import org.gradle.api.GradleScriptException
import org.gradle.api.tasks.TaskAction

class UpdateTask extends DefaultTask {
    @TaskAction
    def exec() {
        def baseDir = project.git.directory
        println "Dependencies managed by gag:"
        project.git.gitDependencies.repos.each() { Repo repo ->
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
            def repoDir = project.file(baseDir + '/' + repo.name)
            if (!repoDir.exists()) {
                // This is the first time, so we clone
                println "  Initialize dependency '${repo.name}' from '${repo.location}'..."
                def cmd = "git clone ${repo.location} ${baseDir}/${repo.name}"
                def proc = cmd.execute()
                proc.waitFor()
                if (proc.exitValue() == 0) {
                    println "  Completed successfully."
                } else {
                    println "Failed to initialize. Check your configuration and network connection."
                    println "${proc.err.text}"
                    // Exit with error
                    throw new GradleScriptException(
                            "Failed to initialize. Check your configuration and network connection.", null)
                }
            }
            checkout repo
            uploadArchives repo
        }
        println "Done. Please put `${baseDir}/` to your .gitignore."
    }

    def checkout(Repo repo) {
        def version = repo.commit == null ? repo.tag : repo.commit;
        def wd = new File("${project.git.directory}/${repo.name}")

        def branch = repo.branch
        if (branch == null || branch.length() == 0) {
            branch = "master"
        }
        execProcessBuilder(["git", "checkout", "-f", branch], wd, "git")
        execProcessBuilder(["git", "fetch"], wd, "git")
        execProcessBuilder(["git", "pull", "origin", branch], wd, "git")

        if (version != null) {
            println "Switch to ${version}..."
            execProcessBuilder(["git", "checkout", "-f", "${version}".toString()], wd, "git")
        }
    }

    def uploadArchives(Repo repo) {
        def wd = new File("${project.git.directory}/${repo.name}")

        // Append uploadArchives command
        println "Appending upload command to build.gradle..."
        def libraryBuildGradle = project.file("${project.git.directory}/${repo.name}/${repo.libraryProject}/build.gradle")
        println libraryBuildGradle.path
        def relativeRepoPath = "../../.repo"
        def version = repo.commit == null ? repo.tag : repo.commit
        libraryBuildGradle.append("""
apply plugin: 'maven'

uploadArchives {
    repositories {
        mavenDeployer {
            repository(url: uri('${relativeRepoPath}'))
            pom.groupId = "${repo.groupId}"
            pom.version = "${version}"
        }
    }
}
""")

        def gradle = "./gradlew"
        def osName = System.getProperty("os.name").toLowerCase()
        if (osName.contains("windows")) {
            gradle = "gradlew.bat"
        }
        if (!(new File(wd, gradle)).exists()) {
            def versionStr = repo.gradleVersion == null ? "" : " ${repo.gradleVersion}"
            println "Gradle wrapper not found. Generating${versionStr}..."

            // Generating wrapper to temporary directory
            def tmpDir = ".tmp"
            project.file(tmpDir).mkdirs()
            if (repo.gradleVersion != null) {
                project.file(tmpDir.toString() + "/build.gradle").write("""
task wrapper(type: Wrapper) {
    gradleVersion = '${repo.gradleVersion}'
}
""")
            }
            execProcessBuilder([gradle.toString(), "-p", tmpDir, "wrapper"], project.projectDir, "gradlew")

            // Copy generated gradle wrapper to library directory
            project.copy {
                from new File(tmpDir, "gradle").path
                into new File(wd, "gradle").path
            }
            project.copy {
                from(tmpDir) {
                    include 'gradlew*'
                }
                into wd.path
            }
            project.delete(tmpDir)
        }

        println "Assembling and uploading library ${repo.name}..."
        def gradleCommand = [
                "${gradle}".toString(),
                ":${repo.libraryProject}:clean".toString(),
                ":${repo.libraryProject}:assemble".toString(),
                ":${repo.libraryProject}:uploadArchives".toString()
        ]
        def proc = execProcessBuilder(gradleCommand, wd, "gradlew")
        println "Exit value: ${proc.exitValue()}"
        println "${proc.in.text}"
        println "${proc.err.text}"
    }

    static def execProcessBuilder(List<String> gradleCommand, File wd, def tag) {
        def pb = new ProcessBuilder(gradleCommand)
                .directory(wd)
        def env = pb.environment()
        env["PATH"] = System.getenv("PATH")
        def proc = pb.start()

        // Avoid I/O blocking
        def inGobbler = new StreamGobbler(proc.getInputStream(), "${tag}:in")
        def errGobbler = new StreamGobbler(proc.getErrorStream(), "${tag}:err")
        inGobbler.start()
        errGobbler.start()
        proc.waitFor()
        inGobbler.join()
        errGobbler.join()
        proc
    }
}
