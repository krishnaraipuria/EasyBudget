package viewmodel

import android.content.Context
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Dao
import com.example.easybudget.R
import data.ExpenseDataBase
import data.dao.ExpenseDao
import data.modal.ExpenseEntity

class HomeViewModel(dao: ExpenseDao) : ViewModel(){
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
    fun getitemlogo(item: ExpenseEntity): Int {
        if(item.category == "Income"){
            return R.drawable.income2
        } else if(item.category == "Starbucks"){
            return R.drawable.ic_starbucks
        }
        return  R.drawable.seaching
    }
}

class HomeViewModelFactory(private val context: Context): ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modelClass: Class<T>): T{
        if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
            val dao = ExpenseDataBase.getDatabase(context).ExpenseDao()
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}