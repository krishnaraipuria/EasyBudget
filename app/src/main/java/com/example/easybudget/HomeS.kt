package com.example.easybudget

import android.graphics.drawable.Icon
import android.icu.text.CaseMap
import android.media.Image
import android.text.Layout
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import data.modal.ExpenseEntity
import text.ExpenseTestView
import viewmodel.HomeViewModel
import viewmodel.HomeViewModelFactory
import java.sql.Date
import java.time.temporal.TemporalAmount

@Composable
fun HomeS(navController: NavController){
    val viewModel : HomeViewModel = HomeViewModelFactory(LocalContext.current).create(HomeViewModel::class.java)
    Surface(
        modifier= Modifier.fillMaxSize()
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (nameRow, list , card, topBar,add) = createRefs()
            Image(painter = painterResource(id=R.drawable.rectangle_9),
                null,
                modifier = Modifier.constrainAs(topBar){
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
            })
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 64.dp, start = 16.dp,end=16.dp)
                .constrainAs(nameRow){
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
            {
                Column() {
                    ExpenseTestView(text = "Hello!", fontSize = 16.sp, color = Color.White)
                    ExpenseTestView(
                        text = "EasyBudget", fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.notification),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
            val state = viewModel.expenses.collectAsState(initial = emptyList())
            val balance = viewModel.getBalance(state.value)
            val income = viewModel.getTotalIncome(state.value)
            val expense = viewModel.getTotalExpense(state.value)
            CardThing(modifier = Modifier
                .constrainAs(card){
                    top.linkTo(nameRow.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }, balance, income, expense
            )
            ExpenseList(modifier = Modifier.fillMaxWidth().constrainAs(list){
                top.linkTo(card.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
                height = Dimension.fillToConstraints
            }, list = state.value, viewModel
            )
            Image(painter =painterResource(R.drawable.addcash) , null, modifier = Modifier.constrainAs(add){
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)

            }.size(48.dp).clip(CircleShape).clickable{
                navController.navigate("add")
            })
        }
    }
}
@Composable
fun CardThing(modifier: Modifier, balance: String, income: String, expense: String){
    Column(modifier = modifier
        .padding(16.dp)
        .fillMaxWidth()
        .height(200.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(Color(0xFF1B5C58))
        .padding(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
            Column(modifier = Modifier.align(Alignment.CenterStart)) {
                ExpenseTestView(text = "Total Balance", fontSize = 16.sp, color = Color.White)
                ExpenseTestView(
                    text = balance,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Image(
                painter = painterResource(id = R.drawable.dot1),
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
        Box(
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) {
            CardRowThing(
                modifier =Modifier.align(Alignment.CenterStart),
                title = "Income",
                amount = income,
                image =  R.drawable.income,

            )
            Spacer(modifier = Modifier.size(16.dp))
            CardRowThing(
                modifier=Modifier.align(Alignment.CenterEnd),
                title = "Expense",
                amount = expense,
                image = R.drawable.expense
            )
        }
    }
}

@Composable
fun ExpenseList(modifier: Modifier, list: List<ExpenseEntity>, viewModel: HomeViewModel){
    LazyColumn(modifier = modifier.padding(horizontal = 16.dp)) {
        item {
            Box(modifier = Modifier.fillMaxWidth()){
                ExpenseTestView(text = "Recent Transactions", fontSize = 20.sp)
                ExpenseTestView(
                    text = "See All",
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
        }
        items(list){ item->
            ExpenseItem(
                title = item.title,
                amount = item.amount.toString(),
                icon = viewModel.getitemlogo(item),
                date = item.date.toString(),
                color = if(item.type=="Income") Color.Green else Color.Red,
            )
        }
    }
}
@Composable
fun CardRowThing(modifier: Modifier, title: String,amount: String,image: Int){
    Column(modifier = modifier) {
        Row() {
            Image(painter = painterResource(id = image), contentDescription = null)
            Spacer(modifier= Modifier.size(8.dp))
            ExpenseTestView(text = title, fontSize = 16.sp, color = Color.White)
        }
        ExpenseTestView(text = amount, fontSize = 20.sp, color = Color.White)
    }
}

@Composable
fun ExpenseItem(title: String, amount: String,icon: Int, date: String, color: Color){
    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)){
        Row() {
            Image(painter = painterResource(id = icon), contentDescription = null,
                modifier = Modifier.size(50.dp))
            Spacer(modifier = Modifier.size(8.dp))
            Column() {
                ExpenseTestView(text = title, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                ExpenseTestView(text = date, fontSize = 12.sp)
            }
        }
        ExpenseTestView(
            text = amount, fontSize = 20.sp, modifier = Modifier.align(Alignment.CenterEnd), color = color,
            fontWeight = FontWeight.SemiBold
        )
    }
}
@Composable
@Preview(showBackground = true)
fun PreviewHomeS(){
    HomeS(rememberNavController())
}