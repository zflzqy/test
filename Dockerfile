# 基础镜像
FROM openjdk:11-jre-slim
# 作者信息
MAINTAINER zflzqy
# 容器目录分配数据卷
#VOLUME /opt/project/myApp/config /config
# 设置时区
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone
# 拷贝jar 把可执行jar包复制到基础镜像的根目录下
COPY target/*.jar /app.jar
#ADD config /config
# 设置暴露的端口号
EXPOSE  8080
# 在镜像运行为容器后执行的命令
ENTRYPOINT ["java","-jar","app.jar","-Dloader.path=./lib","-XX:+UnlockExperimentalVMOptions","XX:+UseCGroupMemoryLimitForHeap"]

