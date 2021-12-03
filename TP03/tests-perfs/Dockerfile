###############################
# Building the application
###############################
FROM mozilla/sbt:8u212_1.2.8


WORKDIR /usr/src/app

# Cache backend dependencies
COPY build.sbt build.sbt
COPY project project


# Install dependencies
RUN sbt update

COPY src src
RUN sbt test:compile

ENTRYPOINT ["sbt"]
CMD ["gatling:test"]

