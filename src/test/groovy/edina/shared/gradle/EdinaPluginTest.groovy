package edina.shared.gradle

import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertTrue

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

class EdinaPluginTest {
	
  private Project project;

  @Before
  public void setup() {
    project = ProjectBuilder.builder().build()
    project.apply plugin: 'java'
    project.apply plugin: 'edina'
  }

  @Test
  public void isMavenPluginApplied() {
    assertNotNull(project.plugins.getPlugin('maven'))
  }
  
  @Test
  public void isJaCoCoPluginApplied() {
    assertNotNull(project.plugins.getPlugin('jacoco'))
  }
  
  @Test
  public void isCheckstylePluginApplied() {
    assertNotNull(project.plugins.getPlugin('checkstyle'))
  }
  
  @Test
  public void isPmdPluginApplied() {
    assertNotNull(project.plugins.getPlugin('pmd'))
  }
  
  @Test
  public void isJDependPluginApplied() {
    assertNotNull(project.plugins.getPlugin('jdepend'))
  }
  
  @Test
  public void isFindBugsPluginApplied() {
    assertNotNull(project.plugins.getPlugin('findbugs'))
  }

  @Test
  public void isSourceCompatible() {
    println 'Source: ' + project.sourceCompatibility
    assertTrue(project.sourceCompatibility == JavaVersion.VERSION_1_7)
  }

  @Test
  public void hasGeodevRepository() {
    assertNotNull(project.repositories.getByName('geodev'))
  }

  @Ignore
  public void hasProvidedCompileScope() {
    println 'Scope: ' + project.configurations.getByName(EdinaPlugin.PROVIDED_COMPILE_CONFIGURATION_NAME)
    assertNotNull(project.configurations.getByName(EdinaPlugin.PROVIDED_COMPILE_CONFIGURATION_NAME))
  }

  @Ignore
  public void hasProvidedRuntimeScope() {
    println 'Scope: ' + project.configurations.getByName(EdinaPlugin.PROVIDED_RUNTIME_CONFIGURATION_NAME)
    assertNotNull(project.configurations.getByName(EdinaPlugin.PROVIDED_RUNTIME_CONFIGURATION_NAME))
  }

  @Test
  public void hasIntegrationSourceSet() {
    println 'SourceSet: ' + project.sourceSets.integration
    assertNotNull(project.sourceSets.integration)
  }

  @Ignore
  public void isIntegrationTaskDependentOnTest() {
//    println 'Tasks: ' + project.tasks.getByName('integration').dependsOn
    assertTrue(project.tasks.getByName('integration').dependsOn.contains(project.tasks.getByName('test')))
  }

  @Ignore
  public void isInstallTaskDependentOnTest() {
//    println 'Tasks: ' + project.tasks.getByName('install').dependsOn
    assertTrue(project.tasks.getByName('install').dependsOn.contains(project.tasks.getByName('test')))
  }

  @Ignore
  public void isUploadArchivesTaskDependentOnTest() {
//    println 'Tasks: ' + project.tasks.getByName('integration').dependsOn
    assertTrue(project.tasks.getByName('uploadArchives').dependsOn.contains(project.tasks.getByName('test')))
  }

  @Ignore
  public void hasSlf4jDependency() {
    // TODO: How do we get a list of dependencies.
    println 'Dependency: ' + project.dependencies.modules
    println 'Component: ' + project.dependencies.components
    println 'Config: ' + project.configurations['compile'].name
  }

}
