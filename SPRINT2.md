# Sprint 2: especificaci?n y trazabilidad

- HU-05/06: administrador y due?o crean y editan viajes con fechas v?lidas, precio positivo y cupos no negativos. Validaci?n: pruebas de creaci?n, edici?n y permisos.
- HU-07: el cat?logo permite buscar por nombre o destino, sin distinguir may?sculas. Validaci?n: b?squeda en pruebas MVC.
- HU-08: la disponibilidad se muestra en cat?logo y detalle. La reserva y edici?n adquieren bloqueo sobre el mismo viaje. Validaci?n: pruebas de cupos y reserva.
- HU-09: el empleado asigna un gu?a activo o deja el viaje sin gu?a y puede registrar hasta 1000 caracteres de notas. Validaci?n: asignaci?n, gu?a inactivo y acceso denegado.
- HU-10/11: empleado o administrador registra el presupuesto en PENDIENTE_ADMIN; administrador lo presenta al due?o, cambi?ndolo a PENDIENTE_DUENO. Validaci?n: flujo y permisos.
- HU-12: due?o aprueba o rechaza ?nicamente presupuestos presentados. Se registra qui?n present? y qui?n decidi?; estados finales no se pueden cambiar. Validaci?n: transici?n y acceso denegado.

## Tareas y revisi?n

El equipo debe asignar en su tablero las tareas de especificaci?n, implementaci?n, pruebas y revisi?n cruzada; el plan de trabajo original identifica responsable y validador por historia. La ejecuci?n de las pruebas no sustituye la revisi?n del Product Owner ni la revisi?n cruzada del equipo.

## Datos anteriores

Al iniciar con PostgreSQL, `schema.sql` convierte PENDIENTE a PENDIENTE_ADMIN y conserva los estados finales; Hibernate a?ade las nuevas columnas. Haga una copia de seguridad antes de actualizar una base existente. La migraci?n requiere permisos ALTER y UPDATE en la tabla budget. La aplicaci?n est? configurada para el esquema public. El script es repetible; no borra filas.
