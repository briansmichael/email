FROM bellsoft/liberica-openjre-alpine-musl:11
WORKDIR /
ARG VERSION
ADD /target/email-$VERSION.jar app.jar
EXPOSE 8080
CMD java -jar app.jar