package com.robbyari.testandroid.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    navigateToTest1: () -> Unit,
    navigateToTest2: () -> Unit,
    navigateToTest3: () -> Unit,
    navigateToTest4: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Button(onClick = {
            navigateToTest1()
        }) {
            Text(text = "Test 1")
        }
        Button(onClick = {
            navigateToTest2()
        }) {
            Text(text = "Test 2")
        }
        Button(onClick = {
            navigateToTest3()
        }) {
            Text(text = "Test 3")
        }
        Button(onClick = {
            navigateToTest4()
        }) {
            Text(text = "Test 4")
        }
    }
}