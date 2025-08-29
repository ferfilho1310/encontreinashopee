package br.com.encontreinashopee.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.encontreinashopee.intent.SearchCupomsIntent
import br.com.encontreinashopee.model.CupomModel
import br.com.encontreinashopee.repository.cupoms.CupomRepository
import br.com.encontreinashopee.state.DataState
import br.com.encontreinashopee.track.OffersTrackContract
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class CupomViewModel(
    val repository: CupomRepository,
    private val tracker: OffersTrackContract
) : ViewModel() {

    val dataIntent = Channel<SearchCupomsIntent>(Channel.UNLIMITED)
    private val _dataState = MutableStateFlow<DataState>(DataState.Loading)
    val dataState: StateFlow<DataState> = _dataState

    init {
        handleIntent()
    }

    fun onClickCupomTracker(cupomModel: CupomModel) {
        tracker.trackClickCupons(cupomModel)
    }

    private fun handleIntent() {
        viewModelScope.launch {
            dataIntent.consumeAsFlow().collect {
                when (it) {
                    is SearchCupomsIntent.SearchCupoms -> loadCupoms()
                }
            }
        }
    }

    private fun loadCupoms() {
        viewModelScope.launch {
            _dataState.value = DataState.Loading
            repository.getCupoms()
                .catch {
                    _dataState.value = DataState.Error(it.message)
                }.collect {
                    _dataState.value = DataState.ResponseData(it.sortedByDescending { it.id })
                }
        }
    }
}