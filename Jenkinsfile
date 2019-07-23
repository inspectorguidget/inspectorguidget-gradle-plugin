def githubStatusCheck(String state, String description){
    def commitHash = checkout(scm).GIT_COMMIT
    githubNotify account: 'inspectorguidget',sha: "${commitHash}", status: state, description: description, credentialsId: 'github-token', repo: 'inspectorguidget-gradle-plugin'
}


pipeline {
    agent any

    tools {
        jdk 'jdk11'
    }

    stages {

        stage('Github Pending') {
            steps{
                script{
                    githubStatusCheck("PENDING", "Currently building the project");
                }
            }
        }

        stage ('Tools Info') {
            steps {
                sh '''
                    java -version
                    mvn -v
                '''
            }
        }

        stage ('Git') {
            steps {
                //going to build on the branch master
                git branch: 'master', url: "https://github.com/inspectorguidget/inspectorguidget-gradle-plugin"
            }
        }

        stage ('Artifactory configuration') {
            steps {
                rtServer (
                    id: "InriaArtifactoryServer",
                    url: 'http://maven.irisa.fr/artifactory',
                    credentialsId: 'credRepoInria'
                )

                rtGradleDeployer (
                    id: "gradleDeployer",
                    serverId: "InriaArtifactoryServer",
                    repo: "malai-public-snapshot",
                )
            }
        }

        stage ('Build') {
            steps {
                rtGradleRun (
                    buildFile: 'build.gradle',
                    deployerId : 'gradleDeployer',
                    tasks : 'clean artifactoryPublish',
                    useWrapper: true
                )
            }
        }

        stage ('Publish build info') {
            steps {
                rtPublishBuildInfo (
                    serverId: "InriaArtifactoryServer"
                )
            }
        }
    }

    post{
        success {
            githubStatusCheck("SUCCESS", "Build succeeded");
        }
        failure {
            githubStatusCheck("FAILURE", "Build failed");
        }
    }
}