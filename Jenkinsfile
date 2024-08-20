#!/usr/bin/env groovy
def BranchList = ["master", "dev", "stage"]
def branch = "${env.BRANCH_NAME}"
pipeline {
	agent any
	environment { 
		MAVEN_OPTS="-Xmx8G -Xms4G"
		PRO_VERSION="`mvn org.apache.maven.plugins:maven-help-plugin:3.2.0:evaluate -Dexpression=project.version -q -DforceStdout`"
    }
    stages {
		stage ('Update POM') {
			steps {
				script { 
					if (branch == 'master'){
						sh '''
							echo $PRO_VERSION
							mvn versions:set -DnewVersion=$PRO_VERSION-${BUILD_ID}
						'''
					} else {
						sh '''
							mvn versions:set -DnewVersion=$PRO_VERSION-SNAPSHOT
						'''
					}
				}
			}
		}
		stage ('Build project') {
			steps {
				script { 
					if (branch == 'master' || branch == 'dev'){
						sh '''
							mvn clean deploy
						'''
					} else {
						sh '''
							mvn clean install
						'''
					}
				}
			}
		}
	}
	post {
		always {
			cleanWs()
		}
	}
}
