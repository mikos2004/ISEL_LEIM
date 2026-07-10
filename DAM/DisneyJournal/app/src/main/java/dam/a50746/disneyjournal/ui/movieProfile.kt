package dam.a50746.disneyjournal.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import dam.a50746.disneyjournal.ui.theme.BlueTri
import dam.a50746.disneyjournal.ui.theme.DarkBluePri
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import coil.compose.SubcomposeAsyncImage
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dam.a50746.disneyjournal.api.fetchMovieDetails
import dam.a50746.disneyjournal.ui.theme.BlueSec
import dam.a50746.disneyjournal.ui.theme.RedDelete
import dam.a50746.disneyjournal.ui.theme.YellowStar
import java.io.ByteArrayOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieProfile(
    movieTitle: String,
    imageUrl: String?,
    onBackClick: () -> Unit,
) {
    val currentUser = Firebase.auth.currentUser
    val db = Firebase.firestore
    var isStarFilled by remember { mutableStateOf(false) }
    var releaseDate by remember { mutableStateOf("Loading...") }
    var runTime by remember { mutableStateOf("Loading...") }

    var customPosterBitmap by remember(currentUser?.uid) { mutableStateOf<Bitmap?>(null) }
    var showPosterSelectionDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val inputStream = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            customPosterBitmap = bitmap
            uploadCustomPoster(movieTitle, bitmap, currentUser?.uid, db)
        }
    }

    var showAddToListDialog by remember { mutableStateOf(false) }
    var userLists by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var ignoreNextListenerUpdate by remember { mutableStateOf(false) }

    LaunchedEffect(currentUser, movieTitle) {
        if (currentUser?.uid != null) {
            db.collection("users").document(currentUser.uid)
                .addSnapshotListener { document, _ ->
                    if (!ignoreNextListenerUpdate) {
                        document?.let {
                            val favoriteFilms = it.get("favoriteFilms") as? List<Map<String, String>> ?: emptyList()
                            isStarFilled = favoriteFilms.any { it["title"] == movieTitle }
                        }
                    } else {
                        ignoreNextListenerUpdate = false
                    }
                }
        } else {
            isStarFilled = false
        }
    }

    LaunchedEffect(currentUser) {
        if (currentUser?.uid != null) {
            db.collection("users").document(currentUser.uid)
                .collection("myLists")
                .addSnapshotListener { snapshot, _ ->
                    val lists = snapshot?.documents?.map { doc ->
                        doc.data?.plus("id" to doc.id) ?: emptyMap()
                    } ?: emptyList()
                    userLists = lists
                }
        }
    }

    // Verificar se o filme já está favoritado
    LaunchedEffect(movieTitle) {
        if (currentUser?.uid != null) {
            db.collection("users").document(currentUser.uid)
                .get()
                .addOnSuccessListener { document ->
                    val favoriteFilms = document.get("favoriteFilms") as? List<Map<String, String>> ?: emptyList()
                    isStarFilled = favoriteFilms.any { it["title"] == movieTitle }
                }
        }

        // procruar detalhes do filme
        val (date, time) = fetchMovieDetails(movieTitle)
        releaseDate = date ?: "Information not available"
        runTime = time ?: "Information not available"
    }

    // loading do poster se existir
    LaunchedEffect(movieTitle, currentUser) {
        customPosterBitmap = null // Reset ao mudar de user ou filme

        if (currentUser?.uid != null) {
            db.collection("users").document(currentUser.uid)
                .get()
                .addOnSuccessListener { document ->
                    val customPosters = document.get("customPosters") as? List<Map<String, String>> ?: emptyList()
                    val posterData = customPosters.find { it["title"] == movieTitle }
                    posterData?.get("img")?.let { base64Image ->
                        try {
                            val imageBytes = Base64.decode(base64Image, Base64.DEFAULT)
                            customPosterBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                        } catch (e: Exception) {
                            // erro
                        }
                    }
                }
        }
    }



    // Função para alternar o estado de favorito
    fun toggleFavorite() {
        if (currentUser?.uid == null) return

        val userRef = db.collection("users").document(currentUser.uid)
        val filmData = mapOf(
            "title" to movieTitle,
            "imageUrl" to (imageUrl ?: "")
        )

        db.runTransaction { transaction ->
            val document = transaction.get(userRef)
            val currentFavorites = document.get("favoriteFilms") as? MutableList<Map<String, String>> ?: mutableListOf()

            if (isStarFilled) {
                // Remover dos favoritos
                currentFavorites.removeAll { it["title"] == movieTitle }
            } else {
                // Adicionar aos favoritos
                currentFavorites.add(filmData)
            }

            transaction.update(userRef, "favoriteFilms", currentFavorites)
        }.addOnSuccessListener {
            isStarFilled = !isStarFilled
        }
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
                            text = movieTitle,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        Spacer(Modifier.width(8.dp))
                        IconButton(onClick = { toggleFavorite() }) {
                            Icon(
                                painter = painterResource(
                                    id = if (isStarFilled) {
                                        R.drawable.baseline_star_24
                                    } else {
                                        R.drawable.baseline_star_outline_24
                                    }
                                ),
                                contentDescription = if (isStarFilled) "Unfavorite" else "Favorite",
                                tint = YellowStar,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .size(55.dp)
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            tint = BlueTri,
                            contentDescription = "Localized description"
                        )
                    }
                },
                actions = {

                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            tint = BlueTri,
                            contentDescription = "Localized description",
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        ScrollMovieProfile(
            innerPadding = innerPadding,
            imageUrl = if (customPosterBitmap != null) null else imageUrl,
            customPosterBitmap = customPosterBitmap,
            releaseDate = releaseDate,
            runTime = runTime,
            showAddToListDialog = showAddToListDialog,
            onShowAddToListDialogChange = { showAddToListDialog = it },
            userLists = userLists,
            movieTitle = movieTitle,
            currentUserUid = currentUser?.uid,
            db = db,
            onPosterClick = { showPosterSelectionDialog = true }
        )
    }

    if (showPosterSelectionDialog) {
        AlertDialog(
            onDismissRequest = { showPosterSelectionDialog = false },
            containerColor = BlueSec,
            title = { Text("Change Poster", color = BlueTri) },
            text = { Text("Do you want to select a custom poster from your gallery? Choose a photo approximately 500x500.", color = BlueTri) },
            confirmButton = {
                Column {
                    Button(
                        onClick = {
                            showPosterSelectionDialog = false
                            galleryLauncher.launch("image/*")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BluePri,
                            contentColor = BlueTri
                        )
                    ) {
                        Text("Select from Gallery")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    if (customPosterBitmap != null) {
                        Button(
                            onClick = {
                                showPosterSelectionDialog = false
                                removeCustomPoster(movieTitle, currentUser?.uid, db)
                                customPosterBitmap = null
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = RedDelete,
                                contentColor = Color.White
                            )
                        ) {
                            Text("Set Default")
                        }
                    }
                }
            },
            dismissButton = {
                Button(
                    onClick = { showPosterSelectionDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BluePri,
                        contentColor = BlueTri
                    )
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}


fun removeCustomPoster(
    movieTitle: String,
    userId: String?,
    db: FirebaseFirestore
) {
    userId?.let { uid ->
        db.collection("users").document(uid)
            .get()
            .addOnSuccessListener { document ->
                val currentPosters = document.get("customPosters") as? MutableList<Map<String, String>> ?: mutableListOf()

                currentPosters.removeAll { it["title"] == movieTitle }

                // Atualiza o Firestore
                db.collection("users").document(uid)
                    .update("customPosters", currentPosters)
                    .addOnSuccessListener {
                        // Success
                    }
                    .addOnFailureListener {
                        // Handle error
                    }
            }
    }
}

@Composable
fun ScrollMovieProfile(
    innerPadding: PaddingValues,
    imageUrl: String?,
    customPosterBitmap: Bitmap?,
    releaseDate: String,
    runTime: String,
    showAddToListDialog: Boolean,
    onShowAddToListDialogChange: (Boolean) -> Unit,
    userLists: List<Map<String, Any>>,
    movieTitle: String,
    currentUserUid: String?,
    db: FirebaseFirestore,
    onPosterClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))

            // Card com o poster
            Card(
                modifier = Modifier
                    .width(200.dp)
                    .aspectRatio(2f/3f)
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(8.dp))
                    .clickable { onPosterClick() }, // Add click handler
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                if (customPosterBitmap != null) {
                    Image(
                        bitmap = customPosterBitmap.asImageBitmap(),
                        contentDescription = "Custom Movie Poster",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else if (imageUrl != null) {
                    SubcomposeAsyncImage(
                        model = imageUrl,
                        contentDescription = "Movie Poster",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                        loading = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.mickey_shadow),
                                    contentDescription = "Loading",
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        },
                        error = {
                            Image(
                                painter = painterResource(id = R.drawable.mickey_shadow),
                                contentDescription = "Error",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.mickey_shadow),
                        contentDescription = "Movie Poster",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Botão "Add to List"
            Button(
                onClick = { onShowAddToListDialogChange(true) },
                modifier = Modifier
                    .width(200.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BluePri,
                    contentColor = BlueTri
                )
            ) {
                Text("Add to List", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Secção "Movie Data"
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Movie Data",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = DarkBluePri
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Informações do filme
                MovieInfoItem("Release Date", releaseDate)
                Divider(modifier = Modifier.padding(vertical = 8.dp), thickness = 0.5.dp)
                MovieInfoItem("Run Time", runTime)
                Divider(modifier = Modifier.padding(vertical = 8.dp), thickness = 0.5.dp)
            }
        }
    }

    if (showAddToListDialog) {
        AddToListDialog(
            lists = userLists,
            onDismiss = { onShowAddToListDialogChange(false) },
            onConfirm = { selectedListIds ->
                addMovieToLists(movieTitle, imageUrl, selectedListIds, currentUserUid, db)
            }
        )
    }
}

@Composable
fun MovieInfoItem(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = DarkBluePri,
                fontWeight = FontWeight.SemiBold
            )
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = DarkBluePri
            ),
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun AddToListDialog(
    lists: List<Map<String, Any>>,
    onDismiss: () -> Unit,
    onConfirm: (List<String>) -> Unit
) {
    val selectedLists = remember { mutableStateMapOf<String, Boolean>() }

    LaunchedEffect(lists) {
        lists.forEach { list ->
            selectedLists[list["id"] as? String ?: ""] = false
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = BlueSec,
        title = { Text("Add to List", color = BlueTri ) },
        text = {
            Column {
                Text("Select lists to add this movie to:",
                    color = BlueTri,
                    modifier = Modifier.padding(bottom = 8.dp),)

                LazyColumn {
                    items(lists) { list ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val listId = list["id"] as? String ?: ""
                                    selectedLists[listId] = !(selectedLists[listId] ?: false)
                                }
                                .padding(8.dp)
                        ) {
                            Checkbox(
                                checked = selectedLists[list["id"] as? String ?: ""] ?: false,
                                onCheckedChange = { isChecked ->
                                    val listId = list["id"] as? String ?: ""
                                    selectedLists[listId] = isChecked
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = BluePri,
                                    uncheckedColor = BluePri,
                                    checkmarkColor = BlueTri
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = list["listName"] as? String ?: "",
                                color = BlueTri,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val selectedIds = selectedLists.filter { it.value }.keys.toList()
                    onConfirm(selectedIds)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = BluePri,
                    contentColor = BlueTri
                )
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = BluePri,
                    contentColor = BlueTri
                )
            ) {
                Text("Cancel")
            }
        }
    )
}

private fun addMovieToLists(
    movieTitle: String,
    imageUrl: String?,
    listIds: List<String>,
    userId: String?,
    db: FirebaseFirestore
) {
    if (userId == null || listIds.isEmpty()) return

    val filmData = mapOf(
        "title" to movieTitle,
        "imageUrl" to (imageUrl ?: "")
    )

    listIds.forEach { listId ->
        db.collection("users").document(userId)
            .collection("myLists").document(listId)
            .update("films", FieldValue.arrayUnion(filmData))
            .addOnFailureListener { e ->
                // Tratar erro
            }
    }
}

// funcao de upload do poster personalizado
private fun uploadCustomPoster(
    movieTitle: String,
    bitmap: Bitmap,
    userId: String?,
    db: FirebaseFirestore
) {
    userId?.let { uid ->
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos)
        val imageBytes = baos.toByteArray()
        val base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT)

        val posterData = mapOf(
            "title" to movieTitle,
            "img" to base64Image
        )

        db.collection("users").document(uid)
            .get()
            .addOnSuccessListener { document ->
                val currentPosters = document.get("customPosters") as? MutableList<Map<String, String>> ?: mutableListOf()

                // Remove existing poster for this movie if exists
                currentPosters.removeAll { it["title"] == movieTitle }

                // Add new poster
                currentPosters.add(posterData)

                // Update in Firestore
                db.collection("users").document(uid)
                    .update("customPosters", currentPosters)
                    .addOnSuccessListener {
                        // Success
                    }
                    .addOnFailureListener {
                        // Handle error
                    }
            }
    }
}

