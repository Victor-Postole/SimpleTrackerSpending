package com.ag_apps.spending_tracker.spending_overview.presentation

import com.ag_apps.spending_tracker.core.domain.models.Spending
import java.time.ZonedDateTime

data class SpendingOverviewState(
    val spendingId: Int = 0,
    val spendingList: List<Spending> = emptyList(), //one for dateListResources
    val datesList: List<ZonedDateTime> = emptyList(),//one for the ui updates
    val urlImages: List<String>? = emptyList(),
    val balance: Double = 0.0,
    val pickedDate: ZonedDateTime  = ZonedDateTime.now(),
    val isDropdownPickerDropDownMenuVisible: Boolean = false
)
