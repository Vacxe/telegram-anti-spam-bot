FROM eclipse-temurin:23-jdk

ARG BOT_VERSION

RUN apt-get update
RUN apt-get install -y wget unzip

# Install released Version from artefacts
RUN wget -q "https://github.com/Vacxe/telegram-anti-spam-bot/releases/download/$BOT_VERSION/telegram-anti-spam-bot.tar" && \
    tar -xvf "telegram-anti-spam-bot.tar" -C /usr/local &&  \
    rm "telegram-anti-spam-bot.tar"

ENV PATH="${PATH}:/usr/local/telegram-anti-spam-bot/bin/"

ENTRYPOINT ["telegram-anti-spam-bot"]