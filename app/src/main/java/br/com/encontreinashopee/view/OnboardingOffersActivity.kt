package br.com.encontreinashopee.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.encontreinashopee.R
import br.com.encontreinashopee.util.PreferencesManager
import br.com.encontreinashopee.view.Key.OFFER_LIST
import br.com.encontreinashopee.view.Key.ONBOARDING
import br.com.encontreinashopee.view.Key.PREFERENCES_KEY
import br.com.encontreinashopee.view.ui.theme.EncontreinashopeeTheme

class OnboardingOffersActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EncontreinashopeeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ComposeNavigation()
                }
            }
        }
    }
}

@Composable
fun OnboardingScreen(navHostController: NavHostController) {
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painterResource(R.drawable.icononboarding),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier.height(300.dp)
        )

        Text(
            text = "\"Encontrei na Shoppe\" é um aplicativo que vai te ajudar a" +
                    " encontrar uma variedade de produtos disponíveis na Shoppe." +
                    "Com uma interface intuitiva, você pode encontrar ofertas, promoções e preço baixo," +
                    " além de receber recomendações de compra.",
            modifier = Modifier.padding(24.dp)
        )

        Button(
            onClick = {
                navHostController.navigate(OFFER_LIST) {
                    popUpTo(ONBOARDING) {
                        inclusive = true
                    }
                }

                preferencesManager.saveData(PREFERENCES_KEY, true)
            },
            colors = ButtonDefaults.buttonColors(Color(0xFFfa7000)),
        ) {
            Text(text = "Ver as Ofertas")
        }
    }
}

@Composable
fun ComposeNavigation() {
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ONBOARDING) {
        composable(ONBOARDING) {
            val preferences = preferencesManager.getData(PREFERENCES_KEY, false)
            if (preferences) {
                OfferList()
            } else {
                OnboardingScreen(navHostController = navController)
            }
        }
        composable(OFFER_LIST) {
            OfferList()
        }
    }
}

object Key {
    const val ONBOARDING = "Onboarding"
    const val OFFER_LIST = "OfferList"
    const val PREFERENCES_KEY = "Key"
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    val navController = rememberNavController()

    EncontreinashopeeTheme {
        OnboardingScreen(navController)
    }
}