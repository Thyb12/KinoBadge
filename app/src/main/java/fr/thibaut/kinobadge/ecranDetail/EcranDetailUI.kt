package fr.thibaut.kinobadge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import fr.thibaut.kinobadge.ecranDetail.DetailBadgeViewModel
import fr.thibaut.kinobadge.model.Badge
import fr.thibaut.kinobadge.ui.theme.KinoBadgeTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailBadgeActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val badgeId = intent.getIntExtra("badgeId", -1)

        setContent {
            val vm: DetailBadgeViewModel = viewModel()
            val uiState = vm.uiState.collectAsStateWithLifecycle().value

            LaunchedEffect(badgeId) {
                vm.load(badgeId)
            }

            KinoBadgeTheme {
                Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Column {
                        TopAppBar(
                            title = { Text("Détail du badge") },
                            colors = TopAppBarDefaults.topAppBarColors(),
                            modifier = Modifier.fillMaxWidth(),
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Retour")
                                }
                            }
                        )

                        when (val s = uiState) {
                            is DetailBadgeViewModel.DetailUiState.Loading -> {
                                Chargement()
                            }

                            is DetailBadgeViewModel.DetailUiState.Error -> {
                              ResilienceContent(s = s, vm = vm)
                            }

                            is DetailBadgeViewModel.DetailUiState.Success -> {
                                DetailBadgeContent(badge = s.badge)
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
private fun Chargement() {
    Box(
        Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        CircularProgressIndicator(Modifier.align(Alignment.TopCenter))
        Text(
            "Chargement...",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 56.dp)
        )
    }
}

@Composable
private fun ResilienceContent(
    s: DetailBadgeViewModel.DetailUiState.Error = DetailBadgeViewModel.DetailUiState.Error("Résilience"),
    vm: DetailBadgeViewModel = viewModel()
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Oups : ${s.message}", style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(12.dp))
        Button(onClick = { vm.refresh() }) { Text("Réessayer") }
    }
}
@Composable
private fun DetailBadgeContent(badge: Badge) {
    val imageUrl = if (badge.unlocked_date != null) badge.images_url.unlocked else badge.images_url.locked

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier.size(64.dp)
        )

        Text(text = badge.name, style = MaterialTheme.typography.headlineMedium)

        Text(text = badge.description, style = MaterialTheme.typography.bodyMedium)

        if (badge.unlocked_date != null) {
            Text(
                text = "Débloqué le : ${formatTimestamp(badge.unlocked_date)}",
                style = MaterialTheme.typography.bodySmall
            )
        } else {
            Text(text = "Badge non débloqué", style = MaterialTheme.typography.bodySmall)
        }

        Text(
            text = "Progression : ${(badge.unlocked_percent ?: 0)}%",
            style = MaterialTheme.typography.bodySmall
        )

        LinearProgressIndicator(
            progress = { ((badge.unlocked_percent ?: 0) / 100f).coerceIn(0f, 1f) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

private fun formatTimestamp(timestampSeconds: Long): String {
    val date = Date(timestampSeconds * 1000)
    val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return format.format(date)
}