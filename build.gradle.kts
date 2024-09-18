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
	// tests
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testImplementation("com.h2database:h2")

	// base
	implementation("org.springframework.boot:spring-boot-starter-web")

	// jackson
	implementation("com.fasterxml.jackson.core:jackson-databind")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

	// persistance
	implementation("org.springframework.boot:spring-boot-starter-data-jpa") // Spring Data JPA
	runtimeOnly("org.postgresql:postgresql")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
