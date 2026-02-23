package com.atek.scs.feature.domain.station_reporting.sale_report/*
package com.atek.scs.features.domain.station_reporting.sale_report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import com.atek.scs.features.domain.station_reporting.StationReportingViewModel
import com.atek.scs.features.domain.station_reporting.sale_report.configuration.SaleReportResponse

data class SaleReport(
    val response: SaleReportResponse, val stationId: Int
) : Screen {

    @Composable
    override fun Content() {
        val viewModel = remember {
            StationReportingViewModel(
                navigateToTomEosInputScreen = {}, navigateToTvmEosInputScreen = {},
                navigateToSaleReport = {},
            )
        }
        View(viewModel)
    }

    @Composable
    fun View(viewModel: StationReportingViewModel) {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            val data = response.getStation() ?: return

            Text(
                "DAILY STATION WISE SALE REPORT",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(8.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                Row {
                    Text(text = "Start Date: ")
                    Text(text = viewModel.startDate)
                }
                Row {
                    Text(text = "End Date: ")
                    Text(text = viewModel.endDate)
                }
            }

            HorizontalDivider(Modifier.fillMaxWidth(), 1.dp, Color.LightGray)

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Row(Modifier.weight(1f)) {
                    Text(text = "Total RiderShip: ")
                }
                Row(Modifier.weight(1f)) {
                    Text(text = data.totalRidership)
                }
            }

            // ----------------------------------- SJT -------------------------------------------------------
            HorizontalDivider(Modifier.fillMaxWidth(), 1.dp, Color.LightGray)
            Text("SJT", fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
            Spacer(Modifier.height(24.dp))

            response.getSJTData().forEach {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    Row(Modifier.weight(1f)) {
                        Text(text = it.key)
                    }
                    Row(Modifier.weight(1f)) {
                        Text(text = it.value)
                    }
                }
            }

            // --------------------------------- RJT ----------------------------------------------------------
            HorizontalDivider(Modifier.fillMaxWidth(), 1.dp, Color.LightGray)
            Text("RJT", fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
            Spacer(Modifier.height(24.dp))

            response.getRjtData()?.forEach {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    Row(Modifier.weight(1f)) {
                        Text(text = it.key)
                    }
                    Row(Modifier.weight(1f)) {
                        Text(text = it.value)
                    }
                }
            }

            // ----------------------------------- MQR TP -----------------------------------------------------
            HorizontalDivider(Modifier.fillMaxWidth(), 1.dp, Color.LightGray)
            Text("MQR TP", fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
            Spacer(Modifier.height(24.dp))

            response.getMqrTpData()?.forEach {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    Row(Modifier.weight(1f)) {
                        Text(text = it.key)
                    }
                    Row(Modifier.weight(1f)) {
                        Text(text = it.value)
                    }
                }
            }

            // --------------------------------------- NCMC CARD -----------------------------------------------
            HorizontalDivider(Modifier.fillMaxWidth(), 1.dp, Color.LightGray)
            Text("NCMC CARD", fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
            Spacer(Modifier.height(24.dp))

            response.getNcmcData()?.forEach {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    Row(Modifier.weight(1f)) {
                        Text(text = it.key)
                    }
                    Row(Modifier.weight(1f)) {
                        Text(text = it.value)
                    }
                }
            }

            // ---------------------------------------- NCMC SV ------------------------------------------------
            HorizontalDivider(Modifier.fillMaxWidth(), 1.dp, Color.LightGray)
            Text("NCMC SV", fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
            Spacer(Modifier.height(24.dp))

            response.getNcmcSvData()?.forEach {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    Row(Modifier.weight(1f)) {
                        Text(text = it.key)
                    }
                    Row(Modifier.weight(1f)) {
                        Text(text = it.value)
                    }
                }
            }

            // ------------------------------------------- NCMC TP ---------------------------------------------
            HorizontalDivider(Modifier.fillMaxWidth(), 1.dp, Color.LightGray)
            Text("NCMC TP", fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
            Spacer(Modifier.height(24.dp))

            response.getNcmcTpData()?.forEach {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    Row(Modifier.weight(1f)) {
                        Text(text = it.key)
                    }
                    Row(Modifier.weight(1f)) {
                        Text(text = it.value)
                    }
                }
            }

            // ------------------------------------- MQR MMRCL APP --------------------------------------------
            HorizontalDivider(Modifier.fillMaxWidth(), 1.dp, Color.LightGray)
            Text("MQR MMRCL APP", fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
            Spacer(Modifier.height(24.dp))

            response.getMMRCLAppData()?.forEach {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    Row(Modifier.weight(1f)) {
                        Text(text = it.key)
                    }
                    Row(Modifier.weight(1f)) {
                        Text(text = it.value)
                    }
                }
            }

            // ------------------------------------- MQR ONDC APP ---------------------------------------------
            HorizontalDivider(Modifier.fillMaxWidth(), 1.dp, Color.LightGray)
            Text("MQR ONDC APP", fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
            Spacer(Modifier.height(24.dp))

            response.getONDCAppData()?.forEach {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    Row(Modifier.weight(1f)) {
                        Text(text = it.key)
                    }
                    Row(Modifier.weight(1f)) {
                        Text(text = it.value)
                    }
                }
            }

            // ------------------------------------- MQR MUMBAI ONE APP ---------------------------------------
            HorizontalDivider(Modifier.fillMaxWidth(), 1.dp, Color.LightGray)
            Text("MQR MUMBAI ONE APP", fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
            Spacer(Modifier.height(24.dp))

            response.getMumbaiOneAppData()?.forEach {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    Row(Modifier.weight(1f)) {
                        Text(text = it.key)
                    }
                    Row(Modifier.weight(1f)) {
                        Text(text = it.value)
                    }
                }
            }
        }
    }


}
*/
