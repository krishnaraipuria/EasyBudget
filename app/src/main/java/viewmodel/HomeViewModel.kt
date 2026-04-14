package viewmodel

import androidx.room.Dao
import data.dao.ExpenseDao
import data.modal.ExpenseEntity

class HomeViewModel(dao: ExpenseDao) {
    val expenses = dao.getAllExpense()

    fun getBalance(list: List<ExpenseEntity>): String{
        var total = 0.0
        list.forEach {
            if(it.type=="Income"){
                total+= it.amount
            } else{
                total-=it.amount
            }
        }
        return "₹ ${total}"
    }

    fun getTotalExpense(list: List<ExpenseEntity>): String{
        var total = 0.0
        list.forEach {
            if(it.type=="Expense"){
                total+= it.amount
            }
        }
        return "₹ ${total}"
    }

    fun getTotalIncome(list: List<ExpenseEntity>): String{
        var total = 0.0
        list.forEach {
            if(it.type=="Income"){
                total+= it.amount
            }
        }
        return "₹ ${total}"
    }
}