package com.ag_apps.spending_tracker.spending_details.di

import com.ag_apps.spending_tracker.spending_details.domain.UpsertSpendingusecase
import com.ag_apps.spending_tracker.spending_details.presentation.SpendingDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val spendingDetailsModule = module {

    single {UpsertSpendingusecase(get())}

    viewModel {
        SpendingDetailsViewModel(get(),get())
    }
}