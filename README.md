Mini Red Social

Una mini red social desarrollada con Spring Boot, Spring Security JWT, Spring Data JPA/Hibernate y una arquitectura moderna para despliegue con Docker y Kubernetes. Este proyecto permite a los usuarios registrarse, iniciar sesión, interactuar con publicaciones, gestionar amistades y manejar tokens de autenticación.

Características

Gestión de usuarios: Registro, login y roles (por ejemplo, USER, ADMIN).

Seguridad: Autenticación y autorización mediante JWT.

Publicaciones: Los usuarios pueden crear y ver publicaciones.

Amistades: Los usuarios pueden enviar y aceptar solicitudes de amistad.

Tokens: Gestión de tokens de acceso y refresh tokens para seguridad.

Testing:

Postman: Pruebas manuales de endpoints.

JUnit y Mockito: Tests unitarios y de integración.

Despliegue CI/CD: Automatizado con GitHub Actions, contenedores Docker y orquestación con Kubernetes.

Tecnologías

Backend: Java 17+, Spring Boot, Spring Data JPA, Hibernate.

Seguridad: Spring Security, JWT.

Base de datos: H2 (desarrollo) / PostgreSQL (producción).

Testing: JUnit, Mockito, Postman.

DevOps: Docker, Kubernetes, GitHub Actions.
