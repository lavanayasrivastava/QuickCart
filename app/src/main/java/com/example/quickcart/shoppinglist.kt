package com.example.quickcart

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun Shoppinglist(modifier: Modifier) {
    var sItems by remember{ mutableStateOf(listOf<shoppingItem>())}
    var showDialog by remember{ mutableStateOf( false) }
    var itemName by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("1") }
    var unit by remember { mutableStateOf("Qty") }
    var qexpanded by remember { mutableStateOf(false) }
    var uexpanded by remember { mutableStateOf(false) }
    var inputcustom by remember { mutableStateOf("") }


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {

        Button(
            onClick =  {
                showDialog = true
                itemName=" "
                quantity=" 1 "
                inputcustom=" "
                },
            Modifier
                .align(Alignment.CenterHorizontally)
                .padding(50.dp)
        ) {
            Text("Add Item")
        }
        LazyColumn (
            Modifier
                .fillMaxSize()
                .padding(16.dp)){
            items(sItems){

                    itemVar ->
                if(itemVar.isEditing){
                    shoppingListEditor(itemVar,
                        onEditComplete = { editedName, editedQuantity, editedUnit ->
                            // why it.copy and not itemVar.copy
                            sItems = sItems.map { (it.copy(isEditing = false)) }
                            val editedEntry = sItems.find { itemVar.id == it.id  }
                            // whats the importance of ? when using let
                            editedEntry?.let {
                                // why it. and not itemVar.
                                it.itemName = editedName
                                it.quantity = editedQuantity
                                it.unit = editedUnit
                            }
                        }
                    )
                }else{
                    ShoppingDisplay(itemVar,
                        onEditClick =  {
                            // why it.copy and not itemVar.copy
                            sItems = sItems.map{it.copy(isEditing = itemVar.id == it.id)}
                        },
                        onDeleteClick = {
                            // how can you delete object from a list
                            sItems = sItems - itemVar})
                }
                Spacer(modifier=Modifier.height(8.dp))
            }

    }}
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {showDialog=false},
            confirmButton = {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                )
                {
                    Button(onClick = {
                        if(itemName.isNotBlank()){
                       var finalQuantity=if(quantity=="custom")
                       {
                           inputcustom.toIntOrNull()?:1
                       }
                       else{quantity.toIntOrNull() ?: 1}
                           var newItem = shoppingItem(
                                    id = sItems.size+1,
                                    itemName = itemName,
                                    unit=unit,
                                    quantity = finalQuantity
                                )
                                sItems = sItems + newItem
                            }

                        showDialog=false
                        unit="Qty"
                        }

                    ){
                        Text("ADD")
                    }

                    Button(onClick = {showDialog=false}) {
                        Text("CANCEL")
                    }
                }

            },
            title = {Text("ADD SHOPPING ITEMS")},
            text = {
                Column {
                    OutlinedTextField(
                        value=itemName,
                        onValueChange = {itemName = it},
                        modifier = Modifier.fillMaxWidth(),
                        label = {Text("Enter Item Name")},
                        singleLine = true
                    )
            Row(modifier = Modifier.fillMaxWidth(),
                Arrangement.SpaceBetween){
                Box{
                    Button(onClick = {qexpanded=true}) {
                        Text(quantity)
                        Icon(
                            Icons.Default.ArrowDropDown,
                            contentDescription = "dropdown arrow in use"
                        )
                    }
                DropdownMenu(expanded = qexpanded,onDismissRequest = {qexpanded=false} ) {
                    DropdownMenuItem(text={Text("custom")},
                        onClick = {
                            qexpanded=false
                            quantity="custom"


                        })
                    DropdownMenuItem(text={Text("1")},
                        onClick = {
                            qexpanded=false
                            quantity="1"
                            inputcustom=" "
                        })
                    DropdownMenuItem(text={Text("2")},
                        onClick = {
                            qexpanded=false
                            quantity="2"
                            inputcustom=" "
                        })
                    DropdownMenuItem(text={Text("3")},
                        onClick = {
                            qexpanded=false
                            quantity="3"
                            inputcustom=" "
                        })
                }}



                   Box{ Button(onClick = {uexpanded=true}) {
                        Text(unit)
                        Icon(
                            Icons.Default.ArrowDropDown,
                            contentDescription = "dropdown arrow in use"
                        )
                    }
                       DropdownMenu(expanded = uexpanded,onDismissRequest = {uexpanded=false} ) {
                           DropdownMenuItem(text={Text("Qty")},
                               onClick = {
                                   uexpanded=false
                                   unit="Qty"

                               })
                           DropdownMenuItem(text={Text("Kg")},
                               onClick = {
                                   uexpanded=false
                                   unit="kg"
                               })
                           DropdownMenuItem(text={Text("gram")},
                               onClick = {
                                   uexpanded=false
                                   unit="gram"
                               })
                           DropdownMenuItem(text={Text("litre")},
                               onClick = {
                                   uexpanded=false
                                   unit="litre"
                               })


                }}
            }
                    if(quantity=="custom"){
                        Text("ENTER CUSTOM AMOUNT")
                        OutlinedTextField(
                            value=inputcustom,
                            label = {Text("Enter Quantity")},
                            onValueChange = {inputcustom= it},
                            singleLine = true


                            )

                    }

                }
            }
        )


    }


}
data class shoppingItem(
    var id: Int,
    var itemName: String,
    var quantity: Int,
    var unit: String,
    var isEditing: Boolean = false,

)

@Composable
fun ShoppingDisplay(
    obj: shoppingItem,
    onEditClick: ()-> Unit,
    onDeleteClick: ()->Unit
){
    Row (modifier = Modifier
        .fillMaxWidth()
        .border(
            border = BorderStroke(2.dp, color = Color.Blue),
            shape = RoundedCornerShape(40)
        ),
        Arrangement.SpaceEvenly)
    {
        Text(obj.itemName, fontWeight = FontWeight.Bold, modifier = Modifier.padding(8.dp))
        Text(obj.quantity.toString()+" "+(obj.unit),fontWeight = FontWeight.Bold, modifier = Modifier.padding(8.dp))
        IconButton( onClick = onEditClick){
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null
            )
        }
        IconButton( onClick = onDeleteClick){
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null
            )
        }

    }
}

@Composable
fun shoppingListEditor(item:shoppingItem, onEditComplete:(String, Int, String)->Unit){
    var editItem by remember { mutableStateOf(item.itemName) }
    var editQuantity by remember { mutableStateOf(item.quantity.toString()) }
    var editUnit by remember { mutableStateOf(item.unit)}
    var isEditing by remember{ mutableStateOf(item.isEditing) }

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
        Arrangement.SpaceEvenly) {
        Column() {
            BasicTextField(
                value = editItem,
                onValueChange = { editItem = it },
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
                    .border(BorderStroke(2.dp, Color.Red))
            )
            Row{
                BasicTextField(
                    value = editQuantity,
                    onValueChange = { editQuantity = it },
                    singleLine = true,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(8.dp)
                        .border(BorderStroke(2.dp, Color.Red))
                )
                BasicTextField(
                    value = editUnit,
                    onValueChange = { editUnit = it },
                    singleLine = true,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(8.dp)
                        .border(BorderStroke(1.dp, Color.Red))
                )
            }

            Button(onClick = {
                isEditing=false
                onEditComplete(editItem,editQuantity.toIntOrNull()?: 1,editUnit)
            }) {
                Text("Save")
            }


        }
    }
}




