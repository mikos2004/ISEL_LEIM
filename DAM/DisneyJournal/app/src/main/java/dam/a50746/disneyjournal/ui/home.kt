package dam.a50746.disneyjournal.ui

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dam.a50746.disneyjournal.R
import dam.a50746.disneyjournal.ui.theme.BluePri
import dam.a50746.disneyjournal.ui.theme.BlueTri
import dam.a50746.disneyjournal.ui.theme.DarkBluePri
import dam.a50746.disneyjournal.viewmodel.CharacterSearchViewModel
import dam.a50746.disneyjournal.viewmodel.HomeViewModel
import java.net.URLEncoder

// Atualize o HomePage.kt
@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    onAccountClick: () -> Unit = {},
    onArrowClick: () -> Unit = {},
    onPosterClick: () -> Unit = {},
    navController: NavHostController
) {
    var showMenu by remember { mutableStateOf(false) }
    var showSearch by remember { mutableStateOf(false) }
    val searchViewModel: CharacterSearchViewModel = viewModel()
    val searchResults by searchViewModel.searchResults.collectAsState()
    val isSearching by searchViewModel.isSearching.collectAsState()

    Scaffold(
        topBar = {
            LargeTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BluePri,
                    titleContentColor = BlueTri,
                ),
                title = {
                    if (showSearch) {
                        var searchQuery by remember { mutableStateOf("") }

                        Box(modifier = Modifier.fillMaxWidth()) {
                            TextField(
                                value = searchQuery,
                                onValueChange = {
                                    searchQuery = it
                                    searchViewModel.onSearchQueryChanged(it)
                                },
                                placeholder = { Text("Search characters...") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    disabledContainerColor = Color.Transparent,
                                    focusedIndicatorColor = BlueTri,
                                    unfocusedIndicatorColor = BlueTri,
                                    cursorColor = BlueTri,
                                    focusedTextColor = BlueTri,
                                    unfocusedTextColor = BlueTri,
                                    focusedPlaceholderColor = BlueTri.copy(alpha = 0.5f),
                                    unfocusedPlaceholderColor = BlueTri.copy(alpha = 0.5f)
                                ),
                                singleLine = true,
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.Search,
                                        contentDescription = "Search",
                                        tint = BlueTri
                                    )
                                },
                                trailingIcon = {
                                    if (searchQuery.isNotEmpty()) {
                                        IconButton(
                                            onClick = {
                                                searchQuery = ""
                                                searchViewModel.onSearchQueryChanged("")
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Close,
                                                contentDescription = "Clear",
                                                tint = BlueTri
                                            )
                                        }
                                    }
                                }
                            )
                        }
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "Disney Journal",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            IconButton(
                                onClick = onAccountClick,
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.account_circle),
                                    contentDescription = "Account",
                                    tint = BlueTri,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }
                    }
                },
                actions = {
                    if (!showSearch) {
                        IconButton(onClick = { showSearch = true }) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                tint = BlueTri,
                                contentDescription = "Search"
                            )
                        }
                    }
                    IconButton(onClick = { showMenu = true }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            tint = BlueTri,
                            contentDescription = "Menu options",
                        )
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
                                        popUpTo("home") { inclusive = true }
                                    }
                                }
                            )
                        }
                    }
                },
            )
        },
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            if (showSearch && searchResults.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(innerPadding)
                ) {
                    items(searchResults) { character ->
                        Text(
                            text = character.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .clickable {
                                    val encodedName = URLEncoder.encode(character.name, "UTF-8")
                                    val encodedImageUrl = character.imageUrl?.let {
                                        URLEncoder.encode(it, "UTF-8")
                                    } ?: ""
                                    navController.navigate(
                                        "charProfile/${character.id}/${encodedName}/${encodedImageUrl}"
                                    )

                                },
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = DarkBluePri
                            )
                        )
                    }
                }
            } else if (isSearching) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = BluePri)
                }
            } else if (showSearch && searchResults.isEmpty() && searchViewModel.searchQuery.value.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No characters found", color = DarkBluePri)
                }
            } else {
                ScrollContentHome(
                    innerPadding = innerPadding,
                    onArrowClick = onArrowClick,
                    onPosterClick = { item ->
                        val encodedTitle = URLEncoder.encode(item.contentDescription, "UTF-8")
                        val encodedUrl = URLEncoder.encode(item.imageUrl ?: "", "UTF-8")
                        navController.navigate("movieProfile/$encodedTitle/$encodedUrl")
                    },
                    navController = navController
                )
            }

            // Botão de voltar quando a busca está ativa
            if (showSearch) {
                IconButton(
                    onClick = {
                        showSearch = false
                        searchViewModel.onSearchQueryChanged("")
                    },
                    modifier = Modifier
                        .padding(top = innerPadding.calculateTopPadding() + 8.dp, end = 8.dp)
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = BlueTri
                    )
                }
            }
        }
    }
}

