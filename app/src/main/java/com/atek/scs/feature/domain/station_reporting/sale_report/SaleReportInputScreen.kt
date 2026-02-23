package com.atek.scs.feature.domain.station_reporting.sale_report/*
package com.atek.scs.features.domain.station_reporting.sale_report

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.atek.scs.features.domain.station_reporting.StationReportingViewModel
import com.atek.scs.features.domain.station_reporting.end_of_shift_report.model.TomEosInputScreen.DesktopDateTimeInput

object SaleReportInputScreen: Screen {

     fun readResolve(): Any = SaleReportInputScreen

    @Composable
    override fun Content() {

        val viewModel = remember { StationReportingViewModel(
            navigateToTomEosInputScreen = {},
            navigateToTvmEosInputScreen = {},
            navigateToEosDataScreen = {     },
        ) }

        ReportContainer(viewModel)
        LaunchedEffect(Unit){
           */
/* viewModel.fetchSaleReport()*//*

        }
    }

    @Composable
    fun ReportContainer(viewModel: StationReportingViewModel) {
        Box(
            Modifier.fillMaxSize(),
        ){
            Column(
                Modifier.fillMaxSize(),
            ) {
                Row (
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ){
                    Text("SALE REPORT")
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
                                DesktopDateTimeInput(
                                    label = "Start Date",
                                    value = viewModel.startDate,
                                    onValueChange = { viewModel.startDate = it }
                                )

                                DesktopDateTimeInput(
                                    label = "End Date",
                                    value = viewModel.endDate,
                                    onValueChange = { viewModel.endDate = it }
                                )
                            }

                            Spacer(Modifier.height(32.dp))
                            Card(
                                Modifier.width(220.dp).height(80.dp).padding(4.dp)
                                    .clickable{

                             */
/*           viewModel.fetchSaleReport()*//*

                                    },
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                ),
                            ) {
                                Box(
                                    Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                                ) {
                                    Text("CONTINUE")
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}
*/
