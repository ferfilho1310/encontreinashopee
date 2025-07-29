package br.com.encontreinashopee.view.productoffer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.core.view.WindowCompat
import br.com.encontreinashopee.R
import br.com.encontreinashopee.intent.SearchOffersDataIntent
import br.com.encontreinashopee.model.BannerList
import br.com.encontreinashopee.model.OfferCardModel
import br.com.encontreinashopee.state.DataState
import br.com.encontreinashopee.ui.theme.EncontreinashopeeTheme
import br.com.encontreinashopee.util.AdvertasingIds
import br.com.encontreinashopee.util.autoimageslider.AutoSlide.AutoSlidingCarousel
import br.com.encontreinashopee.viewmodel.ProductViewModel
import coil.compose.rememberImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.firebase.FirebaseApp
import org.koin.androidx.compose.koinViewModel

class ProductOfferActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        setContent {
            CompositionLocalProvider(
                LocalLayoutDirection provides LayoutDirection.Ltr
            ) {
                EncontreinashopeeTheme {
                    SetComposableStatusBar(Black)
                    Surface(
                        modifier = Modifier.fillMaxSize(), color = Color.Black
                    ) {
                        OfferList()
                    }
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
fun OfferList(
    viewModel: ProductViewModel = koinViewModel(),
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.dataIntent.send(SearchOffersDataIntent.SearchOffers)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .background(Black)
    ) {
        SearchOffers()
    }
}

@Composable
fun AdmobBanner(adSize: AdSize = AdSize.LARGE_BANNER) {
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        factory = { context ->
            AdView(context).apply {
                setAdSize(adSize)
                adUnitId = AdvertasingIds.ADMOB
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun BannerOffer(listener: ListenerBanner) {
    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .padding(top = 8.dp, end = 2.dp, start = 2.dp)
    ) {
        val listImage = arrayListOf(
            BannerList(img = painterResource(id = R.drawable.banner1)),
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
fun SearchOffers(viewModel: ProductViewModel = koinViewModel()) {

    val state = viewModel.dataState.collectAsState().value
    when (state) {
        is DataState.Loading -> ProgressBar(true)
        is DataState.ResponseData<*> -> {
            ListOffers(state.data as List<OfferCardModel>)
        }

        is DataState.Error -> Unit
    }
}

@Composable
fun ListOffers(products: List<OfferCardModel>) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        BannerOffer {

        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(products.size) { index ->
                val product = products[index]
                Card(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Image(
                            painter = rememberImagePainter(data = product.offerImage),
                            contentDescription = null,
                            modifier = Modifier
                                .size(80.dp)
                                .padding(end = 16.dp),
                            contentScale = ContentScale.Crop
                        )

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                        ) {
                            Text(
                                text = product.offerTitle.orEmpty(),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                color = Black
                            )
                            Text(
                                text = product.offerPrice.orEmpty(),
                                color = Color(0xFFFF6600),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                            Button(
                                onClick = {
                                    val intent =
                                        Intent(
                                            Intent.ACTION_VIEW,
                                            product.urlOffer.orEmpty().toUri()
                                        )
                                    context.startActivity(intent)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(
                                        0xFFFF6600
                                    )
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(40.dp)
                            ) {
                                Text(text = "Ver Oferta", color = White)
                            }
                        }
                    }
                }
            }
        }

        AdmobBanner()
    }
}

private fun shareSheetOffer(context: Context, model: OfferCardModel?) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TITLE, "Oferta")
        putExtra(
            Intent.EXTRA_TEXT,
            "Dê uma olhada nesta oferta\n" + model?.offerTitle + ".\n" + "Clique no link: " + model?.urlOffer
        )
        type = "text/plain"
    }

    val shareIntent =
        Intent.createChooser(sendIntent, "Você está compartilhando: " + model?.offerTitle)
    context.startActivity(shareIntent)
}

@Composable
fun ProgressBar(isVisible: Boolean) {
    if (isVisible) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.corujinha))

        Column(
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            LottieAnimation(composition, modifier = Modifier.size(150.dp))

            Text(
                modifier = Modifier.padding(top = 12.dp),
                text = "Buscando Ofertas...",
                color = White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

fun interface ListenerShared {
    fun onClickShare(urlAffiliate: String)
}

fun interface ListenerBanner {
    fun onClickLinkBanner(urlImage: String)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EncontreinashopeeTheme {
        val sampleProducts = listOf(
            OfferCardModel(
                offerTitle = "Smart TV 4K",
                offerPrice = "$499.99",
                offerImage = "https://link-da-imagem-1.jpg",
                urlOffer = "https://site-do-produto-1.com",
                id = 1,
                description = ""
            ),
            OfferCardModel(
                offerTitle = "Smart TV 4K",
                offerPrice = "$499.99",
                offerImage = "https://link-da-imagem-1.jpg",
                urlOffer = "https://site-do-produto-1.com",
                id = 2,
                description = ""
            ),
            OfferCardModel(
                offerTitle = "Smart TV 4K",
                offerPrice = "$499.99",
                offerImage = "https://link-da-imagem-1.jpg",
                urlOffer = "https://site-do-produto-1.com",
                id = 3,
                description = ""
            ),
            OfferCardModel(
                offerTitle = "Smart TV 4K",
                offerPrice = "$499.99",
                offerImage = "https://link-da-imagem-1.jpg",
                urlOffer = "https://site-do-produto-1.com",
                id = 4,
                description = ""
            )
        )

        ListOffers(sampleProducts)
    }
}
