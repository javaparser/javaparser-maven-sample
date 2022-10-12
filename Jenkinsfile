 node {
  stage ('checkout'){
    bat "echo Running Sample_maven1"
    bat "echo Checking out codebase"
    git 'https://github.com/nikkhel/Sample_maven1.git'
    }
    stage ('Build'){
        bat "cd %WORKSPACE% && mvn clean install"
    }
    stage ('Artifactory_upload') {
        bat "echo Running Artifactory Upload"
        bat "echo Artifactory Upload Completed"
    }
    stage ('Sonar_scan') {
        bat "echo Running Sonar scan"
        bat "echo Sonar Scan completed"
    }
    stage ('DEV_DEPLOY'){
        bat "echo Deploying to DEV"
        bat "echo DEV Deployment completed"
    }
    stage ('Selenium Tests') {
        bat "echo Running Selenium tests "
        bat "echo Selenium test completed"
    }
    stage ('QA Deployment Approval') {
        input: 'Deploy to QA'
    }
    stage('QA Deploy') {
        bat "echo Deploying to QA"
        bat "echo QA Deployment is completed"
    }
    stage ('PROD Deployment Approval') {
        input: 'Deploy to PROD'
    }
    stage ('PROD Deploy') {
        bat "echo Deploying to PROD"
        bat "echo PROD Deployment is completed"
    }
 }