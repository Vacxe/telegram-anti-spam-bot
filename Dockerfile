FROM openjdk:17-jdk-slim

COPY build/distributions/tgantispam.tar /

RUN tar -xvf "tgantispam.tar" -C /usr/local

ENV PATH="${PATH}:/usr/local/tgantispam/bin/"

ENTRYPOINT ["tgantispam"]
