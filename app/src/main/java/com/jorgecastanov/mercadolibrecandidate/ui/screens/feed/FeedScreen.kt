package com.jorgecastanov.mercadolibrecandidate.ui.screens.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jorgecastanov.mercadolibrecandidate.ui.components.ProductCard
import com.jorgecastanov.mercadolibrecandidate.ui.theme.MercadoLibreCandidateTheme
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.jorgecastanov.mercadolibrecandidate.R
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.jorgecastanov.mercadolibrecandidate.data.model.Product
import com.jorgecastanov.mercadolibrecandidate.ui.components.FeedAppBar
import com.jorgecastanov.mercadolibrecandidate.ui.screens.feed.FeedState.Idle
import com.jorgecastanov.mercadolibrecandidate.ui.screens.feed.FeedState.Products
import com.jorgecastanov.mercadolibrecandidate.ui.screens.feed.FeedState.Error
import com.jorgecastanov.mercadolibrecandidate.ui.screens.feed.FeedState.Loading
import com.jorgecastanov.mercadolibrecandidate.ui.navigation.goProductDetail
import com.jorgecastanov.mercadolibrecandidate.ui.screens.feed.FeedIntent.FetchProducts

@Composable
fun FeedScreen(
    navController: NavController,
    viewModel: FeedViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            FeedAppBar(
                title = stringResource(R.string.app_name),
                onSearchClicked = { search ->
                    viewModel.productIntent.trySend(FetchProducts(search))
                }
            )
        },
        content = {
            MercadoLibreCandidateTheme {
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    RenderStates(
                        state = state,
                        navController = navController
                    )
                }
            }
        }
    )
}

@Composable
fun RenderStates(state: FeedState, navController: NavController) {
    when (state) {
        is Idle -> {
            OnEmptyProducts()
        }
        is Loading -> {
            OnLoadingProducts()
        }
        is Products -> {
            val products = (state).products
            OnLoadedProducts(products, navController)
        }
        is Error -> {
            val error = (state).error.toString()
            OnError(error)
        }
    }
}

@Composable
fun OnEmptyProducts() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.empty),
            style = MaterialTheme.typography.h2
        )
    }
}

@Composable
fun OnError(error: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = error,
            style = MaterialTheme.typography.h2
        )
    }
}

@Composable
fun OnLoadedProducts(products: List<Product>, navController: NavController) {
    if (products.isNotEmpty()) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            items(products) { product ->
                ProductCard(
                    title = product.title,
                    price = product.price,
                    condition = product.condition,
                    available_quantity = product.available_quantity,
                    thumbnail = product.thumbnail,
                    onTab = {
                        goProductDetail(navController, product)
                    }
                )
            }
        }
    } else {
        OnEmptyProducts()
    }
}

@Composable
fun OnLoadingProducts() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}
