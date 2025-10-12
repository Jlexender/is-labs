.PHONY: clean build push

BUILD_PATH:=app/build/libs/
DEPLOY_PATH=/home/studs/s408724/wildfly/standalone/deployments/
USERNAME=s408724
HOST=se.ifmo.ru

clean:
	@echo "Cleaning..."
	./gradlew clean

build: 
	@echo "Building..."
	./gradlew build

serve: build
	@echo "Pushing to server..."
	scp -P 2222 $(BUILD_PATH)*.war $(USERNAME)@$(HOST):$(DEPLOY_PATH)

push:
	@echo "Pushing to git..."
	git add .
	git commit
	git push


