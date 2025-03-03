package com.ag_apps.spending_tracker.spending_details.presentation

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ag_apps.spending_tracker.core.domain.models.Spending
import com.ag_apps.spending_tracker.spending_details.domain.UpsertSpendingusecase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.ZonedDateTime


@SuppressLint("UnsafeOptInUsageError")
class SpendingDetailsViewModel(
    private val spendingId: Int,
    private val upsertSpendingUseCase: UpsertSpendingusecase
) : ViewModel() {

    var state by mutableStateOf(SpendingDetailsState())
        private set

    private val _eventChannel = Channel<SpendingDetailsEvent>()
    val event = _eventChannel.receiveAsFlow()

    init {
        // Check if spendingId is valid (not -1), and populate the state if necessary
        if (spendingId > 0) {
            fetchSpendingDetails(spendingId)
        }
    }

    fun onAction(action: SpendingDetailsActions) {
        when (action) {
            is SpendingDetailsActions.UpdateKilograms -> {
                state = state.copy(kilograms = action.newKilograms)
            }
            is SpendingDetailsActions.UpdateName -> {
                state = state.copy(name = action.newName)
            }
            is SpendingDetailsActions.UpdatePrice -> {
                state = state.copy(price = action.newPrice)
            }
            is SpendingDetailsActions.UpdateQuantity -> {
                state = state.copy(quantity = action.newQuantity)
            }

            SpendingDetailsActions.SaveSpending -> {
                viewModelScope.launch {
                    if (saveSpending()) {
                        _eventChannel.send(SpendingDetailsEvent.SaveSuccesed)
                    } else {
                        _eventChannel.send(SpendingDetailsEvent.SaveFailed)
                    }
                }
            }
        }
    }

    private suspend fun saveSpending(): Boolean {
        // Create the Spending object with the current state
        val spending = Spending(
            spendingId = if (spendingId == -1) null else spendingId, // For new entries, passing null for ID
            name = state.name,
            price = state.price,
            kilograms = state.kilograms,
            quantity = state.quantity,
            dateTimeUtc = ZonedDateTime.now()
        )

        // Use the UpsertSpendinguseCase to save or update the spending
        return upsertSpendingUseCase(spending)
    }

    private suspend fun checkIfSpendingIsNotEmpty(spendingId: Int){
        if (spendingId > 0) {
            // Assuming the spending data source (upsertSpendingUseCase) can also retrieve the spending
            // For now, assuming you have logic inside the use case that handles fetching if necessary
            viewModelScope.launch {
                val existingSpending = upsertSpendingUseCase.getSpendingById(spendingId)
                existingSpending?.let {
                    state = state.copy(
                        name = it.name,
                        price = it.price,
                        kilograms = it.kilograms,
                        quantity = it.quantity
                    )
                }
            }
        }
    }

    // Extracted method to fetch spending details
    private fun fetchSpendingDetails(spendingId: Int) {
        viewModelScope.launch {
            val existingSpending = upsertSpendingUseCase.getSpendingById(spendingId)
            existingSpending?.let {
                state = state.copy(
                    name = it.name,
                    price = it.price,
                    kilograms = it.kilograms,
                    quantity = it.quantity
                )
            }
        }
    }


}
