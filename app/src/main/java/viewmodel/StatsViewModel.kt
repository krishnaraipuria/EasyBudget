package viewmodel


import android.content.Context
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Dao
import com.example.easybudget.R
import com.example.easybudget.ui.theme.Utils
import data.ExpenseDataBase
import data.dao.ExpenseDao
import data.modal.ExpenseEntity

class StatsViewModel(dao: ExpenseDao) : ViewModel(){

}

class StatsViewModelFactory(private val context: Context): ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modelClass: Class<T>): T{
        if(modelClass.isAssignableFrom(StatsViewModel::class.java)){
            val dao = ExpenseDataBase.getDatabase(context).ExpenseDao()
            @Suppress("UNCHECKED_CAST")
            return StatsViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}