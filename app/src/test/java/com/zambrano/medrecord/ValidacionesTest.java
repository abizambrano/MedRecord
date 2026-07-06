package com.zambrano.medrecord;

import org.junit.Test;
import static org.junit.Assert.*;

// Clase de pruebas unitarias para las funciones de validacion de MedRecord.
// Estas pruebas van en app/src/test/java/ y se ejecutan en la JVM local,
// sin necesitar emulador ni dispositivo Android.
public class ValidacionesTest {

    // ─── PRUEBA 1: Validacion de correo electronico ──────────────────────────

    @Test
    public void correo_formatoInvalido_sinArroba_retornaFalse() {
        // ARRANGE — preparar el dato de entrada invalido
        String correoSinArroba = "abizambranouce.edu.ec";

        // ACT — ejecutar la funcion a probar
        boolean resultado = ValidationUtils.esCorreoValidoPuro(correoSinArroba);

        // ASSERT — verificar que el resultado es el esperado
        assertFalse("Un correo sin @ no debe ser valido", resultado);
    }

    @Test
    public void correo_formatoValido_retornaTrue() {
        // ARRANGE
        String correoValido = "abi.zambrano@uce.edu.ec";

        // ACT
        boolean resultado = ValidationUtils.esCorreoValidoPuro(correoValido);

        // ASSERT
        assertTrue("Un correo con formato valido debe pasar la validacion", resultado);
    }

    @Test
    public void correo_vacio_retornaFalse() {
        // ARRANGE — caso de borde: correo completamente vacio
        String correoVacio = "";

        // ACT
        boolean resultado = ValidationUtils.esCorreoValidoPuro(correoVacio);

        // ASSERT
        assertFalse("Un correo vacio no debe ser valido", resultado);
    }

    @Test
    public void correo_soloArroba_retornaFalse() {
        // ARRANGE — caso de borde: correo con solo el simbolo @
        String correoSoloArroba = "@";

        // ACT
        boolean resultado = ValidationUtils.esCorreoValidoPuro(correoSoloArroba);

        // ASSERT
        assertFalse("Un correo que es solo @ no debe ser valido", resultado);
    }

    // ─── PRUEBA 2: Validacion de contraseña ─────────────────────────────────

    @Test
    public void password_menosDe6Caracteres_retornaFalse() {
        // ARRANGE
        String passwordCorta = "abc";

        // ACT
        boolean resultado = ValidationUtils.esPasswordValida(passwordCorta);

        // ASSERT
        assertFalse("Una contraseña de menos de 6 caracteres debe ser rechazada", resultado);
    }

    @Test
    public void password_exactamente6Caracteres_retornaTrue() {
        // ARRANGE — caso limite: exactamente 6 caracteres
        String password6 = "abc123";

        // ACT
        boolean resultado = ValidationUtils.esPasswordValida(password6);

        // ASSERT
        assertTrue("Una contraseña de exactamente 6 caracteres debe ser aceptada", resultado);
    }

    @Test
    public void password_masde6Caracteres_retornaTrue() {
        // ARRANGE
        String passwordLarga = "mipassword123";

        // ACT
        boolean resultado = ValidationUtils.esPasswordValida(passwordLarga);

        // ASSERT
        assertTrue("Una contraseña de mas de 6 caracteres debe ser valida", resultado);
    }

    @Test
    public void password_vacia_retornaFalse() {
        // ARRANGE — caso de borde: contraseña vacia
        String passwordVacia = "";

        // ACT
        boolean resultado = ValidationUtils.esPasswordValida(passwordVacia);

        // ASSERT
        assertFalse("Una contraseña vacia no debe ser valida", resultado);
    }

    // ─── PRUEBA 3: Logica de negocio del CRUD (Medicamento) ─────────────────

    @Test
    public void nombreMedicamento_vacio_retornaFalse() {
        // ARRANGE
        String nombreVacio = "";

        // ACT
        boolean resultado = ValidationUtils.esNombreMedicamentoValido(nombreVacio);

        // ASSERT
        assertFalse("Un medicamento con nombre vacio no debe ser valido", resultado);
    }

    @Test
    public void nombreMedicamento_valido_retornaTrue() {
        // ARRANGE
        String nombreValido = "Amoxicilina";

        // ACT
        boolean resultado = ValidationUtils.esNombreMedicamentoValido(nombreValido);

        // ASSERT
        assertTrue("Un medicamento con nombre valido debe ser aceptado", resultado);
    }

    @Test
    public void dosis_cero_retornaFalse() {
        // ARRANGE — caso de borde: dosis de 0 no tiene sentido medico
        int dosisInvalida = 0;

        // ACT
        boolean resultado = ValidationUtils.esDosisValida(dosisInvalida);

        // ASSERT
        assertFalse("Una dosis de 0 no debe ser valida", resultado);
    }

    @Test
    public void dosis_positiva_retornaTrue() {
        // ARRANGE
        int dosisValida = 500;

        // ACT
        boolean resultado = ValidationUtils.esDosisValida(dosisValida);

        // ASSERT
        assertTrue("Una dosis de 500mg debe ser valida", resultado);
    }

    @Test
    public void contraseñas_iguales_retornaTrue() {
        // ARRANGE
        String password = "mipassword123";
        String confirmPassword = "mipassword123";

        // ACT
        boolean resultado = ValidationUtils.contraseñasCoinciden(password, confirmPassword);

        // ASSERT
        assertTrue("Dos contraseñas iguales deben coincidir", resultado);
    }

    @Test
    public void contraseñas_diferentes_retornaFalse() {
        // ARRANGE
        String password = "mipassword123";
        String confirmPassword = "otrapassword456";

        // ACT
        boolean resultado = ValidationUtils.contraseñasCoinciden(password, confirmPassword);

        // ASSERT
        assertFalse("Dos contraseñas diferentes no deben coincidir", resultado);
    }
}
