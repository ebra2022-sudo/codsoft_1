package com.example.codsofttodo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode

import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun GetStartScreen(modifier: Modifier = Modifier, navController: NavController) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.White,
                    Color.White,
                    Color.Blue
                )
            )
        )
        .padding(20.dp)
        ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(painter = painterResource(id = R.drawable.baseline_subject_24), contentDescription = null, tint = Color.Black)
            Text(text =" Welcome to ", fontFamily = FontFamily.Serif, fontSize = 22.sp, fontWeight = FontWeight.W600, color = Color.Blue)
            Text(text ="the schedule", fontFamily = FontFamily.Serif, fontSize = 22.sp, fontWeight = FontWeight.W600, color = Color.Green)
        }
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .then(modifier),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally){
            Image(painter = painterResource(id = R.drawable.getstarted) ,
                contentDescription = null, contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(50.dp))
            Text(text = "Manage your \nEveryday task lists", color = Color.Black,
                textAlign = TextAlign.Center, fontWeight = FontWeight.W500, fontStyle = FontStyle.Italic, fontSize = 20.sp)
            Text(text = "Begin organizing your tasks effortlessly \nwith our intuitive to-do list app." +
                    " \nSimply download, prioritize, and conquer \nyour day with ease.",
                color = Color.LightGray,
                textAlign = TextAlign.Center)
            Box(modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.tertiary
                        )
                    )
                ).clickable { navController.navigate("new task")}
            ) {
                Text(text = "Get Started", modifier = Modifier.padding(15.dp),
                    color = MaterialTheme.colorScheme.onPrimary)
            }

        }
    }
}




@Preview(showBackground = true)
@Composable
private fun GetScreenPreview() {
    //GetStartScreen()
}