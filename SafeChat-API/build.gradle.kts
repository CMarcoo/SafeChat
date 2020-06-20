plugins {
    java
    id("com.github.johnrengelman.shadow") version "5.2.0"
    id("io.freefair.lombok") version "5.1.0"
}

group = "me.thevipershow"
version = "1.1.1"

repositories {
    jcenter()
    mavenCentral()
    maven(url = "https://jitpack.io")
    maven(url = "https://repo.aikar.co/content/groups/aikar/")
    maven(url = "https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    testCompile("junit", "junit", "4.12")
    compileOnly(project(":SafeChat-Plugin"))
    implementation(project(":SafeChat-Core"))
    compileOnly(group = "com.destroystokyo.paper", name = "paper-api", version = "1.15.2-R0.1-SNAPSHOT")
}

tasks {
    named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
        exclude("me/thevipershow/safechat/core/commands/*")
        exclude("me/thevipershow/safechat/core/configuration/**")
        exclude("me/thevipershow/safechat/core/events/FlagEventListener.class")
        exclude("me/thevipershow/safechat/core/checks/*")
        exclude("me/thevipershow/safechat/core/sql/databases/MySQLDatabaseX.class")
        exclude("me/thevipershow/safechat/core/sql/databases/SQLiteDatabaseX.class")
        archiveClassifier.set("")
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}