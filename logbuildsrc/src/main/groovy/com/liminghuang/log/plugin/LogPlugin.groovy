package com.liminghuang.log.plugin


import org.gradle.api.Plugin
import org.gradle.api.Project

public class LogPlugin implements Plugin<Project> {

    @Override
    void apply(Project target) {
        final def log = target.logger

        log.error "============================="
        log.error "Javassist start modify Class!"
        log.error "============================="

//        def android = project.extensions.getByType(AppExtension)
//        android.registerTransform(new LogTransform(target))
        target.android.registerTransform(new LogTransform(target))
    }
}