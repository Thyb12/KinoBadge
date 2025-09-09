package fr.thibaut.kinobadge.di

import fr.thibaut.kinobadge.data.repository.KinomapRepositoryImpl
import fr.thibaut.kinobadge.data.repository.KinomapService
import fr.thibaut.kinobadge.data.repository.KinomapServiceImpl
import fr.thibaut.kinobadge.detail.DetailBadgePresenter
import fr.thibaut.kinobadge.domain.repository.KinomapRepository
import fr.thibaut.kinobadge.domain.usecase.RecupererBadgeUseCase
import fr.thibaut.kinobadge.domain.usecase.RecupererBadgesUseCase
import fr.thibaut.kinobadge.ecranPrincipal.EcranPrincipalPresenter

object Injector {

    // Data
    private val service: KinomapService by lazy { KinomapServiceImpl() }
    private val repository: KinomapRepository by lazy { KinomapRepositoryImpl(service) }

    // Domain
    private val getBadgesUseCase: RecupererBadgesUseCase by lazy { RecupererBadgesUseCase(repository) }
    private val getBadgeUseCase: RecupererBadgeUseCase by lazy { RecupererBadgeUseCase(getBadgesUseCase) }

    //Presenteurs
    fun provideEcranPrincipalPresenter(): EcranPrincipalPresenter =
        EcranPrincipalPresenter(getBadgesUseCase)

    fun provideDetailBadgePresenter(): DetailBadgePresenter =
        DetailBadgePresenter(getBadgeUseCase)
}
