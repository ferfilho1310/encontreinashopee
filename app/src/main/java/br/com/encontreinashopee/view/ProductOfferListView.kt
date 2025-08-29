package br.com.encontreinashopee.view

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import br.com.encontreinashopee.R
import br.com.encontreinashopee.intent.SearchOffersDataIntent
import br.com.encontreinashopee.model.BannerList
import br.com.encontreinashopee.model.OfferCardModel
import br.com.encontreinashopee.state.DataState
import br.com.encontreinashopee.ui.theme.EncontreinashopeeTheme
import br.com.encontreinashopee.util.AdvertasingIds
import br.com.encontreinashopee.util.autoimageslider.AutoSlide.AutoSlidingCarousel
import br.com.encontreinashopee.viewmodel.ProductViewModel
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import org.koin.androidx.compose.koinViewModel

@Composable
fun OfferList(
    viewModel: ProductViewModel = koinViewModel(),
    navHostController: NavHostController
) {
    val context = LocalContext.current
    ChangeColorNavBar(statusBarColor = White, navBarColor = Black, darkIcon = true)

    LaunchedEffect(key1 = Unit) {
        viewModel.dataIntent.send(SearchOffersDataIntent.SearchOffers)
    }

    val state = viewModel.dataState.collectAsState().value

    when (state) {
        is DataState.Loading -> ProgressBar(true, "Buscando Ofertas...")
        is DataState.ResponseData<*> -> {
            ListOffers(state.data as List<OfferCardModel>, navHostController) { offerCardModel ->
                navegateToOffer(offerCardModel.urlOffer, context)
                viewModel.onClickOffer(offerCardModel)
            }
        }

        is DataState.Error -> Unit
    }
}

private fun navegateToOffer(urlOffer: String?, context: Context) {

    val intent = Intent(
        Intent.ACTION_VIEW,
        urlOffer.orEmpty().toUri()
    )
    context.startActivity(intent)
}

@Composable
fun ListOffers(
    products: List<OfferCardModel>,
    navHostController: NavHostController? = null,
    listener: ListenerOffer? = null
) {

    Scaffold(
        modifier = Modifier
            .safeDrawingPadding()
            .background(Color.LightGray),
        topBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
                        text = "Ofertas para Você",
                        style = TextStyle(
                            color = Black
                        ),
                        fontWeight = Bold,
                        fontSize = 18.sp
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navHostController?.navigate(ScreenNavigationKeys.Key.CUPOMS)
                },
                containerColor = White
            ) {
                Column(
                    modifier = Modifier
                        .padding(end = 8.dp, top = 8.dp, bottom = 8.dp, start = 8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier
                            .size(30.dp),
                        tint = Color(0xFFFF6600),
                        imageVector = Icons.Filled.ConfirmationNumber,
                        contentDescription = "Voltar"
                    )

                    Text("Cupons", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            AdmobBanner()
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .weight(1f),
                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 8.dp)
            ) {
                items(products.size) { index ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(6.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = White
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            AsyncImage(
                                model = products[index].offerImage,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(150.dp)
                                    .padding(end = 16.dp),
                                contentScale = ContentScale.Crop
                            )

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 8.dp)
                            ) {
                                Text(
                                    text = products[index].offerTitle.orEmpty(),
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 16.sp,
                                    color = Black
                                )
                                Text(
                                    text = products[index].offerPrice.orEmpty(),
                                    color = Color(0xFFFF6600),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )

                                Button(
                                    onClick = {
                                        listener?.onClickOffer(products[index])
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(
                                            0xFFFF6600
                                        )
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .height(40.dp)
                                        .fillMaxWidth()
                                        .align(Alignment.End)
                                ) {
                                    Text(text = "Ver Oferta na Shopee", color = White)
                                }
                            }
                        }
                    }
                }
            }
        }
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
            .padding(top = 8.dp, end = 2.dp, start = 2.dp, bottom = 8.dp)
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
fun ProgressBar(isVisible: Boolean, progressTitle: String) {
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
                text = progressTitle,
                color = White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

fun interface ListenerBanner {
    fun onClickLinkBanner(urlImage: String)
}

fun interface ListenerOffer {
    fun onClickOffer(model: OfferCardModel)
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
            ),
            OfferCardModel(
                offerTitle = "Smart TV 4K",
                offerPrice = "$499.99",
                offerImage = "https://link-da-imagem-1.jpg",
                urlOffer = "https://site-do-produto-1.com",
                id = 2,
            ),
            OfferCardModel(
                offerTitle = "Smart TV 4K",
                offerPrice = "$499.99",
                offerImage = "https://link-da-imagem-1.jpg",
                urlOffer = "https://site-do-produto-1.com",
                id = 3,
            ),
            OfferCardModel(
                offerTitle = "Smart TV 4K",
                offerPrice = "$499.99",
                offerImage = "https://link-da-imagem-1.jpg",
                urlOffer = "https://site-do-produto-1.com",
                id = 4
            )
        )

        ListOffers(sampleProducts)
    }
}
