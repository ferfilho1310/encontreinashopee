package br.com.encontreinashopee.view.offerdetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import br.com.encontreinashopee.model.OfferCardModel
import br.com.encontreinashopee.view.offerdetails.ui.theme.EncontreinashopeeTheme
import br.com.encontreinashopee.view.productoffer.BottomSheetAlert
import br.com.encontreinashopee.view.productoffer.ListenerBottomSheet
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


class ProducOfferDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent
        val extras = intent.extras
        val offer: OfferCardModel? = extras?.getParcelable("offerModel")

        setContent {
            EncontreinashopeeTheme {
                SetComposableStatusBar(Color(0xFFfa7000))
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    OfferDetail(offerCardModel = offer)
                }
            }
        }
    }
}

@Composable
fun OfferDetail(offerCardModel: OfferCardModel? = null) {

    val context = LocalContext.current
    val intent = remember {
        Intent(Intent.ACTION_VIEW, Uri.parse(offerCardModel?.urlOffer.orEmpty()))
    }

    var showSheet by remember { mutableStateOf(false) }

    if (showSheet) {
        BottomSheetAlert(offerCardModel, object : ListenerBottomSheet {
            override fun onClickButton() {
                context.startActivity(intent)
            }

            override fun onDismiss() {
                showSheet = false
            }
        }
        )
    }

    Column(
        Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(Color.LightGray),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(1f, false)
                .padding(bottom = 80.dp)
                .fillMaxHeight()
                .background(Color.LightGray)
        ) {

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                item(0) {
                    if (!offerCardModel?.idVideo.isNullOrEmpty()) {
                        Card(
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .width(360.dp)
                                .padding(4.dp)
                                .height(200.dp),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 20.dp
                            )
                        ) {
                            YoutubeVideo()
                        }
                    }
                }

                offerCardModel?.listImage?.let {
                    items(it.size) { index ->
                        Card(
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 20.dp
                            )
                        ) {
                            ImageProductDetails(urlImage = it[index])
                        }
                    }
                }
            }

            Card(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                )
            ) {
                Text(
                    text = offerCardModel?.offerTitle.toString(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 12.dp, start = 12.dp, top = 4.dp),
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = offerCardModel?.offerPrice.toString(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 12.dp, start = 12.dp, top = 4.dp),
                )
            }

            Card(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, top = 4.dp, end = 4.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                )
            ) {
                Text(
                    text = "Informações do produto",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 12.dp, start = 12.dp, top = 4.dp)
                )

                Text(
                    text = offerCardModel?.description.toString(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 12.dp, start = 12.dp, top = 4.dp),
                )
            }
        }

        Button(
            onClick = {
                showSheet = true
            },
            modifier = Modifier
                .padding(start = 8.dp, bottom = 8.dp, end = 8.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(Color(0xFFfa7000)),
            elevation = ButtonDefaults.buttonElevation(20.dp)
        ) {
            Text(text = "Ir Para Oferta", style = TextStyle(color = Color.White))
        }
    }
}

@Composable
fun ImageProductDetails(urlImage: String) {
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
            .size(200.dp)
            .fillMaxWidth(),
        contentScale = ContentScale.Crop,
    )
}

@Composable
fun YoutubeVideo(/*idVideo: String*/) {
    Column {

        AndroidView(modifier = Modifier
            .fillMaxWidth()
            .size(380.dp), factory = {
            val view = YouTubePlayerView(it)
            view.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    super.onReady(youTubePlayer)
                    youTubePlayer.loadVideo("xhkKGXk-QvI", 0f)
                }
            })
            view
        }
        )
    }
}

@Composable
fun SetComposableStatusBar(color: Color) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(color)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    EncontreinashopeeTheme {
        OfferDetail()
    }
}