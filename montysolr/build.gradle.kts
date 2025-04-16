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
	implementation("org.apache.solr:solr-core:9.6.1")
	implementation("org.apache.lucene:lucene-core:9.10.0")
	implementation("org.apache.lucene:lucene-queryparser:9.10.0")
	implementation("org.apache.lucene:lucene-join:9.10.0")
	implementation("org.apache.lucene:lucene-misc:9.10.0")
	implementation("org.apache.lucene:lucene-suggest:9.10.0")
	implementation("org.apache.commons:commons-lang3:3.14.0")

	implementation("com.google.guava:guava:33.2.1-jre")
	implementation("com.anyascii:anyascii:0.3.2")
	implementation("org.mapdb:mapdb:3.0.10")
	//implementation("org.python:jython-standalone:2.7.3")
	implementation(project(":jython"))

	testImplementation("junit:junit:4.13.2")
	testImplementation("org.antlr:stringtemplate:3.2.1")
	testImplementation("org.apache.solr:solr-test-framework:9.6.1")
	testImplementation("org.apache.solr:solr-scripting:9.6.1")
	testImplementation("org.apache.lucene:lucene-test-framework:9.10.0")
	testImplementation("org.apache.lucene:lucene-backward-codecs:9.10.0")
	testImplementation("org.apache.lucene:lucene-codecs:9.10.0")
	testImplementation("com.univocity:univocity-parsers:2.9.1")
	testImplementation("commons-io:commons-io:2.16.1")
}

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(17))
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

