package fr.thibaut.kinobadge.domain.usecase

import fr.thibaut.kinobadge.Result
import fr.thibaut.kinobadge.model.Badge

class RecupererBadgeUseCase(
    private val getBadges: RecupererBadgesUseCase
) {
    operator fun invoke(
        badgeId: Int,
        onResult: (Result<Badge?>) -> Unit
    ) {
        getBadges { res ->
            val list = res.success
            if (list != null) {
                val found = list.asSequence()
                    .flatMap { it.data.asSequence() }
                    .flatMap { it.badges.asSequence() }
                    .firstOrNull { it.id == badgeId }
                onResult(Result(success = found))
            } else {
                onResult(Result(error = res.error ?: "Unknown error"))
            }
        }
    }
}
