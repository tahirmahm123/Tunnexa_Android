package com.tunnexa.android.ui.views.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class HomeVM : ViewModel() {
    var isVisible = mutableStateOf(false)
        private set

    fun show() {
        isVisible.value = true
    }

    fun hide() {
        isVisible.value = false
    }

    fun toggle() {
        isVisible.value = !isVisible.value
    }
}