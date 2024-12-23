plugins {
    id("java-library")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":platform.desktop"))
    implementation("com.xenoamess:jxinput:1.0.0")
    runtimeOnly(libs.logging.runtime)
}
