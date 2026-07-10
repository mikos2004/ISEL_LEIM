package dam.a50746.disneyjournal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dam.a50746.disneyjournal.R
import dam.a50746.disneyjournal.api.fetchMoviePoster
import dam.a50746.disneyjournal.ui.CarouselItemHome
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _goldenAgePosters = MutableStateFlow<List<CarouselItemHome>>(emptyList())
    val goldenAgePosters: StateFlow<List<CarouselItemHome>> = _goldenAgePosters

    private val _wartimePosters = MutableStateFlow<List<CarouselItemHome>>(emptyList())
    val wartimePosters: StateFlow<List<CarouselItemHome>> = _wartimePosters

    private val _silverAgePosters = MutableStateFlow<List<CarouselItemHome>>(emptyList())
    val silverAgePosters: StateFlow<List<CarouselItemHome>> = _silverAgePosters

    private val _bronzeAgePosters = MutableStateFlow<List<CarouselItemHome>>(emptyList())
    val bronzeAgePosters: StateFlow<List<CarouselItemHome>> = _bronzeAgePosters

    private val _renaissancePosters = MutableStateFlow<List<CarouselItemHome>>(emptyList())
    val renaissancePosters: StateFlow<List<CarouselItemHome>> = _renaissancePosters

    private val _postRenaissancePosters = MutableStateFlow<List<CarouselItemHome>>(emptyList())
    val postRenaissancePosters: StateFlow<List<CarouselItemHome>> = _postRenaissancePosters

    private val _revivalPosters = MutableStateFlow<List<CarouselItemHome>>(emptyList())
    val revivalPosters: StateFlow<List<CarouselItemHome>> = _revivalPosters

    private val goldenAgeMovies = listOf(
        "Snow White and the Seven Dwarfs",
        "Pinocchio (film)",
        "Fantasia",
        "Dumbo (1941 film)",
        "Bambi (film)"
    )

    private val wartimeMovies = listOf(
        "Saludos Amigos",
        "The Three Caballeros",
        "Make Mine Music",
        "Melody Time",
        "Fun and Fancy Free",
        "The Adventures of Ichabod and Mr. Toad",
    )

    private val silverAgeMovies = listOf(
        "Cinderella (1950 film)",
        "Alice in Wonderland (1951 film)",
        "Peter Pan (film)",
        "Lady and the Tramp",
        "Sleeping Beauty",
        "One Hundred and One Dalmatians",
        "The Sword in the Stone",
        "The Jungle Book",
        "The Aristocats",
    )

    private val bronzeAgeMovies = listOf(
        "Robin Hood (film)",
        "The Many Adventures of Winnie the Pooh",
        "The Rescuers",
        "The Fox and the Hound",
        "The Black Cauldron",
        "The Great Mouse Detective",
        "Oliver %26 Company",
    )

    private val renaissanceMovies = listOf(
        "The Little Mermaid (1989 film)",
        "The Rescuers Down Under",
        "Beauty and the Beast (1991 film)",
        "Aladdin (film)",
        "The Lion King",
        "Pocahontas (film)",
        "The Hunchback of Notre Dame",
        "Hercules (film)",
        "Mulan (1998 film)",
        "Tarzan (film)"
    )

    private val postRenaissanceMovies = listOf(
        "Fantasia 2000",
        "Dinosaur",
        "The Emperor's New Groove",
        "Atlantis:_The_Lost_Empire",
        "Lilo %26 Stitch",
        "Treasure Planet",
        "Brother Bear",
        "Home on the Range",
        "Chicken Little (film)",
        "Meet the Robinsons",
        "Bolt (film)"
    )

    private val revivalMovies = listOf(
        "The Princess and the Frog",
        "Tangled",
        "Winnie the Pooh (film)",
        "Wreck-It Ralph (film)",
        "Frozen",
        "Big Hero 6",
        "Zootopia",
        "Moana (film)",
        "Ralph Breaks the Internet",
        "Frozen II",
    )



    init {
        fetchGoldenAgePosters()
        fetchWartimePosters()
        fetchSilverAgePosters()
        fetchBronzeAgePosters()
        fetchRenaissancePosters()
        fetchPostRenaissancePosters()
        fetchRevivalPosters()
    }

    private fun fetchGoldenAgePosters() {
        viewModelScope.launch {
            val posters = mutableListOf<CarouselItemHome>()
            for ((index, title) in goldenAgeMovies.withIndex()) {
                val url = try {
                    fetchMoviePoster(title).also {
                        println("Fetched URL for $title: $it")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }

                posters.add(
                    CarouselItemHome(
                        id = index,
                        imageResId = R.drawable.mickey_shadow,
                        imageUrl = url,
                        contentDescription = title
                    )
                )
            }
            _goldenAgePosters.value = posters
        }
    }

    private fun fetchWartimePosters() {
        viewModelScope.launch {
            val posters = mutableListOf<CarouselItemHome>()
            for ((index, title) in wartimeMovies.withIndex()) {
                val url = try {
                    fetchMoviePoster(title).also {
                        println("Fetched URL for $title: $it")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }

                posters.add(
                    CarouselItemHome(
                        id = index,
                        imageResId = R.drawable.mickey_shadow,
                        imageUrl = url,
                        contentDescription = title
                    )
                )
            }
            _wartimePosters.value = posters
        }
    }

    private fun fetchSilverAgePosters() {
        viewModelScope.launch {
            val posters = mutableListOf<CarouselItemHome>()
            for ((index, title) in silverAgeMovies.withIndex()) {
                val url = try {
                    fetchMoviePoster(title).also {
                        println("Fetched URL for $title: $it")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }

                posters.add(
                    CarouselItemHome(
                        id = index,
                        imageResId = R.drawable.mickey_shadow,
                        imageUrl = url,
                        contentDescription = title
                    )
                )
            }
            _silverAgePosters.value = posters
        }
    }

    private fun fetchBronzeAgePosters() {
        viewModelScope.launch {
            val posters = mutableListOf<CarouselItemHome>()
            for ((index, title) in bronzeAgeMovies.withIndex()) {
                val url = try {
                    fetchMoviePoster(title).also {
                        println("Fetched URL for $title: $it")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }

                posters.add(
                    CarouselItemHome(
                        id = index,
                        imageResId = R.drawable.mickey_shadow,
                        imageUrl = url,
                        contentDescription = title
                    )
                )
            }
            _bronzeAgePosters.value = posters
        }
    }

    private fun fetchRenaissancePosters() {
        viewModelScope.launch {
            val posters = mutableListOf<CarouselItemHome>()
            for ((index, title) in renaissanceMovies.withIndex()) {
                val url = try {
                    fetchMoviePoster(title).also {
                        println("Fetched URL for $title: $it")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }

                posters.add(
                    CarouselItemHome(
                        id = index,
                        imageResId = R.drawable.mickey_shadow,
                        imageUrl = url,
                        contentDescription = title
                    )
                )
            }
            _renaissancePosters.value = posters
        }
    }

    private fun fetchPostRenaissancePosters() {
        viewModelScope.launch {
            val posters = mutableListOf<CarouselItemHome>()
            for ((index, title) in postRenaissanceMovies.withIndex()) {
                val url = try {
                    fetchMoviePoster(title).also {
                        println("Fetched URL for $title: $it")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }

                posters.add(
                    CarouselItemHome(
                        id = index,
                        imageResId = R.drawable.mickey_shadow,
                        imageUrl = url,
                        contentDescription = title
                    )
                )
            }
            _postRenaissancePosters.value = posters
        }
    }

    private fun fetchRevivalPosters() {
        viewModelScope.launch {
            val posters = mutableListOf<CarouselItemHome>()
            for ((index, title) in revivalMovies.withIndex()) {
                val url = try {
                    fetchMoviePoster(title).also {
                        println("Fetched URL for $title: $it")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }

                posters.add(
                    CarouselItemHome(
                        id = index,
                        imageResId = R.drawable.mickey_shadow,
                        imageUrl = url,
                        contentDescription = title
                    )
                )
            }
            _revivalPosters.value = posters
        }
    }
}
