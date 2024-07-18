package br.com.encontreinashopee.view.productoffer

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import br.com.encontreinashopee.R
import br.com.encontreinashopee.intent.SearchProductDataIntent
import br.com.encontreinashopee.model.BannerList
import br.com.encontreinashopee.model.OfferCardModel
import br.com.encontreinashopee.model.OfferStoriesModel
import br.com.encontreinashopee.state.DataState
import br.com.encontreinashopee.ui.theme.EncontreinashopeeTheme
import br.com.encontreinashopee.util.autoimageslider.AutoSlide.AutoSlidingCarousel
import br.com.encontreinashopee.viewmodel.ProductViewModel
import coil.compose.SubcomposeAsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.RequestConfiguration
import com.google.common.math.IntMath.mod
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

const val ID_ADS = "ca-app-pub-2528240545678093/5993578881"

class ProductOfferActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        setContent {
            EncontreinashopeeTheme {
                SetComposableStatusBar(Color(0xFFfa7000))
                Surface(
                    modifier = Modifier.fillMaxSize(), color = Color(0xFFD3D3D3)
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
fun StoriesBottomSheet(
    model: OfferStoriesModel,
    listenerBottomSheet: ListenerBottomSheet? = null
) {
    val modalBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = {
            listenerBottomSheet?.onDismiss()
        },
        sheetState = modalBottomSheetState,
        dragHandle = {
            BottomSheetDefaults.DragHandle()
        },
    ) {

        Card(
            modifier = Modifier.padding(2.dp),
            border = BorderStroke(2.dp, Color(0xFFfa7000))
        ) {
            ImageProduct(urlImage = model.urlImage.orEmpty())
        }

        Column {
            Text(
                text = model.titleOffer.orEmpty(),
                style = TextStyle(color = Black),
                modifier = Modifier.padding(bottom = 4.dp, top = 8.dp, start = 4.dp),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
            )

            Row(modifier = Modifier.padding(start = 4.dp, bottom = 8.dp, top = 4.dp)) {
                Text(
                    text = "A partir de: ",
                    fontSize = 15.sp,
                    style = TextStyle(color = Black)
                )

                Text(
                    text = model.priceOffer.orEmpty(),
                    style = TextStyle(color = Black),
                    modifier = Modifier,
                    fontSize = 15.sp,
                )
            }
        }

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    listenerBottomSheet?.onClickButton()
                },
                colors = ButtonDefaults.buttonColors(Color(0xFFfa7000)),
                modifier = Modifier.padding(bottom = 64.dp),
            ) {
                Text(
                    text = "Ir Para Oferta",
                    fontSize = 16.sp,
                    style = TextStyle(color = White)
                )
            }
        }
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
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold
                            )
                        ) {
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
        viewModel.dataIntent.send(SearchProductDataIntent.SearchStories)
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
    when (val list = viewModel.dataStateExclusiveProduct.collectAsState().value) {
        is DataState.Loading -> {
            ProgressBar(true)
            ListProduct(
                listProduct = arrayListOf()
            )
        }

        is DataState.ResponseData<*> -> {
            ProgressBar(false)
            ListProduct(
                listProduct = list.data as List<OfferCardModel>
            )
        }

        is DataState.Error -> {
            ProgressBar(true)
            ListProduct(
                listProduct = arrayListOf()
            )
        }

        is DataState.Inactive -> Unit
    }
}

