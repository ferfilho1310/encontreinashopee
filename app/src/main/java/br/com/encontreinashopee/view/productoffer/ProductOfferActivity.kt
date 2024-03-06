package br.com.encontreinashopee.view.productoffer

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.encontreinashopee.R
import br.com.encontreinashopee.intent.SearchProductDataIntent
import br.com.encontreinashopee.model.BannerList
import br.com.encontreinashopee.model.OfferCardModel
import br.com.encontreinashopee.state.SearchProductExclusiveDataState
import br.com.encontreinashopee.ui.theme.EncontreinashopeeTheme
import br.com.encontreinashopee.util.autoimageslider.AutoSlide.AutoSlidingCarousel
import br.com.encontreinashopee.view.offerdetails.ProducOfferDetailActivity
import br.com.encontreinashopee.viewmodel.ProductViewModel
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.FirebaseApp
import org.koin.androidx.compose.koinViewModel

class ProductOfferActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
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
    offerCardModel: OfferCardModel?,
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
                ImageProductBottomSheet(urlImage = offerCardModel?.offerImage.orEmpty())
                Text(
                    buildAnnotatedString {
                        append("Agora você será direcionado para a página do(a) ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("${offerCardModel?.offerTitle}")
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
                Text(text = "Ir para Oferta", color = White)
            }
        }
    }
}

@Composable
fun OfferList(
    viewModel: ProductViewModel = koinViewModel(),
) {
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
    viewModel: ProductViewModel = koinViewModel(),
) {
    val context = LocalContext.current

    when (val list = viewModel.dataStateExclusiveProduct.collectAsState().value) {
        is SearchProductExclusiveDataState.Loading -> {
            ProgressBar(true)
            ListProduct(
                listProduct = arrayListOf()
            )
        }

        is SearchProductExclusiveDataState.ResponseData -> {
            ProgressBar(false)
            ListProduct(
                listProduct = list.data
            ) {
                val intent = Intent(context, ProducOfferDetailActivity::class.java)
                intent.putExtra("offerModel", it)
                context.startActivity(intent)
            }
        }

        is SearchProductExclusiveDataState.Error -> {
            ProgressBar(true)
            ListProduct(
                listProduct = arrayListOf()
            )
        }

        is SearchProductExclusiveDataState.Inactive -> Unit
    }
}

@Composable
fun ListProduct(
    listProduct: List<OfferCardModel>,
    viewModel: ProductViewModel = koinViewModel(),
    offerDetail: ListenerOfferDetail? = null
) {
    val context = LocalContext.current
    val textState = remember {
        mutableStateOf(TextFieldValue(""))
    }

    SearchView(state = textState, placeHolder = "Pequise por Ofertas...")

    val searchText = textState.value.text

    BannerOffer {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
        context.startActivity(browserIntent)
    }

    Text(
        text = "Ofertas para você",
        modifier = Modifier.padding(start = 4.dp, top = 4.dp),
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
    )

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2)
    ) {
        items(
            viewModel.filterOffer(searchText, listProduct)
        ) {
            OfferCard(offerCardModel = it, offerDetail = offerDetail)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchView(
    state: MutableState<TextFieldValue>,
    placeHolder: String
) {

    TextField(
        value = state.value,
        onValueChange = { value ->
            state.value = value
        },
        Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, top = 8.dp, bottom = 8.dp, end = 4.dp)
            .clip(RoundedCornerShape(30.dp))
            .border(1.dp, Color.DarkGray, RoundedCornerShape(30.dp)),
        placeholder = {
            Text(text = placeHolder)
        },
        trailingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "",
                tint = Color.Black
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            containerColor = White,
        ),
        maxLines = 1,
        singleLine = true,
        textStyle = TextStyle(
            color = Color.Black, fontSize = 20.sp
        ),
    )

}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun BannerOffer(listener: ListenerBanner) {
    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .padding(4.dp)
    ) {
        val listImage = arrayListOf(
            BannerList(img = painterResource(id = R.drawable.banner)),
            BannerList(img = painterResource(id = R.drawable.comunidade), url = URL.url),
        )

        AutoSlidingCarousel(itemsCount = listImage.size) {
            Image(
                painter = listImage[it].img,
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(White)
                    .height(150.dp)
                    .clickable {
                        if (!listImage[it].url.isNullOrEmpty()) {
                            listener.onClickLinkBanner(listImage[it].url.orEmpty())
                        }
                    },
                contentScale = ContentScale.Crop,
            )
        }
    }
}

@Composable
fun OfferCard(
    offerCardModel: OfferCardModel,
    offerDetail: ListenerOfferDetail? = null
) {
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
                    offerDetail?.onClickOffer(offerCardModel)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 12.dp, start = 12.dp, top = 12.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFFfa7000)),
                elevation = ButtonDefaults.buttonElevation(10.dp)
            ) {
                Text(text = "Saiba Mais", style = TextStyle(color = White))
            }

            OutlinedButton(
                onClick = {
                    shareSheetOffer(context, offerCardModel)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp, start = 8.dp, bottom = 12.dp),
                colors = ButtonDefaults.buttonColors(White),
                elevation = ButtonDefaults.buttonElevation(10.dp),
                border = BorderStroke(1.dp, Color(0xFFfa7000))
            ) {
                Text(
                    text = "Compartilhar Oferta",
                    style = TextStyle(fontSize = 14.sp, color = Color(0xFFfa7000))
                )
            }
        }
    }
}


fun shareSheetOffer(context: Context, offerCardModel: OfferCardModel) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TITLE, "Titulo")
        putExtra(
            Intent.EXTRA_TEXT,
            "Dê uma olhada nesta oferta\n" + offerCardModel.offerTitle + ".\n" + "Clique no link: " + offerCardModel.urlOffer
        )
        type = "text/plain"
    }

    val shareIntent =
        Intent.createChooser(sendIntent, "Você está compartilhando: " + offerCardModel.offerTitle)
    context.startActivity(shareIntent)
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
fun ProgressBar(isVisible: Boolean) {

    if (isVisible) {
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
}

interface ListenerBottomSheet {
    fun onClickButton()
    fun onDismiss()
}

fun interface ListenerBanner {
    fun onClickLinkBanner(urlImage: String)
}

fun interface ListenerOfferDetail {

    fun onClickOffer(offerCardModel: OfferCardModel)
}

object URL {
    const val url = "https://chat.whatsapp.com/KR4Pvdr4AQwCeP3Bsu1lLg"
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
