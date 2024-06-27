package br.com.encontreinashopee.view.onboarding

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.SideEffect
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
import br.com.encontreinashopee.view.ScreenNavigationKeys.Key.OFFER_LIST
import br.com.encontreinashopee.view.ScreenNavigationKeys.Key.ONBOARDING
import br.com.encontreinashopee.view.ScreenNavigationKeys.Key.PREFERENCES_KEY
import br.com.encontreinashopee.view.productoffer.OfferList
import br.com.encontreinashopee.view.productoffer.SetComposableStatusBar
import br.com.encontreinashopee.view.ui.theme.EncontreinashopeeTheme

class OnboardingOffersActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EncontreinashopeeTheme {
                SetComposableStatusBar(Color(0xFFfa7000))
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CheckNotificationPolicyAccess()
                    ComposeNavigation()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun CheckNotificationPolicyAccess() {
    val getPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Unit
        } else {
            Unit
        }
    }

    SideEffect {
        getPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
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
            painterResource(R.drawable.logo_fundo_branca),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier.height(300.dp)
        )

        Text(
            text = "\"Achadinhos\" é um aplicativo que vai te ajudar a" +
                    " encontrar uma variedade de produtos disponíveis na Shoppe e produtos importados no AliExpress. " +
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


@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    val navController = rememberNavController()

    EncontreinashopeeTheme {
        OnboardingScreen(navController)
    }
}