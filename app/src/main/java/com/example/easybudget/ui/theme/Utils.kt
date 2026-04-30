package com.example.easybudget.ui.theme

import com.example.easybudget.R
import data.modal.ExpenseEntity
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {
    fun formatLongToReadable(dateInMillis: Long): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return formatter.format(Date(dateInMillis))
    }

    fun formatLongChart(dateInMillis: Long): String {
        val formatter = SimpleDateFormat("dd-MMM", Locale.getDefault())
        return formatter.format(Date(dateInMillis))
    }

    fun formatToDecimal(value: Double): String {
        return String.format(Locale.getDefault(), "%.2f", value)
    }

    fun getMillsFromDate(dateString: String?): Long {
        return getMilliFromDate(dateString)
    }

    fun getMilliFromDate(dateString: String?): Long {
        var date = Date()
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        try {
            dateString?.let {
                date = formatter.parse(it) ?: Date()
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date.time
    }
    fun getitemlogo(item: ExpenseEntity): Int {
        if(item.category == "Income"){
            return R.drawable.income2
        } else if(item.category == "Starbucks"){
            return R.drawable.ic_starbucks
        }
        return  R.drawable.seaching
    }
}
