package com.ag_apps.spending_tracker.core.domain.repositories

import com.ag_apps.spending_tracker.core.domain.models.Spending
import java.time.ZonedDateTime


interface LocalSpendingDataSource {

    suspend fun getAllSpendings(): List<Spending>

    suspend fun getSpendingsByDate(dateTimeUtc: ZonedDateTime): List<Spending>

    suspend fun getAllDates(): List<ZonedDateTime>

    suspend fun getSpending(id: Int): Spending

    suspend fun upsertSpending(spending: Spending)

    suspend fun getSpendBalance(): Double

    suspend fun deleteSpending(id: Int)
}