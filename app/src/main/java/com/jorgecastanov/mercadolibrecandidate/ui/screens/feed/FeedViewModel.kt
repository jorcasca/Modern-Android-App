package com.jorgecastanov.mercadolibrecandidate.ui.screens.feed

import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModel
import com.jorgecastanov.mercadolibrecandidate.data.api.utils.ResponseException.*
import com.jorgecastanov.mercadolibrecandidate.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    val productIntent = Channel<FeedIntent>()
    private val _state = MutableStateFlow<FeedState>(FeedState.Idle)

    val state: StateFlow<FeedState>
        get() = _state

    init {
        handleIntents()
    }

    private fun handleIntents() {
        viewModelScope.launch {
            productIntent.consumeAsFlow().collect { intent ->
                when (intent) {
                    is FeedIntent.FetchProducts -> fetchProduct(intent.keyWord)
                }
            }
        }
    }

    private fun fetchProduct(keyWord: String) {
        viewModelScope.launch {
            _state.value = FeedState.Loading
            _state.value = try {
                val result = productRepository.getProducts(keyWord)
                when {
                    result.isSuccess -> FeedState.Products(result.getOrElse { emptyList() })
                    result.isFailure -> FeedState.Error(result.exceptionOrNull()?.message)
                    else -> FeedState.Error(UnknownErrorException().message)
                }
            } catch (e: Exception) {
                FeedState.Error(e.message)
            }
        }
    }
}
