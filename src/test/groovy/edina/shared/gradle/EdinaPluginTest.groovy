package edina.shared.gradle

import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertTrue

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before
import org.junit.Test

class EdinaPluginTest {
	
  private Project project;

  @Before
  public void setup() {
    project = ProjectBuilder.builder().build()
    project.apply plugin: 'edina'
  }

  @Test
  public void isMavenPluginApplied() {
    assertNotNull(project.plugins.getPlugin('maven'))
  }

  @Test
  public void isJavaPluginApplied() {
    assertNotNull(project.plugins.getPlugin('java'))
  }

  @Test
  public void isSourceCompatible() {
    def result = project.sourceCompatibility == JavaVersion.VERSION_1_8
    assertTrue(result)
  }

  @Test
  public void hasGeodevRepository() {
    assertNotNull(project.repositories.getByName('geodev'))
  }

}
