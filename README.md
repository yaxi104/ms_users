# User Microservice (Hexagonal Architecture)

Este microservicio permite la autenticación y gestión de usuarios con distintos roles (ADMIN, PROPIETARIO, EMPLEADO)
siguiendo el patrón de arquitectura hexagonal.
Incluye seguridad, validaciones robustas, y documentación OpenAPI/Swagger.

---

## Descripción

Este servicio expone endpoints para:

Autenticación de usuarios (/login)

Creación de usuarios con rol PROPIETARIO (solo por usuarios ADMIN)

Consulta de usuarios por email o por ID

---

## Tecnologías

* ![Java 17](https://img.shields.io/badge/Java_17-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white)
* ![Spring Boot](https://img.shields.io/badge/Spring_Boot_3.5.5-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
* ![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
* ![Spring Validation](https://img.shields.io/badge/Spring_Validation-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
* ![MapStruct](https://img.shields.io/badge/MapStruct-FF6550?style=for-the-badge&logoColor=white)
* ![MySQL](https://img.shields.io/badge/MySQL-00000F?style=for-the-badge&logo=mysql&logoColor=white)
* ![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)
* ![JUnit 5](https://img.shields.io/badge/JUnit_5-25A162?style=for-the-badge&logo=java&logoColor=white)
* ![Jacoco](https://img.shields.io/badge/Jacoco-C71A36?style=for-the-badge&logo=checkmarx&logoColor=white)
* ![Arquitectura Hexagonal](https://img.shields.io/badge/Arquitectura-Hexagonal-blueviolet?style=for-the-badge)

---

## Endpoints disponibles

Método Ruta Roles permitidos Descripción
POST /food-court/users/api/v1/login Público Autentica un usuario y retorna un JWT
POST /food-court/users/api/v1/owner ADMIN Crea un nuevo usuario con rol PROPIETARIO
GET /food-court/users/api/v1/user ADMIN, PROPIETARIO, EMPLEADO Consulta usuario por email
GET /food-court/users/api/v1/{id} ADMIN, PROPIETARIO, EMPLEADO Consulta usuario por ID

---

## Swagger UI

La documentación OpenAPI está disponible en:

http://localhost:8080/food-court/users/swagger-ui/index.html

---

## Ejemplos de solicitud + Validaciones

### Login (POST /food-court/users/api/v1/login)

Validaciones del Request:
Campo Validación
email Obligatorio, formato válido de email
password Obligatorio, no puede estar en blanco
Ejemplo:
curl --location 'http://localhost:8080/food-court/users/api/v1/login' \
--header 'Content-Type: application/json' \
--data-raw '{
"email": "admin@plazoleta.com",
"password": "Admin123!"
}'

### Crear Propietario (POST /food-court/users/api/v1/owner)

Validaciones del Request:
Campo Validación
firstName Obligatorio
lastName Obligatorio
idNumber Obligatorio, solo números (regex: \d+)
phoneNumber Obligatorio, máx. 13 caracteres, solo números, puede comenzar con +
dateBirth Obligatorio, debe ser una fecha en el pasado (mayor de 18 años)
email Obligatorio, formato válido de email
password Obligatorio, se envía en texto plano y será encriptada internamente
Ejemplo:
curl --location 'http://localhost:8080/food-court/users/api/v1/owner' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer <jwt_token>' \
--data-raw '{
"firstName": "Pastini",
"lastName": "Comelini",
"idNumber": "5416164524545",
"phoneNumber": "+573001356985",
"dateBirth": "2002-01-17",
"email": "propietariopasta@plazoleta.com",
"password": "Propietariopasta"
}'

### Obtener usuario por email (GET /food-court/users/api/v1/user?email=)

Validaciones:
Parámetro Validación
email Obligatorio, válido
Ejemplo:
curl --location 'http://localhost:8080/food-court/users/api/v1/user?email=propietario@plazoleta.com' \
--header 'Authorization: Bearer <jwt_token>'

### Obtener usuario por ID (GET /food-court/users/api/v1/{id})

Validaciones:
Parámetro Validación
id Long positivo
Ejemplo:
curl --location 'http://localhost:8080/food-court/users/api/v1/3' \
--header 'Authorization: Bearer <jwt_token>'

---

## Seguridad

Los endpoints protegidos requieren JWT enviado en el header Authorization: Bearer <token>.

Acceso a endpoints controlado con anotaciones @PreAuthorize.