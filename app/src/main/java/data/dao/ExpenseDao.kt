package data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import data.modal.ExpenseEntity
import data.modal.ExpenseSummary
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expense_table")
    fun getAllExpense(): Flow<List<ExpenseEntity>>

    @Query("SELECT type, date, SUM(amount) AS total_amount FROM expense_table where type = :type GROUP BY type, date ORDER BY date")
    fun getAllExpenseByDate(type: String = "Expense"): Flow<List<ExpenseSummary>>
    @Insert
    suspend fun insertExpense(expenseEntity: ExpenseEntity)

    @Delete
    suspend fun deleteExpense(expenseEntity: ExpenseEntity)

    @Update
    suspend fun updateExpense(expenseEntity: ExpenseEntity)
}