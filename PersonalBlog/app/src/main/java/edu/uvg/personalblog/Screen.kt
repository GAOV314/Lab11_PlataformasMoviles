package edu.uvg.personalblog

// Importa los íconos que se usarán para cada pantalla en la barra de navegación.
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

// Clase sellada Screen que representa cada pantalla de la aplicación con propiedades de ruta, título e ícono.
// La clase Screen es sellada para que todas las pantallas estén definidas dentro de esta misma clase, facilitando su control y uso en la navegación.
sealed class Screen(val route: String, val title: String, val icon: ImageVector) {

    // Objeto que representa la pantalla Home con ruta, título e ícono específicos.
    object Home : Screen("home-screen", "Guardar Datos", Icons.Filled.AddCircle)

    // Objeto que representa la pantalla Profile con ruta, título e ícono específicos.
    object Profile : Screen("profile-screen", "Ver Datos", Icons.Filled.Person)

    // Objeto que representa la pantalla Settings con ruta, título e ícono específicos.
    object Settings : Screen("settings", "Configuración", Icons.Filled.Settings)
}
