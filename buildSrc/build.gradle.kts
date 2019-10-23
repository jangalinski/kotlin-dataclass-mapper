plugins {
  `kotlin-dsl`
}

apply {
  from("../gradle/repositories.gradle.kts")
}

kotlinDslPluginOptions {
  experimentalWarning.set(false)
}

