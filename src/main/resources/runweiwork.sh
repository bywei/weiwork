#!/bin/bash
source /etc/profile
WORK_DIR="/home/weiwork/apps/"
LOGS_DIR="/home/weiwork/logs/"
cd $WORK_DIR
JAR_NAME=weiwork-1.5.9.RELEASE.jar
APP_NAME=weiwork

cd $WORK_DIR

if (ps aux | grep "$APP_NAME" | grep -v "grep") then
  echo kill $APP_NAME
  ps x | grep $APP_NAME | grep -v grep | awk '{print $1}' | xargs kill -9
  sleep 5
fi
 
echo  Starting run JAVA JAR $APP_NAME
nohup java -jar $JAR_NAME >> $LOGS_DIR/$APP_NAME.console.log 2>&1 &