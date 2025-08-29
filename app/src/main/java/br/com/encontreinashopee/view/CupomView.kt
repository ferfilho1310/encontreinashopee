package br.com.encontreinashopee.view

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import br.com.encontreinashopee.R
import br.com.encontreinashopee.intent.SearchCupomsIntent
import br.com.encontreinashopee.model.CupomModel
import br.com.encontreinashopee.state.DataState
import br.com.encontreinashopee.util.AdvertasingIds
import br.com.encontreinashopee.viewmodel.CupomViewModel
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoadCupoms(
    viewModel: CupomViewModel = koinViewModel(),
    navController: NavHostController
) {
    ChangeColorNavBar(statusBarColor = White, navBarColor = Black, darkIcon = true)

    LaunchedEffect(Unit) {
        viewModel.dataIntent.send(SearchCupomsIntent.SearchCupoms)
    }

    val state = viewModel.dataState.collectAsState().value
    when (state) {
        is DataState.Loading -> ProgressBarCupom(true, "Buscando Cupoms...")
        is DataState.ResponseData<*> -> {
            ProgressBarCupom(false, "Buscando Cupoms...")
            val cupomList = state.data as List<CupomModel>

            if (cupomList.isNotEmpty()) {
                CupomList(cupomList, navController, isHaveCupom = true, viewModel)
            } else {
                CupomList(cupomList, navController, isHaveCupom = false, viewModel)
            }
        }

        is DataState.Error -> Unit
    }

}

@Composable
fun CupomList(
    cupomList: List<CupomModel>,
    navController: NavHostController? = null,
    isHaveCupom: Boolean,
    viewModel: CupomViewModel? = null
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            CupomTopBar(navController = navController)
        },
        modifier = Modifier
            .safeDrawingPadding()
            .fillMaxSize()
            .background(Color.LightGray),
        bottomBar = {
            AdmobBannerCupom()
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = CenterHorizontally
        ) {

            if (isHaveCupom) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .padding(top = 8.dp)
                ) {
                    items(cupomList.size) {
                        CupomItem(cupomList[it]) { cupomModel ->
                            viewModel?.onClickCupomTracker(cupomModel)
                            val intent = Intent(Intent.ACTION_VIEW, cupomModel.linkCupom?.toUri())
                            context.startActivity(intent)
                        }
                    }
                }
            } else {
                EmptyCupom(paddingValues = innerPadding)
            }
        }
    }
}

@Composable
private fun EmptyCupom(paddingValues: PaddingValues) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally,
        modifier = Modifier.padding(paddingValues)
    ) {
        Image(
            modifier = Modifier.size(150.dp),
            painter = painterResource(R.drawable.ticker_fundo_branco),
            contentDescription = "Ticket"
        )
        Text(
            text = "Em breve terá cupons de desconto\npara você aproveitar ;)",
            style = TextStyle(
                color = Black
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun CupomTopBar(navController: NavHostController? = null) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                navController?.navigate(ScreenNavigationKeys.Key.OFFER_LIST) {
                    popUpTo(0) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },
        ) {
            Icon(
                tint = Color(0xFFFF6600),
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Voltar"
            )
        }

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier,
                text = "Cupons de Desconto",
                style = TextStyle(
                    color = Black
                ),
                fontWeight = Bold,
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.size(40.dp))
    }
}

@Composable
fun CupomItem(
    cupomModel: CupomModel,
    listenerClickCupons: ListenerClickCupons
) {
    val context = LocalContext.current
    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = White
        )
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {

            AsyncImage(
                model = cupomModel.imageCupom,
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .padding(end = 16.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = cupomModel.title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Black
                )

                Text(
                    text = cupomCopy(cupomModel.cupom),
                    modifier = Modifier
                        .padding(top = 8.dp)
                )

                Button(
                    onClick = {
                        listenerClickCupons.onClickCupom(cupomModel)

                        clipboardManager.setText(AnnotatedString(cupomModel.cupom))
                        Toast.makeText(context, "Cupom Copiado", Toast.LENGTH_SHORT).show()

                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(
                            0xFFFF6600
                        )
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .align(Alignment.End)
                        .fillMaxWidth()
                        .padding(top = 36.dp)
                ) {
                    Text(text = "Copiar e usar meu cupom", color = White, fontSize = 12.sp)
                }
            }
        }
    }
}

fun cupomCopy(cupom: String) =
    buildAnnotatedString {
        append("Cupom: ")
        withStyle(style = SpanStyle(fontWeight = Bold)) {
            append(cupom)
        }
    }

fun interface ListenerClickCupons {
    fun onClickCupom(model: CupomModel)
}

@Composable
fun AdmobBannerCupom(adSize: AdSize = AdSize.LARGE_BANNER) {
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

@Composable
fun ProgressBarCupom(isVisible: Boolean, progressTitle: String? = null) {
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
                text = progressTitle.orEmpty(),
                color = White,
                fontWeight = Bold
            )
        }
    }
}

@Preview
@Composable
fun Preview() {
    val cupoms = arrayListOf(
        CupomModel(
            id = 1,
            "testes 1",
            "cupom 1",
            imageCupom = "https://firebasestorage.googleapis.com/v0/b/encontrei-na-shoppe.appspot.com/o/058b1983a4323534332d579dde0dd193.jpg?alt=media&token=e36fe051-5000-408a-ac9b-aa38e9cb460f",
            linkCupom = ""

        ),
        CupomModel(
            id = 2,
            "testes 2",
            "cupom 2",
            imageCupom = "https://firebasestorage.googleapis.com/v0/b/encontrei-na-shoppe.appspot.com/o/058b1983a4323534332d579dde0dd193.jpg?alt=media&token=e36fe051-5000-408a-ac9b-aa38e9cb460f",
            linkCupom = ""
        )
    )

    CupomList(cupomList = cupoms, isHaveCupom = true)
}