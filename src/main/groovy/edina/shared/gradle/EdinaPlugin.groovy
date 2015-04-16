package edina.shared.gradle

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.quality.FindBugs
import org.gradle.logging.StyledTextOutputFactory
import org.gradle.logging.StyledTextOutput.Style

import edina.shared.gradle.tasks.IntegrationTestTask


class EdinaPlugin implements Plugin<Project> {

  static final String EXTENSION_NAME = 'edinaArgs'
  static final String PROVIDED_COMPILE_CONFIGURATION_NAME = "compileProvided";
  static final String PROVIDED_RUNTIME_CONFIGURATION_NAME = "runtimeProvided";

  @Override
  void apply(Project project) {
    project.buildscript {
      repositories {
        jcenter()
      }
      dependencies {
        classpath 'net.researchgate:gradle-release:2.0.2'
      } 
    }
    
    project.apply(plugin: 'net.researchgate.release')
    
    project.extensions.create(EXTENSION_NAME, EdinaPluginExtension)
   
    configureRepositories(project) 
    configureDependencies(project)
    configurePlugins(project)
    configureSourceSets(project)
    configureTasks(project)
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

    project.plugins.withId('java') {
      // configure all java projects
      addProvidedScope(project)
    }

    project.plugins.withId('war') {
      // additional configuration for war projects
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
  
  private void configureDependencies(Project project) {
    project.configurations {
      deployerJars
    }
    
    project.dependencies {
      compile 'org.slf4j:slf4j-api:1.7.9'
      compile 'net.researchgate:gradle-release:2.0.2'
      testCompile 'junit:junit:4.11'
      deployerJars 'org.apache.maven.wagon:wagon-ssh:2.2'
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
  }

  private void configureTasks(Project project) {
    project.task('integration', type: IntegrationTestTask)
    
    // Set dependency between integration and test tasks.
    def integrationTask = project.tasks.getByPath("integration")
    def testTask = project.tasks.getByPath("test")
    integrationTask.dependsOn(testTask)

    // Set dependency between install, uploadArchives and test tasks.
    def installTask = project.tasks.getByPath("install")
    def deployTask = project.tasks.getByPath("uploadArchives")
    installTask.dependsOn(testTask)
    deployTask.dependsOn(testTask)

    // Configure styled test output, tests have 3 status'
    // * GREEN  - PASSED
    // * YELLOW - SKIPPED
    // * RED    - FAILED
    System.setProperty("org.gradle.color.failure", "RED")
    System.setProperty("org.gradle.color.progressstatus", "YELLOW")
    System.setProperty("org.gradle.color.success", "GREEN")
    project.tasks.getByName("test") {
      def out = services.get(StyledTextOutputFactory).create("colored-test-output")
      out.style(Style.Normal)

      beforeSuite { suite ->
        if (suite.name.startsWith("Test Run") || suite.name.startsWith("Gradle Worker")) return
        out.println("\n" + suite.name)
      }
      afterTest { descriptor, result ->
        def style
        if (result.failedTestCount > 0) style = Style.Failure
        else if (result.skippedTestCount > 0) style = Style.ProgressStatus
        else style = Style.Success

        out.text('  ').withStyle(style).println(descriptor.name)
      }
    }
    
    project.uploadArchives {
      repositories.mavenDeployer {
        configuration = project.configurations.deployerJars
        repository(url: project.properties['edinaRepository']) {
          authentication(userName: project.properties['mavenUsername'], password: project.properties['mavenPassword'])
        }
      }
    }
  }
  
}
