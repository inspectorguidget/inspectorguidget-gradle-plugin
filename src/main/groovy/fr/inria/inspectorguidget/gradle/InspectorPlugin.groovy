package fr.inria.inspectorguidget.gradle

import fr.inria.inspectorguidget.api.analyser.UIDataAnalyser
import fr.inria.inspectorguidget.data.UIData

import com.beust.klaxon.Klaxon
import org.apache.log4j.lf5.LogLevel
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.logging.Logger
import org.gradle.internal.impldep.org.testng.collections.Lists

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
      logger.info("Starting extracting data...")

      def analyser = new UIDataAnalyser()
      def pw

      logger.info("Adding input ressource...")
      analyser.addInputResource(project.getProjectDir().getPath())

      logger.info("adding dependencies path...")

      List dep = new ArrayList()

      project.configurations.each { conf ->
        conf.getAllArtifacts().each { art ->
          dep.add(art.file.getAbsolutePath())
        }
      }

      String[] dependencies = new String[dep.size()];
      for(int i=0; i<dep.size();i++){
        dependencies[i] = dep.get(i)
      }
      analyser.setSourceClasspath(dependencies)

      logger.info("Extracting data...")
      UIData data = analyser.extractUIData()

      logger.info("Building data file...")
      try {
        pw = new PrintWriter(extension.filename)  // add parameter to change fileName
        pw.print(new Klaxon().toJsonString(data, null))
      } catch (FileNotFoundException e) {
        logger.error("can't write in data.json" , e.printStackTrace())

      } finally {
        if (pw != null) {
          pw.close()
        }
      }
    }
  }
}
