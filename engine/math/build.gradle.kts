plugins {
    id("java-library")
}

val osNameArch : String  by rootProject.extra

dependencies {
    api(libs.joml)
    testImplementation(platform("org.junit:junit-bom:5.11.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
    maxHeapSize = "1G"
    maxParallelForks=1
}