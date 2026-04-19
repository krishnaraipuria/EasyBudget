package com.example.easybudget.ui.theme

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {
    fun fromatlongtoreadable(dateInMillis: Long): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return formatter.format(Date(dateInMillis))
    }
}