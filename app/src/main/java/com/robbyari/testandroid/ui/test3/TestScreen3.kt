package com.robbyari.testandroid.ui.test3

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.robbyari.testandroid.utils.Utils

@Composable
fun TestScreen3(viewModel: Test3ViewModel = hiltViewModel()) {
    val path by viewModel.path.observeAsState(emptyList())

    var parentName by remember { mutableStateOf("") }
    var childName by remember { mutableStateOf("") }

    var showAlertDialog by remember { mutableStateOf(false) }
    var alertMessage by remember { mutableStateOf("") }

    var editDialog by remember { mutableStateOf(false) }
    var deleteDialog by remember { mutableStateOf(false) }
    var selectedName by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.getAllPath()

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(36.dp))

        OutlinedTextField(
            value = parentName,
            onValueChange = { parentName = it },
            label = { Text("Parent name") },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = childName,
            onValueChange = { childName = it },
            label = { Text("Child name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.checkExistPath(childName) { exists ->
                    if (exists) {
                        alertMessage = "Path sudah ada, gunakan path lain!"
                        showAlertDialog = true
                    } else {
                        viewModel.savePath(childName, parentName)
                        childName = ""
                        parentName = ""
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Simpan")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(Modifier.padding(top = 8.dp)) {
            items(path) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.name,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(onClick = {
                        selectedName = Utils.getLastName(item.name)
                        editDialog = true
                    }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                    }

                    IconButton(onClick = {
                        selectedName = Utils.getLastName(item.name)
                        deleteDialog = true
                    }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            }
        }
    }

    if (showAlertDialog) {
        Alert(
            alertMessage = alertMessage,
            onDismiss = { showAlertDialog = false },
            onConfirm = { showAlertDialog = false }
        )
    }

    if (editDialog || deleteDialog) {
        PathDialog(
            initialName = selectedName,
            onDismiss = {
                editDialog = false
                deleteDialog = false
            },
            onSave = { newName ->
                if (editDialog) {
                    viewModel.updatePathName(newName, selectedName)
                    viewModel.getAllPath()
                } else if (deleteDialog) {
                    viewModel.deletePathWithChildrenByName(selectedName)
                }
                editDialog = false
                deleteDialog = false
            },
            title = if (editDialog) "Edit Path" else "Hapus Path",
            textConfirm = if (editDialog) "Simpan" else "Hapus"
        )
    }
}

@Composable
fun Alert(
    alertMessage: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Peringatan") },
        text = { Text(alertMessage) },
        confirmButton = {
            Button(onClick = { onConfirm() }) {
                Text("OK")
            }
        }
    )
}

@Composable
fun PathDialog(
    initialName: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
    title: String,
    textConfirm: String
) {
    var newName by remember { mutableStateOf(initialName) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            OutlinedTextField(
                value = newName,
                onValueChange = { newName = it },
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(onClick = { onSave(newName) }) {
                Text(textConfirm)
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}
