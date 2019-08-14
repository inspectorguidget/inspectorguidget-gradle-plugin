/*
 * This file is part of InspectorGuidget.
 * InspectorGuidget is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * InspectorGuidget is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with InspectorGuidget.  If not, see <https://www.gnu.org/licenses/>.
 */

package fr.inria.inspectorguidget.gradle

import fr.inria.inspectorguidget.api.analyser.UIDataAnalyser
import fr.inria.inspectorguidget.data.UIData

import com.beust.klaxon.Klaxon
import org.gradle.api.Plugin
import org.gradle.api.Project


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

      if(extension.filename == null){
        extension.filename = "data.json"
      }

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
