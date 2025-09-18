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


