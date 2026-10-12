# EasyBudget – AI-Powered Expense Tracker

EasyBudget is a privacy-first Android expense tracker that combines expense management, financial analytics, and an AI Financial Assistant powered by FastAPI, LangGraph, and Groq LLMs.

## Tech Stack

**Android Application**
- Kotlin
- Jetpack Compose
- MVVM Architecture
- Room Database (SQLite)
- Retrofit and Kotlin Coroutines

**AI Backend**
- Python and FastAPI
- LangGraph
- Groq LLM integration
- Tavily Web Search API

## Project Structure

**Android Application**
- UI and screen management
- ViewModels and business logic
- Repository and data management
- Room SQLite local database
- AI Assistant API integration

**AI Backend**
- FastAPI REST endpoints: `/chat` and `/chat/sql_result`
- LangGraph agent workflow
- Groq LLM integration
- SQL query generation and result processing

## Engineering Highlights

- **Privacy-First AI Architecture:** Financial records remain in the local Room database, while the AI backend generates SQL queries for on-device execution.
- **Offline Fallback:** A local SQLite query engine supports eligible queries when the AI backend is unavailable.
- **AI-Powered Financial Insights:** Natural-language questions are processed through a FastAPI and LangGraph workflow.
- **Reactive Data Management:** Kotlin Flow and Jetpack Compose keep the UI synchronized with database changes.

## Future Improvements

- Custom budget limits and spending alerts
- Encrypted cloud synchronization
- Voice-based transaction entry
