TODAY_DATE = $(shell date +%Y%m%d)

build-docker:
	./gradlew distTar
	docker build -t "vacxe/telegram-anti-spam:$(TODAY_DATE)" .
	docker save --output "telegram-anti-spam-$(TODAY_DATE).tar" "vacxe/telegram-anti-spam:$(TODAY_DATE)"