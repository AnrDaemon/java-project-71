
all: build

build:
	./gradlew build

check:
	./gradlew check

run: IN_FORMAT ?= json
run: OUT_FORMAT ?= stylish
run: VARIANT ?= flat
run:
	java -jar build/libs/gendiff-1.0-SNAPSHOT-all.jar --format $(OUT_FORMAT) src/test/resources/file1-$(VARIANT).$(IN_FORMAT) src/test/resources/file2-$(VARIANT).$(IN_FORMAT)

help:
	java -jar build/libs/gendiff-1.0-SNAPSHOT-all.jar --help

.PHONY: all build check help run
