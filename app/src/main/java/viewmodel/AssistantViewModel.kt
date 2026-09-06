package viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class Message(
    val content: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

class AssistantViewModel : ViewModel() {
    private val _messages = mutableStateListOf<Message>()
    val messages: List<Message> get() = _messages

    init {
        _messages.add(Message("Hello! I'm your AI financial assistant. How can I help you today?", false))
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return
        
        _messages.add(Message(text, true))
        
        viewModelScope.launch {
            // just for now:)
            delay(1000)
            val response = generateMockResponse(text)
            _messages.add(Message(response, false))
        }
    }

    private fun generateMockResponse(query: String): String {
        return when {
            query.contains("balance", ignoreCase = true) -> "Your current balance is ₹ 5,420.00."
            query.contains("spend", ignoreCase = true) -> "You spent ₹ 1,200.00 on Groceries this week."
            else -> "I'm analyzing your spending patterns. Could you please provide more details?"
        }
    }
}

class AssistantViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AssistantViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AssistantViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
