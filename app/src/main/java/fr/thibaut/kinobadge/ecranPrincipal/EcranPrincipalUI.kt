package fr.thibaut.kinobadge.ecranPrincipal

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.thibaut.kinobadge.DetailBadgeActivity
import fr.thibaut.kinobadge.model.Category
import fr.thibaut.kinobadge.ui.composants.BadgeCard
import fr.thibaut.kinobadge.ui.composants.TopBarModeAffichage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EcranPrincipalUI(
    vm: EcranPrincipalViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val isCompact by vm.isCompact.collectAsStateWithLifecycle()

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopBarModeAffichage(
                isCompact = isCompact,
                onLayoutModeChange = { vm.handleEvent(EcranPrincipalViewModel.MainEvent.SetCompact(it)) },
                scrollBehavior = scrollBehavior
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = uiState) {
                is EcranPrincipalViewModel.MainUiState.Loading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }

                is EcranPrincipalViewModel.MainUiState.Empty -> {
                    Text("Aucun badge pour le moment.", Modifier.align(Alignment.Center))
                }

                is EcranPrincipalViewModel.MainUiState.Error -> {
                    Column(
                        Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Oups : ${state.message}")
                        Spacer(Modifier.height(12.dp))
                        Button(onClick = { vm.handleEvent(EcranPrincipalViewModel.MainEvent.Refresh) }) {
                            Text("Réessayer")
                        }
                    }
                }

                is EcranPrincipalViewModel.MainUiState.Success -> {
                    val ctx = LocalContext.current
                    Contenu(
                        categories = state.categories,
                        compact = isCompact,
                        modifier = Modifier.fillMaxSize(),
                        onBadgeClick = { id ->
                            ctx.startActivity(
                                Intent(ctx, DetailBadgeActivity::class.java).putExtra("badgeId", id)
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun Contenu(
    categories: List<Category>,
    modifier: Modifier = Modifier,
    compact: Boolean = false,
    onBadgeClick: (Int) -> Unit
) {
    val columns = if (compact) GridCells.Fixed(2) else GridCells.Fixed(1)

    LazyVerticalGrid(
        columns = columns,
        modifier = modifier
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 12.dp)
    ) {
        categories.forEach { category ->
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

            items(
                items = category.badges,
                key = { it.id }
            ) { badge ->
                val progress = ((badge.unlocked_percent ?: 0) / 100f).coerceIn(0f, 1f)
                val iconUrl = if (badge.unlocked_date != null) badge.images_url.unlocked else badge.images_url.locked

                BadgeCard(
                    titre = badge.name,
                    progression = progress,
                    date = badge.unlocked_date,
                    icone = iconUrl,
                    compact = compact,
                    onClick = { onBadgeClick(badge.id) }
                )
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}
