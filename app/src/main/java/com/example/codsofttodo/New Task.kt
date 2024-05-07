package com.example.codsofttodo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.codsofttodo.ui.theme.CodSoftTodoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTaskScreen(navController: NavController) {
    Scaffold(
        topBar = {
        TopAppBar(
            title = {Text("New Task", fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold)},
            navigationIcon = {
                IconButton(onClick = { navController.navigate("todo list screen") }) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary)
            )
                 },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("new task")
                }
            ) {
                Icon(imageVector = Icons.Default.Check, contentDescription = null)
            }
        }) {
        var text1 by remember {
            mutableStateOf("")
        }
        Column(modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .padding(it)
            .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(40.dp)) {
            Column {
                Text(text = "What is to be done?", fontWeight = FontWeight.Medium)
                TextField(value = text1 , onValueChange = {new: String -> text1 = new} ,
                     modifier = Modifier.fillMaxWidth(),
                    trailingIcon =  {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(painter = painterResource(id = R.drawable.microphone_settings), contentDescription = null)
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,


                    ),
                    label = { Text(text = "Enter Task Here", modifier = Modifier.fillMaxWidth())}
                )

            }
            Column {
                Text(text = "Due date",fontWeight = FontWeight.Medium)
                TextField(value = text1 , onValueChange = {new: String -> text1 = new} ,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon =  {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(painter = painterResource(id = R.drawable.calendar), contentDescription = null)
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,


                        ),
                    label = { Text(text = "Set the date here", modifier = Modifier.fillMaxWidth())}
                )
                Text(text = "Notification",textDecoration = TextDecoration.Underline, color = Color.Magenta)
                Text(text = "No notification if date not set")

            }
            Column {
                Text(text = "Add to List", fontWeight = FontWeight.Medium)
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                    Text(text = "Default", modifier = Modifier.weight(4f))
                    IconButton(onClick = { /*TODO*/ }, modifier = Modifier.weight(1f)) {
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                    IconButton(onClick = { /*TODO*/ }, modifier = Modifier.weight(1f)) {
                        Icon(painter = painterResource(id = R.drawable.format_list_bulleted), contentDescription = null)
                    }
                }
            }
        }
    }
}



