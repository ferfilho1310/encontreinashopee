package br.com.encontreinashopee.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import br.com.encontreinashopee.intent.SearchProductDataIntent
import br.com.encontreinashopee.model.OfferCardModel
import br.com.encontreinashopee.state.SearchProductDataState
import br.com.encontreinashopee.state.SearchProductExclusiveDataState
import br.com.encontreinashopee.ui.theme.EncontreinashopeeTheme
import br.com.encontreinashopee.viewmodel.ProductViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity(), ListenerProduct {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EncontreinashopeeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting()
                }
            }
        }
    }

    override fun onClickButtonProduct() {
        TODO("Not yet implemented")
    }

    override fun onClickProduct() {
        TODO("Not yet implemented")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(viewModel: ProductViewModel = koinViewModel()) {

    LaunchedEffect(key1 = "") {
        viewModel.dataIntent.send(SearchProductDataIntent.SearchOffers)
        viewModel.dataIntent.send(SearchProductDataIntent.SearchOffersExclusive)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Red,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Encontrei na Shopee", color = Color.White)
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item(1) {
                OfferExclusive(paddingValues = innerPadding)
            }
            item(2) {
                OffersList()
            }
        }
    }
}

@Composable
fun OfferExclusive(paddingValues: PaddingValues, viewModel: ProductViewModel = koinViewModel()) {
    when (val list = viewModel.dataStateExclusiveProduct.collectAsState().value) {
        is SearchProductExclusiveDataState.Loading -> Unit
        is SearchProductExclusiveDataState.ResponseData -> {
            LazyRow(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxWidth()
            ) {
                items(
                    items = list.data,
                    itemContent = { item ->
                        OfferCard(offerCardModel = item)
                    }
                )
            }
        }

        is SearchProductExclusiveDataState.Error -> Unit
        is SearchProductExclusiveDataState.Inactive -> Unit
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfferCard(offerCardModel: OfferCardModel) {

    val listenerProduct: ListenerProduct? = null

    Card(
        onClick = { listenerProduct?.onClickProduct() }
    ) {
        Column {
            ImageProduct(urlImage = offerCardModel.offerImage)
            Text(text = offerCardModel.offerTitle)
            Button(
                onClick = { listenerProduct?.onClickButtonProduct() },
                modifier = Modifier.background(color = Color.Yellow)
            ) {
                Text(text = "Saiba Mais", style = TextStyle(color = Color.White))
            }
        }
    }
}

@Composable
fun ImageProduct(urlImage: String) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(urlImage)
            .size(Size.ORIGINAL)
            .build()
    )
    Image(painter = painter, contentDescription = "")
}

@Composable
fun OffersList(viewModel: ProductViewModel = koinViewModel()) {
    when (val list = viewModel.dataState.collectAsState().value) {
        is SearchProductDataState.Loading -> Unit
        is SearchProductDataState.ResponseData -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
            ) {
                items(
                    items = list.data,
                    itemContent = { item ->
                        OfferCard(offerCardModel = item)
                    }
                )
            }
        }

        is SearchProductDataState.Error -> Unit
        is SearchProductDataState.Inactive -> Unit
    }
}

interface ListenerProduct {

    fun onClickButtonProduct()

    fun onClickProduct()
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EncontreinashopeeTheme {
        Greeting()
    }
}