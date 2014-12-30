package edina.shared.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.MavenPlugin


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
    project.plugins.apply(JavaPlugin)
	project.sourceCompatibility = '1.8'
	println "Java Compiler: ${project.sourceCompatibility}"

    project.plugins.apply(MavenPlugin)

	// TODO: Set dependency between maven install and Java test tasks.
	def installTask = project.tasks.getByPath("install")
	def testTask = project.tasks.getByPath("test")
	println "Default Tasks: ${installTask}, ${testTask}"
	installTask.dependsOn(testTask)
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
