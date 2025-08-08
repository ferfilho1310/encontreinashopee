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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
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
import br.com.encontreinashopee.view.ui.theme.EncontreinashopeeTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class OnboardingOffersActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CompositionLocalProvider(
                LocalLayoutDirection provides LayoutDirection.Ltr
            ) {
                EncontreinashopeeTheme {
                    SetComposableStatusBar(Color.Black)
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = Color.Black
                    ) {
                        CheckNotificationPolicyAccess()
                        ComposeNavigation()
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

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .safeDrawingPadding()
            .height(IntrinsicSize.Min)
    ) {
        Image(
            painterResource(R.drawable.fundo_de_tela_5),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            contentScale = ContentScale.FillHeight,
            alignment = Alignment.CenterEnd,
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
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Ir Para Ofertas")
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