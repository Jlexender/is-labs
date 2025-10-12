.PHONY: clean build push serve deploy

BUILD_PATH:=app/build/libs
WILDFLY_PATH=/home/studs/s408724/wildfly
USERNAME=s408724
HOST=se.ifmo.ru

clean:
	@echo "Cleaning..."
	./gradlew clean

build: 
	@echo "Building..."
	./gradlew clean build

serve: build
	@echo "Pushing to server..."
	scp -P 2222 $(BUILD_PATH)/*.war \
				$(BUILD_PATH)/*.war.dodeploy \
				$(USERNAME)@$(HOST):$(WILDFLY_PATH)/standalone/deployments/

push:
	@echo "Pushing to git..."
	git add .
	git commit
	git push


HTTP_SRV_PORT=8841
HTTP_MGMT_PORT=18800







