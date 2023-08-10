plugins {
	java
}

repositories {
	mavenCentral()

	// Required for Solr restlet dependency
	maven {
		url = uri("https://maven.restlet.talend.com")
	}
}

dependencies {
	implementation(project(":montysolr"))
}

// Copy libs to build/libs so they can be added to solr's lib path later
tasks.register<Copy>("copyDependenciesToLib") {
    into("$buildDir/libs")
    from(project.configurations.runtimeClasspath)
}

tasks {
	build {
		dependsOn("copyDependenciesToLib")
	}
}
