build-docker:
	./gradlew distTar
    docker build -t "tgantispam" .
    docker save --output tgantispam.tar tgantispam