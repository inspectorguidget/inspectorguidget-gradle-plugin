package fr.inria.inspectorguidget.gradle

import fr.inria.inspectorguidget.api.analyser.UIDataAnalyser
import fr.inria.inspectorguidget.data.UIData

import com.beust.klaxon.Klaxon
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.logging.Logger

import javax.swing.JList

/**
 * Gradle plugin for InspectorGuidget.
 */
class InspectorPlugin implements Plugin<Project> {
  @Override
  void apply(final Project project) {
    def extension = project.extensions.create('inspectorguidget', InspectorExtension)

    project.task('extractData') {
      
      def logger = project.getLogger()
      logger.lifecycle("Starting extracting data...")

      def analyser = new UIDataAnalyser()
      def pw

      logger.lifecycle("Adding input ressource...")
      analyser.addInputResource(project.getProjectDir().getPath())

      logger.lifecycle("adding dependencies path...")

      JList<String> dependencies

      project.configurations.each { conf ->
        println "    Configuration: ${conf.name}"
        conf.allDependencies.each { dep ->
          dependencies.add(dep.toString())
        }
      }
      String[] mydep = new String[dependencies.size()]
      for(int i=0; i<dependencies.length;i++){
        mydep[i] = dependencies.getComponent(i)
        logger.lifecycle(mydep[i])
      }


      //analyser.setSourceClasspath(dependencies)

      logger.lifecycle("Extracting data...")
      /*UIData data = analyser.extractUIData()

      logger.lifecycle("Building data file...")
      try {
        pw = new PrintWriter(extension.filename)  // add parameter to change fileName
        pw.print(new Klaxon().toJsonString(data, null))
      } catch (FileNotFoundException e) {
        logger.log(LogLevel.ERROR, " ", e.fillInStackTrace())

      } finally {
        if (pw != null) {
          pw.close()
        }
      }*/
    }
  }
}
