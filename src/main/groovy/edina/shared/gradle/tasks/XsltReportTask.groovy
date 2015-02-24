package edina.shared.gradle.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

class XsltReportTask extends DefaultTask {
  
  @InputFile
  File inputFile
  
  @InputFile
  File xslStyleFile
  
  @Input
  @Optional
  Map<String, String> params = [:]
  
  @OutputFile
  File outputFile
  
  XsltReportTask() {
    onlyIf {
      inputFile.exists()
    }
  }
  
  @TaskAction
  void start() {
    ant.xslt(in: inputFile, style: xslStyleFile, out: outputFile) {
      params.each { key, value ->
        ant.param(name: key, expression: value)
      }
    }
  }

}
