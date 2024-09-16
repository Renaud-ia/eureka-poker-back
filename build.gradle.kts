plugins {
	java
	id("org.springframework.boot") version "3.3.3"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "fr.eurekapoker"
version = "0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	implementation("com.fasterxml.jackson.core:jackson-databind:2.15.0")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.0")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