@Composable
fun ListProduct(
    listProduct: List<OfferCardModel>,
) {
    val context = LocalContext.current
    val textState = remember {
        mutableStateOf(TextFieldValue(""))
    }

    SearchView(state = textState, placeHolder = "Pequise por Ofertas...")

    val searchText = textState.value.text

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(Modifier.weight(1f)) {
            item {
                BannerOffer {
                    try {
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                        context.startActivity(browserIntent)
                    } catch (ex: Exception) {
                        Toast.makeText(
                            context,
                            "Você não possui o app da Shoppe instalado. Baixe o app e aproveite as ofertas",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

            item {
                Text(
                    text = "Stories de Ofertas",
                    modifier = Modifier.padding(start = 4.dp, top = 4.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                SearchStoriesOffers()
            }

            itemsIndexed(
                listProduct.filter {
                    it.offerTitle.orEmpty().contains(searchText, ignoreCase = true)
                }
            ) { index, model ->
                val mod = mod(index, 3)
                if(mod != 0) {
                    OfferCard(offerCardModel = model)
                } else if(index != 0) {
                    AdmobBanner(AdSize.BANNER)
                }
            }
        }
        AdmobBanner()
    }
}

@Composable
fun AdmobBanner(adSize: AdSize = AdSize.FULL_BANNER) {
    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = { context ->
            AdView(context).apply {
                setAdSize(adSize)
                adUnitId = ID_ADS
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}


@Composable
fun SearchStoriesOffers(viewModel: ProductViewModel = koinViewModel()) {
    when (val listStores = viewModel.dataStateStories.collectAsState().value) {
        is DataState.Loading -> {

        }

        is DataState.ResponseData<*> -> {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(listStores.data as List<OfferStoriesModel>) {
                    Stories(it)
                }
            }
        }

        is DataState.Error -> {

        }

        is DataState.Inactive -> Unit
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
            .padding(start = 4.dp, top = 2.dp, bottom = 4.dp, end = 4.dp)
            .height(50.dp),
        placeholder = {
            Text(text = placeHolder, fontSize = 12.sp)
        },
        trailingIcon = {
            Icon(
                Icons.Default.Search, contentDescription = "", tint = Color.Black
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            containerColor = White,
        ),
        maxLines = 1,
        singleLine = true,
        textStyle = TextStyle(
            color = Black, fontSize = 20.sp
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
            BannerList(img = painterResource(id = R.drawable.banner_principal)),
            BannerList(img = painterResource(id = R.drawable.banner1), url = URL.url),
        )

        AutoSlidingCarousel(itemsCount = listImage.size) {
            Image(
                painter = listImage[it].img,
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(White)
                    .wrapContentHeight()
                    .height(118.dp)
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
fun Stories(
    model: OfferStoriesModel
) {
    val context = LocalContext.current
    val intent = remember {
        Intent(Intent.ACTION_VIEW, Uri.parse(model.urlOffer.orEmpty()))
    }
    var showSheet by remember { mutableStateOf(false) }

    if (showSheet) {
        StoriesBottomSheet(
            model,
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

    Column {
        SubcomposeAsyncImage(
            model = model.urlImage,
            modifier = Modifier
                .height(100.dp)
                .width(100.dp)
                .clip(CircleShape)
                .border(
                    BorderStroke(1.dp, Color(0xFFfa7000)),
                    CircleShape
                )
                .clickable {
                    showSheet = true
                },
            contentScale = ContentScale.Crop,
            contentDescription = "",
            loading = {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(36.dp))
                }
            }
        )
        Box(modifier = Modifier.width(110.dp)) {
            Text(
                text = model.titleOffer.orEmpty(),
                color = Color(0xFFfa7000),
                modifier = Modifier.padding(start = 2.dp, bottom = 8.dp),
                maxLines = 1,
                fontSize = 13.sp,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun OfferCard(
    offerCardModel: OfferCardModel,
    viewModel: ProductViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val intent = remember {
        Intent(Intent.ACTION_VIEW, Uri.parse(offerCardModel.urlOffer.orEmpty()))
    }
    val coroutineScope = rememberCoroutineScope()

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
                    coroutineScope.launch {
                        viewModel.dataIntent.send(
                            SearchProductDataIntent.ClickOffer(
                                offerCardModel, context
                            )
                        )
                    }
                    showSheet = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 12.dp, start = 12.dp, top = 12.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFFfa7000)),
                elevation = ButtonDefaults.buttonElevation(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(text = "Ir para Oferta", style = TextStyle(color = White))
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
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "",
                    modifier = Modifier.padding(end = 8.dp),
                    tint = Color(0xFFfa7000)
                )

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
    SubcomposeAsyncImage(
        model = urlImage,
        modifier = Modifier
            .height(360.dp)
            .fillMaxWidth(),
        contentScale = ContentScale.Crop,
        contentDescription = "",
        loading = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(36.dp))
            }
        }
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
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(36.dp))
            }
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
                text = "Buscando Ofertas...",
                color = Black,
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

object URL {
    const val url = "https://chat.whatsapp.com/KR4Pvdr4AQwCeP3Bsu1lLg"
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EncontreinashopeeTheme {
        val model = OfferCardModel(offerTitle = "teste", offerPrice = "R$ 120,50")

        OfferCard(model)
    }
}
