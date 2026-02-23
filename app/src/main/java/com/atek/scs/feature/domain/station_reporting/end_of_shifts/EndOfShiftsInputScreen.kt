package com.atek.scs.feature.domain.station_reporting.end_of_shifts

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.atek.scs.feature.domain.station_reporting.StationReportingScreen
import com.atek.scs.feature.domain.station_reporting.StationReportingViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import kotlin.toString

class EndOfShiftsInputScreen(
) : Screen {

    private var navigator: Navigator? = null


    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    override fun Content() {
        navigator = LocalNavigator.currentOrThrow

        val viewModel = remember {
            StationReportingViewModel(
                navigateToTomEosInputScreen = {},
                navigateToTvmEosInputScreen = {},
                navigateToEosDataScreen = { response ->
                    navigator?.push(EosDataScreen(response))
                }
            )
        }
        InputView(viewModel)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DesktopDateTimeInput(
        label: String,
        value: String,
        onValueChange: (String) -> Unit
    ) {
        var showDatePicker by remember { mutableStateOf(false) }
        var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
        var selectedTime by remember { mutableStateOf<LocalTime?>(null) }

        Column {
            OutlinedTextField(
                value = value,
                onValueChange = { },
                modifier = Modifier.width(300.dp).padding(6.dp),
                label = { Text(label) },
                readOnly = true,
                trailingIcon = {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            showDatePicker = true
                        }
                    )
                }
            )
        }

        if (showDatePicker) {
            val dateState = rememberDatePickerState()
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val millis = dateState.selectedDateMillis
                            if (millis != null) {
                                val date = Instant.ofEpochMilli(millis)
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                                selectedDate = date
                                if (selectedTime != null)
                                    onValueChange("$date ${selectedTime.toString()}")
                                else
                                    onValueChange(date.toString())
                            }
                            showDatePicker = false
                        }
                    ) { Text("OK") }
                }
            ) { DatePicker(state = dateState) }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun InputView(viewModel: StationReportingViewModel) {
        Column(Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.background)) {

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    "EQUIPMENTS END OF SHIFT REPORTS",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 40.sp,
                    fontFamily = FontFamily.Serif
                )
            }

            Divider(
                Modifier.fillMaxWidth(),
                color = Color.LightGray,
                thickness = 1.dp
            )

            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedCard(
                    Modifier.fillMaxSize(0.7f),
                    elevation = CardDefaults.outlinedCardElevation(8.dp)
                ) {

                    Column(
                        Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {

                            DesktopDateTimeInput(
                                label = "Start Date",
                                value = viewModel.startDate,
                                onValueChange = { viewModel.startDate = it }
                            )

                            OutlinedTextField(
                                value = viewModel.equipmentId,
                                onValueChange = {
                                    if (it.length <= 6) {
                                        viewModel.equipmentId = it
                                        viewModel.equipmentIdError = null
                                    }
                                },
                                singleLine = true,
                                label = { Text("Enter Equipment") },
                                isError = viewModel.equipmentIdError != null,
                                supportingText = {
                                    viewModel.equipmentIdError?.let { msg ->
                                        Text(msg, color = MaterialTheme.colorScheme.error)
                                    }
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Done
                                ),
                                modifier = Modifier
                                    .width(300.dp)
                                    .padding(6.dp)
                            )
                        }

                        Spacer(Modifier.height(32.dp))

                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {

                            Card(
                                modifier = Modifier
                                    .width(220.dp)
                                    .height(80.dp)
                                    .padding(4.dp)
                                    .clickable {
                                        navigator?.push(StationReportingScreen)
                                    },
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Box(
                                    Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "Back",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Card(
                                modifier = Modifier
                                    .width(220.dp)
                                    .height(80.dp)
                                    .padding(4.dp)
                                    .clickable { viewModel.fetchEqsEoslist() },
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Box(
                                    Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "CONTINUE",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                    }
                }
            }
        }
    }
}
