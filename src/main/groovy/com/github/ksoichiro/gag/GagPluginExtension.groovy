package com.github.ksoichiro.gag

import org.gradle.api.Project

class GagPluginExtension {
    String directory = ".gag"

    Project project

    GagPluginExtension(Project project) {
        this.project = project
    }
}
