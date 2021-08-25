FROM java:11-jre
MAINTAINER Sylvie Share <wrz2@mail.ru>
ADD ./target/kfbah.jar /app/
CMD ["java","-jar","/app/smlr.jar"]
EXPOSE 8080