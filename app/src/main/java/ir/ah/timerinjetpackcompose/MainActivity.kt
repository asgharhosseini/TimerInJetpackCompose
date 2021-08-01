package ir.ah.timerinjetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.foundation.Canvas
import androidx.compose.material.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import ir.ah.timerinjetpackcompose.ui.theme.TimerInJetpackComposeTheme
import kotlinx.coroutines.*
import kotlin.math.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
           Surface(
               color = Color.Black,
               modifier = Modifier.fillMaxSize()
           ) {
               Box(contentAlignment = Alignment.Center,
                 ){
                   Timer(
                       totalTime = 180*1000L,
                       handlerColor =Color.Green ,
                       inActiveBarColor =Color.DarkGray ,
                       activeBarColor =Color.Green,
                       modifier = Modifier.size(200.dp)
                   )

               }

           }
        }
    }
}

@Composable
fun Timer(
    totalTime: Long,
    handlerColor: Color,
    inActiveBarColor: Color,
    activeBarColor: Color,
    modifier: Modifier=Modifier,
    initialValue: Float = 0f,
    strokeWidth: Dp = 5.dp
) {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    var value by remember {
        mutableStateOf(initialValue)
    }
    var currentTime by remember {
        mutableStateOf(totalTime)
    }
    var isTimerRunning by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = currentTime, key2 = isTimerRunning){
        if (currentTime > 0 && isTimerRunning){
            delay(100L)
            currentTime-=100L
            value =currentTime / totalTime.toFloat()
        }
    }

    Box(contentAlignment = Alignment.Center,
    modifier = modifier.onSizeChanged {
        size=it
    }) {
        Canvas(modifier = modifier) {
            drawArc(
                color = inActiveBarColor,
                startAngle = -215f,
                sweepAngle = 250f,
                useCenter = false,
                size = Size(size.width.toFloat(), size.height.toFloat()),
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                color = activeBarColor,
                startAngle = -215f,
                sweepAngle = 250f * value,
                useCenter = false,
                size = Size(size.width.toFloat(), size.height.toFloat()),
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
            val center = Offset(size.width / 2f, size.height / 2f)
            val beta = (250f * value + 145f) * (PI / 180f).toFloat()
            val r = size.width / 2f
            val a = cos(beta) * r
            val b = sin(beta) * r
            drawPoints(
                listOf(Offset(center.x + a, center.y + b)),
                pointMode = PointMode.Points,
                color = handlerColor,
                strokeWidth = (strokeWidth * 3f).toPx(),
                cap = StrokeCap.Round
            )
        }

        Text(
            text = (currentTime / 1000L).toString(),
            fontSize = 44.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Button(
            onClick = {
                if(currentTime <= 0L) {
                    currentTime = totalTime
                    isTimerRunning = true
                } else {
                    isTimerRunning = !isTimerRunning
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (!isTimerRunning || currentTime <= 0L) {
                    Color.Green
                } else {
                    Color.Red
                }
            )
        ) {
            Text(
                text = if (isTimerRunning && currentTime >= 0L) "Stop"
                else if (!isTimerRunning && currentTime >= 0L) "Start"
                else "Restart"
            )
        }



    }


}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TimerInJetpackComposeTheme {


    }
}