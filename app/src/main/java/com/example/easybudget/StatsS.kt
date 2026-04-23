package com.example.easybudget

import android.media.Image
import android.os.DropBoxManager
import android.text.Layout
import android.view.LayoutInflater
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import text.ExpenseTestView
import viewmodel.StatsViewModel
import viewmodel.StatsViewModelFactory
import java.security.KeyStore
import kotlin.enums.EnumEntries

@Composable
fun StatsS(navController: NavController){
    Scaffold(topBar = {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(top=16.dp, start = 16.dp, end = 8.dp, bottom = 8.dp)){

            Image(painter = painterResource(R.drawable.back), contentDescription = null
                , modifier = Modifier.align(Alignment.CenterStart),colorFilter = ColorFilter.tint(Color.Black)
                )
            ExpenseTestView(
                text = "Add Expense",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp).align(Alignment.Center)
            )
            Image(painter = painterResource(R.drawable.dot1), contentDescription = null
                , modifier = Modifier.align(Alignment.CenterEnd),
                    colorFilter = ColorFilter.tint(Color.Black)
            )
        }
    }) {
        val viewModel = StatsViewModelFactory(navController.context).create(StatsViewModel::class.java)
        val dataState = viewModel.entries.collectAsState(emptyList())
        Column(modifier = Modifier.padding(it)) {
            val entries = viewModel.getEntriesForChart(dataState.value)
            LineChart(entries = entries)
        }
    }
}

@Composable
fun LineChart(entries: List<Entry>){
    val context = LocalContext.current
    AndroidView(factory = {
        val view = LayoutInflater.from(context).inflate(R.layout.statslinechart,null)
        view
    }, modifier = Modifier){ view->
        val lineChart = view.findViewById<LineChart>(R.id.lineChart)

        val dateSet = LineDataSet(entries, "Expense").apply{
            color = Color.Black.toArgb()
            valueTextColor = Color.Black.toArgb()
            valueTextSize = 12f
        }
        lineChart.data = com.github.mikephil.charting.data.LineData(dateSet)
        lineChart.invalidate()
    }
}

