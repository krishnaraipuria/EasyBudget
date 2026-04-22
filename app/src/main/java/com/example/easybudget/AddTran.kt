package com.example.easybudget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.easybudget.ui.theme.Utils
import data.modal.ExpenseEntity
import kotlinx.coroutines.launch
import text.ExpenseTestView
import viewmodel.AddTranviewmodel
import viewmodel.AddTranviewmodelFactory

@Composable
fun AddTran(navController: NavController){
    val viewModel = AddTranviewmodelFactory(LocalContext.current).create(AddTranviewmodel::class.java)
    val coroutineScope = rememberCoroutineScope()
    Surface(modifier= Modifier.fillMaxSize()) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (nameRow, list, card, topBar) = createRefs()
            Image(
                painter = painterResource(id = R.drawable.rectangle_9),
                null,
                modifier = Modifier.constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(top=60.dp, start = 16.dp, end = 16.dp)
                .constrainAs(nameRow){
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }){

                Image(painter = painterResource(R.drawable.back), contentDescription = null
                    , modifier = Modifier.align(Alignment.CenterStart))
                ExpenseTestView(
                    text = "Add Expense",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(16.dp).align(Alignment.Center)
                )
                Image(painter = painterResource(R.drawable.dot1), contentDescription = null
                , modifier = Modifier.align(Alignment.CenterEnd))
            }
            Forms(modifier = Modifier.padding(top = 60.dp).constrainAs(card){
                top.linkTo(nameRow.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },onAddExpenseClick = {
                coroutineScope.launch {
                   if(viewModel.addExpense(it)){
                       navController.popBackStack()
                   }
                }
            }  )
        }
    }
}
@Composable
fun Forms(modifier: Modifier, onAddExpenseClick:(model: ExpenseEntity )-> Unit ){
    val name = remember {
        mutableStateOf("")
    }
    val amount = remember {
        mutableStateOf("")
    }
    val date = remember {
        mutableStateOf(0L)
    }
    val dateDialogVisibility = remember {
        mutableStateOf(false)
    }
    val category= remember {
        mutableStateOf("")
    }
    val type= remember {
        mutableStateOf("")
    }
    Column(modifier = modifier
        .padding(16.dp)
        .fillMaxWidth()
        .shadow(16.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(Color.White)
        .padding(16.dp)
        .verticalScroll(rememberScrollState()))
    {
        ExpenseTestView(text = "Name", fontSize = 14.sp)
        Spacer(modifier = Modifier.size(4.dp))
        OutlinedTextField(value = name.value, onValueChange = {
            name.value = it
        }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.size(8.dp))


        ExpenseTestView(text = "Amount", fontSize = 14.sp)
        Spacer(modifier = Modifier.size(4.dp))
        OutlinedTextField(value = amount.value, onValueChange = {
            amount.value = it
        }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.size(8.dp))

        ExpenseTestView(text = "Date", fontSize = 14.sp)
        Spacer(modifier = Modifier.size(4.dp))
        OutlinedTextField(value = if(date.value==0L)"" else Utils.formatLongToReadable(date.value), onValueChange = {
            {}
        }, modifier = Modifier.fillMaxWidth().clickable{dateDialogVisibility.value=true}, enabled = false, colors = OutlinedTextFieldDefaults.colors(
            disabledBorderColor = Color.Black,
            disabledTextColor = Color.Black
        ))
        Spacer(modifier = Modifier.size(8.dp))


        ExpenseTestView(text = "Category", fontSize = 14.sp)
        Spacer(modifier = Modifier.size(4.dp))
        ExpenseDropDown(listOf("Groceries","Spotify","Mobile recharge / Internet","Salary","Refunds"), onItemSelect = {category.value=it})
        Spacer(modifier = Modifier.size(8.dp))

        ExpenseTestView(text = "Type", fontSize = 14.sp)
        Spacer(modifier = Modifier.size(4.dp))
        ExpenseDropDown(listOf("Expense","Income"), onItemSelect = {type.value=it})
        Spacer(modifier = Modifier.size(8.dp))

        Button(onClick = {
            val model = ExpenseEntity(
                null,name.value,amount.value.toDoubleOrNull()?:0.0, date.value,category.value,type.value
            )
            onAddExpenseClick(model)
        },
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .fillMaxWidth()){
            ExpenseTestView(text = "Add Expense", fontSize = 14.sp, color = Color.White)
        }
    }
    if(dateDialogVisibility.value){
    Expensedatepart(onDateChange = {date.value=it
                                   dateDialogVisibility.value=false}, onDismiss = {
        dateDialogVisibility.value=false
    })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Expensedatepart(
    onDateChange: (date: Long) -> Unit,
    onDismiss: () -> Unit
){
    val datePicker = rememberDatePickerState()
    val selectedDate = datePicker.selectedDateMillis ?: 0L
    DatePickerDialog({onDismiss()},
        { TextButton(onClick = {onDateChange(selectedDate)}){
            ExpenseTestView(text = "OK")
        }
        },
        dismissButton = {
            TextButton(onClick = {onDateChange(selectedDate)}) {
                ExpenseTestView(text = "Cancel")
            }
        }
        ) {
        DatePicker(state = datePicker)

    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseDropDown(listofitems: List<String>, onItemSelect: (item:String) -> Unit){
    val expanded = remember {
        mutableStateOf(false)
    }
    val selectedItem = remember {
        mutableStateOf<String>(listofitems[0])
    }
    ExposedDropdownMenuBox(expanded= expanded.value, onExpandedChange = {expanded.value=it}) {
        TextField(value = selectedItem.value, onValueChange = {}, readOnly = true,
            modifier = Modifier.fillMaxWidth().menuAnchor(type= MenuAnchorType.PrimaryEditable, enabled=true),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value)
            }
            )
        ExposedDropdownMenu(expanded =expanded.value, onDismissRequest = {}) {
            listofitems.forEach {
                DropdownMenuItem(text = {Text(text = it)}, onClick = {selectedItem.value=it
                    onItemSelect(selectedItem.value)
                expanded.value=false
                })
            }
        }
    }
}
@Composable
@Preview(showBackground = true)
fun AddTranPreview(){
    AddTran(rememberNavController())
}