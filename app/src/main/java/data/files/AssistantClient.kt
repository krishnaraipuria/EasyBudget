package data.files

import android.database.Cursor
import androidx.sqlite.db.SimpleSQLiteQuery
import com.google.gson.annotations.SerializedName
import data.ExpenseDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.Locale
import java.util.concurrent.TimeUnit

data class ChatReq(val question: String)

data class SqlResultReq(
    @SerializedName("thread_id") val threadId: String,
    val rows: List<Map<String, Any?>>,
    val error: String?,
)

data class ChatResp(
    val status: String,
    @SerializedName("thread_id") val threadId: String = "",
    val sql: String? = null,
    val answer: String? = null,
    val route: String? = null,
    val sources: List<String>? = null,
)

interface AssistantApi {
    @POST("chat") suspend fun chat(@Body req: ChatReq): ChatResp
    @POST("chat/sql_result") suspend fun sqlResult(@Body req: SqlResultReq): ChatResp
}

object AssistantClient {
    private const val DEFAULT_BASE_URL = "http://10.0.2.2:8000/"

    fun createApi(baseUrl: String = DEFAULT_BASE_URL): AssistantApi {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AssistantApi::class.java)
    }
}

class AssistantRepository(
    private val api: AssistantApi,
    private val db: ExpenseDataBase,
) {
    suspend fun ask(question: String): ChatResp {
        return try {
            var resp = api.chat(ChatReq(question))
            var steps = 0
            while ((resp.status == "needs_sql") && (steps++ < 4)) {
                val (rows, error) = runReadOnly(resp.sql.orEmpty())
                resp = api.sqlResult(SqlResultReq(resp.threadId, rows, error))
            }
            resp
        } catch (_: Exception) {
            fallbackLocalQuery(question)
        }
    }

    private val forbidden = Regex(
        "\\b(insert|update|delete|drop|alter|create|replace|attach|detach|pragma|vacuum|truncate)\\b",
        RegexOption.IGNORE_CASE,
    )

    suspend fun runReadOnly(rawSql: String): Pair<List<Map<String, Any?>>, String?> =
        withContext(Dispatchers.IO) {
            val sql = rawSql.trim().trimEnd(';')
            if ((!sql.startsWith("select", ignoreCase = true)) || (";" in sql) || forbidden.containsMatchIn(sql)) {
                return@withContext Pair(emptyList(), "Only single SELECT queries are allowed")
            }
            try {
                db.openHelper.readableDatabase.query(SimpleSQLiteQuery(sql)).use { c ->
                    Pair(c.toRows(maxRows = 100), null)
                }
            } catch (e: Exception) {
                Pair(emptyList(), e.message ?: "SQL error")
            }
        }

    private fun Cursor.toRows(maxRows: Int): List<Map<String, Any?>> {
        val out = mutableListOf<Map<String, Any?>>()
        while ((moveToNext()) && (out.size < maxRows)) {
            val row = LinkedHashMap<String, Any?>()
            for (i in 0 until columnCount) {
                row[getColumnName(i)] = when (getType(i)) {
                    Cursor.FIELD_TYPE_INTEGER -> getLong(i)
                    Cursor.FIELD_TYPE_FLOAT -> getDouble(i)
                    Cursor.FIELD_TYPE_STRING -> getString(i)
                    else -> null
                }
            }
            out.add(row)
        }
        return out
    }

    private suspend fun fallbackLocalQuery(q: String): ChatResp {
        val queryLower = q.lowercase()
        val sql = when {
            queryLower.contains("balance") || queryLower.contains("total") -> {
                "SELECT type, SUM(amount) as total FROM expense_table GROUP BY type"
            }
            queryLower.contains("income") -> {
                "SELECT SUM(amount) as total FROM expense_table WHERE type = 'Income'"
            }
            queryLower.contains("expense") || queryLower.contains("spent") || queryLower.contains("expense ") -> {
                "SELECT SUM(amount) as total FROM expense_table WHERE type = 'Expense'"
            }
            queryLower.contains("recent") || queryLower.contains("latest") || queryLower.contains("history") -> {
                "SELECT title, amount, category, type FROM expense_table ORDER BY date DESC LIMIT 5"
            }
            else -> {
                "SELECT title, amount, category, type FROM expense_table ORDER BY amount DESC LIMIT 5"
            }
        }

        val (rows, error) = runReadOnly(sql)
        val answerText = if ((error != null) || rows.isEmpty()) {
            "I couldn't find any transaction records matching your query."
        } else {
            when {
                queryLower.contains("balance") || queryLower.contains("total") -> {
                    var inc = 0.0
                    var exp = 0.0
                    for (r in rows) {
                        val t = r["type"]?.toString()
                        val amt = (r["total"] as? Number)?.toDouble() ?: 0.0
                        if (t.equals("Income", ignoreCase = true)) inc += amt
                        else if (t.equals("Expense", ignoreCase = true)) exp += amt
                    }
                    val bal = inc - exp
                    "Your total balance is ₹ ${String.format(Locale.US, "%.2f", bal)} (Income: ₹ ${String.format(Locale.US, "%.2f", inc)}, Expense: ₹ ${String.format(Locale.US, "%.2f", exp)})."
                }
                queryLower.contains("income") -> {
                    val tot = (rows.firstOrNull()?.get("total") as? Number)?.toDouble() ?: 0.0
                    "Your total income is ₹ ${String.format(Locale.US, "%.2f", tot)}."
                }
                queryLower.contains("expense") || queryLower.contains("spent") || queryLower.contains("expense ") -> {
                    val tot = (rows.firstOrNull()?.get("total") as? Number)?.toDouble() ?: 0.0
                    "Your total expenses are ₹ ${String.format(Locale.US, "%.2f", tot)}."
                }
                else -> {
                    val itemsSummary = rows.joinToString("\n") { row ->
                        "• ${row["title"] ?: "Transaction"}: ₹ ${row["amount"]} (${row["category"] ?: "General"})"
                    }
                    "Here is what I found in your database:\n$itemsSummary"
                }
            }
        }

        return ChatResp(
            status = "done",
            threadId = "local_fallback",
            sql = sql,
            answer = answerText,
        )
    }
}
