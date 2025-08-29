package br.com.encontreinashopee.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import br.com.encontreinashopee.R
import br.com.encontreinashopee.util.PreferencesManager
import br.com.encontreinashopee.view.ScreenNavigationKeys.Key.OFFER_LIST
import br.com.encontreinashopee.view.ScreenNavigationKeys.Key.ONBOARDING
import br.com.encontreinashopee.view.ScreenNavigationKeys.Key.PREFERENCES_KEY
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun OnboardingView(
    navHostController: NavHostController
) {
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }

    ChangeColorNavBar(statusBarColor = Color.Black, navBarColor = Color.Black, darkIcon = false)

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.Black)
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
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Text(text = "Ir Para Ofertas")
        }
    }
}