package com.atek.scs.feature.domain.station_reporting.end_of_shift_report.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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
import com.atek.scs.feature.domain.station_reporting.end_of_shift_report.TvmEosReportScreen
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import kotlin.toString

class TvmEosInputScreen() : Screen {

    fun readResolve(): Any = TvmEosInputScreen()
    private var navigator: Navigator? = null

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    override fun Content() {
        navigator = LocalNavigator.currentOrThrow

        val viewModel = remember {
            StationReportingViewModel(
                navigateToTomEosInputScreen = { },
                navigateToTvmEosInputScreen = { response ->
                    navigator?.push(TvmEosReportScreen(response, 0))
                },
                navigateToEosDataScreen = { },
            )
        }
        Preview(viewModel)
        LaunchedEffect(Unit) {
            viewModel.onCreate()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DesktopDateTimeInput(
        label: String, value: String, onValueChange: (String) -> Unit
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
                        Icons.Default.DateRange, contentDescription = null, modifier = Modifier.clickable {
                            showDatePicker = true
                        })
                })
        }

        if (showDatePicker) {
            val dateState = rememberDatePickerState()
            DatePickerDialog(onDismissRequest = { showDatePicker = false }, confirmButton = {
                TextButton(
                    onClick = {
                        val millis = dateState.selectedDateMillis
                        if (millis != null) {
                            val date = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                            selectedDate = date
                            if (selectedTime != null) onValueChange("$date ${selectedTime.toString()}")
                            else onValueChange(date.toString())
                        }
                        showDatePicker = false
                    }) { Text("OK") }
            }) { DatePicker(state = dateState) }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun Preview(viewModel: StationReportingViewModel) {

        val nextFocus = remember { FocusRequester() }

        Column(
            Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.background),
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    "TVM END OF SHIFT REPORT",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 40.sp,
                    fontFamily = FontFamily.Serif,
                )
            }
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedCard(
                    Modifier.fillMaxSize(.7f), elevation = CardDefaults.outlinedCardElevation(8.dp)
                ) {
                    Column(
                        Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                        ) {

                            OutlinedTextField(
                                value = viewModel.equipmentId,
                                onValueChange = {
                                    viewModel.equipmentId = it
                                    viewModel.equipmentIdError = null

                                    when {
                                        it.isBlank() -> {
                                            viewModel.equipmentIdError = "Equipment ID cannot be blank!"
                                        }

                                        it.any { !it.isDigit() } -> {
                                            viewModel.equipmentIdError = "Equipment ID must be numeric!"
                                        }

                                        !it.contains("03") -> {
                                            viewModel.equipmentIdError = "Equipment ID not valid!"
                                        }
                                    }
                                },
                                singleLine = true,
                                label = { Text("Enter Equipment") },
                                isError = viewModel.equipmentIdError != null,
                                supportingText = {
                                    viewModel.equipmentIdError?.let {
                                        Text(it, color = MaterialTheme.colorScheme.error)
                                    }
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
                                ),
                                modifier = Modifier.width(300.dp).padding(6.dp).focusRequester(nextFocus)
                            )

                            OutlinedTextField(
                                value = viewModel.userId,
                                onValueChange = {
                                    viewModel.userId = it
                                    viewModel.userIdError = null
                                },
                                singleLine = true,
                                label = { Text("Enter UserID") },
                                isError = viewModel.userIdError != null,
                                supportingText = {
                                    viewModel.userIdError?.let {
                                        Text(it, color = MaterialTheme.colorScheme.error)
                                    }
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
                                ),
                                modifier = Modifier.width(300.dp).padding(6.dp).focusRequester(nextFocus)
                            )

                            OutlinedTextField(
                                value = viewModel.shiftId?.toString() ?: "",
                                onValueChange = {
                                    viewModel.shiftId = it.toIntOrNull()
                                    viewModel.shiftIdError = null
                                },
                                singleLine = true,
                                label = { Text("Enter ShiftId") },
                                isError = viewModel.shiftIdError != null,
                                supportingText = {
                                    viewModel.shiftIdError?.let {
                                        Text(it, color = MaterialTheme.colorScheme.error)
                                    }
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
                                ),
                                modifier = Modifier.width(300.dp).padding(6.dp).focusRequester(nextFocus)
                            )
                        }

                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                        ) {
                            DesktopDateTimeInput(
                                label = "Start Date",
                                value = viewModel.startDate,
                                onValueChange = { viewModel.startDate = it })

                            DesktopDateTimeInput(
                                label = "End Date",
                                value = viewModel.endDate,
                                onValueChange = { viewModel.endDate = it })
                        }

                        Spacer(Modifier.height(48.dp))
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                        ) {

                            Card(
                                Modifier.width(250.dp).height(70.dp).clip(RoundedCornerShape(8.dp)).padding(6.dp)
                                    .clickable {
                                        navigator?.push(StationReportingScreen)
                                    }, colors = CardDefaults.cardColors(
                                    MaterialTheme.colorScheme.primary,
                                )
                            ) {
                                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Text("Back")
                                }
                            }

                            Card(
                                Modifier.width(250.dp).height(70.dp).clip(RoundedCornerShape(8.dp)).padding(6.dp)
                                    .clickable {
                                        if (viewModel.validateInputs()) {
                                            viewModel.fetchTVMReport()
                                        }
                                    }, colors = CardDefaults.cardColors(
                                    MaterialTheme.colorScheme.primary,
                                )
                            ) {
                                Row(
                                    Modifier.fillMaxSize().padding(horizontal = 12.dp),
                                ) {
                                    Row(
                                        Modifier.weight(.1f).fillMaxHeight(),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Icon(Icons.Default.Info, contentDescription = "Reports")
                                    }
                                    Row(
                                        Modifier.weight(1f).fillMaxHeight(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center,
                                    ) {
                                        Text("Generate Report", modifier = Modifier.padding(start = 12.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}