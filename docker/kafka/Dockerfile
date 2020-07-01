FROM ubuntu:bionic

WORKDIR /opt

RUN apt-get clean
RUN apt-get update -y --fix-missing
RUN apt-get install -y nano zip python curl apt-utils iputils-ping wget

RUN wget https://econdatasci.s3.eu-west-2.amazonaws.com/dev/jdk-8u231-linux-x64.tar.gz
RUN wget https://econdatasci.s3.eu-west-2.amazonaws.com/dev/kafka_2.12-2.5.0.tgz

RUN tar -xzf kafka_2.12-2.5.0.tgz
RUN tar -xzf jdk-8u231-linux-x64.tar.gz

RUN addgroup hadoop
RUN useradd -ms /bin/bash  hduser
RUN usermod -a -G hadoop hduser
RUN chown -R hduser:hadoop /opt/kafka_2.12-2.5.0

ENV JAVA_HOME /opt/jdk1.8.0_231

USER hduser

#kafka default ports:

#    9092, can be changed on server.properties;

#zookeeper default ports:

#    2181 for client connections;
#    2888 for follower(other zookeeper nodes) connections;
#    3888 for inter nodes connections;


EXPOSE 9092 2181 2888 3888

WORKDIR /opt/kafka_2.12-2.5.0

CMD ["bin/kafka-server-start.sh","config/server.properties"]
