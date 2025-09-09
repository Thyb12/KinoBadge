package fr.thibaut.kinobadge.ecranDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.thibaut.kinobadge.model.Badge
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailBadgeViewModel : ViewModel() {

    private val presenter = fr.thibaut.kinobadge.di.Injector.provideDetailBadgePresenter()

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState

    private var lastBadgeId: Int? = null

    fun load(badgeId: Int) {
        if (badgeId == -1) {
            _uiState.value = DetailUiState.Error("Identifiant de badge invalide")
            return
        }
        lastBadgeId = badgeId
        setLoading()

        presenter.recupererLeBadge(badgeId) { res ->
            viewModelScope.launch {
                _uiState.value = when {
                    res.error != null   -> DetailUiState.Error(res.error)
                    res.success == null -> DetailUiState.Error("Badge introuvable")
                    else                -> DetailUiState.Success(res.success)
                }
            }
        }
    }

    fun refresh() { lastBadgeId?.let(::load) }

    private fun setLoading() { _uiState.value = DetailUiState.Loading }

    sealed interface DetailUiState {
        data object Loading : DetailUiState
        data class Success(val badge: Badge) : DetailUiState
        data class Error(val message: String) : DetailUiState
    }
}
