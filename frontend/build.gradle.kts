plugins {
    base
    id("com.github.node-gradle.node") version "1.5.1"
}

tasks {
    named("yarn_build") {
        dependsOn("yarn_install")
    }
    
    named("yarn_test") {
        dependsOn("yarn_install")
    }
}