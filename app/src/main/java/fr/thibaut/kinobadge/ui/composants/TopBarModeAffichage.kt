package fr.thibaut.kinobadge.ui.composants

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Reorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarModeAffichage(
    isCompact
: Boolean,
    onIsEtendueChange: (Boolean) -> Unit,
    title: String = "KinoBadge",
    subtitle: String? = "Badges et progression",
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier
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
                Icon(
                    Icons.Default.Reorder,
                    contentDescription = null,
                    modifier = Modifier.padding(start = 12.dp)
                )
                Spacer(Modifier.width(8.dp))
                Switch(
                    checked = !isCompact
,
                    onCheckedChange = { checked ->
                        onIsEtendueChange(!checked)
                    }
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}
