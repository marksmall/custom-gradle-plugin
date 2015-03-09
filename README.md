# Shared Edina Gradle Plugin

## Overview

This project defines a re-usable Gradle custom Plugin.  The plugin provides sensible Edina specific configuration and for 
defined plugins.  Each plugin used is defined below.

List of Plugins:

* Java
* War
* Maven
* JaCoCo
* Checkstyle
* PMD
* JDepend
* FindBugs

The above list of plugins are all documented at: http://gradle.org/docs/current/userguide/userguide_single.html

This custom plugin also sets default confuration for project properties e.g. The Java Compiler version (1.8).  This can be 
overridden in specific project **build.gradle** file.

**Integration Tests** default to **src/test/integTest** to separate concerns.

The default **Maven** repositories to be used are:

* jcenter (https://bintray.com/bintray/jcenter)
* Maven Local (Your local Maven Repository e.g. .m2/repository)
* Edina Maven Repository (https://geodev.edina.ac.uk/maven-repository)

Some dependencies should be used by all code, that list may be difficult to agree on but the current list is:

* SLF4J 1.7.9
* JUnit 4.11

The Maven **Provided Scope** is missing from the **Java Plugin**, so we need to provide it ourselves, this is called 
**compileProvided**. e.g.

compileProvided 'junit:junit:4.11'

The **War Plugin** provides this but isn't available for libraries as it creates a *.war, not a *.jar file.  The syntax for 
War projects is:

providedCompile 'javax.servlet:javax.servlet-api:3.0.1'

The War Plugin also provides tasks to package the code into a WAR file.