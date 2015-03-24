package edina.shared.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project


class EdinaPlugin implements Plugin<Project> {

  static final String EXTENSION_NAME = 'edinaArgs'

  @Override
  void apply(Project project) {
    project.extensions.create(EXTENSION_NAME, EdinaPluginExtension)
  }
  
}
