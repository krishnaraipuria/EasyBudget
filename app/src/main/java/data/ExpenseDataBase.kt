package data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import data.dao.ExpenseDao
import data.modal.ExpenseEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [ExpenseEntity::class], version = 1)
abstract class ExpenseDataBase: RoomDatabase(){
    abstract fun ExpenseDao(): ExpenseDao

    companion object{
        const val DATABASE_NAME = "expense_database"

        @JvmStatic
        fun getDatabase(context: Context): ExpenseDataBase{
            return Room.databaseBuilder(
                context,
                ExpenseDataBase::class.java,
                DATABASE_NAME
                    ).addCallback(object : RoomDatabase.Callback(){
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            basicData(context)
                        }
                fun  basicData(context: Context){
                    CoroutineScope(Dispatchers.IO).launch {
                        val dao = getDatabase(context).ExpenseDao()
                        dao.insertExpense(ExpenseEntity(id = 1, title = "Salary", amount = 500.0, date = System.currentTimeMillis(), category = "Income", type = "Income"))
                        dao.insertExpense(ExpenseEntity(id = 2, title = "Food", amount = 200.0, date = System.currentTimeMillis(), category = "Food", type = "Expense"))
                    }
                }
                    })
                .build()
        }
    }
}