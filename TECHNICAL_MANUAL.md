# Manual Tecnico — MedRecord v1.0

> Aplicacion Android de recordatorio de medicamentos  
> Universidad Central del Ecuador — Metodologia de la Investigacion  
> Fecha de entrega: Julio 2026

---

## 1. Descripcion del sistema

**Problema que resuelve:** El 50% de los pacientes con tratamiento cronico no toma sus medicamentos correctamente por olvido. MedRecord resuelve este problema enviando notificaciones automaticas exactamente a la hora configurada por el usuario para cada medicamento.

**Usuario objetivo:** Paciente adulto con uno o mas medicamentos de uso diario que necesita recordatorios puntuales sin depender de conexion a internet.

**Alcance del MVP (Historias de usuario implementadas):**

| ID | Historia de usuario | Estado |
|----|---------------------|--------|
| HU01 | Como paciente, quiero registrar medicamentos con nombre, dosis y horario | Completada |
| HU02 | Como paciente, quiero recibir una notificacion en el horario programado | Completada |
| HU03 | Como paciente, quiero marcar un medicamento como tomado | Completada (via historial) |
| HU04 | Como paciente, quiero ver mi historial de tomas | Implementada (base de datos) |
| HU05 | Como paciente, quiero crear mi perfil basico | Completada (registro y login) |

---

## 2. Arquitectura de la aplicacion

### Diagrama de capas

```
┌─────────────────────────────────────────────────┐
│                  CAPA UI                        │
│  LoginActivity | RegisterActivity               │
│  MainActivity  | AgregarMedicamentoActivity     │
└───────────────────┬─────────────────────────────┘
                    │
┌───────────────────▼─────────────────────────────┐
│              CAPA DE LOGICA                     │
│  ValidationUtils | PasswordUtils               │
│  NotificationHelper | AlarmReceiver            │
└───────────────────┬─────────────────────────────┘
                    │
┌───────────────────▼─────────────────────────────┐
│              CAPA DE DATOS                      │
│  UserDatabaseHelper (SQLiteOpenHelper)          │
│  SQLite Database: medrecord.db                  │
│  SharedPreferences: medrecord_prefs             │
└─────────────────────────────────────────────────┘
```

**Patron de diseno:** Arquitectura de 3 capas (Presentacion / Logica de negocio / Datos). No se usa MVVM porque el proyecto usa Java nativo sin dependencias de Jetpack avanzado, manteniendo la simplicidad y compatibilidad con el entorno de desarrollo.

**Flujo principal de datos:**
1. La Activity recibe la accion del usuario
2. Llama al metodo correspondiente en UserDatabaseHelper en un hilo secundario (Thread)
3. SQLite ejecuta la operacion
4. El resultado se devuelve al hilo principal con runOnUiThread()
5. La Activity actualiza la UI

---

## 3. Modelo de datos

### Diagrama Entidad-Relacion

```
┌──────────────────┐         ┌───────────────────────┐
│    usuarios      │  1   N  │     medicamentos       │
│──────────────────│─────────│───────────────────────│
│ id_usuario  (PK) │         │ id_medicamento (PK)   │
│ nombre           │         │ nombre                │
│ correo (UNIQUE)  │         │ descripcion           │
│ contrasena_hash  │         │ dosis_mg              │
│ fecha_registro   │         │ unidad                │
└──────────────────┘         │ hora                  │
                             │ id_usuario (FK)       │
                             └───────────────────────┘
```

### Descripcion de tablas

**Tabla `usuarios`**
- `id_usuario`: INTEGER, PK, autoincrement
- `nombre`: TEXT, nombre completo del usuario
- `correo`: TEXT, unico, usado como identificador de sesion
- `contrasena_hash`: TEXT, hash SHA-256 de la contraseña (nunca en texto plano)

**Tabla `medicamentos`**
- `id_medicamento`: INTEGER, PK, autoincrement
- `nombre`: TEXT, nombre del medicamento
- `descripcion`: TEXT, notas adicionales (opcional)
- `dosis_mg`: INTEGER, dosis en la unidad especificada
- `unidad`: TEXT, unidad de medida (mg, ml, pastilla, capsula, gota)
- `hora`: TEXT, hora del recordatorio en formato HH:mm (24 horas)
- `id_usuario`: INTEGER, FK → usuarios.id_usuario

**Version de la base de datos:** 3
**Nombre del archivo:** `medrecord.db` (almacenado internamente en el dispositivo)

---

## 4. Tecnologias y librerias

