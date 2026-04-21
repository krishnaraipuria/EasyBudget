package viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import data.ExpenseDataBase
import data.dao.ExpenseDao
import data.modal.ExpenseEntity

class AddTranviewmodel(val dao: ExpenseDao) : ViewModel() {
    suspend fun addExpense(expenseEntity: ExpenseEntity): Boolean{
        return try{
            dao.insertExpense(expenseEntity)
            true
        }catch (ex: Throwable){
            return false;
        }
    }
}
class AddTranviewmodelFactory(private val context: Context): ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modelClass: Class<T>): T{
        if(modelClass.isAssignableFrom(AddTranviewmodel::class.java)){
            val dao = ExpenseDataBase.getDatabase(context).ExpenseDao()
            @Suppress("UNCHECKED_CAST")
            return AddTranviewmodel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}