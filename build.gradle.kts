plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.asciidoctor.jvm.convert") version "3.3.2"
}

group = "community.whatever"
version = "0.0.1-SNAPSHOT"
val coroutinesVersion = "1.10.1"
val awaitilityVersion = "4.2.0"
val mockkVersion = "1.13.17"
val restAssuredVersion = "5.5.0"
val datafakerVersion = "2.4.2"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

configurations {
    create("asciidoctorExt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    // kotlinx-coroutines-test는 코틀린 코루틴을 테스트하기 위한 라이브러리입니다.
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
    // awaitility은 비동기 코드를 테스트하기 위한 라이브러리입니다.
    testImplementation("org.awaitility:awaitility-kotlin:$awaitilityVersion")
    // mockk는 목 객체를 생성하기 위한 라이브러리입니다.
    testImplementation("io.mockk:mockk:$mockkVersion")
    // webTestClient를 사용하기 위해 webflux를 추가합니다.
    testImplementation("org.springframework.boot:spring-boot-starter-webflux")
    // Spring Context외 E2E 테스트를 위해 RestAssured를 추가합니다.
    testImplementation("io.rest-assured:rest-assured:$restAssuredVersion")
    // 자동 API 문서화를 위해 Spring REST Docs를 추가합니다.
    testImplementation("org.springframework.restdocs:spring-restdocs-restassured")
    // RestDocs를 사용하여 API 문서를 생성하기 위해 asciidoctor를 추가합니다.
    testImplementation("org.springframework.restdocs:spring-restdocs-asciidoctor")
    // Dummy Data로 테스트를 하기 위해 Datafacker를 추가합니다.
    testImplementation("net.datafaker:datafaker:$datafakerVersion")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

val snippetsDir by extra { file("build/generated-snippets") }

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    outputs.dir(snippetsDir)
    useJUnitPlatform()
}
