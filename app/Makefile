
all: build

build:
	./gradlew build

check:
	./gradlew check

run: FORMAT ?= json
run:
	java -jar build/libs/gendiff-1.0-SNAPSHOT-all.jar src/test/resources/file1.$(FORMAT) src/test/resources/file2.$(FORMAT)

.PHONY: all build check run
