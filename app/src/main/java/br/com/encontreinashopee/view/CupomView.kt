package br.com.encontreinashopee.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import br.com.encontreinashopee.R
import br.com.encontreinashopee.intent.SearchCupomsIntent
import br.com.encontreinashopee.model.CupomModel
import br.com.encontreinashopee.state.DataState
import br.com.encontreinashopee.util.AdvertasingIds
import br.com.encontreinashopee.viewmodel.CupomViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoadCupoms(viewModel: CupomViewModel = koinViewModel(), navController: NavHostController) {
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
                CupomList(cupomList, navController, isHaveCupom = true)
            } else {
                CupomList(cupomList, navController, isHaveCupom = false)
            }
        }

        is DataState.Error -> Unit
    }

}

@Composable
fun CupomList(
    cupomList: List<CupomModel>,
    navController: NavHostController? = null,
    isHaveCupom: Boolean
) {
    Column(
        Modifier
            .fillMaxSize()
            .fillMaxWidth()
            .safeDrawingPadding()
            .background(Color.LightGray)
    ) {
        IconButton(
            onClick = {
                navController?.navigate(ScreenNavigationKeys.Key.OFFER_LIST) {
                    popUpTo(0) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        ) {
            Icon(
                tint = Color(0xFFFF6600),
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Voltar"
            )
        }

        if (isHaveCupom) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                items(cupomList.size) {
                    CupomItem(cupomList[it])
                }
            }
        } else {
            Text(
                modifier = Modifier,
                text = "Em breve terá mais cupoms de \ndesconto para você aproveitar ;)",
                style = TextStyle(
                    color = Color(
                        0xFFFF6600
                    )
                )
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            AdmobBannerCupom()
        }
    }
}

@Composable
fun CupomItem(cupomModel: CupomModel) {
    val context = LocalContext.current
    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = White
        )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            cupomModel.let {
                Text(
                    text = it.title,
                    modifier = Modifier.padding(start = 8.dp, top = 8.dp)
                )
                Text(
                    text = it.cupom,
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp),
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = {
                    clipboardManager.setText(AnnotatedString(cupomModel.cupom))
                    Toast.makeText(context, "Cupom Copiado", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF6600)
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 8.dp, bottom = 8.dp)
            ) {
                Text(text = "Resgatar Cupom", color = White)
            }
        }
    }
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
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview
@Composable
fun Preview() {
    val cupoms = arrayListOf(
        CupomModel("testes 1", "cupom 1"),
        CupomModel("testes 2", "cupom 2")
    )

    CupomList(cupomList = cupoms, isHaveCupom = true)
}