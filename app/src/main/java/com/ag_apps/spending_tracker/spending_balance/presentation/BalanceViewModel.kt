package com.ag_apps.spending_tracker.spending_balance.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ag_apps.spending_tracker.core.domain.repositories.CoreRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


class BalanceViewModel(private val coreRepository: CoreRepository)
    :ViewModel() {
        var state by mutableStateOf(BalanceState())
           private set

        init{
            viewModelScope.launch {
                state = state.copy (
                    balanceState = coreRepository.getBalance()
                )
            }
        }

        fun onAction(action: BalanceAction){
            when(action) {
                is BalanceAction.OnBalanceChanged -> {
                    state = state.copy (
                        balanceState = action.newBalance
                    )
                }

                BalanceAction.OnBalanceSaved -> {
                    viewModelScope.launch {
                        coreRepository.updateBalance(state.balanceState)
                    }
                }
            }
        }
}