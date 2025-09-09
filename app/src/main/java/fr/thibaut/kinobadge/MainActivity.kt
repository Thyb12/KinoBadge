package fr.thibaut.kinobadge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import fr.thibaut.kinobadge.ui.theme.KinoBadgeTheme
import fr.thibaut.kinobadge.ecranPrincipal.EcranPrincipalUI



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KinomapBadge()
        }
    }
}


@Composable
fun KinomapBadge() {
    KinoBadgeTheme {
        EcranPrincipalUI()
    }
}