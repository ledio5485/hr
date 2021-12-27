plugins {
	id("org.springframework.boot") version "2.5.6"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("idea")
	kotlin("jvm") version "1.5.31"
	kotlin("plugin.spring") version "1.5.31"
	kotlin("plugin.jpa") version "1.5.31"
}

group = "io.led"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
	mavenCentral()
}

extra["testcontainersVersion"] = "1.16.2"
extra["springCloudVersion"] = "2020.0.4"

configurations {
	all {
		exclude(group = "com.vaadin.external.google", module = "android-json")
		exclude(group = "net.minidev", module = "json-smart")
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-hateoas")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.security:spring-security-web:5.6.0")
	implementation("org.springframework.cloud:spring-cloud-starter-sleuth")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.10.+")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-guava:2.11.+")
	implementation("org.flywaydb:flyway-core")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib")
	implementation("org.hibernate:hibernate-validator:6.1.5.Final")
	implementation("org.springdoc:springdoc-openapi-ui:1.5.2")
	implementation("org.springdoc:springdoc-openapi-kotlin:1.5.2")
	implementation("org.springdoc:springdoc-openapi-security:1.5.2")
	implementation("io.github.microutils:kotlin-logging:2.0.3")
	implementation("net.logstash.logback:logstash-logback-encoder:6.3")
	implementation("org.jgrapht:jgrapht-core:1.4.0")
	implementation("org.jgrapht:jgrapht-io:1.4.0")
	implementation("org.json:json:20210307")

	runtimeOnly("org.postgresql:postgresql")

	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}
	testImplementation("io.mockk:mockk:1.11.0")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:postgresql")
}

dependencyManagement {
	imports {
		mavenBom("org.testcontainers:testcontainers-bom:${property("testcontainersVersion")}")
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
