package dam.a50746.disneyjournal.ui

import android.content.Context
import android.util.Log
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import coil3.ImageLoader
import coil3.compose.SubcomposeAsyncImage
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.ImageRequest
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import dam.a50746.disneyjournal.api.CharacterData
import dam.a50746.disneyjournal.api.fetchCharacters
import dam.a50746.disneyjournal.api.fetchMoviePoster
import dam.a50746.disneyjournal.ui.theme.RedDelete
import dam.a50746.disneyjournal.ui.theme.YellowStar
import okhttp3.OkHttpClient
import java.net.URLEncoder


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacProfile(
    characterId: Int,
    characterName: String,
    imageUrl: String?,
    onBackClick: () -> Unit,
    onItemClick: () -> Unit,
    navController: NavHostController
) {
    val currentUser = Firebase.auth.currentUser
    val db = Firebase.firestore
    var isStarFilled by remember { mutableStateOf(false) }
    var characterData by remember { mutableStateOf<CharacterData?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    var ignoreNextListenerUpdate by remember { mutableStateOf(false) }

    LaunchedEffect(currentUser, characterId) {
        if (currentUser?.uid != null) {
            db.collection("users").document(currentUser.uid)
                .addSnapshotListener { document, _ ->
                    if (!ignoreNextListenerUpdate) {
                        document?.let {
                            val favoriteChars = it.get("favoriteCharacters") as? List<Map<String, Any>> ?: emptyList()
                            isStarFilled = favoriteChars.any { (it["id"] as? Number)?.toInt() == characterId }
                        }
                    } else {
                        ignoreNextListenerUpdate = false
                    }
                }
        } else {
            isStarFilled = false
        }
    }

    LaunchedEffect(characterId) {
        if (currentUser?.uid != null) {
            db.collection("users").document(currentUser.uid)
                .addSnapshotListener { document, _ ->
                    if (!ignoreNextListenerUpdate) {
                        document?.let {
                            val favoriteChars = it.get("favoriteCharacters") as? List<Map<String, Any>> ?: emptyList()
                            isStarFilled = favoriteChars.any { (it["id"] as? Number)?.toInt() == characterId }
                        }
                    } else {
                        ignoreNextListenerUpdate = false
                    }
                }
        }

        try {
            val response = fetchCharacters(characterName)
            val exactCharacter = response.data.find { it.id == characterId }
            characterData = exactCharacter
            isLoading = false
        } catch (e: Exception) {
            e.printStackTrace()
            isLoading = false
        }
    }

    // Função para alternar o estado de favorito
    fun toggleFavorite() {
        if (currentUser?.uid == null) return

        val userRef = db.collection("users").document(currentUser.uid)
        val charData = mapOf(
            "id" to characterId,
            "name" to characterName,
            "imageUrl" to (imageUrl ?: "")
        )

        db.runTransaction { transaction ->
            val document = transaction.get(userRef)
            val currentFavorites = document.get("favoriteCharacters") as? MutableList<Map<String, Any>> ?: mutableListOf()

            if (isStarFilled) {
                currentFavorites.removeAll { (it["id"] as? Number)?.toInt() == characterId }
            } else {
                currentFavorites.add(charData)
            }

            transaction.update(userRef, "favoriteCharacters", currentFavorites)
        }.addOnSuccessListener {
            ignoreNextListenerUpdate = true
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
                            text = characterName,
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
        ScrollCharacProfile(
            innerPadding = innerPadding,
            onItemClick = onItemClick,
            imageUrl = imageUrl,
            characterData = characterData,
            isLoading = isLoading,
            navController = navController
        )
    }
}

@Composable
fun ScrollCharacProfile(
    innerPadding: PaddingValues,
    onItemClick: () -> Unit,
    imageUrl: String?,
    characterData: CharacterData?,
    isLoading: Boolean,
    navController: NavHostController
) {
    LazyColumn(
        modifier = Modifier
            .padding(innerPadding)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))

            // Card com a foto do personagem
            Card(
                modifier = Modifier
                    .width(200.dp)
                    .aspectRatio(2f/3f)
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(8.dp)),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                if (imageUrl != null) {
                    val imageLoader = provideImageLoaderWithUserAgent()
                    SubcomposeAsyncImage(
                        model = imageUrl,
                        imageLoader = imageLoader,
                        contentDescription = "Character Photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
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
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.cinderella_prof),
                        contentDescription = "Character Photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Movies",
                    modifier = Modifier.align(Alignment.TopStart),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = DarkBluePri
                    )
                )
            }
        }

        if (isLoading) {
            item {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = BluePri)
                }
            }
        } else if (characterData?.films.isNullOrEmpty()) {
            item {
                Text(
                    text = "No movies found for this character",
                    color = DarkBluePri,
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else {
            items(characterData?.films ?: emptyList()) { filmTitle ->
                var posterUrl by remember { mutableStateOf<String?>(null) }

                // Procurar o poster do filme
                LaunchedEffect(filmTitle) {
                    posterUrl = fetchMoviePoster(filmTitle)
                }

                ListItem(
                    colors = ListItemDefaults.colors(
                        containerColor = BluePri,
                        headlineColor = BlueTri,
                        supportingColor = BlueTri,
                    ),
                    modifier = Modifier
                        .clickable {
                            //navController.navigate("movieProfile/$filmTitle/$posterUrl")
                            val encodedTitle = URLEncoder.encode(filmTitle, "UTF-8")
                            val encodedPosterUrl = URLEncoder.encode(posterUrl ?: "", "UTF-8")
                            navController.navigate("movieProfile/$encodedTitle/$encodedPosterUrl")
                        }
                        .fillMaxWidth(),
                    leadingContent = {
                        if (posterUrl != null) {
                            SubcomposeAsyncImage(
                                model = posterUrl,
                                contentDescription = "Poster of $filmTitle",
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
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.mickey_shadow),
                                contentDescription = "Default poster",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(MaterialTheme.shapes.small)
                            )
                        }
                    },
                    headlineContent = {
                        Text(
                            text = filmTitle,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    trailingContent = {
                        IconButton(onClick = { /* Handle delete */ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.delete),
                                contentDescription = "Delete $filmTitle",
                                tint = RedDelete
                            )
                        }
                    }
                )
                Divider(
                    thickness = 0.5.dp,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = BlueTri.copy(alpha = 0.3f)
                )
            }
        }
    }
}

@Composable
fun provideImageLoaderWithUserAgent(): ImageLoader {
    val context = LocalContext.current

    val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .header("User-Agent", "Mozilla/5.0")
                .build()
            chain.proceed(request)
        }
        .build()

    return remember {
        ImageLoader.Builder(context)
            .components {
                add(OkHttpNetworkFetcherFactory(client))
            }
            .build()
    }
}
