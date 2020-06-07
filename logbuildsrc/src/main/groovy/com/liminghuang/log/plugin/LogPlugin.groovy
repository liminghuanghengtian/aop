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

        target.android.registerTransform(new LogTransform(target))
    }
}