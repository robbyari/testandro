package com.robbyari.testandroid.ui.test4

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robbyari.testandroid.core.AppDatabase
import com.robbyari.testandroid.model.PathData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class Test4ViewModel @Inject constructor(
    private val appDatabase: AppDatabase
) : ViewModel() {
    private val _rootPath = MutableLiveData<List<PathData>>()
    val rootPath: LiveData<List<PathData>> = _rootPath

    private val _path = MutableLiveData<Map<Int, List<PathData>>>() // Menggunakan Map agar path lain tidak hilang
    val path: LiveData<Map<Int, List<PathData>>> = _path

    fun getPathWithoutParent() {
        viewModelScope.launch {
            appDatabase.pathDao().getRootPaths().collect { rootPaths ->
                val pathList = rootPaths.map { path ->
                    PathData(
                        name = path.name,
                        parentId = path.id,
                        parentName = ""
                    )
                }
                _rootPath.postValue(pathList)
            }
        }
    }

    fun getPathByIdParent(id: Int) {
        viewModelScope.launch {
            appDatabase.pathDao().getPathsByParentId(id).collect { childPaths ->
                val updatedMap = _path.value.orEmpty().toMutableMap() // Simpan data lama
                updatedMap[id] = childPaths.map { path ->
                    PathData(
                        name = path.name,
                        parentId = path.id,
                        parentName = ""
                    )
                }
                _path.postValue(updatedMap)
            }
        }
    }
}