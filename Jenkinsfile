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
							mvn clean deploy -s settings.xml
						'''
					} else {
						sh '''
							mvn clean deploy -s settings.xml
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
