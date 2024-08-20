#!/usr/bin/env groovy
def BranchList = ["master", "dev", "stage"]
def branch = "${env.BRANCH_NAME}"
pipeline {
	agent any
    stages {
		stage ('Update POM') {
			steps {
				script { 
					sh "projectVersion=`mvn org.apache.maven.plugins:maven-help-plugin:3.2.0:evaluate -Dexpression=project.version -q -DforceStdout`"
					if (branch == 'master' || branch == 'dev'){
						sh '''
							mvn versions:set -DnewVersion=$projectVersion-${BUILD_ID}
						'''
					} else {
						sh '''
							mvn versions:set -DnewVersion=$projectVersion-SNAPSHOT
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
