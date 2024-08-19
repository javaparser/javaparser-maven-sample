#!/usr/bin/env groovy
def BranchList = ["master", "dev", "stage"]
def branch = "${env.BRANCH_NAME}"
pipeline {
	agent any
    stages {
		stage ('Update POM') {
			steps {
				script { 
					sh '''
						#!/bin/bash
						projectVersion=$(mvn org.apache.maven.plugins:maven-help-plugin:3.2.0:evaluate -Dexpression=project.version -q -DforceStdout)
						echo $projectVersion
						if [[ ${GIT_BRANCH} == "master" ]];
						then
							mvn versions:set -DnewVersion=$projectVersion-${BUILD_ID}
						else
							mvn versions:set -DnewVersion=$projectVersion-SNAPSHOT
						fi
					'''
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
