package edina.shared.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.MavenPlugin
import org.gradle.api.plugins.quality.FindBugs


class EdinaPlugin implements Plugin<Project> {

  static final String EXTENSION_NAME = 'edinaArgs'
  static final String PROVIDED_COMPILE_CONFIGURATION_NAME = "compileProvided";
  static final String PROVIDED_RUNTIME_CONFIGURATION_NAME = "runtimeProvided";

  @Override
  void apply(Project project) {
    project.extensions.create(EXTENSION_NAME, EdinaPluginExtension)

    addRepositories(project)
    configurePlugins(project)
    addDependencies(project)
    addProvidedScope(project)
    configureSourceSets(project)
  }
  
  private void configureSourceSets(Project project) {
    project.sourceSets {
      integrationTest {
        java {
          srcDir 'src/test/integTest/java'
        }
        resources {
          srcDir 'src/test/integTest/resources'
        }

        compileClasspath = project.sourceSets.main.output + project.configurations.testRuntime
        runtimeClasspath = output + compileClasspath
      }
    }
  }
  
  /**
   * Add and configure plugins for all Java based projects.
   * 
   * @param project
   */
  private void configurePlugins(Project project) {
    project.plugins.apply(JavaPlugin)
    project.sourceCompatibility = '1.8'
//    println "Java Compiler: ${project.sourceCompatibility}"

    project.plugins.apply(MavenPlugin)

    // Set dependency between maven install and Java test tasks.
    def installTask = project.tasks.getByPath("install")
    def testTask = project.tasks.getByPath("test")
//    println "Default Tasks: ${installTask}, ${testTask}"
    installTask.dependsOn(testTask)
        
    // Add Code Coverage tools.
    project.plugins.apply('jacoco')
    project.plugins.apply('checkstyle')
    project.plugins.apply('pmd')
    project.plugins.apply('jdepend')
    project.plugins.apply('findbugs')
    project.tasks.withType(FindBugs) {
      reports {
        xml.enabled = false
        html.enabled = true
      }
    }
  }
  
  private void addRepositories(Project project) {
    project.repositories {
      jcenter()
      mavenLocal()
      maven {
        name 'geodev'
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
  
  private void addProvidedScope(Project project) {
    def configurations = project.configurations

    def provideCompileConfiguration = configurations.create(PROVIDED_COMPILE_CONFIGURATION_NAME).setVisible(false)
    provideCompileConfiguration.setDescription("Additional compile classpath for libraries that should not be part of the WAR archive.")

    def provideRuntimeConfiguration = configurations.create(PROVIDED_RUNTIME_CONFIGURATION_NAME).setVisible(false) //.extendsFrom(provideCompileConfiguration)
    provideRuntimeConfiguration.setDescription("Additional runtime classpath for libraries that should not be part of the WAR archive.")

    configurations.getByName(JavaPlugin.COMPILE_CONFIGURATION_NAME).extendsFrom(provideCompileConfiguration)
    configurations.getByName(JavaPlugin.RUNTIME_CONFIGURATION_NAME).extendsFrom(provideRuntimeConfiguration)
        
//      project.configurations {
//              provided
//      }
//      
//      project.sourceSets {
//              main { compileClasspath += configurations.provided }
//      }
  }

}
