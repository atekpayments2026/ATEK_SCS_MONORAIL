package com.atek.scs.feature.domain.station_reporting.end_of_shift_report

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.atek.scs.feature.domain.station_reporting.StationReportingViewModel
import com.atek.scs.feature.domain.station_reporting.end_of_shift_report.configuration.TomEosResponse

data class TomEosReportScreen(
    val report: TomEosResponse,
    val index: Int
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current

        val viewModel = remember { StationReportingViewModel(
            navigateToTomEosInputScreen = {},
            navigateToTvmEosInputScreen = {},
            navigateToEosDataScreen = {}
        ) }

        EosView(
            viewModel = viewModel,
            onBack = { navigator?.pop() }
        )
        LaunchedEffect(Unit) {
            viewModel.onCreate()
        }

    }

    @Composable
    fun EosView(
        viewModel: StationReportingViewModel,
        onBack: () -> Unit
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .background(color = Color.LightGray),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Column(
                Modifier
                    .fillMaxWidth(.30f)
                    .fillMaxHeight()
                    .padding(top = 40.dp)
                    .background(MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(Modifier.height(20.dp))

                Text(
                    "MUMBAI METRO LINE-3",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
                Text(
                    "TOM END OF SHIFT REPORT",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

                Spacer(Modifier.height(20.dp))

                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Top
                ) {

                    val data = report.data[index]

                    Row(Modifier.fillMaxWidth()) {
                        Text(
                            "Station :",
                            Modifier.weight(1f)
                        )
                        Text(
                            viewModel.stationName.toString(),
                            Modifier.weight(1f)
                        )
                    }
                    Row(Modifier.fillMaxWidth()) {
                        Text(
                            "Equipment :",
                            Modifier.weight(1f),
                            textAlign = TextAlign.Start

                        )
                        Text(
                            data.eq_id,
                            Modifier.weight(1f),
                            textAlign = TextAlign.Start
                        )
                    }

                    Row(Modifier.fillMaxWidth()) {
                        Text(
                            "Operator / Shift :",
                            Modifier.weight(1f),
                            textAlign = TextAlign.Start
                        )
                        Text(
                            "${data.user_id} / ${data.shift_id}",
                            Modifier.weight(1f),
                            textAlign = TextAlign.Start
                        )
                    }

                    Row(Modifier.fillMaxWidth()) {
                        Text(
                            "Start Date :",
                            Modifier.weight(1f),
                            textAlign = TextAlign.Start
                        )
                        Text(
                            data.shift_start,
                            Modifier.weight(1f),
                            textAlign = TextAlign.Start
                        )
                    }

                    Row(Modifier.fillMaxWidth()) {
                        Text(
                            "End Date :",
                            Modifier.weight(1f),
                            textAlign = TextAlign.Start
                        )
                        Text(
                            data.shift_end,
                            Modifier.weight(1f),
                            textAlign = TextAlign.Start
                        )
                    }

                    customHorizontalDivider()

                    Text(
                        "PAPER QR",
                        Modifier.align(Alignment.CenterHorizontally),
                        fontWeight = FontWeight.Bold,
                    )
                    Row(
                        Modifier.fillMaxWidth(),
                    ) {
                        Row(
                            Modifier.weight(1.5f),
                            horizontalArrangement = Arrangement.End,
                        ) {
                            Text("COUNT", Modifier.padding(end = 54.dp), fontWeight = FontWeight.Bold)
                        }
                        Row(
                            modifier = Modifier.weight(.8f),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Text("AMOUNT", fontWeight = FontWeight.Bold)
                        }
                    }
                    report.getTomPaperData(index).forEach {
                        Row(Modifier.fillMaxWidth()) {
                            Text(
                                it.key,
                                Modifier.weight(1f),
                                textAlign = TextAlign.Start
                            )

                            Text(
                                it.value.first,
                                Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = "${it.value.second.toDouble()}",
                                Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    customHorizontalDivider()

                    Text(
                        "NCMC METRO",
                        Modifier.align(Alignment.CenterHorizontally),
                        fontWeight = FontWeight.Bold,
                    )
                    report.getTomNcmcMetroData(index).forEach {
                        Row(Modifier.fillMaxWidth()) {
                            Text(
                                it.key,
                                Modifier.weight(1f),
                                textAlign = TextAlign.Start
                            )

                            Text(
                                it.value.first,
                                Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = "${it.value.second.toDouble()}",
                                Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    customHorizontalDivider()

                    Text(
                        "NET COLLECTION PQR & NCMC METRO",
                        Modifier.align(Alignment.CenterHorizontally),
                        fontWeight = FontWeight.Bold,
                    )

                    report.getTomPaperAndNcmcNetColletion(index).forEach {
                        Row(Modifier.fillMaxWidth()) {
                            Text(
                                it.key,
                                Modifier.weight(1f),
                                textAlign = TextAlign.Start
                            )

                            Text(
                                it.value.first,
                                Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = "${it.value.second.toDouble()}",
                                Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    customHorizontalDivider()

                    Text(
                        "NCMC BANK",
                        Modifier.align(Alignment.CenterHorizontally),
                        fontWeight = FontWeight.Bold,
                    )

                    report.getTomNcmcBankData(index).forEach {
                        Row(Modifier.fillMaxWidth()) {
                            Text(
                                it.key,
                                Modifier.weight(1f),
                                textAlign = TextAlign.Start
                            )

                            Text(
                                it.value.first,
                                Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = "${it.value.second.toDouble()}",
                                Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    customHorizontalDivider()

                    Text(
                        "NET COLLECTION NCMC BANK",
                        Modifier.align(Alignment.CenterHorizontally),
                        fontWeight = FontWeight.Bold,
                    )

                    report.getTomNetCollectionNcmcBank(index).forEach {
                        Row(Modifier.fillMaxWidth()) {
                            Text(
                                it.key,
                                Modifier.weight(1f),
                                textAlign = TextAlign.Start
                            )

                            Text(
                                it.value.first,
                                Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = "${it.value.second.toDouble()}",
                                Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    customHorizontalDivider()

                    Text(
                        "TOTAL COLLECTION",
                        Modifier.align(Alignment.CenterHorizontally),
                        fontWeight = FontWeight.Bold,
                    )

                    report.getTomTotalCollection(index).forEach {
                        Row(Modifier.fillMaxWidth()) {
                            Text(
                                it.key,
                                Modifier.weight(1f),
                                textAlign = TextAlign.Start
                            )

                            Text(
                                it.value.first,
                                Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = "${it.value.second.toDouble()}",
                                Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    customHorizontalDivider()

                    Text(
                        "******** END OF REPORT ********",
                        Modifier.align(Alignment.CenterHorizontally),
                        fontWeight = FontWeight.Bold,
                    )

                    Row(
                        Modifier.fillMaxWidth().padding(8.dp),
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        Card(
                            modifier = Modifier
                                .height(70.dp)
                                .fillMaxWidth()
                                .padding(8.dp)
                                .align(Alignment.CenterVertically)
                                .clickable {
                                    onBack()
                                },
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text("Back")
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun customHorizontalDivider(){
        Spacer(Modifier.height(8.dp))
        Box(
            Modifier
                .fillMaxWidth()
                .height(1.dp)
                .padding(horizontal = 4.dp)
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
        )
        Spacer(Modifier.height(12.dp))
    }
}