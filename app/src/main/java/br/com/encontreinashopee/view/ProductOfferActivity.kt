package br.com.encontreinashopee.view

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.encontreinashopee.intent.SearchProductDataIntent
import br.com.encontreinashopee.model.OfferCardModel
import br.com.encontreinashopee.state.SearchProductDataState
import br.com.encontreinashopee.state.SearchProductExclusiveDataState
import br.com.encontreinashopee.ui.theme.EncontreinashopeeTheme
import br.com.encontreinashopee.viewmodel.ProductViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EncontreinashopeeTheme {
                SetComposableStatusBar(color = Color.Red)
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    OfferList()
                }
            }
        }
    }
}

@Composable
fun SetComposableStatusBar(color: Color) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(color)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfferList(viewModel: ProductViewModel = koinViewModel()) {

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray)
        ) {
            OfferExclusive(paddingValues = innerPadding)
            OffersList()
        }
    }
}

@Composable
fun OfferExclusive(
    paddingValues: PaddingValues,
    viewModel: ProductViewModel = koinViewModel()
) {
    when (val list = viewModel.dataStateExclusiveProduct.collectAsState().value) {
        is SearchProductExclusiveDataState.Loading -> {
            ProgressBar(isVisible = true)
            ListProductExclusive(
                isVisible = false,
                listProduct = arrayListOf(),
                paddingValues = paddingValues
            )
        }

        is SearchProductExclusiveDataState.ResponseData -> {
            ProgressBar(isVisible = false)
            ListProductExclusive(
                isVisible = true,
                listProduct = list.data,
                paddingValues = paddingValues
            )
        }

        is SearchProductExclusiveDataState.Error -> {
            ProgressBar(isVisible = false)
            ListProductExclusive(
                isVisible = false,
                listProduct = arrayListOf(),
                paddingValues = paddingValues
            )
        }

        is SearchProductExclusiveDataState.Inactive -> Unit
    }
}

@Composable
fun ListProductExclusive(
    isVisible: Boolean,
    listProduct: ArrayList<OfferCardModel>,
    paddingValues: PaddingValues
) {
    if (isVisible)
        LazyRow(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            items(
                items = listProduct,
                itemContent = { item ->
                    OfferCard(offerCardModel = item)
                }
            )
        }

}

@Composable
fun ListProduct(
    isVisible: Boolean,
    listProduct: ArrayList<OfferCardModel>
) {
    if (isVisible)
        Text(
            text = "Produtos escolhidos para você",
            modifier = Modifier.padding(start = 12.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        items(listProduct.size) { index ->
            OfferCard(offerCardModel = listProduct[index])
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfferCard(offerCardModel: OfferCardModel) {

    val context = LocalContext.current
    val intent =
        remember { Intent(Intent.ACTION_VIEW, Uri.parse(offerCardModel.urlOffer.orEmpty())) }

    Card(
        onClick = {
            context.startActivity(intent)
        },
        modifier = Modifier.padding(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 20.dp
        )
    ) {
        Column(
            modifier = Modifier
                .width(200.dp)
                .background(Color.White)
        ) {
            ImageProduct(urlImage = offerCardModel.offerImage.orEmpty())

            Text(
                text = offerCardModel.offerTitle.orEmpty(),
                modifier = Modifier.padding(12.dp)
            )

            Button(
                onClick = { context.startActivity(intent) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 12.dp, start = 12.dp, bottom = 12.dp),
                colors = ButtonDefaults.buttonColors(Color.Red),
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
            .build()
    )
    Image(
        painter = painter,
        contentDescription = "",
        modifier = Modifier
            .height(165.dp)
            .padding(top = 12.dp)
    )
}

@Composable
fun ProgressBar(isVisible: Boolean) {
    if (isVisible) {
        CircularProgressIndicator(
            progress = 18f,
            modifier = Modifier
                .height(40.dp)
                .clip(RoundedCornerShape(10.dp)),
            color = Color.Red
        )
    }
}

@Composable
fun OffersList(viewModel: ProductViewModel = koinViewModel()) {
    when (val list = viewModel.dataState.collectAsState().value) {
        is SearchProductDataState.Loading -> {
            ProgressBar(isVisible = true)
            ListProduct(isVisible = false, listProduct = arrayListOf())
        }

        is SearchProductDataState.ResponseData -> {
            ProgressBar(isVisible = false)
            ListProduct(isVisible = true, listProduct = list.data)
        }

        is SearchProductDataState.Error -> {
            ProgressBar(isVisible = false)
        }

        is SearchProductDataState.Inactive -> Unit
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EncontreinashopeeTheme {
        ProgressBar(isVisible = true)
    }
}
