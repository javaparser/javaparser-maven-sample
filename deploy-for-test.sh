#!/bin/bash

### Remove old war files
rm -rf /tomcat/test/webapps/student*

### Copy the war file
cp /var/lib/jenkins/workspace/sample-test1/target/student*.war /tomcat/test/webapps/student.war

sleep 20

RESPONSE=$(curl -s -I http://localhost:8082/student/index.jsp | head -1  | awk '{print $2}')
echo RESPONSE = $RESPONSE
if [ "$RESPONSE" != 200 ]; then
  echo "Response from student APP is not OK"
  exit 1
fi

URL=$(cat /var/lib/jenkins/jobs/sample-test1/builds/$BUILD_NUMBER/log | grep Uploaded | grep war |xargs -n1 | grep ^http)
sed -i -e "s|URL-LOC|$URL|" deploy.yml
