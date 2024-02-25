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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.encontreinashopee.R
import br.com.encontreinashopee.intent.SearchProductDataIntent
import br.com.encontreinashopee.model.OfferCardModel
import br.com.encontreinashopee.state.SearchProductExclusiveDataState
import br.com.encontreinashopee.ui.theme.EncontreinashopeeTheme
import br.com.encontreinashopee.view.autoimageslider.AutoSlide.AutoSlidingCarousel
import br.com.encontreinashopee.viewmodel.ProductViewModel
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.pager.ExperimentalPagerApi
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetAlert(
    offerCardModel: OfferCardModel,
    listenerBottomSheet: ListenerBottomSheet? = null
) {
    val modalBottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = {
            listenerBottomSheet?.onDismiss()
        },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {


        Column {
            Row {
                ImageProductBottomSheet(urlImage = offerCardModel.offerImage.orEmpty())
                Text(
                    buildAnnotatedString {
                        append("Agora você será direcionado para a página do(a) ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("${offerCardModel.offerTitle}")
                        }
                        append(" no app da Shopee. Boas compras!")
                    }
                )
            }

            Button(
                onClick = {
                    listenerBottomSheet?.onClickButton()
                },
                colors = ButtonDefaults.buttonColors(Color(0xFFfa7000)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 50.dp, start = 12.dp, end = 12.dp)
            ) {
                Text(text = "Navegar para Oferta", color = Color.White)
            }
        }
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
            ListProduct(
                false,
                listProduct = arrayListOf(),
            )
        }

        is SearchProductExclusiveDataState.ResponseData -> {
            ListProduct(
                true,
                listProduct = list.data,
            )
        }

        is SearchProductExclusiveDataState.Error -> {
            ListProduct(
                false,
                listProduct = arrayListOf(),
            )
        }

        is SearchProductExclusiveDataState.Inactive -> Unit
    }
}

@Composable
fun ListProduct(
    isVisible: Boolean,
    listProduct: List<OfferCardModel>,
) {
    if (isVisible) {
        BannerOffer()

        Text(
            text = "Produtos para você",
            modifier = Modifier.padding(start = 12.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(150.dp),
            verticalItemSpacing = 4.dp,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            content = {
                items(listProduct.size) { item ->
                    OfferCard(offerCardModel = listProduct[item])
                }
            }
        )

    } else {
        ProgressBar()
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun BannerOffer() {
    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .padding(4.dp)
    ) {

        val listImage = arrayListOf(
            painterResource(id = R.drawable.banner),
            painterResource(id = R.drawable.comunidade)
        )

        AutoSlidingCarousel(itemsCount = listImage.size) {
            Image(
                painter = listImage[it],
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(White)
                    .height(150.dp),
                contentScale = ContentScale.Crop,
            )
        }
    }
}

@Composable
fun OfferCard(offerCardModel: OfferCardModel) {

    val context = LocalContext.current
    val intent = remember {
        Intent(Intent.ACTION_VIEW, Uri.parse(offerCardModel.urlOffer.orEmpty()))
    }
    var showSheet by remember { mutableStateOf(false) }

    if (showSheet) {
        BottomSheetAlert(
            offerCardModel,
            object : ListenerBottomSheet {
                override fun onClickButton() {
                    context.startActivity(intent)
                }

                override fun onDismiss() {
                    showSheet = false
                }
            }
        )
    }

    Card(
        modifier = Modifier.padding(4.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 20.dp
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
        ) {

            ImageProduct(urlImage = offerCardModel.offerImage.orEmpty())

            if (!offerCardModel.tag.isNullOrEmpty()) {
                Text(
                    text = offerCardModel.tag,
                    color = Color.Red,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                    fontWeight = FontWeight.Bold,
                )
            }

            Text(
                text = offerCardModel.offerTitle.orEmpty(),
                modifier = Modifier.padding(start = 8.dp, top = 4.dp),
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
                onClick = {
                    showSheet = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 12.dp, start = 12.dp, bottom = 12.dp, top = 12.dp),
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
fun ImageProductBottomSheet(urlImage: String) {
    SubcomposeAsyncImage(
        model = urlImage,
        modifier = Modifier
            .height(100.dp)
            .width(100.dp)
            .padding(8.dp),
        contentScale = ContentScale.Crop,
        contentDescription = "",
        loading = {
            CircularProgressIndicator()
        }
    )
}

@Composable
fun ProgressBar() {

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.corujinha))

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        LottieAnimation(composition, modifier = Modifier.size(150.dp))

        Text(
            modifier = Modifier.padding(top = 12.dp),
            text = "Buscando Ofertas...", color = Color.Black,
            fontWeight = FontWeight.Bold
        )
    }
}

interface ListenerBottomSheet {
    fun onClickButton()
    fun onDismiss()
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EncontreinashopeeTheme {
        val model =
            OfferCardModel(offerTitle = "teste", offerPrice = "R$ 120,50")

        OfferCard(model)
    }
}
