package com.ag_apps.spending_tracker.core.domain.repositories


interface CoreRepository {
    suspend fun updateBalance(balance: Double)
    suspend fun getBalance(): Double
}