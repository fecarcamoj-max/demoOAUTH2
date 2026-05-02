# Presentacion demoAuth: JPA + Base de Datos Local

## Diapositiva 1. Objetivo

- Integrar el proyecto `demoAuth` con una base de datos local.
- Reemplazar los usuarios en memoria por usuarios persistidos.
- Mantener el login JWT funcionando.
- Dejar soporte para MySQL y MariaDB.

## Diapositiva 2. Problema Inicial

- El proyecto solo tenia Spring Security y OAuth2.
- No existian entidades JPA.
- No habia repositorios.
- Los usuarios se definian en memoria dentro de `SecurityConfig`.
- La aplicacion no podia persistir usuarios ni roles.

## Diapositiva 3. Dependencias Agregadas

- `spring-boot-starter-data-jpa`
- `mysql-connector-j`
- `mariadb-java-client`
- `h2` para pruebas

Archivo:

- `pom.xml`

## Diapositiva 4. Configuracion de Base de Datos

Se separo la configuracion por perfiles:

- `application.properties`
  Configuracion comun y perfil activo por defecto.
- `application-mysql.properties`
  Conexion para MySQL local.
- `application-mariadb.properties`
  Conexion para MariaDB local.

Variables principales:

- `DB_PROFILE`
- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`

## Diapositiva 5. Modelo de Datos

Entidades creadas:

- `AppUser`
  Usuario persistido con `id`, `username`, `password`, `enabled`.
- `AppRole`
  Rol persistido con `id` y `name`.

Relacion:

- `AppUser` tiene una relacion `ManyToMany` con `AppRole`.

## Diapositiva 6. Acceso a Datos

Repositorios creados:

- `AppUserRepository`
- `AppRoleRepository`

Funcion:

- Consultar usuarios por username.
- Consultar roles por nombre.
- Guardar usuarios y roles en la base.

## Diapositiva 7. Servicios Implementados

- `DatabaseUserDetailsService`
  Conecta Spring Security con la base de datos.
- `DemoUserService`
  Servicio de ejemplo para listar y crear usuarios.
- `DataSeederConfig`
  Crea automaticamente un usuario demo al iniciar.

Usuario inicial:

- username: `user`
- password: `password`
- role: `ROLE_USER`

## Diapositiva 8. Seguridad Actualizada

Antes:

- `SecurityConfig` usaba `InMemoryUserDetailsManager`.

Ahora:

- Spring Security usa `DatabaseUserDetailsService`.
- El login valida usuarios almacenados en la base.
- El JWT sigue emitiendose igual desde `AuthController`.

## Diapositiva 9. Endpoints de Demo

Endpoints principales:

- `POST /api/auth/login`
  Autentica y devuelve token JWT.
- `GET /api/auth/me`
  Devuelve el usuario autenticado.
- `GET /api/users`
  Lista usuarios guardados en la base.
- `POST /api/users`
  Crea un usuario demo nuevo.

## Diapositiva 10. Pruebas

- Se agrego configuracion de test con H2.
- Las pruebas ya no dependen de que MySQL o MariaDB esten levantados.
- El proyecto fue validado con `mvnw.cmd test`.

Resultado:

- `BUILD SUCCESS`

## Diapositiva 11. Como Ejecutar con MySQL

Ejemplo:

```powershell
$env:DB_PROFILE="mysql"
$env:DB_URL="jdbc:mysql://localhost:3306/demoauth_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
$env:DB_USERNAME="root"
$env:DB_PASSWORD="root"
.\mvnw.cmd spring-boot:run
```

## Diapositiva 12. Como Ejecutar con MariaDB

Ejemplo:

```powershell
$env:DB_PROFILE="mariadb"
$env:DB_URL="jdbc:mariadb://localhost:3306/demoauth_db"
$env:DB_USERNAME="root"
$env:DB_PASSWORD="root"
.\mvnw.cmd spring-boot:run
```

## Diapositiva 13. Archivos Clave

- `pom.xml`
- `src/main/resources/application.properties`
- `src/main/resources/application-mysql.properties`
- `src/main/resources/application-mariadb.properties`
- `src/main/java/com/example/demoAuth/security/SecurityConfig.java`
- `src/main/java/com/example/demoAuth/auth/AuthController.java`
- `src/main/java/com/example/demoAuth/user/AppUser.java`
- `src/main/java/com/example/demoAuth/user/AppRole.java`
- `src/main/java/com/example/demoAuth/user/AppUserRepository.java`
- `src/main/java/com/example/demoAuth/user/AppRoleRepository.java`
- `src/main/java/com/example/demoAuth/user/DatabaseUserDetailsService.java`
- `src/main/java/com/example/demoAuth/user/DemoUserService.java`
- `src/main/java/com/example/demoAuth/user/DemoUserController.java`
- `src/main/java/com/example/demoAuth/user/DataSeederConfig.java`

## Diapositiva 14. Cierre

- El proyecto ya puede trabajar con una base relacional local.
- La autenticacion paso de memoria a persistencia.
- Se dejo una base util para seguir evolucionando a microservicios.
- El siguiente paso natural seria persistir tambien clientes OAuth2 y llaves de firma.
