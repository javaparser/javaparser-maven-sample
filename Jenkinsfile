#!/usr/bin/env groovy
def BranchList = ["master", "dev", "stage"]
def branch = "${env.BRANCH_NAME}"
pipeline {
	agent any
    stages {
		stage ('Update POM') {
			steps {
				script { 
					if (branch == 'master'){
						sh '''
							PRO_VERSION=`mvn org.apache.maven.plugins:maven-help-plugin:3.2.0:evaluate -Dexpression=project.version -q -DforceStdout`
							mvn versions:set -DnewVersion=$PRO_VERSION-${BUILD_ID} -s settings.xml
						'''
					} else {
						sh '''
							PRO_VERSION=`mvn org.apache.maven.plugins:maven-help-plugin:3.2.0:evaluate -Dexpression=project.version -q -DforceStdout`
							mvn versions:set -DnewVersion=$PRO_VERSION-SNAPSHOT -s setting.xml
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
