package viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.easybudget.ui.theme.Utils
import com.github.mikephil.charting.data.Entry
import data.ExpenseDataBase
import data.dao.ExpenseDao


import data.modal.ExpenseSummary

class StatsViewModel(dao: ExpenseDao) : ViewModel() {
    val entries = dao.getAllExpenseByDate()
    val topEntries =dao.getTopExpense()
    fun getEntriesForChart(entries: List<ExpenseSummary>): List<Entry> {
        val list = mutableListOf<Entry>()
        for (entry in entries) {
            list.add(Entry(entry.date.toFloat(), entry.total_amount.toFloat()))
        }
        return list
    }
}

class StatsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatsViewModel::class.java)) {
            val dao = ExpenseDataBase.getDatabase(context).ExpenseDao()
            @Suppress("UNCHECKED_CAST")
            return StatsViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
