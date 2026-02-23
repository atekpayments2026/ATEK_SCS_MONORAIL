package com.atek.scs.feature.domain.station_reporting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.atek.scs.feature.domain.station_reporting.end_of_shift_report.TomEosReportScreen
import com.atek.scs.feature.domain.station_reporting.end_of_shift_report.TvmEosReportScreen
import com.atek.scs.feature.domain.station_reporting.end_of_shift_report.model.TomEosInputScreen
import com.atek.scs.feature.domain.station_reporting.end_of_shift_report.model.TvmEosInputScreen
import com.atek.scs.feature.domain.station_reporting.end_of_shifts.EndOfShiftsInputScreen
import com.atek.scs.feature.domain.station_reporting.end_of_shifts.EosDataScreen
import com.atek.scs.feature.domain.station_summary.StationSummaryScreen

object StationReportingScreen : Screen {

    private fun readResolve(): Any = StationReportingScreen

    @Composable
    override fun Content() {

        var selectedScreen by remember { mutableStateOf<Screen?>(null) }

        val viewModel = remember {
            StationReportingViewModel(
                navigateToTomEosInputScreen = { response ->
                    selectedScreen = TomEosReportScreen(response, index = 0)
                },
                navigateToTvmEosInputScreen = { response ->
                    selectedScreen = TvmEosReportScreen(response, index = 0)
                },
                navigateToEosDataScreen = { response ->
                    selectedScreen = EosDataScreen(response)
                }
            )
        }

        TwoPaneLayout(viewModel, selectedScreen) {
            selectedScreen = null
        }
    }

    // MAIN LAYOUT
    @Composable
    fun TwoPaneLayout(
        viewModel: StationReportingViewModel,
        selectedScreen: Screen?,
        onClearSelection: () -> Unit
    ) {

        val navigator = LocalNavigator.currentOrThrow

        Row(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {

            Column(
                Modifier
                    .weight(0.27f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    MenuButton("TVM END OF SHIFT") {

                        navigator.push(TvmEosInputScreen())
                    }

                    MenuButton("TOM/EFO END OF SHIFT REPORT") {

                        navigator.push(TomEosInputScreen())
                    }

                    MenuButton("EQUIPMENTS END OF SHIFTS") {

                        navigator.push(EndOfShiftsInputScreen())
                    }
                }
                Column {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .padding(8.dp)
                            .clickable {
                                navigator.push(StationSummaryScreen)
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
                }


            }

            Box(
                Modifier
                    .width(1.dp)
                    .fillMaxHeight()
                    .background(Color.LightGray)
            )

            Box(
                Modifier
                    .weight(0.73f)
                    .fillMaxHeight()
            ) {
                if (selectedScreen != null) {
                    when (selectedScreen) {
                        is TomEosReportScreen -> selectedScreen.EosView(viewModel) {
                            onClearSelection()
                        }

                        is TvmEosReportScreen -> selectedScreen.Content()
                        is EosDataScreen -> selectedScreen.Content()
                    }
                } else {
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }


    @Composable
    fun MenuButton(title: String, onClick: () -> Unit) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(8.dp)
                .clickable { onClick() },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}


/*
package com.atek.scs.features.domain.station_reporting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.atek.scs.features.domain.station_reporting.end_of_shift_report.TomEosReportScreen
import com.atek.scs.features.domain.station_reporting.end_of_shift_report.TvmEosReportScreen
import com.atek.scs.features.domain.station_reporting.end_of_shift_report.model.TomEosInputScreen
import com.atek.scs.features.domain.station_reporting.end_of_shift_report.model.TvmEosInputScreen
import com.atek.scs.features.domain.station_reporting.end_of_shifts.EndOfShiftsInputScreen
import com.atek.scs.features.domain.station_reporting.end_of_shifts.EosDataScreen
import com.atek.scs.utils.SelectedScreen

object StationReportingScreen : Screen {
    private fun readResolve(): Any = StationReportingScreen

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        var selectedScreen by remember { mutableStateOf<Screen?>(null) }

        val viewModel = remember {
            StationReportingViewModel(
                navigateToTomEosInputScreen = {response ->
                    selectedScreen = TomEosReportScreen(response, index = 0)
                },
                navigateToTvmEosInputScreen = { response ->
                    selectedScreen = TvmEosReportScreen(response, index = 0)
                },
                navigateToEosDataScreen = { endOfShiftsResponse ->
                    selectedScreen = EosDataScreen(endOfShiftsResponse)
                }
            )
        }

        TabOption(viewModel, selectedScreen)
    }

    @Composable
    fun TabOption(viewModel: StationReportingViewModel, selectedScreen: Screen?){

        var selectedTab by remember { mutableStateOf(SelectedScreen.entries.first()) }

        Row(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                Modifier
                    .weight(0.27f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
            ) {
                SelectedScreen.entries.forEach { screenItem ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp)
                            .padding(8.dp)
                            .clickable {
                                selectedTab = screenItem
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedTab == screenItem)
                                MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Box(
                            Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = screenItem.displayName,
                                style = MaterialTheme.typography.titleMedium,
                                color = if (selectedTab == screenItem) Color.White else Color.Black
                            )
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight()
                    .background(Color.LightGray)
            )

            Box(
                modifier = Modifier
                    .weight(0.73f)
                    .fillMaxHeight()
            ) {
                if (selectedScreen != null) {
                    when (selectedScreen) {
                        is TomEosReportScreen -> selectedScreen.Content()
                        is TvmEosReportScreen -> selectedScreen.Content()
                        is EosDataScreen -> selectedScreen.Content()
                    }
                } else {
                    when (selectedTab) {
                        SelectedScreen.TVM -> TvmEosInputScreen.Content()
                        SelectedScreen.TOM -> TomEosInputScreen.Content()
                        SelectedScreen.EOS -> EndOfShiftsInputScreen.Content()
                    }
                }
            }
        }
    }
}
*/
