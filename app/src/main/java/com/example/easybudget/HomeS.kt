package com.example.easybudget

import android.graphics.drawable.Icon
import android.icu.text.CaseMap
import android.media.Image
import android.text.Layout
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import text.ExpenseTestView
import java.sql.Date
import java.time.temporal.TemporalAmount

@Composable
fun HomeS(){
    Surface(
        modifier= Modifier.fillMaxSize()
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (nameRow, list , card, topBar) = createRefs()
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
            CardThing(modifier = Modifier
                .constrainAs(card){
                    top.linkTo(nameRow.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
            ExpenseList(modifier = Modifier.fillMaxWidth().constrainAs(list){
                top.linkTo(card.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
                height = Dimension.fillToConstraints
            }
            )
        }
    }
}
@Composable
fun CardThing(modifier: Modifier){
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
                    text = "₹ 100,000",
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
                amount = "₹ 10,000",
                image =  R.drawable.income,

            )
            CardRowThing(
                modifier=Modifier.align(Alignment.CenterEnd),
                title = "Expense",
                amount = "₹ 5,565",
                image = R.drawable.expense
            )
        }
    }
}

@Composable
fun ExpenseList(modifier: Modifier){
    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        Box(modifier = Modifier.fillMaxWidth()){
            ExpenseTestView(text = "Recent Transactions", fontSize = 20.sp)
            ExpenseTestView(
                text = "See All",
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
        ExpenseItem(
            title = "Starbucks",
            amount = "- ₹ 100.00",
            icon = R.drawable.ic_starbucks,
            date = "Today",
            color = Color.Red
        )
        ExpenseItem(
            title = "Starbucks",
            amount = "- ₹ 100.00",
            icon = R.drawable.ic_starbucks,
            date = "Today",
            color = Color.Red
        )
        ExpenseItem(
            title = "Starbucks",
            amount = "- ₹ 100.00",
            icon = R.drawable.ic_starbucks,
            date = "Today",
            color = Color.Red
        )
        ExpenseItem(
            title = "Starbucks",
            amount = "- ₹ 100.00",
            icon = R.drawable.ic_starbucks,
            date = "Today",
            color = Color.Red
        )
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
    HomeS()
}