package fr.thibaut.kinobadge.domain.repository

import fr.thibaut.kinobadge.Result
import fr.thibaut.kinobadge.model.KinomapBadge

interface KinomapRepository {
    fun getBadges(onResult: (Result<List<KinomapBadge>>) -> Unit)
}