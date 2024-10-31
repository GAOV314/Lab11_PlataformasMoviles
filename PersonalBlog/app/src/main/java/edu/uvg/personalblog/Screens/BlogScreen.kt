package edu.uvg.personalblog.Screens

// Importaciones necesarias para crear componentes de UI y gestionar Firestore y operaciones asíncronas.
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// Definición de datos de la publicación (Post), con propiedades de texto, imagen, archivo, timestamp, y datos del autor.
data class Post(
    val text: String,
    val imageUrl: String? = null,
    val fileUrl: String? = null,
    val timestamp: Long,
    val firstName: String = "Anónimo",  // Nombre del autor
    val lastName: String = ""           // Apellido del autor
)

@Composable
fun BlogScreen() {
    // Obtiene una instancia de Firestore para interactuar con la base de datos.
    val firestore = FirebaseFirestore.getInstance()
    // Estado para mantener la lista de publicaciones (posts) y su actualización reactiva.
    val posts = remember { mutableStateListOf<Post>() }
    // Define un CoroutineScope para manejar las operaciones asíncronas.
    val coroutineScope = rememberCoroutineScope()

    // LaunchedEffect para cargar datos de Firebase una vez que el Composable se inicializa.
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            // Obtiene la colección "posts" de Firebase ordenada por timestamp.
            val snapshot = firestore.collection("posts")
                .orderBy("timestamp")
                .get()
                .await()

            // Mapea cada documento en Firebase a un objeto Post y lo agrega a la lista de publicaciones.
            val fetchedPosts = snapshot.documents.map { doc ->
                Post(
                    text = doc.getString("text") ?: "",
                    imageUrl = doc.getString("imageUrl"),
                    fileUrl = doc.getString("fileUrl"),
                    timestamp = doc.getLong("timestamp") ?: 0L,
                    firstName = doc.getString("firstName") ?: "Anónimo",
                    lastName = doc.getString("lastName") ?: ""
                )
            }
            // Agrega las publicaciones obtenidas a la lista de posts.
            posts.addAll(fetchedPosts)
        }
    }

    // Muestra la lista de publicaciones en una LazyColumn, que es ideal para listas largas y cargadas dinámicamente.
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(posts) { post ->
            PostItem(post = post)  // Llama al Composable PostItem para mostrar cada publicación individual.
            Spacer(modifier = Modifier.height(16.dp))  // Espacio entre publicaciones.
        }
    }
}

@Composable
fun PostItem(post: Post) {
    // Caja que contiene el contenido de cada publicación, ocupando el ancho completo y con padding.
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column {
            // Muestra el nombre y apellido del usuario que publicó.
            Text(
                text = "Publicado por: ${post.firstName} ${post.lastName}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Muestra el texto de la publicación.
            Text(
                text = post.text,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Si existe una URL de imagen, se muestra utilizando Coil para cargar imágenes.
            post.imageUrl?.let { imageUrl ->
                Image(
                    painter = rememberAsyncImagePainter(model = imageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(bottom = 8.dp)
                )
            }

            // Si existe una URL de archivo, muestra un botón para descargarlo o abrirlo.
            post.fileUrl?.let { fileUrl ->
                TextButton(onClick = {
                    // Acción para descargar el archivo (o abrir el link en el navegador)
                }) {
                    Text("Descargar archivo")
                }
            }

            // Muestra la fecha de publicación, formateada en "dd/MM/yyyy".
            Text(
                text = "Publicado: ${java.text.SimpleDateFormat("dd/MM/yyyy").format(post.timestamp)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
