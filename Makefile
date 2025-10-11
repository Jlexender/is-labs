.PHONY: start stop

start:
	@echo "Starting..."
	./gradlew clean build
	docker compose up --build -d

stop:
	@echo "Stopping..."
	docker compose down