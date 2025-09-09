package fr.thibaut.kinobadge.ui.composants

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Reorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarModeAffichage(
    isCompact: Boolean,
    onLayoutModeChange: (Boolean) -> Unit,
    title: String = "KinoBadge",
    subtitle: String? = "Badges et progression",
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
    onSortClick: (() -> Unit)? = null
) {
    LargeTopAppBar(
        modifier = modifier,
        title = {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(title, style = MaterialTheme.typography.headlineSmall)
                if (subtitle != null) {
                    Text(
                        subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        navigationIcon = {},
        actions = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                if (onSortClick != null) {
                    IconButton(
                        onClick = onSortClick,
                        modifier = Modifier.padding(start = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Reorder,
                            contentDescription = "Changer l’ordre d’affichage"
                        )
                    }
                    Spacer(Modifier.width(4.dp))
                } else {
                    Icon(
                        imageVector = Icons.Default.Reorder,
                        contentDescription = null,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isCompact) "Compact" else "Étendu",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Spacer(Modifier.width(8.dp))
                    Switch(
                        checked = !isCompact,
                        onCheckedChange = { checked -> onLayoutModeChange(!checked) },
                        modifier = Modifier.semantics {
                            contentDescription = "Basculer mode d’affichage"
                        }
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior
    )
}
