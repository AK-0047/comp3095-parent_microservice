plugins {
	java
	id("org.springframework.boot") version "3.3.3"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "ca.gbc"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(22)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}
//Week 10
dependencyManagement{
	imports{
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.3")
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.flywaydb:flyway-core")
	implementation("org.flywaydb:flyway-database-postgresql")
	implementation("org.postgresql:postgresql:42.5.0")
	//week 10
	implementation("org.springframework.cloud:spring-cloud-starter-contract-stub-runner")
	//week11
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
	testImplementation("org.springdoc:springdoc-openapi-starter-webmvc-api:2.6.0")
	//week12
	implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j:3.1.2")

	//week13
	implementation("org.springframework.kafka:spring-kafka:3.3.0")
	testImplementation("org.springframework.kafka:spring-kafka-test:3.3.0")
	testImplementation("org.testcontainers:kafka:1.20.4")


	testImplementation("io.rest-assured:rest-assured:5.3.0")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:postgresql")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
