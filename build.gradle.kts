import io.spring.gradle.dependencymanagement.DependencyManagementPlugin
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformJvmPlugin

plugins {
    base
    kotlin("jvm") version "1.3.61" apply false
    id("io.spring.dependency-management") version "1.0.9.RELEASE" apply false
}

allprojects {
    group = "com.powple"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
    }
}
