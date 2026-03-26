package com.example.basiccallingapp.Viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.basiccallingapp.Repository.CallLogRepository
import com.example.basiccallingapp.model.CallLogEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CallLogViewModel(private val repository : CallLogRepository) : ViewModel() {
    private val _callLogs = MutableStateFlow<List<CallLogEntry>>(emptyList())
    val callLogs = _callLogs.asStateFlow()

    fun loadCallLogs() {
        viewModelScope.launch(Dispatchers.IO) {
            val logs = repository.getCallLogs()
            _callLogs.value = logs
        }
    }

    // Manual Factory because we aren't using Hilt
    class Factory(private val repository: CallLogRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CallLogViewModel(repository) as T
        }
    }
}