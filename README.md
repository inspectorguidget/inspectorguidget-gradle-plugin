# inspectorguidget-gradle-plugin
The inspectorguidget plugin to launch front-end analyses on your project to extract data (json format)

## Installation


To be able to run the plugin, you must add the following repositories to your build.gradle in the buildscript block :
```
repositories {
    maven {
        url 'http://maven.inria.fr/artifactory/malai-public-snapshot'
    }
    maven {
        url 'http://maven-eclipse.github.io/maven'
    }
}

```
Then add the plugin and his dependencies in the buildscript block: 
```
dependencies {
  classpath 'fr.inria.inspectorguidget:inspectorguidget-gradle-plugin:1.0-20190725.135204-1'

  classpath gradleApi()
  classpath localGroovy()
  classpath 'com.android.tools.build:gradle:1.5.0'
  classpath "fr.inria.inspectorguidget:inspectorguidget-java-api:1.0-SNAPSHOT"
  classpath "fr.inria.inspectorguidget:inspectorguidget-java:1.0-SNAPSHOT"
}
```
Then apply the plugin outside the buildscript block using:
```
apply plugin: 'fr.inria.inspectorguidget'
```

## Running the plugin

To run the analysis you can either build the project using:
```
gradle build
```

or directly run the extractData task using :
```
gradle extractData
```
the data file (data.json) is created at the root of the project. You can then visualise the extracted data using inspectorguidget-viz.
