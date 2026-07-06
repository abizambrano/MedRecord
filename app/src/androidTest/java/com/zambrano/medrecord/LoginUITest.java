package com.zambrano.medrecord;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

// Prueba de interfaz de usuario con Espresso.
// Espresso simula las acciones del usuario (tocar, escribir, verificar)
// de forma automatica sobre la app real corriendo en el emulador.
// Va en app/src/androidTest/java/
@RunWith(AndroidJUnit4.class)
public class LoginUITest {

    // ActivityScenarioRule abre la LoginActivity antes de cada prueba
    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    // ─── PRUEBA UI 1: Login con campos vacios muestra error ─────────────────
    @Test
    public void loginCamposVacios_muestraMensajeError() {
        // onView() encuentra el elemento por su ID en el XML
        // perform(click()) simula un toque del usuario sobre el boton
        onView(withId(R.id.loginButton))
                .perform(click());

        // check(matches(isDisplayed())) verifica que el texto sea visible
        // "El correo es obligatorio" es el mensaje de error que setError() muestra
        onView(withText("El correo es obligatorio"))
                .check(matches(isDisplayed()));
    }

    // ─── PRUEBA UI 2: Login con correo invalido muestra error de formato ─────
    @Test
    public void loginCorreoInvalido_muestraErrorFormato() {
        // typeText() simula que el usuario escribe texto en el campo
        onView(withId(R.id.emailEditText))
                .perform(typeText("correosinformato"), closeSoftKeyboard());

        onView(withId(R.id.loginButton))
                .perform(click());

        onView(withText("Formato de correo invalido"))
                .check(matches(isDisplayed()));
    }
}
