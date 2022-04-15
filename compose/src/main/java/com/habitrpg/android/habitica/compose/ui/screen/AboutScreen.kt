package com.habitrpg.android.habitica.compose.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.habitrpg.android.habitica.compose.ui.component.AboutButton
import com.habitrpg.android.habitica.compose.ui.component.AboutLink
import com.habitrpg.android.habitica.compose.ui.component.AboutText
import com.habitrpg.android.habitica.compose.ui.component.HabiticaLogo
import com.habitrpg.android.habitica.compose.ui.theme.HabiticaPalette
import com.habitrpg.android.habitica.compose.ui.theme.HabiticaTheme

enum class AboutElementType {
    LINK, BUTTON, TEXT
}

data class AboutElement(
    val label: String,
    val onClick: () -> Unit,
    val elementType: AboutElementType
)

data class AboutState(val elements: List<AboutElement>)

@Composable
fun AboutScreen(
    modifier: Modifier = Modifier,
    aboutState: AboutState
) {
  //  val scrollState = rememberScrollState()
    Column(
        modifier = modifier
//            .scrollable(
//            state = scrollState,
//            orientation = Orientation.Vertical
//        )
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HabiticaLogo()
        for (element in aboutState.elements) {
            when (element.elementType) {
                AboutElementType.TEXT -> AboutText(
                    modifier = Modifier,
                    text = element.label,
                    onClick = element.onClick
                )
                AboutElementType.LINK -> AboutLink(
                    modifier = Modifier,
                    text = element.label,
                    onClick = element.onClick
                )
                AboutElementType.BUTTON -> AboutButton(
                    onClick = element.onClick,
                    modifier = Modifier,
                    text = element.label
                )
            }
        }
    }
}

@Preview
@Composable
private fun DefaultPreview() {
    HabiticaTheme(colorPalette = HabiticaPalette.valueOf("RED")) {
        AboutScreen(
            aboutState = AboutState(
                elements = listOf(
                    AboutElement(
                        label = "Privacy Policy",
                        elementType = AboutElementType.TEXT,
                        onClick = {}),
                    AboutElement(
                        label = "Terms of Service",
                        elementType = AboutElementType.BUTTON,
                        onClick = {}),
                    AboutElement(
                        label = "Rate OurApp",
                        elementType = AboutElementType.BUTTON,
                        onClick = {}),
                    AboutElement(
                        label = "Asdfasd",
                        elementType = AboutElementType.LINK,
                        onClick = {}),
                    AboutElement(
                        label = "Asdfasd",
                        elementType = AboutElementType.TEXT,
                        onClick = {}),
                    AboutElement(
                        label = "Asdfasd",
                        elementType = AboutElementType.TEXT,
                        onClick = {}),
                )
            )
        )
    }

}