FROM openjdk:11
MAINTAINER Sylvie Share <wrz2@mail.ru>
ADD ./target/kfbah.jar /app/
CMD ["java","-jar","/app/kfbah.jar"]
EXPOSE 8080