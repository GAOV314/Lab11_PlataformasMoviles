// SettingsScreen.kt
package edu.uvg.personalblog

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(userPreferences: UserPreferences) {
    var firstName by remember { mutableStateOf(userPreferences.firstName ?: "") }
    var lastName by remember { mutableStateOf(userPreferences.lastName ?: "") }
    var email by remember { mutableStateOf(userPreferences.email ?: "") }
    var birthDate by remember { mutableStateOf(userPreferences.birthDate ?: "") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Configuración de Usuario", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        TextField(value = firstName, onValueChange = { firstName = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        TextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Apellido") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        TextField(value = email, onValueChange = { email = it }, label = { Text("Correo Electrónico") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        TextField(value = birthDate, onValueChange = { birthDate = it }, label = { Text("Fecha de Nacimiento") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            userPreferences.firstName = firstName
            userPreferences.lastName = lastName
            userPreferences.email = email
            userPreferences.birthDate = birthDate
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Guardar")
        }
    }
}
