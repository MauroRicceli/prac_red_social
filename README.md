# 🌐 Mini Red Social

Una mini red social desarrollada con Spring Boot, Spring Security JWT, Spring Data JPA/Hibernate/MongoDB y una arquitectura moderna para despliegue con Docker y Render.
Permite a los usuarios registrarse, iniciar sesión, interactuar con publicaciones, gestionar amistades y manejar tokens de autenticación.
## ✨ Características

👤 Gestión de usuarios: Registro, login y roles (STANDARD, ADMIN)
🔐 Seguridad: JWT para autenticación y autorización con refresh tokens
📝 Publicaciones: Crear, modificar, eliminar y gestionar publicaciones
💬 Sistema de comentarios: Comentar publicaciones y eliminar comentarios propios
👍 Sistema de likes: Like/unlike en publicaciones con toggle automático
🤝 Amistades: Agregar y eliminar amistades entre usuarios
🔑 Tokens: Manejo seguro de access y refresh tokens con invalidación automática

## 🛠 Tecnologías

Backend: Java 17+, Spring Boot, Spring Data JPA, Hibernate, MongoDB
Seguridad: Spring Security, JWT con refresh tokens
Base de datos: Arquitectura híbrida MySQL + MongoDB
MySQL (usuarios, autenticación) - Local/AWS RDS
MongoDB (publicaciones, amistades, interacciones) - Local/MongoDB Atlas
Testing: JUnit, Mockito, tests unitarios e integración
DevOps: Docker, GitHub Actions CI/CD, Render deployment

##  📋 API Endpoints
### Autenticación

PUT /api/auth/register - Registrar nuevo usuario
GET /api/auth/login - Iniciar sesión
GET /api/auth/renovateTokens - Renovar tokens (requiere autenticación)
POST /api/auth/logout - Cerrar sesión (requiere autenticación)

### Gestión de Amistades

POST /api/user/addFriend - Agregar amigo (requiere autenticación)
DELETE /api/user/removeFriend - Eliminar amigo (requiere autenticación)

### Publicaciones

POST /api/user/createPublication - Crear nueva publicación (requiere autenticación)
PUT /api/user/modifyPublication - Modificar publicación propia (requiere autenticación)
DELETE /api/user/removePublication - Eliminar publicación propia (requiere autenticación)
GET /api/user/getAllPublications - Listar todas las publicaciones (solo ADMIN)

### Interacciones

POST /api/user/likePublication - Dar/quitar like a publicación (requiere autenticación)
POST /api/user/commentPublication - Comentar publicación (requiere autenticación)
DELETE /api/user/removeCommentPublication - Eliminar comentario propio (requiere autenticación)

## 🏗 Arquitectura
Base de Datos Híbrida
El sistema utiliza una arquitectura híbrida que aprovecha las fortalezas de cada tipo de base de datos:
MySQL - Datos transaccionales críticos:

## Usuarios y autenticación
Tokens de sesión
Relaciones que requieren consistencia ACID

## MongoDB - Datos de interacción social:

Publicaciones con contenido flexible
Sistema de comentarios anidados
Likes con información embebida del usuario
Amistades con metadatos temporales

## Seguridad

Autenticación JWT con access tokens (corta duración)
Refresh tokens para renovación segura de sesiones
Validación de autorización en cada endpoint
Verificación de propiedad para operaciones CRUD
Manejo de roles (STANDARD/ADMIN) con Spring Security

## 🚀 Instalación y Uso
Prerrequisitos

Java 17+
Docker y Docker Compose
Maven 3.6+

## Variables de Entorno
Crear archivo .env con:
DB_URL=jdbc:mysql://localhost:3306/your_database
DB_ROOT_USERNAME=your_username
DB_ROOT_PASSWORD=your_password
SECURITY_PASSWORD=your_security_password
SECURITY_JWT_SECRET_KEY=your_jwt_secret_key
TIME_EXPIRATION_TOKEN=3600000
TIME_REFRESH_TOKEN=86400000
MONGO_DATABASE=your_mongo_database
MONGO_ROOT_USER=your_mongo_user
MONGO_ROOT_PASSWORD=your_mongo_password
MONGO_URI=mongodb://localhost:27017/your_database
Desarrollo Local

Clonar el repositorio
Configurar variables de entorno
Ejecutar bases de datos con Docker:

bash   docker-compose up -d

## 🔄 CI/CD Pipeline

El proyecto incluye un pipeline automatizado con GitHub Actions:

Unit Tests - Ejecuta tests unitarios en cada push/PR
Integration Tests - Ejecuta tests de integración si los unitarios pasan
Docker Build & Push - Construye y sube imagen a DockerHub si todos los tests pasan
Deployment - Deploy automático a Render con bases de datos en la nube

## 🌍 Deploy en Producción

La aplicación está configurada para deployment automático en Render con:

Base de datos SQL: AWS RDS (MySQL)
Base de datos NoSQL: MongoDB Atlas
Aplicación: Render con Docker container
CI/CD: GitHub Actions con environments separados

## 📝 Decisiones de Diseño

Arquitectura Híbrida
La decisión de usar MySQL + MongoDB se basó en optimizar cada tipo de dato según sus características:

Transacciones críticas (usuarios, tokens) requieren consistencia ACID → MySQL
Interacciones sociales (publicaciones, likes, comentarios) se benefician de flexibilidad de esquema → MongoDB

Sistema de Likes Embebido
Los likes se almacenan como Set embebido en cada publicación para:

Evitar consultas cross-database
Optimizar lecturas (más frecuentes que escrituras)
Mantener consistencia automática con el contador

Validación de Autorización
Cada endpoin
