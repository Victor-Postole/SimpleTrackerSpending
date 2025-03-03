package com.ag_apps.spending_tracker.spending_overview.presentation

import androidx.annotation.OptIn
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.ag_apps.spending_tracker.core.domain.repositories.CoreRepository
import com.ag_apps.spending_tracker.core.domain.repositories.LocalSpendingDataSource
import com.ag_apps.spending_tracker.core.domain.models.Spending
import com.ag_apps.spending_tracker.spending_overview.domain.UpsertGetImages
import com.ag_apps.spending_tracker.spending_overview.presentation.util.randomColor
import kotlinx.coroutines.launch
import java.time.ZonedDateTime


class SpendingOverviewViewModel(
    private val spendingDataSource: LocalSpendingDataSource,
    private val coreRepository: CoreRepository,
    private val upsertGetImages: UpsertGetImages
) : ViewModel() {

    var state by mutableStateOf(SpendingOverviewState())
        private set

    init {


        viewModelScope.launch {
            val getAllSpending = spendingDataSource.getAllSpendings()
            val concatenatedString = getAllSpending.joinToString(separator = "+") { it.name }
            val images = upsertGetImages.getImagesByName(concatenatedString)

            state = state.copy(
                urlImages = images
            )
        }
    }

    @OptIn(UnstableApi::class)
    fun onAction(action: SpendingOverviewAction) {
        when (action) {
            SpendingOverviewAction.LoadSpendingOverviewAndBalance -> {
                loadSpendingListAndBalance()
            }

            is SpendingOverviewAction.OnDateChange -> {
                val newDate = state.datesList[action.newDate]
                viewModelScope.launch {
                    state = state.copy(
                        pickedDate = newDate,
                        spendingList = getSpendingListByDate(newDate)
                    )
                }
            }

            is SpendingOverviewAction.OnDeleteSpending -> {
                viewModelScope.launch {
                    spendingDataSource.deleteSpending(action.spendingId)
                    state = state.copy(
                        spendingList = getSpendingListByDate(state.pickedDate),
                        datesList = spendingDataSource.getAllDates(),
                        balance = coreRepository.getBalance() - spendingDataSource.getSpendBalance(),
                    )
                }
            }
        }
    }

    private fun loadSpendingListAndBalance() {
        viewModelScope.launch {
            println("loadSpendingListAndBalance: ${coreRepository.getBalance()}")
            val allDates = spendingDataSource.getAllDates()

            state = state.copy(
                spendingList = getSpendingListByDate(
                    allDates.lastOrNull() ?: ZonedDateTime.now()
                ),
                balance = coreRepository.getBalance() - spendingDataSource.getSpendBalance(),
                pickedDate = allDates.lastOrNull() ?: ZonedDateTime.now(),
                datesList = allDates.reversed()
            )
        }
    }

    private suspend fun getSpendingListByDate(date: ZonedDateTime): List<Spending> {
        return spendingDataSource
            .getSpendingsByDate(date)
            .reversed()
            .map { it.copy(color = randomColor()) }
    }

}