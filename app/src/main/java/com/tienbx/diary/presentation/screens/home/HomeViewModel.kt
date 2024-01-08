package com.tienbx.diary.presentation.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tienbx.diary.data.repository.Diaries
import com.tienbx.diary.data.repository.MongoDB
import com.tienbx.diary.util.RequestState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    var diaries: MutableState<Diaries> = mutableStateOf(RequestState.Idle)

    init {
        observeAllDiaries()
    }

    private fun observeAllDiaries() = viewModelScope.launch {
        MongoDB.getAllDiaries().collectLatest { result ->
            diaries.value = result
        }
    }
}
