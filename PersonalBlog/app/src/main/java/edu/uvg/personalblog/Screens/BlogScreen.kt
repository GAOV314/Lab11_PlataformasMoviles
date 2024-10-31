package edu.uvg.personalblog.Screens

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
    val firestore = FirebaseFirestore.getInstance()
    val posts = remember { mutableStateListOf<Post>() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val snapshot = firestore.collection("posts")
                .orderBy("timestamp")
                .get()
                .await()
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
            posts.addAll(fetchedPosts)
        }
    }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(posts) { post ->
            PostItem(post = post)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun PostItem(post: Post) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column {
            // Mostrar nombre y apellido del usuario
            Text(
                text = "Publicado por: ${post.firstName} ${post.lastName}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Mostrar el texto de la publicación
            Text(
                text = post.text,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Mostrar la imagen si existe, utilizando Coil
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

            // Mostrar el link para descargar el archivo si existe
            post.fileUrl?.let { fileUrl ->
                TextButton(onClick = {
                    // Acción para descargar el archivo (o abrir el link en el navegador)
                }) {
                    Text("Descargar archivo")
                }
            }

            // Mostrar la fecha de la publicación (timestamp)
            Text(
                text = "Publicado: ${java.text.SimpleDateFormat("dd/MM/yyyy").format(post.timestamp)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
