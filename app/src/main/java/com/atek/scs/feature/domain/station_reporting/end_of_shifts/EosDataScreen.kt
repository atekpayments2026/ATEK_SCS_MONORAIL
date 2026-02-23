package com.atek.scs.feature.domain.station_reporting.end_of_shifts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.atek.scs.feature.domain.station_reporting.StationReportingViewModel
import com.atek.scs.feature.domain.station_reporting.end_of_shift_report.TomEosReportScreen
import com.atek.scs.feature.domain.station_reporting.end_of_shift_report.TvmEosReportScreen
import com.atek.scs.feature.domain.station_reporting.end_of_shifts.configuration.EndOfShiftsResponse

data class EosDataScreen(
    val eosList: EndOfShiftsResponse
) : Screen {

    private var navigator: Navigator? = null

    @Composable
    override fun Content() {
        navigator = LocalNavigator.currentOrThrow

        val viewModel = remember {
            ShiftInfoViewModel(
                navigateToTomEosInputScreen = { response ->
                    navigator?.replaceAll(TomEosReportScreen(response, 0))
                },
                navigateToTvmEosInputScreen = { response ->
                    navigator?.replaceAll(TvmEosReportScreen(response, 0))
                },
                navigateToEosDataScreen = { }
            )
        }
        val vm = remember {
            StationReportingViewModel(
                navigateToTomEosInputScreen = {},
                navigateToTvmEosInputScreen = {},
                navigateToEosDataScreen = {}
            )
        }

        EosDataScreenContent(eosList, viewModel, vm)

    }

    @Composable
    fun EosDataScreenContent(
        eosList: EndOfShiftsResponse,
        viewModel: ShiftInfoViewModel,
        vm: StationReportingViewModel,
    ) {
        val eosData = eosList.data

        Column(
            Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.background),
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "END OF SHIFTS",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 40.sp,
                    fontFamily = FontFamily.Serif,
                )
            }

            HorizontalDivider(
                Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = Color.LightGray
            )

            Spacer(Modifier.height(24.dp))
            Box(Modifier.fillMaxSize()) {
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        Modifier.fillMaxSize(.5f),
                    ) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(eosData) { item ->
                                OutlinedCard(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                        .padding(4.dp)
                                        .clickable {
                                            viewModel.fetchSpecificReport(vm)
                                        },
                                    elevation = CardDefaults.outlinedCardElevation(6.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(12.dp),
                                    ) {
                                        InfoRow(label = "Shift Id :", value = item.shiftId.toString())
                                        InfoRow(label = "Station ID :", value = item.stnId.toString())
                                        InfoRow(label = "Equipment ID :", value = item.eqId)
                                        InfoRow(label = "Operator :", value = item.operator)
                                        InfoRow(label = "Start :", value = item.startDate)
                                        InfoRow(label = "End :", value = item.endDate)
                                    }
                                }
                            }
                        }
                    }
                    Card(
                        Modifier
                            .width(480.dp)
                            .height(70.dp)
                            .clickable {
                                navigator?.pop()
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                        )
                    ) {
                        Box(
                            Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Back")
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun InfoRow(
        label: String,
        value: String,
        onClick: (() -> Unit)? = null
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .let {
                    if (onClick != null) it.clickable { onClick() }
                    else it
                }
                .padding(vertical = 4.dp)
        ) {

            Text(
                label,
                modifier = Modifier.weight(1f)
            )
            Text(
                value,
                modifier = Modifier.weight(1f)
            )
        }
    }

}
