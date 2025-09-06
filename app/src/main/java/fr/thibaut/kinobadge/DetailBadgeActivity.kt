package fr.thibaut.kinobadge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import coil.compose.AsyncImage
import com.google.gson.Gson
import fr.thibaut.kinobadge.ui.theme.KinoBadgeTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

class DetailBadgeActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val badgeId = intent.getIntExtra("badgeId", -1)

        setContent {
            var badge by remember { mutableStateOf<Badge?>(null) }
            var estEnChargement by remember { mutableStateOf(false) }

            // Charger le badge dès que le composable est affiché
            LaunchedEffect(badgeId) {
                estEnChargement = true
                recupererDonnees(badgeId) { result ->
                    badge = result
                }
                estEnChargement = false
            }

            KinoBadgeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        TopAppBar(
                            title = { Text(text = "Détail du badge") },
                            colors = TopAppBarDefaults.topAppBarColors(),
                            modifier = Modifier.fillMaxWidth(),
                            navigationIcon = {
                                androidx.compose.material3.IconButton(onClick = { finish() }) {
                                    androidx.compose.material3.Icon(
                                        imageVector = Icons.Default.ArrowBackIosNew,
                                        contentDescription = "Retour"
                                    )
                                }
                            }
                        )
                        if (estEnChargement) {
                            Text(
                                text = "Chargement...",
                                modifier = Modifier.padding(24.dp),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        } else {
                            if (badge == null) {
                                Text(
                                    text = "Résilience",
                                    modifier = Modifier.padding(24.dp),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            } else {
                                DetailBadgeScreen(badge!!)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun recupererDonnees(badgeId: Int, onSuccess: (Badge?) -> Unit) {
        lifecycleScope.launch {
            val result: Badge? = withContext(Dispatchers.IO) {
                val url = URL("https://api.kinomap.dev/v4/badges/mobile-tech-test?appToken=Y7pNWqI4nlYuGBILm46tqw57aKInntGTpzQau30To8WDSt6ZOU60GHWG8QSyWIs1TsFrnheftxBmmFWxR4eKhUWruEndo0aXaZVC6tn9fWhdBDb0ThVvmY6E")
                val urlConnection = url.openConnection() as HttpURLConnection
                try {
                    val input = BufferedReader(InputStreamReader(urlConnection.inputStream))
                    val response = StringBuilder()
                    var line: String?
                    while (input.readLine().also { line = it } != null) {
                        response.append(line)
                    }
                    val gson = Gson()
                    val apiResponse = gson.fromJson(response.toString(), ReponseApi::class.java)

                    apiResponse.data
                        .flatMap { it.badges }
                        .find { it.id == badgeId }
                } finally {
                    urlConnection.disconnect()
                }
            }
            onSuccess(result)
        }
    }
}

@Composable
fun DetailBadgeScreen(badge: Badge) {
    val url = if (badge.unlocked_date != null) {
        badge.images_url.unlocked
    } else {
        badge.images_url.locked
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = url,
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )
        Text(
            text = badge.name,
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = badge.description,
            style = MaterialTheme.typography.bodyMedium
        )
        if (badge.unlocked_date != null) {
            val date = formatTimestamp(badge.unlocked_date)
            Text(
                text = "Débloqué le : $date",
                style = MaterialTheme.typography.bodySmall
            )
        } else {
            Text(
                text = "Badge non débloqué",
                style = MaterialTheme.typography.bodySmall
            )
        }
        Text(
            text = "Progression : ${(badge.unlocked_percent ?: 0)}%",
            style = MaterialTheme.typography.bodySmall
        )
        LinearProgressIndicator(
            progress = { ((badge.unlocked_percent ?: 0) / 100f).coerceIn(0f, 1f) },
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}
