PROJECT_DIR := spring-boot-angular-basic-lw2
FRONTEND_DIR := $(PROJECT_DIR)/src/main/js/my-app
K6_SCRIPT := k6/load-test.js
BACKEND_URL ?= http://localhost:8081

.PHONY: test-api test-e2e test-load help install-e2e start-backend check-backend

help:
	@echo "Automation testing commands:"
	@echo "  make test-api       - REST API tests (MockMvc, no server required)"
	@echo "  make test-e2e       - Cypress E2E tests (backend + frontend must be running)"
	@echo "  make test-load      - k6 load test (backend must be running on port 8081)"
	@echo "  make start-backend  - Start Spring Boot backend (port 8081)"
	@echo "  make install-e2e    - Install npm deps and Cypress binary"

install-e2e:
	cd $(FRONTEND_DIR) && npm install && npx cypress install

start-backend:
	cd $(PROJECT_DIR) && mvn spring-boot:run

check-backend:
	@if ! curl -sf "$(BACKEND_URL)/students" > /dev/null 2>&1; then \
		echo "Error: Backend not reachable at $(BACKEND_URL)/students"; \
		echo ""; \
		echo "Start the backend in another terminal first:"; \
		echo "  make start-backend"; \
		echo "  # or: cd $(PROJECT_DIR) && mvn spring-boot:run"; \
		exit 1; \
	fi
	@echo "Backend is running at $(BACKEND_URL)"

test-api:
	cd $(PROJECT_DIR) && mvn test

test-e2e: install-e2e
	cd $(FRONTEND_DIR) && npm run e2e

test-load: check-backend
	@if command -v k6 >/dev/null 2>&1; then \
		BASE_URL="$(BACKEND_URL)" k6 run $(K6_SCRIPT); \
	elif command -v docker >/dev/null 2>&1; then \
		docker run --rm --network host -e BASE_URL="$(BACKEND_URL)" -i grafana/k6 run - < $(K6_SCRIPT); \
	else \
		echo "Error: k6 not found."; \
		echo "Install k6: https://grafana.com/docs/k6/latest/set-up/install-k6/"; \
		echo "Or install Docker to run k6 via container."; \
		exit 1; \
	fi
