package fr.thibaut.kinobadge.ui.composants

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Carpenter
import androidx.compose.material.icons.filled.Cases
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.NaturePeople
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

@Composable
fun BadgeCard(
    titre: String,
    progression: Float? = null,
    date: Long? = null,
    icone: String? = null,
    compact: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        if (compact) {
            CarteCompact(
                titre = titre,
                icone = icone,
                modifier = Modifier
            )
        } else {
            CarteEtendue(
                titre = titre,
                progression = progression,
                date = date,
                icone = icone,
                modifier = Modifier
            )
        }
    }
}

@Composable
private fun CarteCompact(
    titre: String,
    icone: String? = null,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .aspectRatio(1f)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (icone != null) {
            AsyncImage(
                model = icone,
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        Text(
            text = titre,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun CarteEtendue(
    titre: String,
    progression: Float? = null,
    date: Long? = null,
    icone: String? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icone != null) {
            AsyncImage(
                model = icone,
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        ) {
            Text(
                text = titre,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            if (date != null) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Débloqué en ${formatTimestamp(date)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else if (progression != null) {
                Spacer(Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { progression.coerceIn(0f, 1f) },
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }
        }

        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = "Voir le détail",
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}



@Preview
@Composable
fun CarteCompactApercu() {
    val testListe = listOf(
        Pair("Badge 1", Icons.Default.Hotel),
        Pair("Badge numero 2", Icons.Default.NaturePeople),
        Pair("Badge numero 3", Icons.Default.Carpenter),
        Pair("Badge numero 4", Icons.Default.Cases),
    )
    Surface {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(testListe) { it ->
                BadgeCard(
                    titre = it.first,
                    progression = Random.nextFloat(),
                    icone = "https://static.kinomap.com/badges/activity_finisher_mptc-win.png",
                    compact = true,
                    onClick = {}
                )
            }
        }
    }
}

@Preview
@Composable
fun CarteEtendueApercu() {
    val testListe = listOf(
        Pair("Badge numero 1", Icons.Default.Hotel),
        Pair("Badge numero 2", Icons.Default.NaturePeople),
        Pair("Badge numero 3", Icons.Default.Carpenter),
        Pair("Badge numero 4", Icons.Default.Cases),
    )
    Surface {
        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(testListe) { it ->
                BadgeCard(
                    titre = it.first,
                    progression = 0.7f,
                    icone = "https://static.kinomap.com/badges/activity_finisher_mptc-win.png",
                    compact = false,
                    onClick = {}
                )
            }
        }
    }
}

fun formatTimestamp(timestampSeconds: Long): String {
    val date = Date(timestampSeconds * 1000)
    val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return format.format(date)
}