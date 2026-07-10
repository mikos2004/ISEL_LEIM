package dam.a50746.disneyjournal.ui


import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dam.a50746.disneyjournal.R
import dam.a50746.disneyjournal.ui.theme.BluePri
import dam.a50746.disneyjournal.ui.theme.BlueSec
import dam.a50746.disneyjournal.ui.theme.BlueTri
import dam.a50746.disneyjournal.ui.theme.DarkBluePri
import dam.a50746.disneyjournal.ui.theme.RedDelete

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItemDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.TextField
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.navigation.NavHostController
import coil3.compose.SubcomposeAsyncImage
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import dam.a50746.disneyjournal.api.fetchMoviePoster
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListView(
    listName: String = "List Name",
    movieTitles: List<String>,
    onBackClick: () -> Unit,
    onItemClick: (String) -> Unit,
    navController: NavHostController,
    fromHome: Boolean = false
) {
    val currentUser = Firebase.auth.currentUser
    val db = Firebase.firestore
    var showDeleteDialog by remember { mutableStateOf(false) }
    var movieTitlesState by remember { mutableStateOf(movieTitles.toMutableList()) }

    // Adicionar estados para watchedCount e selectedStatus
    var watchedCount by remember { mutableStateOf("0") }
    var selectedStatus by remember { mutableStateOf("Watching") }

    // Carregar os dados da lista quando o componente é montado
    LaunchedEffect(listName) {
        if (currentUser?.uid == null) return@LaunchedEffect

        db.collection("users").document(currentUser.uid)
            .collection("myLists")
            .whereEqualTo("listName", listName)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents[0]
                    // Carregar watchedCount se existir
                    document.getLong("watchedCount")?.let {
                        watchedCount = it.toString()
                    }
                    // Carregar selectedStatus se existir
                    document.getString("selectedStatus")?.let {
                        selectedStatus = it
                    }
                }
            }
    }

    // Função para atualizar watchedCount e selectedStatus no Firestore
    fun updateListStats(newWatchedCount: String, newStatus: String) {
        if (currentUser?.uid == null) return

        db.collection("users").document(currentUser.uid)
            .collection("myLists")
            .whereEqualTo("listName", listName)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val updates = hashMapOf<String, Any>(
                        "watchedCount" to (newWatchedCount.toIntOrNull() ?: 0),
                        "selectedStatus" to newStatus
                    )
                    querySnapshot.documents[0].reference.update(updates)
                }
            }
    }

    // Função para apagar a lista
    fun deleteList() {
        if (currentUser?.uid == null) return


        onBackClick()

        // encontrar a lista no Firebase
        db.collection("users").document(currentUser.uid)
            .collection("myLists")
            .whereEqualTo("listName", listName)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    // Delete the document
                    querySnapshot.documents[0].reference.delete()
                        .addOnSuccessListener {
                            // List deleted successfully
                        }
                        .addOnFailureListener {
                            // Handle error
                        }
                }
            }
            .addOnFailureListener {
                // Handle error
            }
    }

    // Modal para confirmação
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete List") },
            text = { Text("Are you sure you want to delete this list? This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        deleteList()
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RedDelete)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BluePri,
                    titleContentColor = BlueTri,
                ),
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            listName,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        // O botao delete aparece apenas se nao vier do home
                        if (!fromHome) {
                            IconButton(onClick = { showDeleteDialog = true }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.delete),
                                    contentDescription = "Delete List",
                                    tint = RedDelete,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .size(33.dp)
                                )
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            tint = BlueTri,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {

                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            tint = BlueTri,
                            contentDescription = "More options",
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        ScrollContent(
            innerPadding = innerPadding,
            movieTitles = movieTitlesState,
            onItemClick = onItemClick,
            navController = navController,
            listName = listName,
            onMovieDeleted = { deletedTitle ->
                movieTitlesState = movieTitlesState.filter { it != deletedTitle }.toMutableList()
            },
            watchedCount = watchedCount,
            onWatchedCountChange = { newCount ->
                watchedCount = newCount
                updateListStats(newCount, selectedStatus)
            },
            selectedStatus = selectedStatus,
            onSelectedStatusChange = { newStatus ->
                selectedStatus = newStatus
                updateListStats(watchedCount, newStatus)
            },
            fromHome = fromHome
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScrollContent(
    innerPadding: PaddingValues,
    movieTitles: List<String>,
    onItemClick: (String) -> Unit = {},
    navController: NavHostController,
    listName: String,
    onMovieDeleted: (String) -> Unit,
    watchedCount: String,
    onWatchedCountChange: (String) -> Unit,
    selectedStatus: String,
    onSelectedStatusChange: (String) -> Unit,
    fromHome: Boolean = false  // Add this parameter
) {
    val currentUser = Firebase.auth.currentUser
    val db = Firebase.firestore

    // Função para agapar um filme da lista
    fun deleteMovie(movieTitle: String) {
        if (currentUser?.uid == null) return

        db.collection("users").document(currentUser.uid)
            .collection("myLists")
            .whereEqualTo("listName", listName)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents[0]
                    val films = document.get("films") as? List<Map<String, Any>>
                    val filmToRemove = films?.find { it["title"] == movieTitle }

                    if (filmToRemove != null) {
                        document.reference.update("films", FieldValue.arrayRemove(filmToRemove))
                            .addOnSuccessListener {
                                onMovieDeleted(movieTitle)
                            }
                    }
                }
            }
    }

    val totalMovies = movieTitles.size
    val progress = watchedCount.toIntOrNull()?.coerceIn(0, totalMovies)?.toFloat() ?: 0f
    val safeProgress = if (totalMovies > 0) progress / totalMovies else 0f

    // Estado para guardar URLs do poster
    val posterUrls = remember { mutableStateMapOf<String, String?>() }

    LazyColumn(
        modifier = Modifier
            .padding(innerPadding)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        TextField(
                            value = watchedCount,
                            onValueChange = { newValue ->
                                if (newValue.isEmpty() || newValue.toIntOrNull() != null) {
                                    onWatchedCountChange(newValue)
                                }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            textStyle = LocalTextStyle.current.copy(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = DarkBluePri
                            ),
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .height(45.dp)
                                .width(75.dp),
                            singleLine = true
                        )

                        Text(
                            text = "/$totalMovies",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 24.sp
                            ),
                            color = DarkBluePri,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    // Dropdown Menu para status
                    var expanded by remember { mutableStateOf(false) }

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                        modifier = Modifier.width(140.dp)
                    ) {
                        TextField(
                            value = selectedStatus,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .padding(top = 10.dp)
                                .height(45.dp),
                            colors = ExposedDropdownMenuDefaults.textFieldColors(
                                focusedContainerColor = BlueSec.copy(alpha = 0.2f),
                                focusedTrailingIconColor = DarkBluePri,
                                unfocusedTrailingIconColor = DarkBluePri
                            ),
                            textStyle = LocalTextStyle.current.copy(
                                fontSize = 14.sp,
                                color = DarkBluePri
                            )
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            listOf("Watching", "Dropped", "On Hold", "Plan to Watch").forEach { status ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            status,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                color = DarkBluePri
                                            )
                                        )
                                    },
                                    onClick = {
                                        onSelectedStatusChange(status)
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                LinearProgressIndicator(
                    progress = { safeProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = BlueSec,
                    trackColor = BlueSec.copy(alpha = 0.2f)
                )

                Divider(
                    thickness = 1.dp,
                    modifier = Modifier.padding(8.dp),
                    color = BlueSec.copy(alpha = 0.5f)
                )
            }
        }
        items(movieTitles) { title ->
            // prcurar o poster do filme
            LaunchedEffect(title) {
                if (!posterUrls.containsKey(title)) {
                    posterUrls[title] = fetchMoviePoster(title)
                }
            }

            ListItem(
                colors = ListItemDefaults.colors(
                    containerColor = BluePri,
                    headlineColor = BlueTri,
                    supportingColor = BlueTri,
                ),
                modifier = Modifier
                    .clickable {
                        val encodedTitle = URLEncoder.encode(title, "UTF-8")
                        val encodedPosterUrl = URLEncoder.encode(posterUrls[title] ?: "", "UTF-8")
                        navController.navigate("movieProfile/$encodedTitle/$encodedPosterUrl")
                    }
                    .fillMaxWidth(),
                leadingContent = {
                    SubcomposeAsyncImage(
                        model = posterUrls[title],
                        contentDescription = "Poster of $title",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(56.dp)
                            .clip(MaterialTheme.shapes.small),
                        loading = {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = BlueTri
                            )
                        },
                        error = {
                            Image(
                                painter = painterResource(id = R.drawable.mickey_shadow),
                                contentDescription = "Error loading poster",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    )
                },
                headlineContent = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                trailingContent = {
                    if (!fromHome) {
                        IconButton(onClick = { deleteMovie(title) }) {
                            Icon(
                                painter = painterResource(id = R.drawable.delete),
                                contentDescription = "Delete $title",
                                tint = RedDelete
                            )
                        }
                    }
                }
            )
            Divider(
                thickness = 0.5.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

