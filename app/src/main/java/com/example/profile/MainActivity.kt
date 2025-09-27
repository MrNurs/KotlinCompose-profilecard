package com.example.profile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.example.profile.ui.theme.ProfileTheme
import androidx.compose.animation.animateColorAsState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProfileTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Dilmagambet Nurzhigit",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    var isFollowing by rememberSaveable { mutableStateOf(false)}
    var followText by rememberSaveable { mutableStateOf("Follow") }
    var count by rememberSaveable { mutableStateOf(0) }
    val buttonColor by animateColorAsState(
        targetValue = if (isFollowing) Color.Black else Color.White,
        label = "buttonColor"
    )
    val contentColor by animateColorAsState(
        targetValue = if (isFollowing) Color.White else Color.Black,
        label = "contentColor"
    )
    Box(
        modifier = Modifier
            .fillMaxSize()

            .background(Color(red = 12, green = 12, blue = 12) ),
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier = Modifier
                .height(800.dp)
                .width(350.dp)
                .background(Color(red = 10, green = 10, blue = 10))
            ,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,

            ) {
            Image(
                painter = painterResource(id = R.drawable.literallyme),
                contentDescription = "Avatar",
                modifier= Modifier.size(96.dp).clip(CircleShape)


            )
            Spacer(Modifier.height(12.dp))
            Text(name,
                fontSize = 22.sp,
                color = White
            )
            Text(text ="subscribers: "+""+ count.toString(),
                fontSize = 15.sp,
                color = Color(red = 150, green = 150, blue = 150)
            )
            Text(
                "Computer Science | BACKEND/ROBOTICS",
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp),
                color = White
            )
            Button(
                onClick = {
                    if(isFollowing == false){
                        isFollowing = true
                        followText = "UnFollow"
                        count++
                    }else{
                        isFollowing = false
                        followText = "Follow"
                        count--
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor,
                    contentColor = contentColor
                )
            ) {
                Text(followText)
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProfileTheme {
        Greeting("Dilmagambet Nurzhigit")
    }
}