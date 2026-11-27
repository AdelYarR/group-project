.PHONY: build run

build:
	mvnw.cmd clean package

run:
	java -jar target/project-1.0.jar --config src/main/resources/my_config.json
