package com.ag_apps.spending_tracker.spending_details.presentation

sealed interface SpendingDetailsEvent {
    data object SaveFailed: SpendingDetailsEvent
    data object SaveSuccesed: SpendingDetailsEvent
}