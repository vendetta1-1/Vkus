plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.serialization)
}
java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.test {
    useJUnitPlatform()
    val agent = configurations.testRuntimeClasspath.get().files.find {
        it.name.startsWith("byte-buddy-agent")
    }
    agent?.let {
        jvmArgs = listOf("-javaagent:${it.absolutePath}")
    }
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(libs.javax.inject)
    implementation(libs.serialization)
    implementation(libs.coroutines.core)
    // tests
    testImplementation(libs.coroutines.test)
    testImplementation(libs.junit)
    testRuntimeOnly(libs.junit.engine)
    testImplementation(libs.junit.suite)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.junit)
    testImplementation(libs.mockito.kotlin)
}