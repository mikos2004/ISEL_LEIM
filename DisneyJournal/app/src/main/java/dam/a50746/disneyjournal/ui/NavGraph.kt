package dam.a50746.disneyjournal.ui

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHost
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import java.net.URLDecoder
import java.net.URLEncoder

@Composable
fun DisneyApp(context: Context) {
    val navController = rememberNavController()
    val auth = Firebase.auth
    val isUserLoggedIn = auth.currentUser != null

    // Verifica estado do login
    LaunchedEffect(isUserLoggedIn) {
        if (isUserLoggedIn) {
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (isUserLoggedIn) "home" else "login"
    ) {
        composable("home") {
            HomePage(
                onAccountClick = { navController.navigate("profile") },
                onArrowClick = {navController.navigate("list")},
                onPosterClick = {navController.navigate("movieProfile")},
                navController
            )
        }
        composable("profile") {
            UserProfile(
                onBackClick = { navController.popBackStack() },
                onPosterClick = {navController.navigate("movieProfile")},
                onCharClick =  {navController.navigate("charProfile")},
                navController,
                context
            )
        }
        composable(
            "list/{listName}/{movieTitles}/{fromHome}",
            arguments = listOf(
                navArgument("listName") { type = NavType.StringType },
                navArgument("movieTitles") { type = NavType.StringType },
                navArgument("fromHome") { type = NavType.BoolType }
            )
        ) { backStackEntry ->
            val listName = URLDecoder.decode(backStackEntry.arguments?.getString("listName") ?: "", "UTF-8")
            val movieTitlesString = URLDecoder.decode(backStackEntry.arguments?.getString("movieTitles") ?: "", "UTF-8")
            val fromHome = backStackEntry.arguments?.getBoolean("fromHome") ?: false
            val movieTitles = movieTitlesString.split("|")

            ListView(
                listName = listName,
                movieTitles = movieTitles,
                onBackClick = { navController.popBackStack() },
                onItemClick = { movieTitle ->
                    navController.navigate("movieProfile/${URLEncoder.encode(movieTitle, "UTF-8")}/")
                },
                navController = navController,
                fromHome = fromHome
            )
        }
        composable(
            "movieProfile/{movieTitle}/{imageUrl}",
            arguments = listOf(
                navArgument("movieTitle") { type = NavType.StringType },
                navArgument("imageUrl") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val movieTitle = URLDecoder.decode(backStackEntry.arguments?.getString("movieTitle") ?: "", "UTF-8")
            val imageUrl = URLDecoder.decode(backStackEntry.arguments?.getString("imageUrl") ?: "", "UTF-8")

            MovieProfile(
                movieTitle = movieTitle,
                imageUrl = if (imageUrl.isNotEmpty()) imageUrl else null,
                onBackClick = { navController.popBackStack() }
            )
        }


        composable(
            "charProfile/{id}/{name}/{imageUrl}",
            arguments = listOf(
                navArgument("id") { type = NavType.IntType },
                navArgument("name") { type = NavType.StringType },
                navArgument("imageUrl") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            val name = URLDecoder.decode(backStackEntry.arguments?.getString("name") ?: "", "UTF-8")
            val imageUrl = backStackEntry.arguments?.getString("imageUrl")?.let {
                URLDecoder.decode(it, "UTF-8")
            }

            CharacProfile(
                characterId = id,
                characterName = name,
                imageUrl = imageUrl,
                onBackClick = { navController.popBackStack() },
                onItemClick = { navController.navigate("movieProfile")},
                navController
            )
        }
        composable("signup") { CreateAccPage(navController) }
        composable("login") { LoginPage(navController) }
    }
}