image: gradle:alpine

stages:
  - build
  - test

build:
  stage: build
  script: ./gradlew build

test:
  stage: test
  script: ./gradlew check
