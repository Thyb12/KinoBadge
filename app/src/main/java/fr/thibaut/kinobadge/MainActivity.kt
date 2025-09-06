package fr.thibaut.kinobadge

import android.content.Intent
import android.os.Bundle
import androidx.compose.material3.TopAppBarDefaults
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import fr.thibaut.kinobadge.ui.theme.KinoBadgeTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.lang.StringBuilder
import com.google.gson.Gson
import fr.thibaut.kinobadge.ui.composants.BadgeCard
import fr.thibaut.kinobadge.ui.composants.TopBarModeAffichage
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.jvm.java



class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var listeBadge by remember { mutableStateOf<ReponseApi?>(null) }
            var isCompact by remember { mutableStateOf(false) }
            val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
            val navController = rememberNavController()
            KinoBadgeTheme {
                Scaffold(
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        TopBarModeAffichage(
                            isCompact = isCompact,
                            onIsEtendueChange = { isCompact = it },
                            scrollBehavior = scrollBehavior
                        )
                    },
                ) { innerPadding ->
                    listeBadge?.let { data ->
                        NavHost(
                            navController = navController,
                            startDestination = "list"
                        ) {
                            composable("list") {
                                listeBadge?.let { data ->
                                    val context = LocalContext.current
                                    Contenu(
                                        badgeParCategory = data,
                                        modifier = Modifier.padding(innerPadding),
                                        compact = isCompact,
                                        onBadgeClick = { badgeId ->
                                            val intent = Intent(context, DetailBadgeActivity::class.java).apply {
                                                putExtra("badgeId", badgeId)
                                            }
                                            context.startActivity(intent)
                                        }
                                    )
                                }
                            }

                        }

                    }
                    recupererDonnees { result -> listeBadge = result }
                }
            }
        }
    }

    private fun recupererDonnees(onSuccess: (ReponseApi) -> Unit) {
        lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) {
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
                    gson.fromJson(response.toString(), ReponseApi::class.java)
                } finally {
                    urlConnection.disconnect()
                }
            }
            onSuccess(result)
        }
    }
}
@Composable
fun Contenu(
    badgeParCategory: ReponseApi,
    modifier: Modifier = Modifier,
    compact: Boolean = false,
    onBadgeClick: (Int) -> Unit // <- plus de @Composable ici
) {
    val columns = if (compact) GridCells.Fixed(2) else GridCells.Fixed(1)

    LazyVerticalGrid(
        columns = columns,
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 12.dp)
    ) {
        badgeParCategory.data.forEach { category ->
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column {
                    Text(
                        text = category.name,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }

            items(category.badges) { badge ->
                BadgeCard(
                    titre = badge.name,
                    progression = (badge.unlocked_percent ?: 0) / 100f,
                    date = badge.unlocked_date,
                    icone = if (badge.unlocked_date != null) badge.images_url.unlocked else badge.images_url.locked,
                    compact = compact,
                    onClick = { onBadgeClick(badge.id) } // -> ouvre l’Activity avec l’ID
                )
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

fun formatTimestamp(timestampSeconds: Long): String {
    val date = Date(timestampSeconds * 1000)
    val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return format.format(date)
}

