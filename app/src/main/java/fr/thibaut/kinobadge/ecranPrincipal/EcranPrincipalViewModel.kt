package fr.thibaut.kinobadge.ecranPrincipal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.thibaut.kinobadge.model.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EcranPrincipalViewModel() : ViewModel() {
    private val presenter = fr.thibaut.kinobadge.di.Injector.provideEcranPrincipalPresenter()
    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val uiState: StateFlow<MainUiState> = _uiState

    private val _isCompact = MutableStateFlow(true)
    val isCompact: StateFlow<Boolean> = _isCompact

    init {
        recupererListeBadge()
    }

    fun handleEvent(uiEvent: MainEvent) {
        when (uiEvent) {
            MainEvent.Refresh -> recupererListeBadge()
            is MainEvent.SetCompact -> _isCompact.value = uiEvent.value
        }
    }

    private fun recupererListeBadge() {
        _uiState.value = MainUiState.Loading
        presenter.recupererListeBadge { result ->
            viewModelScope.launch {
                val list = result.success
                if (list != null) {
                    _uiState.value = if (list.isEmpty()) MainUiState.Empty else MainUiState.Success(list)
                } else {
                    _uiState.value = MainUiState.Error(result.error ?: "Unknown error")
                }
            }
        }
    }


    sealed interface MainUiState {
        data object Loading : MainUiState
        data object Empty : MainUiState
        data class Success(val categories: List<Category>) : MainUiState
        data class Error(val message: String) : MainUiState
    }

    sealed class MainEvent {
        data object Refresh : MainEvent()
        data class SetCompact(val value: Boolean) : MainEvent()
    }
}
