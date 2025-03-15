package com.robbyari.testandroid.ui.test1

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.robbyari.testandroid.model.UserData

@Composable
fun TestScreen1(viewModel: Test1ViewModel = hiltViewModel()) {
    var name by remember { mutableStateOf("") }
    val users by viewModel.user.observeAsState(emptyList())
    val scrollState = rememberScrollState()

    val usersGrouped = users.groupBy { it.userName.firstOrNull()?.uppercaseChar() ?: '#' }
        .filterKeys { it in 'A'..'Z' }

    LaunchedEffect(Unit) {
        viewModel.getAllUsers()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Masukan Nama") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (name.isNotBlank()) {
                    viewModel.saveUser(UserData(userName = name))
                    name = ""
                    viewModel.getAllUsers()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Simpan")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Daftar Nama:", style = MaterialTheme.typography.titleMedium)

        if (users.isEmpty()) {
            Text("Belum ada data")
        } else {
            users.forEach { user ->
                Text("- ${user.userName}")
            }
        }

        HorizontalDivider(modifier = Modifier.padding(8.dp, 8.dp))

        ('A'..'Z').forEach { letter ->
            Text(
                text = letter.toString(),
                style = MaterialTheme.typography.headlineMedium
            )

            usersGrouped[letter]?.sortedBy { it.userName }?.forEach { user ->
                Text("- ${user.userName}")
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}