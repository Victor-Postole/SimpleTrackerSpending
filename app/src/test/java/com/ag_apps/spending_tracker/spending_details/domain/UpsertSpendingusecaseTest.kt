package com.ag_apps.spending_tracker.spending_details.domain

import com.ag_apps.spending_tracker.core.domain.models.Spending
import com.ag_apps.spending_tracker.core.domain.repositories.LocalSpendingDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*
import java.time.ZonedDateTime
import java.time.ZoneOffset
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UpsertSpendingusecaseTest {

    private lateinit var spendingDataSource: LocalSpendingDataSource
    private lateinit var useCase: UpsertSpendingusecase

    private val now = ZonedDateTime.now(ZoneOffset.UTC)

    @Before
    fun setup() {
        spendingDataSource = mock()
        useCase = UpsertSpendingusecase(spendingDataSource)
    }

    @Test
    fun `invoke with valid spending`() = runBlocking {
        val spending = Spending(1, "Apples", 10.0, 2.0, 1.0, now)

        val result = useCase.invoke(spending)

        assertTrue(result)
        verify(spendingDataSource).upsertSpending(spending)
    }

    @Test
    fun `invoke with blank spending name`() = runBlocking {
        val spending = Spending(1, "", 10.0, 2.0, 1.0, now)

        val result = useCase.invoke(spending)

        assertFalse(result)
        verify(spendingDataSource, never()).upsertSpending(any())
    }

    @Test
    fun `invoke with zero spending price`() = runBlocking {
        val spending = Spending(1, "Apples", 0.0, 2.0, 1.0, now)

        val result = useCase.invoke(spending)

        assertFalse(result)
        verify(spendingDataSource, never()).upsertSpending(any())
    }

    @Test
    fun `invoke with negative spending price`() = runBlocking {
        val spending = Spending(1, "Apples", -5.0, 2.0, 1.0, now)

        val result = useCase.invoke(spending)

        assertFalse(result)
        verify(spendingDataSource, never()).upsertSpending(any())
    }

    @Test
    fun `invoke with negative spending kilograms`() = runBlocking {
        val spending = Spending(1, "Apples", 10.0, -1.0, 1.0, now)

        val result = useCase.invoke(spending)

        assertFalse(result)
        verify(spendingDataSource, never()).upsertSpending(any())
    }

    @Test
    fun `invoke with negative spending quantity`() = runBlocking {
        val spending = Spending(1, "Apples", 10.0, 2.0, -1.0, now)

        val result = useCase.invoke(spending)

        assertFalse(result)
        verify(spendingDataSource, never()).upsertSpending(any())
    }

    @Test
    fun `invoke with zero spending kilograms`() = runBlocking {
        val spending = Spending(1, "Apples", 10.0, 0.0, 1.0, now)

        val result = useCase.invoke(spending)

        assertTrue(result)
        verify(spendingDataSource).upsertSpending(spending)
    }

    @Test
    fun `invoke with zero spending quantity`() = runBlocking {
        val spending = Spending(1, "Apples", 10.0, 2.0, 0.0, now)

        val result = useCase.invoke(spending)

        assertTrue(result)
        verify(spendingDataSource).upsertSpending(spending)
    }

    @Test
    fun `invoke with spending name containing only whitespace`() = runBlocking {
        val spending = Spending(1, "   ", 10.0, 2.0, 1.0, now)

        val result = useCase.invoke(spending)

        assertFalse(result)
        verify(spendingDataSource, never()).upsertSpending(any())
    }

    @Test
    fun `invoke checks spendingDataSource upsertSpending not called on failure`() = runBlocking {
        val spending = Spending(1, "", 0.0, -1.0, -1.0, now)

        val result = useCase.invoke(spending)

        assertFalse(result)
        verify(spendingDataSource, never()).upsertSpending(any())
    }

    @Test
    fun `getSpendingById with existing ID`(): Unit = runBlocking {
        val expected = Spending(1, "Milk", 5.0, 1.0, 1.0, now)
        whenever(spendingDataSource.getSpending(1)).thenReturn(expected)

        val result = useCase.getSpendingById(1)

        assertEquals(expected, result)
        verify(spendingDataSource).getSpending(1)
    }

    @Test(expected = Exception::class)
    fun `getSpendingById with non existing ID`(): Unit = runBlocking {
        whenever(spendingDataSource.getSpending(999)).thenThrow(RuntimeException("Not found"))

        useCase.getSpendingById(999)
    }

    @Test
    fun `getSpendingById with negative ID`(): Unit = runBlocking {
        whenever(spendingDataSource.getSpending(-1)).thenThrow(IllegalArgumentException("Invalid ID"))

        try {
            useCase.getSpendingById(-1)
        } catch (e: IllegalArgumentException) {
            assertEquals("Invalid ID", e.message)
        }
    }

    @Test
    fun `getSpendingById with zero ID`() = runBlocking {
        val spending = Spending(0, "Free sample", 0.1, 0.0, 0.0, now)
        whenever(spendingDataSource.getSpending(0)).thenReturn(spending)

        val result = useCase.getSpendingById(0)

        assertEquals(spending, result)
    }

    @Test
    fun `invoke with spending name at max length`() = runBlocking {
        val longName = "a".repeat(255)
        val spending = Spending(1, longName, 5.0, 1.0, 1.0, now)

        val result = useCase.invoke(spending)

        assertTrue(result)
        verify(spendingDataSource).upsertSpending(spending)
    }

    @Test
    fun `invoke with spending price at max value`() = runBlocking {
        val spending = Spending(1, "Gold", Double.MAX_VALUE, 1.0, 1.0, now)

        val result = useCase.invoke(spending)

        assertTrue(result)
        verify(spendingDataSource).upsertSpending(spending)
    }

    @Test
    fun `invoke with spending kilograms at max value`() = runBlocking {
        val spending = Spending(1, "Wheat", 10.0, Double.MAX_VALUE, 1.0, now)

        val result = useCase.invoke(spending)

        assertTrue(result)
        verify(spendingDataSource).upsertSpending(spending)
    }

    @Test
    fun `invoke with spending quantity at max value`() = runBlocking {
        val spending = Spending(1, "Rice", 10.0, 2.0, Double.MAX_VALUE, now)

        val result = useCase.invoke(spending)

        assertTrue(result)
        verify(spendingDataSource).upsertSpending(spending)
    }

    @Test(expected = Exception::class)
    fun `invoke when spendingDataSource throws exception during upsert`(): Unit = runBlocking {
        val spending = Spending(1, "Apples", 10.0, 2.0, 1.0, now)
        whenever(spendingDataSource.upsertSpending(spending)).thenThrow(RuntimeException("DB error"))

        useCase.invoke(spending)
    }

    @Test(expected = Exception::class)
    fun `getSpendingById when spendingDataSource throws exception during get`(): Unit = runBlocking {
        whenever(spendingDataSource.getSpending(1)).thenThrow(RuntimeException("DB failure"))

        useCase.getSpendingById(1)
    }
}