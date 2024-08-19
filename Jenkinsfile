#!/usr/bin/env groovy
def BranchList = ["master", "dev", "stage"]
def branch = "${env.BRANCH_NAME}"
def build_num = "${env.BUILD_NUMBER}"
pipeline {
	agent any
    stages {
		stage ('Update POM') {
			steps {
				script { 
					sh '''
						projectVersion=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)
						if [ $branch == 'master' ]
							mvn versions:set -DnewVersion=$projectVersion-$build_num -s settings.xml
						else
							mvn versions:set -DnewVersion=$projectVersion-SNAPSHOT -s settings.xml
					'''
				}
			}
		}
		stage ('Build project') {
			steps {
				script { 
					if (branch == 'master' || branch == 'dev'){
						sh '''
							mvn clean deploy -s settings.xml
						'''
					} else {
						sh '''
							mvn clean install -s settings.xml
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
