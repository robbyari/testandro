package com.robbyari.testandroid.ui.test2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robbyari.testandroid.core.AppDatabase
import com.robbyari.testandroid.core.Transaction
import com.robbyari.testandroid.model.TransactionData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class Test2ViewModel @Inject constructor(
    private val appDatabase: AppDatabase
) : ViewModel() {
    private val _transaction = MutableLiveData<List<TransactionData>>()
    val transaction: LiveData<List<TransactionData>> = _transaction

    fun getAllTransaction() {
        viewModelScope.launch {
            appDatabase.transactionDao().getAllTransactionNo().flowOn(Dispatchers.IO)
                .collect { data ->
                    _transaction.value = data.map { transaction ->
                        TransactionData(
                            noTransaction = transaction.noTransaction,
                            createdAt = transaction.createdAt
                        )
                    }
                }
        }
    }

    fun saveTransaction(date: String) {
        viewModelScope.launch {
            val yearMonth = date.substring(0, 7)
            val count = appDatabase.transactionDao().getTransactionCountForMonth(yearMonth) + 1
            val transactionNo = generateTransactionNo(yearMonth, count)

            val transaction = Transaction(
                noTransaction = transactionNo,
                createdAt = date
            )
            appDatabase.transactionDao().saveTransactionNo(transaction)
            getAllTransaction()
        }
    }

    private fun generateTransactionNo(yearMonth: String, count: Int): String {
        val year = yearMonth.substring(2, 4)
        val month = yearMonth.substring(5, 7)
        return "MSK/$year$month/${String.format(Locale.US, "%04d", count)}"
    }
}