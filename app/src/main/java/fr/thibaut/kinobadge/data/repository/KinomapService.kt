package fr.thibaut.kinobadge.data.repository

import fr.thibaut.kinobadge.Result
import fr.thibaut.kinobadge.model.KinomapBadge


interface KinomapService {
    fun recupererBadges(
        onResult: (Result<List<KinomapBadge>>) -> Unit
    )
}