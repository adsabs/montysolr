plugins {
	java
	antlr
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
	implementation("org.python:jython-standalone:2.7.3")

	implementation("org.apache.logging.log4j:log4j-layout-template-json:2.21.1")

	testImplementation("junit:junit:4.13.2")
	testImplementation("org.antlr:stringtemplate:3.2.1")
	testImplementation("org.apache.solr:solr-test-framework:7.7.3")
	testImplementation("org.apache.lucene:lucene-test-framework:7.7.3")
}

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(8))
	}
}

tasks.withType<Test>().all {
	jvmArgs("-Djava.security.egd=file:/dev/./urandom")
}

sourceSets {
	main {
		resources {
			setSrcDirs(listOf("src/main/python"))
		}
	}
}

