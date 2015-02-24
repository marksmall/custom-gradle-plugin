package edina.shared.gradle.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction;

class IntegrationTestTask extends DefaultTask {

  IntegrationTestTask() {
    this.description = 'Run integration tests.'
    this.group = 'Integration'
  }

  @TaskAction
  void start() {
    testClassesDir = sourceSets.integration.output.classesDir
    classpath = sourceSets.integration.runtimeClasspath
  }
  
}
