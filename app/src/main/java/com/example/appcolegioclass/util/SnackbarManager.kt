package com.example.appcolegioclass.util

import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * GESTOR DE MENSAJES (SNACKBAR):
 * Esta clase centraliza la lógica para mostrar mensajes rápidos (notificaciones) en la parte inferior de la pantalla.
 * Permite que cualquier parte de la aplicación muestre un aviso sin necesidad de manejar estados de UI complejos.
 */
object SnackbarManager {
    // Referencia al estado del host de Snackbar de Material3
    private var snackbarHostState: SnackbarHostState? = null
    // Ámbito de corrutina para ejecutar la animación del mensaje fuera del hilo principal
    private var scope: CoroutineScope? = null

    /**
     * INICIALIZACIÓN: Se debe llamar en el Scaffold principal de la aplicación.
     */
    fun init(hostState: SnackbarHostState, coroutineScope: CoroutineScope) {
        snackbarHostState = hostState
        scope = coroutineScope
    }

    /**
     * MOSTRAR MENSAJE: Lanza un mensaje visual en la pantalla.
     * @param message El texto que se desea mostrar al usuario.
     */
    fun showMessage(message: String) {
        scope?.launch {
            snackbarHostState?.showSnackbar(message)
        }
    }
}
