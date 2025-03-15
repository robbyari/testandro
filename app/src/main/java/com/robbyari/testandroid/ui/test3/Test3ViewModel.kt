package com.robbyari.testandroid.ui.test3

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robbyari.testandroid.core.AppDatabase
import com.robbyari.testandroid.core.Path
import com.robbyari.testandroid.model.PathData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class Test3ViewModel @Inject constructor(
    private val appDatabase: AppDatabase
) : ViewModel() {
    private val _path = MutableLiveData<List<PathData>>()
    val path: LiveData<List<PathData>> = _path

    fun savePath(name: String, parentName: String?) {
        viewModelScope.launch {
            try {
                val parentId = withContext(Dispatchers.IO) {
                    parentName?.let { appDatabase.pathDao().getPathIdByName(it) }
                }

                val newPath = Path(name = name, parentId = parentId)
                appDatabase.pathDao().savePath(newPath)

            } catch (e: Exception) {
                Log.d("Test3ViewModel", "Error saving path: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun getAllPath() {
        viewModelScope.launch {
            appDatabase.pathDao().getAllPaths().collectLatest { allPaths ->
                val pathList = allPaths.map { path ->
                    val fullPath = buildFullPath(path, allPaths)
                    val parentName = path.parentId?.let { parentId ->
                        allPaths.find { it.id == parentId }?.name ?: "Unknown"
                    }

                    PathData(
                        name = fullPath,
                        parentId = path.parentId,
                        parentName = parentName
                    )
                }

                _path.postValue(pathList)
            }
        }
    }

    fun checkExistPath(name: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val exists = appDatabase.pathDao().isPathNameExists(name) > 0
            onResult(exists)
        }
    }

    fun updatePathName(newName: String, oldName: String) {
        viewModelScope.launch {
            appDatabase.pathDao().updatePathName(newName, oldName)
        }
    }

    fun deletePathWithChildrenByName(pathName: String) {
        viewModelScope.launch {
            val pathId = appDatabase.pathDao().getPathIdByName(pathName)

            try {
                if (pathId != null) {
                    deletePathWithChildren(pathId)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                getAllPath()
            }
        }
    }

    private suspend fun deletePathWithChildren(pathId: Int) {
        val children = appDatabase.pathDao().getChildren(pathId)

        for (child in children) {
            deletePathWithChildren(child.id)
        }

        appDatabase.pathDao().deletePathById(pathId)
    }

    private fun buildFullPath(path: Path, allPaths: List<Path>): String {
        val parentPath = path.parentId?.let { parentId ->
            allPaths.find { it.id == parentId }?.let { parent ->
                buildFullPath(parent, allPaths)
            }
        }
        Log.d("Test3ViewModel", "buildFullPath")
        return if (parentPath != null) "$parentPath / ${path.name}" else path.name
    }

}