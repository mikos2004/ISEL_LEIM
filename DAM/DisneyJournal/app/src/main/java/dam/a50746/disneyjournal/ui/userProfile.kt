package dam.a50746.disneyjournal.ui


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.net.Uri
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.Box
import dam.a50746.disneyjournal.ui.theme.DarkBluePri
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil3.compose.SubcomposeAsyncImage
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import dam.a50746.disneyjournal.ui.theme.BlueSec
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
//@Preview(showSystemUi = true)
@Composable
fun UserProfile(
    onBackClick: () -> Unit = {},
    onPosterClick: () -> Unit,
    onCharClick: () -> Unit,
    navController : NavHostController,
    context: Context
) {
    var showMenu by remember { mutableStateOf(false) }
    val currentUser = Firebase.auth.currentUser
    var showEditMenu by remember { mutableStateOf(false) }
    var profileImageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val db = Firebase.firestore


    // Launcher para selecionar imagem da galeria
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val inputStream = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            profileImageBitmap = bitmap
            uploadProfileImage(bitmap, db, currentUser?.uid)
        }
    }

    // Carrega a imagem do perfil se existir no Firestore
    LaunchedEffect(Unit) {
        if (currentUser?.uid != null) {
            val docRef = db.collection("users").document(currentUser.uid)
            try {
                val document = docRef.get().await()
                if (document.exists()) {
                    val base64Image = document.getString("profileImage")
                    if (!base64Image.isNullOrEmpty()) {
                        val imageBytes = Base64.decode(base64Image, Base64.DEFAULT)
                        profileImageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                    }
                }
            } catch (e: Exception) {
                // Tratar erro
            }
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
                            text = currentUser?.displayName ?: "Profile",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
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
                    IconButton(onClick = { showEditMenu = true }) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "Edit Account",
                            tint = BlueTri,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = showEditMenu,
                        onDismissRequest = { showEditMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Change Profile Picture") },
                            onClick = {
                                showEditMenu = false
                                galleryLauncher.launch("image/*")
                            }
                        )
                    }

                    // Menu "More"
                    IconButton(onClick = { showMenu = true }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            tint = BlueTri,
                            contentDescription = "More Options",
                        )
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Logout") },
                            onClick = {
                                showMenu = false
                                Firebase.auth.signOut()
                                navController.navigate("login") {
                                    popUpTo("profile") { inclusive = true }
                                }
                            }
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        ScrollUserProfile(innerPadding,
            onCharClick,
            onPosterClick,
            profileImageBitmap?.asImageBitmap(),
            navController
        )
    }
}


