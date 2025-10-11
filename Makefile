.PHONY: clean start stop

start:
	@echo "Starting..."
	./gradlew clean build
	docker compose up --build -d

stop:
	@echo "Stopping..."
	docker compose down

clean:
	@echo "Cleaning..."
	docker compose down --rmi all --volumes --remove-orphans
	./gradlew clean