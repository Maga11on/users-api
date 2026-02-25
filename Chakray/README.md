Users API
=========

Proyecto Spring Boot 3.2.0 desarrollado como parte de una evaluación técnica.
El objetivo es implementar una API REST para gestión de usuarios con validaciones rigurosas, arquitectura modular ( eventos Kafka comentados, si requiere mensajes descomentar).

------------------------------------------------------------
🚀 Tecnologías utilizadas
------------------------------------------------------------
- Java 17
- Spring Boot 3.2.0
- Spring Web / Validation
- Spring Kafka
- Springdoc OpenAPI (Swagger UI)
- JUnit 5 + Mockito
- Jacoco (cobertura de tests)
- Docker / Docker Compose
- Git (versionado de código)

------------------------------------------------------------
📑 Endpoints principales
------------------------------------------------------------
Users:
- POST /users → Crear usuario (RFC/taxId único, validación de email y teléfono).
- GET /users/{id} → Obtener usuario por ID.
- PATCH /users/{id} → Actualizar parcialmente usuario.
- DELETE /users/{id} → Eliminar usuario.

Auth:
- POST /auth/login → Autenticación con taxId + password, devuelve JWT.

------------------------------------------------------------
📘 Validaciones implementadas
------------------------------------------------------------
- RFC/TaxId: formato válido y único.
- Teléfono: regex internacional (+52..., +1...).
- Email: formato estándar.
- Password: reglas de negocio (mínimo 8 caracteres, etc.).

------------------------------------------------------------
🛠️ Cómo ejecutar el proyecto
------------------------------------------------------------
1. Compilar:
   mvn clean package -DskipTests

2. Levantar con Docker Compose:
   docker-compose up --build -d

Esto inicia:
- Zookeeper (localhost:2181)
- Kafka (localhost:9092)
- Users API (localhost:8080)

------------------------------------------------------------
📖 Swagger UI
------------------------------------------------------------
Disponible en:
http://localhost:8080/swagger-ui/index.html

------------------------------------------------------------
🧪 Tests
------------------------------------------------------------
Ejecutar pruebas unitarias y de integración:
   mvn test

Generar reporte de cobertura con Jacoco:
   mvn jacoco:report

Reporte en: target/site/jacoco/index.html

------------------------------------------------------------
📘 Git Workflow
------------------------------------------------------------
- Rama principal: main
- Ramas de features: feature/<nombre>
- Commits pequeños y descriptivos
- Tags para versiones estables (v1.0.0)

------------------------------------------------------------
📌 Autor
------------------------------------------------------------
Alberto Magallón Cuéllar
Senior Java Backend / Microservices Developer
