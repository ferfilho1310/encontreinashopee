package br.com.encontreinashopee.view

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
import br.com.encontreinashopee.viewmodel.ProductViewModel
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.shimmer
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import kotlinx.coroutines.delay
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListOffers(
    products: List<OfferCardModel>,
    navHostController: NavHostController? = null,
    listener: ListenerOffer? = null
) {
    val lazyListState = rememberLazyListState()

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val isCollapsed by remember {
        derivedStateOf { lazyListState.firstVisibleItemIndex > 0 }
    }

    Scaffold(
        modifier = Modifier
            .safeDrawingPadding()
            .background(Color.LightGray)
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopbarListOffers(scrollBehavior)
        },
        floatingActionButton = {
            LazyColumn(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .padding(bottom = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    FloatActionButtonSocialMedia()
                }

                item {
                    FloatActionButtonListOffers(navHostController)
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            AdmobBanner()
        }
    ) { innerPadding ->
        LazyColumn(
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            /*item {
                AnimatedVisibility(visible = !isCollapsed) {
                    AutoCarousel()
                }
            }
*/
            items(products.size) { index ->
                CardOffer(products, index, listener)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopbarListOffers(
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    TopAppBar(
        modifier = Modifier
            .background(Color.LightGray),
        title = {
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = "Ofertas para Você",
                style = TextStyle(
                    color = Black
                ),
                fontWeight = Bold,
                fontSize = 18.sp
            )
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
private fun FloatActionButtonListOffers(
    navHostController: NavHostController? = null,
) {
    FloatingActionButton(
        onClick = {
            navHostController?.navigate(ScreenNavigationKeys.Key.CUPOMS)
        },
        containerColor = White,
        modifier = Modifier.size(69.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(top = 8.dp, bottom = 8.dp),
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

            Text("Cupons", fontSize = 12.sp, fontWeight = Bold)
        }
    }
}

@Composable
private fun FloatActionButtonSocialMedia() {
    val context = LocalContext.current
    val url = "https://www.tiktok.com/@achadinhosincriveis_37"

    FloatingActionButton(
        onClick = {
            navegateToOffer(context = context, urlOffer = url)
        },
        containerColor = White,
        modifier = Modifier.size(69.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(end = 8.dp, top = 8.dp, bottom = 8.dp, start = 8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.tiktok),
                contentDescription = "Minha Imagem",
                modifier = Modifier.size(30.dp),
                tint = Color.Unspecified
            )

            Text("Siga-nos", fontSize = 12.sp, fontWeight = Bold)
        }
    }
}

@Composable
private fun CardOffer(
    products: List<OfferCardModel>,
    index: Int,
    listener: ListenerOffer? = null
) {
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

            SubcomposeAsyncImage(
                model = products[index].offerImage,
                contentDescription = "Minha imagem",
                modifier = Modifier
                    .size(150.dp)
                    .padding(end = 16.dp),
                loading = {
                    ShimmerBox()
                },
                error = {
                    ShimmerBox()
                },
                success = {
                    SubcomposeAsyncImageContent()
                }
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

@Composable
fun ShimmerBox() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .placeholder(
                visible = true,
                color = Color.Gray,
                highlight =
                    PlaceholderHighlight.shimmer(
                        highlightColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                    )
            )
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AutoCarousel(
    modifier: Modifier = Modifier,
    autoScrollDuration: Long = 8000
) {
    val items = arrayListOf(BannerList("", ""))

    val pagerState = rememberPagerState(initialPage = 0) { items.size }

    LaunchedEffect(Unit) {
        while (true) {
            delay(autoScrollDuration)
            val nextPage = (pagerState.currentPage + 1) % items.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
        ) { page ->
            CardCarrossel(items, page)
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (items.size > 1) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                repeat(items.size) { index ->
                    val isSelected = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(if (isSelected) 10.dp else 8.dp)
                            .background(
                                color = if (isSelected) Black else Color.LightGray,
                                shape = CircleShape
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun CardCarrossel(
    items: List<BannerList>,
    page: Int
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp, vertical = 8.dp)
            .clickable {
                navegateToOffer(items[page].url, context)
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Gray)
    ) {
        AsyncImage(
            model = items[page].img,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.Crop,
        )
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

private fun navegateUrl(url: String) {

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
                fontWeight = Bold
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
