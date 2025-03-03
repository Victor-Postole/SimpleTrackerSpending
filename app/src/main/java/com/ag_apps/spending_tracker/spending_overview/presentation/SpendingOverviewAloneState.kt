package com.ag_apps.spending_tracker.spending_overview.presentation

import java.time.ZonedDateTime

data class SpendingOverviewAloneState(
    val spendingId: Int? = 0,
    val name: String = "",
    val price: Double = 0.0,
    val kilograms: Double = 0.0,
    val quantity: Double = 0.0,
    val dateTimeUtc: ZonedDateTime? = ZonedDateTime.now(),
    val color: Int = 0
)
