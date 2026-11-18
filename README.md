# CancunTravel

MVP acad?mico para una agencia de viajes, con Java 25, Spring Boot, Thymeleaf y PostgreSQL. Las reservas son de prueba y no realizan pagos.

## Configuraci?n en Windows

1. Inicie PostgreSQL y cree una base vac?a llamada `cancuntravel`. Por ejemplo, conectado con una cuenta que tenga permiso `CREATEDB`, ejecute `CREATE DATABASE cancuntravel;` en psql o pgAdmin.
2. En PowerShell, desde la ra?z del proyecto, configure las variables para la sesi?n actual con sus credenciales reales de PostgreSQL:

```powershell
$env:DB_URL = 'jdbc:postgresql://localhost:5432/cancuntravel'
$env:DB_USERNAME = 'su_usuario'
$env:DB_PASSWORD = 'su_contrase?a'
powershell -ExecutionPolicy Bypass -File .\iniciar-dev.ps1
```

El script activa `dev` para ese proceso y ejecuta Maven. Tambi?n puede activar `$env:SPRING_PROFILES_ACTIVE = 'dev'` y ejecutar `.\mvnw.cmd spring-boot:run`. Abra http://localhost:8080/welcome. Hibernate crea o actualiza las tablas. La aplicaci?n no crea la base de datos PostgreSQL ni migra datos anteriores de H2 autom?ticamente.

El perfil `dev` crea las siguientes cuentas si no existen. Todas usan **Cancun2026!**. Tambi?n crea tres viajes y ejemplos de presupuesto y evento cuando las tablas correspondientes est?n vac?as. Los datos existentes no se restablecen al reiniciar. Use una base separada y no active `dev` en producci?n.

| Usuario | Rol |
| --- | --- |
| administrador | ADMINISTRADOR |
| dueno | DUENO |
| empleado | EMPLEADO |
| guia | GUIA |
| usuario | USUARIO |

## Recorrido de demostraci?n

1. Visite la bienvenida y el cat?logo; abra un viaje.
2. Inicie sesi?n como `usuario`, registre una reserva de prueba y consulte Mis reservas.
3. Con `administrador` cree o edite un viaje y gestione una cuenta. `dueno` tambi?n puede crear o editar viajes.
4. Con `empleado` registre un presupuesto; cierre sesi?n y, con `dueno`, apru?belo o rech?celo.
5. Con `guia` registre una salida o actividad. Cierre sesi?n entre roles.

## Pruebas y limitaciones

Ejecute `.\mvnw.cmd test`; las pruebas usan H2 en memoria. La desactivaci?n impide nuevos inicios de sesi?n, pero no cierra sesiones ya abiertas. Los eventos son una bit?cora general, sin asociaci?n a viajes o reservas. Las reservas son de un cupo y no incluyen pago, cancelaci?n ni confirmaci?n externa. No hay recuperaci?n de contrase?a, edici?n de roles, migraciones controladas ni aprovisionamiento de administrador para producci?n. Los estilos y la ilustraci?n funcionan sin conexi?n; Bootstrap y las fuentes se cargan desde CDN cuando est?n disponibles.
