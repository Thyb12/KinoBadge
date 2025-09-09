package fr.thibaut.kinobadge.domain.usecase

import fr.thibaut.kinobadge.Result
import fr.thibaut.kinobadge.domain.repository.KinomapRepository
import fr.thibaut.kinobadge.model.KinomapBadge

class RecupererBadgesUseCase(
    private val repository: KinomapRepository
) {
    operator fun invoke(onResult: (Result<List<KinomapBadge>>) -> Unit) {
        repository.getBadges(onResult)
    }
}