@Composable
fun ScrollUserProfile(
    innerPadding: PaddingValues,
    onCharClick: () -> Unit = {},
    onPosterClick: () -> Unit,
    profileImage: ImageBitmap?,
    navController: NavHostController
) {
    val db = Firebase.firestore
    val currentUser = Firebase.auth.currentUser
    var favoriteFilms by remember { mutableStateOf<List<Map<String, String>>>(emptyList()) }
    var favoriteCharacters by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }

    var showCreateListDialog by remember { mutableStateOf(false) }
    var myLists by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }


    // Carregar favoritos
    LaunchedEffect(Unit) {
        if (currentUser?.uid != null) {
            // Carregar filmes favoritos
            db.collection("users").document(currentUser.uid)
                .addSnapshotListener { snapshot, _ ->
                    val films = snapshot?.get("favoriteFilms") as? List<Map<String, String>> ?: emptyList()
                    favoriteFilms = films
                }

            // Carregar personagens favoritos
            db.collection("users").document(currentUser.uid)
                .addSnapshotListener { snapshot, _ ->
                    val chars = snapshot?.get("favoriteCharacters") as? List<Map<String, Any>> ?: emptyList()
                    favoriteCharacters = chars
                }

            // Carregar listas do user
            db.collection("users").document(currentUser.uid)
                .collection("myLists")
                .addSnapshotListener { snapshot, _ ->
                    val lists = snapshot?.documents?.map { doc ->
                        doc.data?.plus("id" to doc.id) ?: emptyMap()
                    } ?: emptyList()
                    myLists = lists
                }
        }
    }


    val carouselTitles0 = if (favoriteCharacters.isNotEmpty()) {
        listOf("Favorite Characters")
    } else {
        emptyList()
    }


    // Adicionar "My Favorite Films" apenas se houver filmes favoritos
    val carouselTitles2 = if (favoriteFilms.isNotEmpty()) {
        listOf("My Favorite Films")
    } else {
        emptyList()
    }

    val carouselTitles3 = if (myLists.isNotEmpty()) {
        listOf("My Lists")
    } else {
        emptyList()
    }

    LazyColumn(
        modifier = Modifier.padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            // Avatar
            Box(modifier = Modifier.padding(15.dp)) {
                if (profileImage != null) {
                    Image(
                        bitmap = profileImage,
                        contentDescription = "User Avatar",
                        modifier = Modifier
                            .size(140.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.default_avatar),
                        contentDescription = "Default Avatar",
                        modifier = Modifier
                            .size(140.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))


            Button(
                onClick = { showCreateListDialog = true },
                modifier = Modifier
                    .width(200.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BluePri,
                    contentColor = BlueTri
                )
            ) {
                Text("Create List", fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(30.dp))
        }


        items(carouselTitles0) { title ->
            DisneyCarouselUserFavoriteCharacters(
                title = title,
                characters = favoriteCharacters,
                onCharClick = onCharClick,
                navController = navController
            )
        }

        items(carouselTitles2) { title ->
            DisneyCarouselUserFavoriteFilms(
                title = title,
                films = favoriteFilms,
                onPosterClick = onPosterClick,
                navController = navController
            )
        }

        items(carouselTitles3) { title ->
            DisneyCarouselUserLists(
                title = title,
                lists = myLists,
                navController = navController
            )
        }
    }

    if (showCreateListDialog) {
        CreateListDialog(
            onDismiss = { showCreateListDialog = false },
            onConfirm = { listName ->
                createNewList(listName, currentUser?.uid, db)
            }
        )
    }
}

@Composable
fun DisneyCarouselUserFavoriteCharacters(
    title: String,
    characters: List<Map<String, Any>>,
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onCharClick: () -> Unit = {}
) {
    Column(modifier = modifier.padding(vertical = 10.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = DarkBluePri
                )
            )
        }

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(characters) { character ->
                Box(
                    modifier = Modifier
                        .height(180.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .clickable {
                            val id = (character["id"] as? Number)?.toInt() ?: 0
                            val name = character["name"]?.toString() ?: ""
                            val imageUrl = character["imageUrl"]?.toString()

                            val encodedName = URLEncoder.encode(name, "UTF-8")
                            val encodedImageUrl = imageUrl?.let { URLEncoder.encode(it, "UTF-8") } ?: ""

                            navController.navigate("charProfile/$id/$encodedName/$encodedImageUrl")
                        }
                ) {
                    SubcomposeAsyncImage(
                        model = character["imageUrl"]?.toString() ?: "",
                        contentDescription = character["name"]?.toString(),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(170.dp)
                            .width(150.dp)
                            .clip(MaterialTheme.shapes.medium),
                        loading = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = BluePri)
                            }
                        },
                        error = {
                            Image(
                                painter = painterResource(id = R.drawable.cinderella_prof),
                                contentDescription = "Error",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun DisneyCarouselUserFavoriteFilms(
    title: String,
    films: List<Map<String, String>>,
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onPosterClick: () -> Unit = {}
) {
    Column(modifier = modifier.padding(vertical = 10.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = DarkBluePri
                )
            )
        }

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(films) { film ->
                Box(
                    modifier = Modifier
                        .height(180.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .clickable {
                            val encodedTitle = URLEncoder.encode(film["title"] ?: "", "UTF-8")
                            val encodedUrl = URLEncoder.encode(film["imageUrl"] ?: "", "UTF-8")
                            navController.navigate("movieProfile/$encodedTitle/$encodedUrl")
                        }
                ) {
                    SubcomposeAsyncImage(
                        model = film["imageUrl"] ?: "",
                        contentDescription = film["title"],
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(170.dp)
                            .width(150.dp)
                            .clip(MaterialTheme.shapes.medium),
                        loading = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.cinderella_poster),
                                    contentDescription = "Loading",
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        },
                        error = {
                            Image(
                                painter = painterResource(id = R.drawable.cinderella_poster),
                                contentDescription = "Error",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    )
                }
            }
        }
    }
}


private fun createNewList(listName: String, userId: String?, db: FirebaseFirestore) {
    userId?.let { uid ->
        val newList = hashMapOf(
            "listName" to listName,
            "films" to emptyList<Map<String, String>>()
        )

        db.collection("users").document(uid)
            .collection("myLists")
            .add(newList)
            .addOnSuccessListener {
                // Lista criada com sucesso
            }
            .addOnFailureListener {
                // Tratar erro
            }
    }
}

@Composable
fun CreateListDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var listName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create New List", color = BlueTri) },
        text = {
            TextField(
                value = listName,
                onValueChange = { listName = it },
                label = { Text("List name", color = BlueTri ) },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = BlueSec,
                    unfocusedContainerColor = BlueSec,
                    focusedTextColor = BlueTri,
                    unfocusedTextColor = BlueTri,
                    focusedLabelColor = BluePri,
                    unfocusedLabelColor = BluePri,
                    focusedIndicatorColor = BluePri,
                    unfocusedIndicatorColor = BluePri
                )
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    if (listName.isNotBlank()) {
                        onConfirm(listName)
                        onDismiss()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = BluePri,
                    contentColor = BlueTri
                )
            ) {
                Text("Create")
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

// Função para upload da imagem
private fun uploadProfileImage(bitmap: Bitmap, db: FirebaseFirestore, userId: String?) {
    userId?.let { uid ->
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos)
        val imageBytes = baos.toByteArray()
        val base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT)

        db.collection("users").document(uid)
            .set(mapOf("profileImage" to base64Image))
            .addOnSuccessListener {
                // Upload bem-sucedido
            }
            .addOnFailureListener {
                // Tratar erro
            }
    }
}

@Composable
fun DisneyCarouselUserLists(
    title: String,
    lists: List<Map<String, Any>>,
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    Column(modifier = modifier.padding(vertical = 10.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = DarkBluePri
                )
            )
        }

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(lists) { list ->
                Box(
                    modifier = Modifier
                        .height(180.dp)
                        .width(150.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .clickable {
                            val encodedListName = URLEncoder.encode(list["listName"]?.toString() ?: "", "UTF-8")
                            val films = (list["films"] as? List<Map<String, String>>)?.map { it["title"] ?: "" } ?: emptyList()
                            // Usar | como separador em vez de vírgula
                            val encodedFilms = URLEncoder.encode(films.joinToString("|"), "UTF-8")
                            navController.navigate("list/$encodedListName/$encodedFilms/false")
                        }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(BluePri.copy(alpha = 0.2f))
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = list["listName"]?.toString() ?: "",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${(list["films"] as? List<*>)?.size ?: 0} films",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

