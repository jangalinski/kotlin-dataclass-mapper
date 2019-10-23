plugins {
  base
  idea

  kotlin("jvm") version Versions.kotlin
  `java-library`
}

apply {
  from("${rootProject.rootDir}/gradle/repositories.gradle.kts")
}


dependencies {
  implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

  implementation(kotlin("stdlib-jdk8"))
  implementation(kotlin("reflect"))

  testImplementation("org.jetbrains.kotlin:kotlin-test")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
  testImplementation("org.assertj:assertj-core:3.11.1")

}
