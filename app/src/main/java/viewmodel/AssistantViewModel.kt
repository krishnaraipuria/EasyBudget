package viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import data.ExpenseDataBase
import data.files.AssistantClient
import data.files.AssistantRepository
import kotlinx.coroutines.launch

data class Message(
    val content: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
)

class AssistantViewModel(
    private val repository: AssistantRepository,
) : ViewModel() {
    private val _messages = mutableStateListOf<Message>()
    val messages: List<Message> get() = _messages

    var isLoading by mutableStateOf(value = false)
        private set

    init {
        _messages.add(
            Message(
                content = "Hello! I'm your AI financial assistant. Ask me anything about your balance, expenses, or income!",
                isUser = false,
            ),
        )
    }

    fun sendMessage(text: String) {
        val trimmed = text.trim()
        if (trimmed.isBlank() || isLoading) return

        _messages.add(Message(trimmed, isUser = true))
        isLoading = true

        viewModelScope.launch {
            try {
                val resp = repository.ask(trimmed)
                var answerText = resp.answer
                if (answerText.isNullOrBlank()) {
                    answerText = "I couldn't process your question right now. Please try again."
                }
                if (!resp.sources.isNullOrEmpty()) {
                    answerText += "\n\nSources:\n" + resp.sources.joinToString("\n") { "• $it" }
                }
                _messages.add(Message(answerText, isUser = false))
            } catch (e: Exception) {
                _messages.add(
                    Message(content = "An error occurred: ${e.message ?: "Unknown error"}", isUser = false),
                )
            } finally {
                isLoading = false
            }
        }
    }
}

class AssistantViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AssistantViewModel::class.java)) {
            val db = ExpenseDataBase.getDatabase(context.applicationContext)
            val api = AssistantClient.createApi()
            val repo = AssistantRepository(api, db)
            @Suppress("UNCHECKED_CAST")
            return AssistantViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
