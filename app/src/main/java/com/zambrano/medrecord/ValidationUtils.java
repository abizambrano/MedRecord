package com.zambrano.medrecord;

// Clase de utilidad con logica de validacion pura (sin dependencias Android).
// Al no depender del framework de Android, sus metodos pueden probarse
// con JUnit 4 sin necesitar un emulador ni contexto Android.
public class ValidationUtils {

    // Valida que el correo tenga formato valido usando regex simple
    // Retorna true si el formato es correcto, false si no
    public static boolean esCorreoValido(String email) {
        if (email == null || email.trim().isEmpty()) return false;
        // Patron basico: texto@dominio.extension
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Version sin dependencia Android para unit tests puros
    public static boolean esCorreoValidoPuro(String email) {
        if (email == null || email.trim().isEmpty()) return false;
        return email.contains("@") && email.contains(".") &&
                email.indexOf("@") > 0 &&
                email.lastIndexOf(".") > email.indexOf("@") + 1 &&
                !email.startsWith("@") &&
                !email.endsWith(".");
    }

    // Valida que la contraseña tenga al menos 6 caracteres
    // Retorna true si es valida, false si no
    public static boolean esPasswordValida(String password) {
        if (password == null) return false;
        return password.length() >= 6;
    }

    // Valida que el nombre de un medicamento no este vacio
    public static boolean esNombreMedicamentoValido(String nombre) {
        return nombre != null && !nombre.trim().isEmpty();
    }

    // Valida que la dosis sea un numero positivo
    public static boolean esDosisValida(int dosis) {
        return dosis > 0;
    }

    // Valida que dos contraseñas coincidan (registro)
    public static boolean contraseñasCoinciden(String password, String confirmPassword) {
        if (password == null || confirmPassword == null) return false;
        return password.equals(confirmPassword);
    }
}
