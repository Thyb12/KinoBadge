package fr.thibaut.kinobadge.data.repository

import fr.thibaut.kinobadge.Result
import fr.thibaut.kinobadge.domain.repository.KinomapRepository
import fr.thibaut.kinobadge.model.KinomapBadge

class KinomapRepositoryImpl(
    private val service: KinomapService
) : KinomapRepository {

    override fun getBadges(onResult: (Result<List<KinomapBadge>>) -> Unit) {
        service.recupererBadges { res ->
            val data = res.success
            if (data != null) {
                onResult(Result(success = data))
            } else {
                onResult(Result(error = res.error ?: "Unknown error"))
            }
        }
    }
}
