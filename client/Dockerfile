FROM gradle:jdk17 as BUILD_IMAGE
COPY ./app/ /home/app/
WORKDIR /home/app
RUN gradle shadowJar
RUN ls /home/app/build/libs

FROM openjdk:17
COPY --from=BUILD_IMAGE /home/app/build/libs/app-all.jar .
ENTRYPOINT ["java","-jar","./app-all.jar"]
