package dam.a50746.disneyjournal.api

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

@Serializable
data class ApiResponse(
    @SerialName("query") val query: Query
)

@Serializable
data class Query(
    @SerialName("pages") val pages: Map<String, Page>
)

@Serializable
data class Page(
    @SerialName("title") val title: String,
    @SerialName("thumbnail") val thumbnail: Thumbnail? = null
)

@Serializable
data class Thumbnail(
    @SerialName("source") val source: String
)

val ktorClient = HttpClient(Android) {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            isLenient = true
            explicitNulls = false
        })
    }
}

suspend fun fetchMoviePoster(movieTitle: String): String? {
    return try {
        val response: ApiResponse = ktorClient.get(
            "https://disney.fandom.com/api.php?action=query&titles=$movieTitle&prop=pageimages&format=json&pithumbsize=500"
        ).body()

        println("API Response: $response") // Para debug

        response.query.pages.values.firstOrNull()?.thumbnail?.source
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}



@Serializable
data class ParseResponse(
    @SerialName("parse") val parse: Parse
)

@Serializable
data class Parse(
    @SerialName("title") val title: String,
    @SerialName("text") val text: TextContent
)

@Serializable
data class TextContent(
    @SerialName("*") val html: String
)

suspend fun fetchMovieDetails(movieTitle: String): Pair<String?, String?> {
    return try {
        val response = ktorClient.get(
            "https://disney.fandom.com/api.php?action=parse&page=$movieTitle&format=json"
        ).body<ParseResponse>()

        val htmlContent = response.parse.text.html

        // Extrair running time
        val runtimeRegex = """<h3 class="pi-data-label pi-secondary-font">Running time</h3>\s*<div class="pi-data-value pi-font">(.*?)</div>""".toRegex()
        val runtime = runtimeRegex.find(htmlContent)?.groupValues?.get(1)

        // Extrair release date
        val releaseRegex = """<h3 class="pi-data-label pi-secondary-font">Released</h3>\s*<div class="pi-data-value pi-font">(.*?)</div>""".toRegex()
        val releaseDate = releaseRegex.find(htmlContent)?.groupValues?.get(1)
            ?.replace(Regex("<[^>]*>"), "") // Remove tags HTML

        Pair(releaseDate, runtime)
    } catch (e: Exception) {
        e.printStackTrace()
        Pair(null, null)
    }
}




// PERSONAGENS


@Serializable
data class Info(
    @SerialName("totalPages") val totalPages: Int,
    @SerialName("count") val count: Int,
    @SerialName("previousPage") val previousPage: String? = null,
    @SerialName("nextPage") val nextPage: String? = null
)

@Serializable
data class CharacterData(
    @SerialName("name") val name: String,
    @SerialName("imageUrl") val imageUrl: String? = null,
    @SerialName("_id") val id: Int,
    @SerialName("films") val films: List<String> = emptyList(),
    @SerialName("tvShows") val tvShows: List<String> = emptyList()
)



@Serializable
data class CharacterApiResponse(
    @SerialName("info") val info: Info,
    @SerialName("data") val data: List<CharacterData>
)

suspend fun fetchCharacters(query: String = "", page: Int = 1): CharacterApiResponse {
    return ktorClient.get(
        "https://api.disneyapi.dev/character?page=$page${if (query.isNotEmpty())"&name=$query" else ""}"
    ).body<CharacterApiResponse>()
}




