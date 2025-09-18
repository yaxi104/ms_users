# 👤 User Microservice (Hexagonal Architecture)

Este microservicio permite la creación de usuarios con rol **PROPIETARIO**, siguiendo una arquitectura **hexagonal**,
integrando seguridad, validaciones, y documentación OpenAPI/Swagger.

---

## 📋 Descripción

Este servicio expone un endpoint para registrar nuevos usuarios. Solo los usuarios con rol `ADMIN` pueden crear usuarios
con rol `PROPIETARIO`.  
El microservicio realiza validaciones como:

- Formato de correo electrónico
- Validación de edad (debe ser mayor de 18 años)
- Longitud y formato del número telefónico
- Restricción de duplicidad de correo

---

## 🚀 Tecnologías

- ☕ **Java 17**
- 🧱 **Spring Boot 3.5.5**
- 🔐 **Spring Security**
- 🧪 **Spring Validation**
- 🔄 **MapStruct**
- 📦 **MySQL**
- 📚 **Swagger/OpenAPI 3 (springdoc-openapi)**
- 🧪 **JUnit 5 + Jacoco (para cobertura)**
- ✅ **Arquitectura Hexagonal**

---

## 🛠️ Endpoints

| Método | Ruta            | Autenticación | Descripción                 |
|--------|-----------------|---------------|-----------------------------|
| POST   | `/api/v1/owner` | `ADMIN`       | Crea un usuario propietario |

---
## 📖 Documentación de la API (Swagger)
http://localhost:8080/user/swagger-ui/index.html

## 📄 Ejemplo de postman

#### Crear Propietario
```http
curl --location 'http://localhost:8080/user/api/v1/owner' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic YWRtaW5AcGxhem9sZXRhLmNvbTpBZG1pbjEyMyE=' \
--header 'Cookie: JSESSIONID=C6B9E1BCEF2D968B43F002304CBF9726' \
--data-raw '{
"firstName": "Pastini",
"lastName": "Comelini",
"idNumber": "5416164524545",
"phoneNumber": "+573001356985",
"dateBirth": "2002-01-17",
"email": "propietariopasta@plazoleta.com",
"password": "Propietariopasta"
}'
```

## 📄 Ejemplo de solicitud

```http
POST /api/v1/owner
Content-Type: application/json
Authorization: Basic base64(admin:password)

{
  "firstName": "Pepito",
  "lastName": "Perez",
  "idNumber": "1234",
  "phoneNumber": "+573167549634",
  "dateBirth": "2000-09-17",
  "email": "test@example.com",
  "password": "password123"
}
```

