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
							PRO_VERSION=`mvn org.apache.maven.plugins:maven-help-plugin:3.2.0:evaluate -Dexpression=project.version -q -DforceStdout -B`
							mvn versions:set -DnewVersion=$PRO_VERSION-${BUILD_ID} -s settings.xml -B
						'''
					} else {
						sh '''
							cat pom.xml
							PRO_VERSION=`mvn org.apache.maven.plugins:maven-help-plugin:3.2.0:evaluate -Dexpression=project.version -q -DforceStdout -B`
							mvn versions:set -DnewVersion=$PRO_VERSION-SNAPSHOT -s settings.xml -B
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
							mvn clean deploy -B
						'''
					} else {
						sh '''
							mvn clean install -B
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
