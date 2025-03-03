package com.ag_apps.spending_tracker.spending_balance.di

import com.ag_apps.spending_tracker.spending_balance.presentation.BalanceViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val balanceModule = module {
    viewModel { BalanceViewModel(get()) }
}