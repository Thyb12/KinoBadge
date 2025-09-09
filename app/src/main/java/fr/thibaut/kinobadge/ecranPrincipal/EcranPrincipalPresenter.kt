package fr.thibaut.kinobadge.ecranPrincipal

import fr.thibaut.kinobadge.Result
import fr.thibaut.kinobadge.domain.usecase.RecupererBadgesUseCase
import fr.thibaut.kinobadge.model.Category
import fr.thibaut.kinobadge.model.KinomapBadge

class EcranPrincipalPresenter(
    private val getBadges: RecupererBadgesUseCase
) {
    fun recupererListeBadge(onResult: (Result<List<Category>>) -> Unit) {
        getBadges { res: Result<List<KinomapBadge>> ->
            val list = res.success
            if (list != null) {
                val categories = list.asSequence()
                    .flatMap { it.data.asSequence() }
                    .toList()
                onResult(Result(success = categories))
            } else {
                onResult(Result(error = res.error ?: "Unknown error"))
            }
        }
    }
}
