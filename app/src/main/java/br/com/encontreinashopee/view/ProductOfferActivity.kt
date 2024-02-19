package br.com.encontreinashopee.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.encontreinashopee.R
import br.com.encontreinashopee.intent.SearchProductDataIntent
import br.com.encontreinashopee.model.OfferCardModel
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
                SetComposableStatusBar(Color(0xFFfa7000))
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFD3D3D3)
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

@Composable
fun OfferList(viewModel: ProductViewModel = koinViewModel()) {
    LaunchedEffect(key1 = Unit) {
        viewModel.dataIntent.send(SearchProductDataIntent.SearchOffersExclusive)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFDCDCDC))
    ) {
        OfferExclusive()
    }
}

@Composable
fun OfferExclusive(
    viewModel: ProductViewModel = koinViewModel()
) {
    when (val list = viewModel.dataStateExclusiveProduct.collectAsState().value) {
        is SearchProductExclusiveDataState.Loading -> {
            ProgressBar(isVisible = true)
            ListProduct(
                listProduct = arrayListOf(),
            )
        }

        is SearchProductExclusiveDataState.ResponseData -> {
            ProgressBar(isVisible = false)
            ListProduct(
                listProduct = list.data,
            )
        }

        is SearchProductExclusiveDataState.Error -> {
            ProgressBar(isVisible = false)
            ListProduct(
                listProduct = arrayListOf(),
            )
        }

        is SearchProductExclusiveDataState.Inactive -> Unit
    }
}

@Composable
fun ListProduct(
    listProduct: List<OfferCardModel>,
) {
    BannerOffer()

    Text(
        text = "Produtos para você",
        modifier = Modifier.padding(start = 12.dp),
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
    )

    LazyVerticalGrid(
        GridCells.Fixed(2)
    ) {
        items(listProduct.size) { item ->
            OfferCard(offerCardModel = listProduct[item])
        }
    }
}

@Composable
fun BannerOffer() {
    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .padding(4.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.banner),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .height(150.dp),
            contentScale = ContentScale.Crop,
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfferCard(offerCardModel: OfferCardModel) {

    val context = LocalContext.current
    val intent = remember {
        Intent(Intent.ACTION_VIEW, Uri.parse(offerCardModel.urlOffer.orEmpty()))
    }

    Card(
        onClick = {
            context.startActivity(intent)
        },
        modifier = Modifier.padding(4.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 20.dp
        ),
    ) {
        Column(
            modifier = Modifier
                .width(186.dp)
                .background(Color.White)
        ) {
            ImageProduct(urlImage = offerCardModel.offerImage.orEmpty())

            Text(
                text = offerCardModel.offerTitle.orEmpty(),
                modifier = Modifier.padding(start = 8.dp, top = 8.dp),
                fontWeight = FontWeight.Bold
            )

            Row(modifier = Modifier.padding(start = 8.dp, top = 2.dp)) {
                Text(
                    text = "A partir de: ",
                )
                Text(
                    text = offerCardModel.offerPrice.orEmpty(),
                )
            }

            Button(
                onClick = { context.startActivity(intent) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 12.dp, start = 12.dp, bottom = 12.dp, top = 22.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFFfa7000)),
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
            .crossfade(true)
            .data(urlImage)
            .build()
    )
    Image(
        painter = painter,
        contentDescription = "",
        modifier = Modifier
            .height(165.dp)
            .fillMaxWidth(),
        contentScale = ContentScale.Crop,
    )
}

@Composable
fun ProgressBar(isVisible: Boolean) {
    if (isVisible) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator(
                progress = 0.5f,
                modifier = Modifier
                    .height(40.dp)
                    .clip(RoundedCornerShape(10.dp)),
                color = Color(0xFFfa7000)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EncontreinashopeeTheme {
        val model = arrayListOf(
            OfferCardModel(offerTitle = "teste", offerPrice = "R$ 120,50"),
            OfferCardModel(offerTitle = "teste"),
            OfferCardModel(offerTitle = "teste"),
            OfferCardModel(offerTitle = "teste"),
            OfferCardModel(offerTitle = "teste"),
            OfferCardModel(offerTitle = "teste"),
            OfferCardModel(offerTitle = "teste")
        )

        ListProduct(model)
    }
}