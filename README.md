# Cloud Youth Banking Microservices System

## 📌 Project Overview

This project is a **cloud‑simulated youth banking system** built using open‑source tools and a microservices architecture. It demonstrates how to design, secure, and monitor distributed backend services using **Java Spring Boot**, **PostgreSQL**, and **Kong API Gateway**.

The system focuses on secure API access and youth banking rules (age 13–25) enforced at the gateway level.

---

## 🏗 Architecture

The system follows a microservices architecture with the following components:

* **Account Service (Spring Boot)** – Manages user accounts
* **Transaction Service (Spring Boot)** – Handles banking transactions
* **Kong API Gateway** – Routes requests and applies security rules
* **FastAPI Backend (optional integration)** – API service exposed through Kong
* **Prometheus** – Metrics and monitoring
* **PostgreSQL** – Database storage

```
Client
   ↓
Kong API Gateway
   ↓
-----------------------------
|     Eureka Server         |
-----------------------------
     ↓              ↓
Account Service   Transaction Service
     ↓              ↓
        PostgreSQL Database
```

---

## 📂 Project Structure

```
cloud-banking/
│
├── pom.xml                  # Parent Maven configuration
├── kong.yml                 # Kong gateway configuration
│
├── account-service/         # Account microservice
│   └── AccountServiceApplication.java
│
└── transaction-service/     # Transaction microservice
```

The parent Maven project manages dependencies and modules for all services.

---

## ⚙️ Technologies Used

* **Java 17**
* **Spring Boot 3.2**
* **Maven (Multi‑module project)**
* **Kong API Gateway**
* **JWT Authentication**
* **PostgreSQL**
* **Kubernetes**  
* **Terraform** 
* **jenkins**
* **Prometheus Monitoring**
* **Docker (optional)**

---

## 🔐 Security Features

The Kong API Gateway enforces:

### ✅ JWT Authentication

Clients must provide a valid JWT token to access the API.

### ✅ Youth Age Verification

A custom Kong pre‑function plugin checks the JWT `age` claim:

* Allowed age: **13–25 years**
* Requests outside this range are rejected

### ✅ Metrics Collection

Prometheus plugin collects API performance metrics.

---

## 🚀 Getting Started

### 1. Prerequisites

Make sure the following are installed:

* Java 17+
* Maven
* Docker & Docker Compose (recommended)
* PostgreSQL

---

### 2. Clone the Repository

```bash
git clone https://github.com/your-username/cloud-banking.git
cd cloud-banking
```

---

### 3. Build the Project

```bash
mvn clean install
```

This builds both microservices:

* account-service
* transaction-service

---

### 4. Run Account Service

```bash
cd account-service
mvn spring-boot:run
```

The service will start on the configured port.

---

### 5. Configure Kong Gateway

Load the declarative Kong configuration:

```bash
kong config db_import kong.yml
```

Or run Kong using Docker with the config mounted.

---

### 6. Access the API

All requests go through Kong:

```
http://localhost:8000/api
```

A valid JWT token with an `age` claim is required.

---

## 🧪 Example JWT Payload

```json
{
  "sub": "user123",
  "age": 22
}
```

Requests without a valid age or outside the allowed range will be denied.

---

# ☸️ Kubernetes Deployment

Kubernetes manifests are located in:

```
/k8s
```

Deploy to cluster:

```bash
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/
```

---

# 🧱 Terraform Infrastructure

Terraform configuration is located in:

```
/terraform
```

Initialize Terraform:

```bash
cd terraform
terraform init
terraform plan
terraform apply
```
---
# 🔐 Environment Variables

Configured inside:

- application.yml
- Docker Compose
- Kubernetes ConfigMaps & Secrets

Important variables:

- DB_HOST
- DB_PORT
- DB_USERNAME
- DB_PASSWORD
- EUREKA_SERVER_URL

---

# 🛑 Troubleshooting

### Port Already in Use

```bash
netstat -ano | findstr 8081
taskkill /PID <PID> /F
```
### Database Connection Issues

- Ensure PostgreSQL container is running
- Check username/password
- Verify DB host in application.yml

---

## 📊 Monitoring

Prometheus metrics are available through Kong and can be visualized using Grafana dashboards.

---

## 🛠 Development Notes

* Multi‑module Maven setup simplifies dependency management
* Services are designed to be containerized
* Gateway rules simulate real‑world banking compliance

---

## 📈 Future Improvements

* Add service discovery
* Add JWT Authentication
* Add API rate limiting
* Add Grafana dashboards
* Implement CI/CD pipelines
* Add container orchestration (Kubernetes)
* Expand authentication and authorization

---

## 📜 License

This project is for educational and demonstration purposes.
