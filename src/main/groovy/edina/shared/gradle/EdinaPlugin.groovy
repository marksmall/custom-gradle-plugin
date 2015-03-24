package edina.shared.gradle

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.quality.FindBugs


class EdinaPlugin implements Plugin<Project> {

  static final String EXTENSION_NAME = 'edinaArgs'

  @Override
  void apply(Project project) {
    project.extensions.create(EXTENSION_NAME, EdinaPluginExtension)
   
    configureRepositories(project) 
    configurePlugins(project)
    configureSourceSets(project)
  }
  
  private void configureRepositories(Project project) {
    project.repositories {
      jcenter()
      mavenLocal()
      maven {
        name 'geodev'
        url 'https://geodev.edina.ac.uk/maven-repository'
      }
    }
  }
  
  /**
   * Add and configure plugins for all Java based projects.
   * 
   * @param project
   */
  private void configurePlugins(Project project) {
    project.sourceCompatibility = JavaVersion.VERSION_1_7
    
    // Apply and configure common plugins.
    project.configure(project) {
      apply plugin: 'jacoco'
      apply plugin: 'pmd'
      apply plugin: 'jdepend'
      apply plugin: 'findbugs'
      apply plugin: 'checkstyle'
      apply plugin: 'maven'
    }
    project.tasks.withType(FindBugs) {
      reports {
        xml.enabled = false
        html.enabled = true
      }
    }
  }
  
  /**
   * Add extra sourcesets common to any project.
   * 
   * @param project
   */
  private void configureSourceSets(Project project) {
    project.sourceSets {
      integration {
        java {
          srcDir 'src/test/integration/java'
        }
        resources {
          srcDir 'src/test/integration/resources'
        }

        compileClasspath = project.sourceSets.main.output + project.configurations.testRuntime
        runtimeClasspath = output + compileClasspath
      }
    }
  }
  
}
