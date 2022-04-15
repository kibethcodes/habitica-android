package com.habitrpg.android.habitica.compose.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.habitrpg.android.habitica.compose.R
import com.habitrpg.android.habitica.compose.ui.theme.HabiticaTheme
import com.habitrpg.android.habitica.compose.ui.theme.brand_icon

@Composable
fun HabiticaLogo() {
    Image(
        painter = painterResource(R.drawable.ic_habitica),
        contentDescription = "Habitica Icon",
        modifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(brand_icon)
    )
}

@Composable
fun AboutButton(modifier: Modifier, onClick: () -> Unit, text: String) {
    Button(
        onClick = onClick,
        modifier = modifier
            .padding(8.dp)
            .width(250.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = HabiticaTheme.colors.windowBackground)
    ) {
        Text(
            text = text,
            color = HabiticaTheme.colors.brandNeon,
            fontSize = 18.sp
        )
    }
}

@Composable
fun AboutText(modifier: Modifier, onClick: () -> Unit, text: String) {
    Text(
        text = text,
        modifier = modifier
            .padding(4.dp)
            .clickable { onClick() },
        fontSize = 14.sp
    )
}

@Composable
fun AboutLink(modifier: Modifier, onClick: () -> Unit, text: String) {
    Text(
        text = text,
        modifier = modifier
            .padding(4.dp)
            .clickable { onClick() },
        fontSize = 18.sp,
        color = HabiticaTheme.colors.brandNeon,
        style = TextStyle(textDecoration = TextDecoration.Underline)
    )
}