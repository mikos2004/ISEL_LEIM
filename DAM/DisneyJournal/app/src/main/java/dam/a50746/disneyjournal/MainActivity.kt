package dam.a50746.disneyjournal


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.firebase.FirebaseApp
import dam.a50746.disneyjournal.ui.DisneyApp
import dam.a50746.disneyjournal.ui.UserProfile
import dam.a50746.disneyjournal.ui.theme.DisneyJournalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        enableEdgeToEdge()
        setContent {
            DisneyApp(this)
        }
    }
}
