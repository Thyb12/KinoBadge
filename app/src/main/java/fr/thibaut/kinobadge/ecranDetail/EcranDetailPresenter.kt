package fr.thibaut.kinobadge.detail

import fr.thibaut.kinobadge.Result
import fr.thibaut.kinobadge.domain.usecase.RecupererBadgeUseCase
import fr.thibaut.kinobadge.model.Badge

class DetailBadgePresenter(
    private val recupererBadgeUseCase: RecupererBadgeUseCase
) {
    fun recupererLeBadge(
        badgeId: Int,
        onResult: (Result<Badge?>) -> Unit
    ) {
        recupererBadgeUseCase(badgeId, onResult)
    }
}
