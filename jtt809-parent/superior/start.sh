#!/bin/bash
##### 业务模块自定义配置区 开始 ##### 
EXE_JAR_NAME="superior-0.0.1-SNAPSHOT.jar"
APP_JVM_CONFIG="-Xmx1024m -Xms512m -XX:+PrintGCDetails -XX:+PrintHeapAtGC -XX:+PrintGCDateStamps -XX:+PrintTenuringDistribution -XX:+HeapDumpOnOutOfMemoryError -verbose:gc -Xloggc:/opt/logs/${APP_NAME%.*}/gc.log -XX:HeapDumpPath=/opt/logs/${APP_NAME%.*}"
##### 业务模块自定义配置区 结束 ##### 
APP_NAME=${EXE_JAR_NAME%.*}
PROFILE="--spring.profiles.active=common,prod"
PF=$2
if [  $PF ]; then  
  case "$PF" in
   "prod")
     PROFILE="--spring.profiles.active=common,prod"
     ;;
   "dev")
     PROFILE="--spring.profiles.active=common,dev"
     ;;
   "test")
     PROFILE="--spring.profiles.active=common,test"
     ;;
   *)
     PROFILE="--spring.profiles.active=common,prod"
     ;;
esac
fi    

source /etc/profile
SHELL_FOLDER=$(cd "$(dirname "$0")";pwd)
common_prp_file=$SHELL_FOLDER/../application-common.properties

FULLPATH_EXE_JAR_NAME=$SHELL_FOLDER/$EXE_JAR_NAME

if [ -f $common_prp_file ];then
    /bin/cp -rf  ${common_prp_file} $SHELL_FOLDER
else
    echo file $common_prp_file not exist!
fi

#使用说明，用来提示输入参数
usage() {
    echo "Usage: sh start.sh [start|stop|restart|status]"
    exit 1
}

#检查程序是否在运行
is_exist() { 
    pid=`ps -ef | grep $APP_NAME | grep -v grep  | grep -v sh | awk '{print $2}'` 
    #如果不存在返回1，存在返回0
    if [ -z "${pid}" ]; then
      return 1
    else
      return 0
    fi
}

#启动方法
start() {
   is_exist
   if [ $? -eq "0" ]; then
     echo "${APP_NAME} is running. pid=${pid} we will stop it frist."
	 stop
   fi
   mkdir -p $SHELL_FOLDER/logs
   echo "start to run ${APP_NAME}"
   app_min_name=${APP_NAME##*/}

   nohup java -jar $APP_JVM_CONFIG  $FULLPATH_EXE_JAR_NAME >/dev/null 2>&1 &
}

#停止方法
stop() {
   is_exist
   if [ $? -eq "0" ]; then
     echo "${APP_NAME} $pid is stoped"
      for id in $pid
       do
          kill -9 $id
       done
   else
     echo "${APP_NAME} is not running"
   fi
}

#输出运行状态
status() {
   is_exist
   if [ $? -eq "0" ]; then
     echo "${APP_NAME} is running. Pid is ${pid}"
   else
     echo "${APP_NAME} is not running."
   fi
}

#重启
restart() {
   stop
   start
}

#根据输入参数，选择执行对应方法，不输入则执行使用说明
case "$1" in
   "start")
     start
     ;;
   "stop")
     stop
     ;;
   "status")
     status
     ;;
   "restart")
     restart
     ;;
   *)
     usage
     ;;
esac
