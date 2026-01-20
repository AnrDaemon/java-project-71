
all: build

clean:
	./gradlew clean --info

build:
	./gradlew build sonar --info

check:
	./gradlew check

run: IN_FORMAT ?= json
run: OUT_FORMAT ?= stylish
run: VARIANT ?= flat
run:
	java -jar build/libs/app-1.0-SNAPSHOT-all.jar --format $(OUT_FORMAT) src/test/resources/file1-$(VARIANT).$(IN_FORMAT) src/test/resources/file2-$(VARIANT).$(IN_FORMAT)

help:
	java -jar build/libs/app-1.0-SNAPSHOT-all.jar --help

.PHONY: all build check clean help run
