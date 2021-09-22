package com.github.vrvs.githubapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn

abstract class BaseViewModel<S, I, SE>(
    initialState: S
) : ViewModel() {

    internal val internalState = MutableStateFlow(initialState)
    val state: StateFlow<S> = internalState

    val interpreter: MutableSharedFlow<I> = MutableSharedFlow()

    internal val internalSideEffect = Channel<SE>()
    val sideEffect: Flow<SE> = internalSideEffect.receiveAsFlow()
}