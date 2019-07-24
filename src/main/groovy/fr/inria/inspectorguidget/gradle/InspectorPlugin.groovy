package fr.inria.inspectorguidget.gradle

import fr.inria.inspectorguidget.api.analyser.UIDataAnalyser
import fr.inria.inspectorguidget.data.UIData

import com.beust.klaxon.Klaxon
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.logging.LogLevel
import org.gradle.api.logging.Logger

/**
 * Gradle plugin for InspectorGuidget.
 */
class InspectorPlugin implements Plugin<Project> {
  @Override
  void apply(final Project project) {

    def extension = project.extensions.create('inspectorguidget', InspectorExtension)

    project.task('extractData') {
      Logger logger = project.getLogger()
      logger.info("Starting extracting data...")

      PrintWriter pw = null
      def analyser = new UIDataAnalyser()

      logger.info("Adding input ressource...")
      analyser.addInputResource(project.getProjectDir().getPath())

      logger.info("adding dependencies path...")
      Configuration configuration = project.getConfigurations().getByName("compile")
      String[] dependencies = configuration.getDependencies().toArray(String[])
      analyser.setSourceClasspath(dependencies)

      logger.info("Extracting data...")
      UIData data = analyser.extractUIData()

      logger.info("Building data file...")
      try {
        pw = new PrintWriter(extension.filename)  // add parameter to change fileName
        pw.print(new Klaxon().toJsonString(data, null))
      } catch (FileNotFoundException e) {
        logger.log(LogLevel.ERROR, " ", e.fillInStackTrace())

      } finally {
        if (pw != null) {
          pw.close()
        }
      }
    }
  }
}
