package app.code.petshopandroidapp.ui.components.config.barchart

import android.graphics.Paint
import android.os.Build
import android.provider.CalendarContract.Colors
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Top
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.code.petshopandroidapp.ui.theme.BackgroundColor
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.round

@Composable
fun BarGraph(
    graphBarData: List<Float>,
    xAxisScaleData: List<String>,
    barData_: List<Long>,
    height: Dp,
    roundType: BarType,
    barWidth: Dp,
    barColor: Color,
    barArrangement: Arrangement.Horizontal
) {

    val barData by remember(barData_) {
        mutableStateOf(barData_.map { it.toFloat() })
    }
    
    android.util.Log.d("BarGraph", "barData_ changed: $barData_, barData: $barData")

    // for getting screen width and height you can use LocalConfiguration
    val configuration = LocalConfiguration.current
    // getting screen width
    val width = configuration.screenWidthDp.dp

    // bottom height of the X-Axis Scale
    val xAxisScaleHeight = 40.dp

    val yAxisScaleSpacing by remember {
        mutableStateOf(100f)
    }
    val yAxisTextWidth by remember {
        mutableStateOf(100.dp)
    }

    // bar shape
    val barShap =
        when (roundType) {
            BarType.CIRCULAR_TYPE -> CircleShape
            BarType.TOP_CURVED -> RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp)
        }

    val density = LocalDensity.current
    // y-axis scale text paint
    val textPaint = remember(density) {
        Paint().apply {
            color = Color.Black.hashCode()
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }

    // for y coordinates of y-axis scale to create horizontal dotted line indicating y-axis scale
    val yCoordinates = mutableListOf<Float>()
    // for dotted line effect
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    // height of vertical line over x-axis scale connecting x-axis horizontal line
    val lineHeightXAxis = 10.dp
    // height of horizontal line over x-axis scale
    val horizontalLineHeight = 5.dp

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopStart
    ) {

        // Layer 1
        // y-axis scale and horizontal dotted lines on graph indicating y-axis scale
        Column(
            modifier = Modifier
                .padding(top = xAxisScaleHeight, end = 3.dp)
                .height(height)
                .fillMaxWidth(),
            horizontalAlignment = CenterHorizontally
        ) {

            Canvas(modifier = Modifier.padding(bottom = 10.dp).fillMaxSize()) {

                // Y-Axis Scale Text - Format số doanh thu (chia 1000 để hiển thị ngắn gọn hơn)
                val maxValue = if (barData.isNotEmpty()) barData.max() else 0f
                val yAxisScaleText = maxValue / 3f
                (0..3).forEach { i ->
                    val value = yAxisScaleText * i
                    val displayValue = if (value >= 1000) {
                        "${(value / 1000).toInt()}k"
                    } else {
                        value.toInt().toString()
                    }
                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            displayValue,
                            30f,
                            size.height - yAxisScaleSpacing - i * size.height / 3f,
                            textPaint
                        )
                    }
                    yCoordinates.add(size.height - yAxisScaleSpacing - i * size.height / 3f)
                }

                // horizontal dotted lines on graph indicating y-axis scale
                (1..3).forEach {
                    drawLine(
                        start = Offset(x = yAxisScaleSpacing +30f, y = yCoordinates[it]),
                        end = Offset(x= size.width, y = yCoordinates[it]),
                        color = Color.Gray,
                        strokeWidth = 5f,
                        pathEffect = pathEffect
                    )
                }

            }

        }

        // Layer 2
        // Graph with Bar Graph and X-Axis Scale
        Box(
            modifier = Modifier
                .padding(start = 50.dp)
                .width(width - yAxisTextWidth)
                .height(height + xAxisScaleHeight),
            contentAlignment = Alignment.BottomCenter
        ) {

            Row(
                modifier = Modifier
                    .width(width - yAxisTextWidth),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = barArrangement
            ) {

                // Graph
                graphBarData.forEachIndexed { index, value ->

                    var animationTriggered by remember(value) {
                        android.util.Log.d("BarGraph", "Remember reset for bar $index, value: $value")
                        mutableStateOf(false)
                    }
                    val graphBarHeight by animateFloatAsState(
                        targetValue = if (animationTriggered) value else 0f,
                        animationSpec = tween(
                            durationMillis = 1000,
                            delayMillis = 0
                        ), label = "barAnimation"
                    )
                    LaunchedEffect(value) {
                        android.util.Log.d("BarGraph", "LaunchedEffect triggered for bar $index, value: $value")
                        animationTriggered = false
                        kotlinx.coroutines.delay(50)
                        animationTriggered = true
                        android.util.Log.d("BarGraph", "Animation triggered for bar $index")
                    }

                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Top,
                        horizontalAlignment = CenterHorizontally
                    ) {

                        // Each Graph
                        Box(
                            modifier = Modifier
                                .padding(bottom = 5.dp)
                                .clip(barShap)
                                .width(barWidth)
                                .height(height - 10.dp)
                                .background(Color.Transparent),
                            contentAlignment = BottomCenter
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(barShap)
                                    .fillMaxWidth()
                                    .fillMaxHeight(graphBarHeight)
                                    .background(barColor)
                            )
                        }

                        // scale x-axis and bottom part of graph
                        Column(
                            modifier = Modifier
                                .height(xAxisScaleHeight),
                            verticalArrangement = Top,
                            horizontalAlignment = CenterHorizontally
                        ) {

                            // small vertical line joining the horizontal x-axis line
                            Box(
                                modifier = Modifier
                                    .clip(
                                        RoundedCornerShape(
                                            bottomStart = 2.dp,
                                            bottomEnd = 2.dp
                                        )
                                    )
                                    .width(horizontalLineHeight)
                                    .height(lineHeightXAxis)
                                    .background(color = Color.Gray)
                            )
                            // scale x-axis
                            Text(
                                modifier = Modifier.padding(bottom = 3.dp),
                                text = xAxisScaleData[index],
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center,
                                color = Color.Black
                            )

                        }

                    }

                }

            }

            // horizontal line on x-axis on the graph
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent),
                horizontalAlignment = CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .padding(bottom = xAxisScaleHeight + 3.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .fillMaxWidth()
                        .height(horizontalLineHeight)
                        .background(Color.Gray)
                )

            }


        }


    }

}
@RequiresApi(Build.VERSION_CODES.O)
fun generateData(mode: StatisticMode): Pair<List<String>, List<Int>> {
    return when (mode) {
        StatisticMode.DAILY -> {
            val startDate = LocalDate.now().minusDays(4)
            val dates = (0..4).map { startDate.plusDays(it.toLong()).format(DateTimeFormatter.ofPattern("dd/MM")) }
            val values = listOf(30, 60, 90, 50, 70)
            Pair(dates, values)
        }

        StatisticMode.MONTHLY -> {
            val months = listOf("01/2024", "02/2024", "03/2024", "04/2024", "05/2024")
            val values = listOf(200, 400, 350, 500, 300)
            Pair(months, values)
        }

        StatisticMode.YEARLY -> {
            val years = listOf("2020", "2021", "2022", "2023", "2024")
            val values = listOf(1500, 1800, 2000, 1700, 2100)
            Pair(years, values)
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StatisticBarGraphWithModeSelector(
    onGetDailyData: suspend () -> Pair<List<String>, List<Long>> = { 
        val (labels, intValues) = generateData(StatisticMode.DAILY)
        Pair(labels, intValues.map { it.toLong() })
    },
    onGetMonthlyData: suspend () -> Pair<List<String>, List<Long>> = { 
        val (labels, intValues) = generateData(StatisticMode.MONTHLY)
        Pair(labels, intValues.map { it.toLong() })
    },
    onGetYearlyData: suspend () -> Pair<List<String>, List<Long>> = { 
        val (labels, intValues) = generateData(StatisticMode.YEARLY)
        Pair(labels, intValues.map { it.toLong() })
    }
) {
    var selectedMode by remember { mutableStateOf(StatisticMode.DAILY) }
    var labels by remember { mutableStateOf<List<String>>(emptyList()) }
    var values by remember { mutableStateOf<List<Long>>(emptyList()) }
    
    android.util.Log.d("StatisticBarGraph", "Component created - selectedMode: $selectedMode, labels: $labels, values: $values")

    // Load data khi selectedMode thay đổi HOẶC khi component mount lần đầu
    LaunchedEffect(selectedMode) {
        android.util.Log.d("StatisticBarGraph", "LaunchedEffect started for mode: $selectedMode")
        val (newLabels, newValues) = when (selectedMode) {
            StatisticMode.DAILY -> onGetDailyData()
            StatisticMode.MONTHLY -> onGetMonthlyData()
            StatisticMode.YEARLY -> onGetYearlyData()
        }
        android.util.Log.d("StatisticBarGraph", "Data loaded - Labels: $newLabels, Values: $newValues")
        labels = newLabels
        values = newValues
        android.util.Log.d("StatisticBarGraph", "State updated - Mode: $selectedMode, Labels: $labels, Values: $values")
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Dropdown menu chọn chế độ thống kê
        var expanded by remember { mutableStateOf(false) }
        Box {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = BackgroundColor,
                    contentColor = Color.Black
                ),
                onClick = { expanded = true }) {
                Text(text = "Thống kê theo: ${selectedMode.getVietnameseName()}")
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                StatisticMode.values().forEach { mode ->
                    DropdownMenuItem(
                        onClick = {
                            selectedMode = mode
                            expanded = false
                        },
                        text = { Text(text = mode.name) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Chỉ hiển thị graph khi có data
        if (values.isNotEmpty()) {
            val maxValue = if (values.max() > 0) values.max() else 1
            val floatValue = values.map { it.toFloat() / maxValue.toFloat() }
            
            android.util.Log.d("StatisticBarGraph", "Rendering BarGraph - MaxValue: $maxValue, FloatValues: $floatValue, Labels: $labels")

            BarGraph(
                graphBarData = floatValue,
                xAxisScaleData = labels,
                barData_ = values,
                height = 300.dp,
                roundType = BarType.TOP_CURVED,
                barWidth = 20.dp,
                barColor = BackgroundColor,
                barArrangement = Arrangement.SpaceEvenly
            )
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewHome() {
    StatisticBarGraphWithModeSelector()
}