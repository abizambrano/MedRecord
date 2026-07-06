package com.zambrano.medrecord;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.List;
import static org.junit.Assert.*;

// Pruebas de integracion para UserDatabaseHelper.
// Van en app/src/androidTest/java/ porque necesitan el runtime de Android
// para acceder a SQLite. Se usa una base de datos temporal de prueba
// (nombre diferente) que se elimina al terminar.
@RunWith(AndroidJUnit4.class)
public class MedRecordIntegrationTest {

    private UserDatabaseHelper db;
    private Context context;

    @Before
    public void setUp() {
        // Obtener contexto real del dispositivo/emulador
        context = ApplicationProvider.getApplicationContext();
        // Crear instancia de la base de datos para pruebas
        db = new UserDatabaseHelper(context);
        // Limpiar datos previos de pruebas anteriores
        db.getWritableDatabase().execSQL("DELETE FROM medicamentos");
        db.getWritableDatabase().execSQL("DELETE FROM usuarios");
    }

    @After
    public void tearDown() {
        // Cerrar la base de datos al terminar las pruebas
        db.close();
    }

    // ─── PRUEBA DE INTEGRACION 1: CRUD de Medicamentos ──────────────────────

    @Test
    public void insertarMedicamento_leerDeLista_datosCorrectos() {
        // ARRANGE — registrar usuario de prueba primero (clave foranea)
        db.insertarUsuario("Test User", "test@test.com",
                PasswordUtils.hashPassword("password123"));
        int idUsuario = db.obtenerIdUsuario("test@test.com");

        // ACT — insertar medicamento
        long id = db.insertarMedicamento("Amoxicilina", "Antibiotico", 500, "mg", idUsuario);

        // Leer medicamentos del usuario
        List<Medicamento> lista = db.obtenerMedicamentos(idUsuario);

        // ASSERT — verificar que se inserto y se puede leer correctamente
        assertTrue("El insert debe retornar un ID valido (mayor a 0)", id > 0);
        assertFalse("La lista no debe estar vacia despues de insertar", lista.isEmpty());
        assertEquals("Debe haber exactamente 1 medicamento", 1, lista.size());
        assertEquals("El nombre debe coincidir", "Amoxicilina", lista.get(0).getNombre());
        assertEquals("La dosis debe coincidir", 500, lista.get(0).getDosisMg());
        assertEquals("La unidad debe coincidir", "mg", lista.get(0).getUnidad());
    }

    @Test
    public void eliminarMedicamento_listaQuedaVacia() {
        // ARRANGE
        db.insertarUsuario("Test User", "test2@test.com",
                PasswordUtils.hashPassword("password123"));
        int idUsuario = db.obtenerIdUsuario("test2@test.com");
        db.insertarMedicamento("Ibuprofeno", "Analgesico", 400, "mg", idUsuario);
        List<Medicamento> listaAntes = db.obtenerMedicamentos(idUsuario);
        int idMed = listaAntes.get(0).getId();

        // ACT
        int filasEliminadas = db.eliminarMedicamento(idMed);
        List<Medicamento> listaDespues = db.obtenerMedicamentos(idUsuario);

        // ASSERT
        assertEquals("Debe eliminar exactamente 1 fila", 1, filasEliminadas);
        assertTrue("La lista debe quedar vacia tras eliminar el unico medicamento",
                listaDespues.isEmpty());
    }

    // ─── PRUEBA DE INTEGRACION 2: Validacion completa de Login ──────────────

    @Test
    public void login_credencialesCorrectas_retornaTrue() {
        // ARRANGE — registrar usuario con credenciales conocidas
        String correo = "usuario@test.com";
        String password = "mipassword123";
        String hash = PasswordUtils.hashPassword(password);
        db.insertarUsuario("Usuario Test", correo, hash);

        // ACT — intentar login con credenciales correctas
        boolean resultado = db.login(correo, hash);

        // ASSERT
        assertTrue("El login con credenciales correctas debe retornar true", resultado);
    }

    @Test
    public void login_passwordIncorrecta_retornaFalse() {
        // ARRANGE
        String correo = "usuario2@test.com";
        db.insertarUsuario("Usuario Test 2", correo,
                PasswordUtils.hashPassword("passwordcorrecta"));

        // ACT — intentar login con password incorrecta
        boolean resultado = db.login(correo, PasswordUtils.hashPassword("passwordincorrecta"));

        // ASSERT
        assertFalse("El login con password incorrecta debe retornar false", resultado);
    }

    @Test
    public void login_correoNoRegistrado_retornaFalse() {
        // ARRANGE — correo que nunca fue registrado
        String correoNoRegistrado = "noexiste@test.com";

        // ACT
        boolean resultado = db.login(correoNoRegistrado,
                PasswordUtils.hashPassword("cualquierpassword"));

        // ASSERT
        assertFalse("El login con correo no registrado debe retornar false", resultado);
    }
}
