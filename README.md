# MedRecord — Recordatorio de Medicamentos 

Aplicacion Android nativa en Java que ayuda a los pacientes a recordar tomar sus medicamentos en el horario correcto.

## Descripcion del problema

Muchos pacientes que siguen tratamientos medicos con uno o mas medicamentos olvidan tomarlos en el horario correcto, lo que afecta directamente la eficacia del tratamiento. MedRecord resuelve este problema enviando notificaciones automaticas en los horarios configurados por el usuario y llevando un historial de cumplimiento para que el paciente pueda hacer seguimiento de su adherencia al tratamiento.

## Objetivo

Desarrollar una aplicacion Android que permita registrar medicamentos con nombre, dosis y horario, programar recordatorios automaticos y consultar el historial de tomas para mejorar la adherencia al tratamiento medico.

## Historias de usuario del MVP

| ID   |                                      Historia de usuario                                                    |
|------|-------------------------------------------------------------------------------------------------------------|
| HU01 | Como paciente, quiero registrar mis medicamentos con nombre, dosis y horario para no olvidar tomarlos.      |
| HU02 | Como paciente, quiero recibir una notificacion en el horario programado para recordar tomar mi medicamento. |
| HU03 | Como paciente, quiero marcar un medicamento como tomado para registrar el cumplimiento.                     |
| HU04 | Como paciente, quiero ver mi historial de tomas para saber si sigo el tratamiento correctamente.            |
| HU05 | Como paciente, quiero crear mi perfil basico para personalizar la aplicacion.                               |

## Tecnologia usada

- **Lenguaje:** Java
- **Framework:** Android nativo (Jetpack)
- **Base de datos:** Room (SQLite local)
- **Arquitectura:** MVVM (Model-View-ViewModel)
- **Notificaciones:** AlarmManager + NotificationManager
- **IDE:** Android Studio

## Instrucciones de instalacion

### Requisitos previos
- Android Studio Hedgehog (2023.1) o superior
- JDK 17
- SDK Android API 24 o superior

### Pasos

1. Clona el repositorio:
   ```bash
   git clone https://github.com/[tu-usuario]/medrecord-android.git
   ```
2. Abre el proyecto en Android Studio
3. Espera que Gradle sincronice (primera vez puede tardar varios minutos)
4. Selecciona un emulador o dispositivo fisico
5. Haz clic en **Run** ▶

## Capturas de pantalla

|      Lista de Medicamentos          |        Agregar Medicamento            |          Historial de Tomas             |
|-------------------------------------|---------------------------------------|-----------------------------------------|
| ![Lista](screenshots/pantalla1.png) | ![Agregar](screenshots/pantalla2.png) | ![Historial](screenshots/pantalla3.png) |

> Las capturas se agregaran cuando la funcionalidad este implementada.

## Estado actual del proyecto

**En desarrollo** — Fase de configuracion del entorno completada.

**Proximos pasos:**
- [ ] Implementar pantalla de lista de medicamentos
- [ ] Configurar base de datos Room
- [ ] Implementar sistema de notificaciones con AlarmManager
- [ ] Crear pantalla de historial de tomas

## Funcionalidades implementadas

- [x] Registro de usuario con validacion de campos
- [x] Login con correo y contraseña
- [x] Hash de contraseñas con SHA-256
- [x] Persistencia de sesion con SharedPreferences
- [x] Base de datos local con SQLiteOpenHelper
- [x] Validacion de formato de correo con Patterns.EMAIL_ADDRESS
- [ ] Pantalla de lista de medicamentos (proxima entrega)
- [ ] Sistema de notificaciones (proxima entrega)

## Arquitectura de datos
 
MainActivity (UI) → UserDatabaseHelper → SQLite (medrecord.db)
                                       ↑
           AgregarMedicamentoActivity (formulario CREATE/UPDATE)
 
### Tablas
- `usuarios`: autenticacion (id, nombre, correo, contrasena_hash)
- `medicamentos`: CRUD principal (id, nombre, descripcion, dosis_mg, unidad, id_usuario)
 
### Notificaciones
AlarmManager + BroadcastReceiver (AlarmReceiver) + NotificationHelper
Canal: medrecord_recordatorios | Hora: 8:00 AM diario
 
### Como probar el CRUD
1. Iniciar sesion con una cuenta registrada
2. Tocar el boton + para agregar un medicamento
3. Rellenar nombre, dosis y unidad → Guardar
4. El medicamento aparece en la lista
5. Tocar un item para editarlo
6. Mantener presionado un item para eliminarlo (con confirmacion)
 
## Funcionalidades implementadas
- [x] CRUD completo de medicamentos (Create, Read, Update, Delete)
- [x] RecyclerView con MedicamentoAdapter
- [x] Formulario unificado para crear y editar
- [x] AlertDialog de confirmacion antes de eliminar
- [x] Snackbar con opcion Deshacer
- [x] Notificaciones locales con AlarmManager
- [x] Canal de notificaciones (API 26+)
- [x] Permiso POST_NOTIFICATIONS (Android 13+)


### Lenguaje
Java — Android nativo

## Autor

Abigail Zambrano — Universidad Central del Ecuador  
Materia: Metodologia de la Investigacion  
Periodo: 2025-2026
