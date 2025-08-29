package br.com.encontreinashopee.view

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.encontreinashopee.util.PreferencesManager
import br.com.encontreinashopee.view.ScreenNavigationKeys.Key.CUPOMS
import br.com.encontreinashopee.view.ScreenNavigationKeys.Key.OFFER_LIST
import br.com.encontreinashopee.view.ScreenNavigationKeys.Key.ONBOARDING
import br.com.encontreinashopee.view.ScreenNavigationKeys.Key.PREFERENCES_KEY
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
fun ChangeColorNavBar(
    statusBarColor: Color,
    navBarColor: Color,
    darkIcon: Boolean
) {
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = darkIcon
        )

        systemUiController.setNavigationBarColor(
            color = navBarColor
        )
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
                OfferList(navHostController = navController)
            } else {
                OnboardingView(navHostController = navController)
            }
        }
        composable(OFFER_LIST) {
            OfferList(navHostController = navController)
        }
        composable(CUPOMS) {
            LoadCupoms(navController = navController)
        }
    }
}