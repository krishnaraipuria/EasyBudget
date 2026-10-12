package data.modal

data class ExpenseSummary(
    val type: String,
    val date: Long,
    @Suppress("PropertyName")
    val total_amount: Double,
)
