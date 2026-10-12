package com.example.easybudget.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.easybudget.R
import viewmodel.AssistantViewModel
import viewmodel.AssistantViewModelFactory
import viewmodel.Message

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIS(
    navController: NavController,
    initialQuestion: String? = null,
) {
    val context = LocalContext.current
    val viewModel: AssistantViewModel = viewModel(
        factory = AssistantViewModelFactory(context),
    )
    val messages = viewModel.messages
    val isLoading = viewModel.isLoading
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    var hasAskedInitial by remember { mutableStateOf(value = false) }

    LaunchedEffect(initialQuestion) {
        if (!initialQuestion.isNullOrBlank() && !hasAskedInitial) {
            hasAskedInitial = true
            viewModel.sendMessage(initialQuestion)
        }
    }

    LaunchedEffect(messages.size, isLoading) {
        val totalItems = messages.size + if (isLoading) 1 else 0
        if (totalItems > 0) {
            listState.animateScrollToItem(totalItems - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Easy Budget AI", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1B5C58),
                ),
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF7F9F9)),
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            ) {
                items(messages) { message ->
                    ChatBubble(message)
                }
                if (isLoading) {
                    item {
                        LoadingBubble()
                    }
                }
            }

            ChatInputBar(
                text = inputText,
                isLoading = isLoading,
                onTextChange = { inputText = it },
            ) {
                if (inputText.isNotBlank() && !isLoading) {
                    viewModel.sendMessage(inputText)
                    inputText = ""
                }
            }
        }
    }
}

@Composable
fun ChatBubble(message: Message) {
    val alignment = if (message.isUser) Alignment.CenterEnd else Alignment.CenterStart
    val bubbleColor = if (message.isUser) Color(0xFF1B5C58) else Color.White
    val textColor = if (message.isUser) Color.White else Color(0xFF222222)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        contentAlignment = alignment,
    ) {
        Surface(
            color = bubbleColor,
            shadowElevation = if (message.isUser) 1.dp else 2.dp,
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (message.isUser) 16.dp else 2.dp,
                bottomEnd = if (message.isUser) 2.dp else 16.dp,
            ),
            modifier = Modifier.widthIn(max = 290.dp),
        ) {
            Text(
                text = message.content,
                modifier = Modifier.padding(14.dp),
                color = textColor,
                fontSize = 15.sp,
                lineHeight = 20.sp,
            )
        }
    }
}

@Composable
fun LoadingBubble() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        Surface(
            color = Color.White,
            shadowElevation = 2.dp,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomEnd = 16.dp, bottomStart = 2.dp),
            modifier = Modifier.widthIn(max = 240.dp),
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    color = Color(0xFF1B5C58),
                    strokeWidth = 2.dp,
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Analyzing database...",
                    color = Color.Gray,
                    fontSize = 14.sp,
                )
            }
        }
    }
}

@Composable
fun ChatInputBar(
    text: String,
    isLoading: Boolean,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp,
        color = Color.White,
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.dot1),
                contentDescription = "AI Assistant",
                modifier = Modifier.size(24.dp),
                tint = Color(0xFF1B5C58),
            )

            Spacer(modifier = Modifier.width(12.dp))
            OutlinedTextField(
                value = text,
                onValueChange = onTextChange,
                enabled = !isLoading,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Ask about expenses, balance...") },
                shape = RoundedCornerShape(24.dp),
                maxLines = 3,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions { onSend() },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color(0xFF1B5C58),
                    unfocusedIndicatorColor = Color.LightGray,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                ),
            )
            Spacer(modifier = Modifier.width(12.dp))
            FloatingActionButton(
                onClick = onSend,
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                containerColor = if (isLoading || text.isBlank()) Color.LightGray else Color(0xFF1B5C58),
                contentColor = Color.White,
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
            }
        }
    }
}
