package com.ag_apps.spending_tracker.spending_overview.di

import com.ag_apps.spending_tracker.spending_overview.domain.UpsertGetImages
import com.ag_apps.spending_tracker.spending_overview.presentation.SpendingOverviewViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val spendingOverviewModule = module {

    single { UpsertGetImages(get()) }

    viewModel { SpendingOverviewViewModel(get(), get(), get())}
}