// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    id("org.sonarqube") version "5.0.0.4638"
    id("org.gradle.maven-publish") // Noncompliant - kotlin:S6634 Core plugins IDs should be replaced by their shortcuts
}

sonar {
    properties {
        property("sonar.projectKey", "baeldung-gradle-kotlin-dsl")
        property("sonar.projectName", "Example of Gradle Project with Kotlin DSL")
    }
}