package data.modal

import androidx.room.Entity
import java.time.temporal.TemporalAmount
@Entity(tableName = "expense_table")
data class ExpenseEntity(
    val id: Int,
    val title: String,
    val amount: Double,
    val date: Long,
    val category: String,
    val type: String
)
