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
						projectVersion=$(mvn org.apache.maven.plugins:maven-help-plugin:3.2.0:evaluate -Dexpression=project.version -q -DforceStdout)
						env
						if [[ $branch == "master" ]];
						then
							mvn versions:set -DnewVersion=$projectVersion-$build_num
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
