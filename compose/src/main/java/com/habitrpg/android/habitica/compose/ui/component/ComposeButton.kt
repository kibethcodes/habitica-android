package com.habitrpg.android.habitica.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.habitrpg.android.habitica.compose.ui.theme.HabiticaTheme

@Composable
fun TestButton() {
    HabiticaTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Button(onClick = {}) {
                Text(text = "Hello World!")
            }
        }
    }
}

@Preview
@Composable
private fun DefaultPreview() {
    TestButton()
}