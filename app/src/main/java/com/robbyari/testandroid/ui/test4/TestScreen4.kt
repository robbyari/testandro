package com.robbyari.testandroid.ui.test4

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.robbyari.testandroid.model.PathData

@Composable
fun TestScreen4(viewModel: Test4ViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) { viewModel.getPathWithoutParent() }

    val rootPath by viewModel.rootPath.observeAsState(emptyList())
    val pathMap by viewModel.path.observeAsState(emptyMap())

    LazyColumn(modifier = Modifier.fillMaxSize().padding(top = 60.dp)) {
        items(rootPath) { root ->
            TreePath(
                pathData = root,
                pathMap = pathMap,
                onExpand = { viewModel.getPathByIdParent(it) }
            )
        }
    }
}

@Composable
fun TreePath(
    pathData: PathData,
    pathMap: Map<Int, List<PathData>>,
    onExpand: (Int) -> Unit,
    level: Int = 0
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(start = (level * 16).dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(if (isExpanded) Color.Blue else Color.Gray)
                .clickable {
                    isExpanded = !isExpanded
                    if (isExpanded) {
                        pathData.parentId?.let { onExpand(it) }
                    }
                }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowRight,
                contentDescription = "Expand/Collapse",
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = pathData.name, color = Color.White)
        }

        if (isExpanded) {
            pathMap[pathData.parentId]?.forEach { child ->
                TreePath(
                    pathData = child,
                    pathMap = pathMap,
                    onExpand = onExpand,
                    level = level + 1
                )
            }
        }
    }
}