| Tecnologia | Version | Uso |
|------------|---------|-----|
| Android Studio | Hedgehog 2023.1+ | IDE de desarrollo |
| Java | 17 | Lenguaje de programacion |
| SDK minimo | API 24 (Android 7.0) | Dispositivos compatibles |
| SDK objetivo | API 36 (Android 14) | Version optimizada |
| Material Components | 1.12.0 | Componentes de UI (TextInputLayout, MaterialButton, etc.) |
| RecyclerView | 1.3.2 | Lista de medicamentos |
| AppCompat | 1.6.1 | Compatibilidad con versiones anteriores de Android |
| AlarmManager | SDK nativo | Programacion de notificaciones exactas |
| NotificationManager | SDK nativo | Gestion del canal de notificaciones |
| SQLiteOpenHelper | SDK nativo | Gestion de base de datos local |
| SharedPreferences | SDK nativo | Persistencia de sesion de usuario |
| JUnit 4 | SDK nativo | Pruebas unitarias |
| AndroidJUnit4 | SDK nativo | Pruebas de integracion |
| Espresso | SDK nativo | Pruebas de interfaz de usuario |

**Permisos requeridos:**
```xml
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
<uses-permission android:name="android.permission.SCHEDULE_EXACT_ALARM" />
```

---

## 5. Instrucciones para compilar

### Requisitos previos
- Android Studio Hedgehog (2023.1) o superior
- JDK 17
- SDK Android API 24 o superior instalado (via SDK Manager)
- Git instalado

### Pasos

```bash
# 1. Clonar el repositorio
git clone https://github.com/abizambrano/MedRecord.git

# 2. Abrir en Android Studio
# File → Open → seleccionar la carpeta MedRecord

# 3. Esperar sincronizacion de Gradle (primera vez ~2-5 min)

# 4. Seleccionar dispositivo (emulador o fisico)

# 5. Ejecutar
# Boton verde ▶ Run o Shift+F10
```

### Posibles problemas al compilar

| Error | Causa | Solucion |
|-------|-------|----------|
| Gradle sync failed | Conexion inestable | File → Sync Project with Gradle Files |
| SDK not found | SDK no instalado | Tools → SDK Manager → instalar API 24+ |
| HAXM required | Emulador sin aceleracion | SDK Manager → SDK Tools → Intel HAXM |

---

## 6. Estructura del repositorio

```
MedRecord/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/zambrano/medrecord/
│   │   │   │   ├── LoginActivity.java
│   │   │   │   ├── RegisterActivity.java
│   │   │   │   ├── MainActivity.java
│   │   │   │   ├── AgregarMedicamentoActivity.java
│   │   │   │   ├── Medicamento.java
│   │   │   │   ├── MedicamentoAdapter.java
│   │   │   │   ├── UserDatabaseHelper.java
│   │   │   │   ├── PasswordUtils.java
│   │   │   │   ├── ValidationUtils.java
│   │   │   │   ├── NotificationHelper.java
│   │   │   │   └── AlarmReceiver.java
│   │   │   ├── res/
│   │   │   │   ├── layout/
│   │   │   │   │   ├── activity_login.xml
│   │   │   │   │   ├── activity_register.xml
│   │   │   │   │   ├── activity_main.xml
│   │   │   │   │   ├── activity_agregar_medicamento.xml
│   │   │   │   │   └── item_medicamento.xml
│   │   │   │   └── values/
│   │   │   │       └── themes.xml
│   │   │   └── AndroidManifest.xml
│   │   ├── test/
│   │   │   └── ValidacionesTest.java
│   │   └── androidTest/
│   │       ├── MedRecordIntegrationTest.java
│   │       └── LoginUITest.java
│   └── build.gradle.kts
├── README.md
├── TECHNICAL_MANUAL.md
└── gradle/
    └── libs.versions.toml
```

---

## 7. Historial de versiones

### v1.0 — MVP Completo (Julio 2026)

**Funcionalidades implementadas:**
- [x] Registro de usuario con validacion de correo y contraseña
- [x] Login con hash SHA-256 y persistencia de sesion
- [x] CRUD completo de medicamentos (crear, listar, editar, eliminar)
- [x] Campo de hora de recordatorio por medicamento
- [x] Notificaciones locales con AlarmManager (hora exacta)
- [x] AlertDialog de confirmacion antes de eliminar
- [x] Snackbar con opcion "Deshacer" tras eliminar
- [x] Estado vacio en pantalla principal
- [x] 14 pruebas unitarias con JUnit 4
- [x] 5 pruebas de integracion con AndroidJUnit4
- [x] 2 pruebas de UI con Espresso

**Pendiente para v2.0:**
- [ ] Historial de tomas con marcado "tomado/omitido"
- [ ] Multiple recordatorios por medicamento (frecuencia)
- [ ] Notificacion de reincio del dispositivo (BOOT_COMPLETED)
- [ ] Exportar historial como PDF
- [ ] Sincronizacion en la nube (opcional)
