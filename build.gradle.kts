plugins {
    // Apply the Java Gradle plugin development plugin to add support for developing Gradle plugins
    `java-gradle-plugin`
    id("java-gradle-plugin")
    id("maven-publish")
    id("com.gradle.plugin-publish") version "1.0.0-rc-1"

}

group = "io.github.inggameteam.ghpkgs"
version = "1.0.0"



repositories {
    // Use Maven Central for resolving dependencies
    mavenCentral()
}

dependencies {
    // Use JUnit test framework for unit tests
    testImplementation("junit:junit:4.13")
}

gradlePlugin {
    // Define the plugin
    val greeting by plugins.creating {
        id = "io.github.inggameteam.ghpkgs"
        displayName = "ghpkgs"
        description = "GitHub Packages"
        implementationClass = "io.github.inggameteam.ghpkgs.GreetingPlugin"
    }
}

pluginBundle {
    website = "https://github.com/IngGameTeam/ghpkgs"
    vcsUrl = "https://github.com/IngGameTeam/ghpkgs.git"
    tags = listOf("publish", "package", "packages", "github")
}

tasks.register("testHello") {
    println(findProperty("github.owner"))
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            val REPO = rootProject.name
            val OWNER = findProperty("github.owner")

            url = uri("https://maven.pkg.github.com/$OWNER/$REPO")
            credentials {

                username = findProperty("github.username") as? String
                password = findProperty("github.token") as? String
            }
        }
    }
    publications {
        register<MavenPublication>(rootProject.name) {
            groupId = rootProject.properties["group"]?.toString()!!
            artifactId = rootProject.name
            version = rootProject.version.toString()
//            artifact(tasks["jar"])
            from(components["java"])
        }
    }

}
