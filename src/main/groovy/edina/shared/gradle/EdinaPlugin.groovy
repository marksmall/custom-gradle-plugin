package edina.shared.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.WarPlugin


class EdinaPlugin implements Plugin<Project> {

  static final String EXTENSION_NAME = 'edinaArgs'

  void apply(Project project) {
    project.extensions.create(EXTENSION_NAME, EdinaPluginExtension)

    addRepositories(project)
	addPlugins(project)
    addDependencies(project)
    addTasks(project)	
  }
  
  private void addTasks(Project project) {
	  
  }
  
  private void addPlugins(Project project) {
    project.plugins.apply(WarPlugin)
	project.sourceCompatibility = '1.8'
  }
  
  private void addRepositories(Project project) {
  	project.repositories {
      jcenter()
	  mavenLocal()
	  maven {
        url 'https://geodev.edina.ac.uk/maven-repository'
      }
    }
  }
  
  private void addDependencies(Project project) {
    project.dependencies {
      compile 'org.slf4j:slf4j-api:1.7.9'
      testCompile 'junit:junit:4.11'
    }
  }
  
}
