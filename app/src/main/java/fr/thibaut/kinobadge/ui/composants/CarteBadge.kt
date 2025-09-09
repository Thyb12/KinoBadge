package fr.thibaut.kinobadge.ui.composants

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun BadgeCard(
    titre: String,
    progression: Float? = null,
    date: Long? = null,
    icone: String? = null,
    compact: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Card(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.semantics {
            role = Role.Button
            contentDescription = buildString {
                append(titre)
                if (date != null) {
                    append(", débloqué le ${formatTimestamp(date)}")
                } else if (progression != null) {
                    val pct = (progression.coerceIn(0f, 1f) * 100).roundToInt()
                    append(", progression $pct%")
                }
            }
        },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        if (compact) {
            CarteCompact(
                titre = titre,
                icone = icone,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(16.dp)
            )
        } else {
            CarteEtendue(
                titre = titre,
                progression = progression,
                date = date,
                icone = icone,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            )
        }
    }
}

@Composable
private fun CarteCompact(
    titre: String,
    icone: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (icone != null) {
            AsyncImage(
                model = icone,
                contentDescription = null,
                modifier = Modifier.size(56.dp)
            )
            Spacer(Modifier.height(8.dp))
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
    progression: Float?,
    date: Long?,
    icone: String?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icone != null) {
            AsyncImage(
                model = icone,
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
        }
        Spacer(Modifier.width(12.dp))
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

            when {
                date != null -> {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Débloqué le ${formatTimestamp(date)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                progression != null -> {
                    Spacer(Modifier.height(6.dp))
                    val clamped = progression.coerceIn(0f, 1f)
                    LinearProgressIndicator(
                        progress = { clamped },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
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

fun formatTimestamp(timestampSeconds: Long): String {
    val date = Date(timestampSeconds * 1000)
    val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return format.format(date)
}
