package com.robbyari.testandroid.ui.test1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robbyari.testandroid.core.AppDatabase
import com.robbyari.testandroid.core.User
import com.robbyari.testandroid.model.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class Test1ViewModel @Inject constructor(
    private val appDatabase: AppDatabase
) : ViewModel() {
    private val _user = MutableLiveData<List<UserData>>()
    val user: LiveData<List<UserData>> = _user

    fun saveUser(user: UserData) {
        viewModelScope.launch {
            val userEntity = User(
                userName = user.userName
            )
            appDatabase.userDao().saveUser(userEntity)
        }
    }

    fun getAllUsers() {
        viewModelScope.launch {
            appDatabase.userDao().getAllUsers().flowOn(Dispatchers.IO).collect { data ->
                _user.value = data.map { user ->
                    UserData(
                        userName = user.userName
                    )
                }
            }
        }
    }
}