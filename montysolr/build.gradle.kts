plugins {
	java
	antlr
	id("org.gradle.test-retry") version "1.5.9"
}

repositories {
	mavenCentral()

	// Required for Solr restlet dependency
	maven {
		url = uri("https://maven.restlet.talend.com")
	}
}

dependencies {
	antlr("org.antlr:antlr:3.5.2")

	implementation("org.antlr:antlr-runtime:3.5.2")
	implementation("org.apache.solr:solr-core:7.7.3")
	implementation("org.apache.lucene:lucene-core:7.7.3")
	implementation("org.apache.lucene:lucene-queryparser:7.7.3")

	implementation("com.anyascii:anyascii:0.3.2")
	//implementation("org.python:jython-standalone:2.7.3")
	implementation(project(":jython"))

	testImplementation("junit:junit:4.13.2")
	testImplementation("org.antlr:stringtemplate:3.2.1")
	testImplementation("org.apache.solr:solr-test-framework:7.7.3")
	testImplementation("org.apache.lucene:lucene-test-framework:7.7.3")
	testImplementation("com.univocity:univocity-parsers:2.9.1")
}

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(8))
	}
}

tasks.withType<Test>().all {
	jvmArgs("-Djava.security.egd=file:/dev/./urandom")

	retry {
		maxRetries.set(3)
		maxFailures.set(10)
		failOnPassedAfterRetry.set(false)
	}
}

tasks.named<JavaCompile>("compileJava") {
	dependsOn(":jython:mergeExposed")
}

sourceSets {
	main {
		resources {
			setSrcDirs(listOf("src/main/python"))
		}
	}
}