data class CarouselItemHome(
    val id: Int,
    @DrawableRes val imageResId: Int,
    val imageUrl: String? = null,
    val contentDescription: String
)

@Composable
fun DisneyCarouselHome(
    title: String,
    items: List<CarouselItemHome>,
    modifier: Modifier = Modifier,
    onArrowClick: (String) -> Unit = {},
    onPosterClick: (CarouselItemHome) -> Unit = {}
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

            IconButton(onClick = { onArrowClick(title) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    tint = DarkBluePri,
                    contentDescription = "Full List"
                )
            }
        }

        // Carousel
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(items) { item ->
                Box(
                    modifier = Modifier
                        .height(180.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .clickable { onPosterClick(item) }
                ) {
                    SubcomposeAsyncImage(
                        model = item.imageUrl,
                        contentDescription = item.contentDescription,
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
                                    painter = painterResource(id = item.imageResId),
                                    contentDescription = "Loading",
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        },
                        error = {
                            Image(
                                painter = painterResource(id = item.imageResId),
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
fun ScrollContentHome(
    innerPadding: PaddingValues,
    onArrowClick: () -> Unit,
    onPosterClick: (CarouselItemHome) -> Unit,
    navController: NavHostController
) {

    val viewModel: HomeViewModel = viewModel()
    val goldenAgePosters by viewModel.goldenAgePosters.collectAsState()
    val wartimePosters by viewModel.wartimePosters.collectAsState()
    val silverAgePosters by viewModel.silverAgePosters.collectAsState()
    val bronzeAgePosters by viewModel.bronzeAgePosters.collectAsState()
    val renaissancePosters by viewModel.renaissancePosters.collectAsState()
    val postRenaissancePosters by viewModel.postRenaissancePosters.collectAsState()
    val revivalPosters by viewModel.revivalPosters.collectAsState()


    // Other carousel items (unchanged)
    val carouselItems = remember {
        listOf(
            CarouselItemHome(0, R.drawable.cinderella_poster, contentDescription = "cupcake"),
            CarouselItemHome(1, R.drawable.cinderella_poster, contentDescription = "donut"),
            CarouselItemHome(2, R.drawable.cinderella_poster, contentDescription = "eclair"),
            CarouselItemHome(3, R.drawable.cinderella_poster, contentDescription = "froyo"),
            CarouselItemHome(4, R.drawable.cinderella_poster, contentDescription = "gingerbread"),
        )
    }

    val carouselTitles = listOf(
        "The Golden Age (1937-1941)",
        "Wartime (1942-1949)",
        "The Silver Age (1950-1967)",
        "The Bronze Age (1968-1988)",
        "The Renaissance (1989-1999)",
        "Post-Renaissance (2000-2008)",
        "Revival (2009-Present)"
    )

    LazyColumn(
        modifier = Modifier.padding(innerPadding),
    ) {
        itemsIndexed(carouselTitles) { index, title ->
            DisneyCarouselHome(
                title = title,
                items = when (index) {
                    0 -> goldenAgePosters
                    1 -> wartimePosters
                    2 -> silverAgePosters
                    3 -> bronzeAgePosters
                    4 -> renaissancePosters
                    5 -> postRenaissancePosters
                    6 -> revivalPosters
                    else -> carouselItems
                },
                onArrowClick = { listName ->
                    val encodedListName = URLEncoder.encode(listName, "UTF-8")
                    // Passar os itens correspondentes à lista
                    val items = when (listName) {
                        "The Golden Age (1937-1941)" -> goldenAgePosters
                        "Wartime (1942-1949)" -> wartimePosters
                        "The Silver Age (1950-1967)" -> silverAgePosters
                        "The Bronze Age (1968-1988)" -> bronzeAgePosters
                        "The Renaissance (1989-1999)" -> renaissancePosters
                        "Post-Renaissance (2000-2008)" -> postRenaissancePosters
                        "Revival (2009-Present)" -> revivalPosters
                        else -> carouselItems
                    }

                    val movieTitles = items.map { it.contentDescription }.toTypedArray()
                    navController.navigate("list/$encodedListName/${URLEncoder.encode(movieTitles.joinToString("|"), "UTF-8")}/true")
               },
                onPosterClick = onPosterClick
            )

        }
    }
}