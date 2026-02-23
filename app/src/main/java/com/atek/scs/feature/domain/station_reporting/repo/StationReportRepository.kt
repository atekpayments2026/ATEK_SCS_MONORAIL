package com.atek.scs.feature.domain.station_reporting.repo

import com.atek.scs.feature.common.network.ApiManager
import com.atek.scs.feature.domain.station_reporting.end_of_shifts.configuration.EndOfShiftsRequest
import com.atek.scs.feature.domain.station_reporting.end_of_shifts.configuration.EndOfShiftsResponse
import com.atek.scs.feature.domain.station_reporting.end_of_shift_report.configuration.TomEosRequest
import com.atek.scs.feature.domain.station_reporting.end_of_shift_report.configuration.TomEosResponse
import com.atek.scs.feature.domain.station_reporting.end_of_shift_report.configuration.TvmEosRequest
import com.atek.scs.feature.domain.station_reporting.end_of_shift_report.configuration.TvmEosResponse
import com.atek.scs.utils.fromJson

object StationReportRepository {

    suspend fun getTomEosReport(tomEosRequest: TomEosRequest): TomEosResponse {
        return """
            {
                "data": [
                    {
                        "stn_id": "Aarey JVLR",
                        "eq_id": "010201",
                        "user_id": "74720",
                        "shift_id": 6,
                        "shift_start": "2025-09-26 00:00:00",
                        "shift_end": "2025-09-27 23:59:59",
                        "paper_qr": {
                            "paper_qr_sale_cash_count": 208,
                            "paper_qr_sale_cash_amount": 11560,
                            "paper_qr_sale_upi_count": 138,
                            "paper_qr_sale_upi_amount": 8340,
                            "paper_qr_sale_ccdc_count": 0,
                            "paper_qr_sale_ccdc_amount": 0,
                            "paper_qr_penalty_cash_count": 0,
                            "paper_qr_penalty_cash_amount": 0,
                            "paper_qr_penalty_upi_count": 0,
                            "paper_qr_penalty_upi_amount": 0,
                            "paper_qr_penalty_ccdc_count": 0,
                            "paper_qr_penalty_ccdc_amount": 0,
                            "paper_qr_refund_count": 0,
                            "paper_qr_refund_amount": 0
                        },
                        "ncmc_metro": {
                            "ncmc_metro_tp_sale_cash_count": 0,
                            "ncmc_metro_tp_sale_cash_amount": 0,
                            "ncmc_metro_tp_sale_upi_count": 0,
                            "ncmc_metro_tp_sale_upi_amount": 0,
                            "ncmc_metro_tp_sale_ccdc_count": 0,
                            "ncmc_metro_tp_sale_ccdc_amount": 0,
                            "ncmc_metro_tp_top_up_cash_count": 0,
                            "ncmc_metro_tp_top_up_cash_amount": 0,
                            "ncmc_metro_tp_top_up_upi_count": 0,
                            "ncmc_metro_tp_top_up_upi_amount": 0,
                            "ncmc_metro_tp_top_up_ccdc_count": 0,
                            "ncmc_metro_tp_top_up_ccdc_amount": 0,
                            "ncmc_metro_tp_refund_count": 0,
                            "ncmc_metro_tp_refund_amount": 0,
                            "ncmc_metro_tp_replace_count": 0,
                            "ncmc_metro_tp_replace_amount": 0,
                            "ncmc_metro_tp_penalty_cash_count": 0,
                            "ncmc_metro_tp_penalty_cash_amount": 0,
                            "ncmc_metro_tp_penalty_upi_count": 0,
                            "ncmc_metro_tp_penalty_upi_amount": 0,
                            "ncmc_metro_tp_penalty_ccdc_count": 0,
                            "ncmc_metro_tp_penalty_ccdc_amount": 0
                        },
                        "net_collection_pqr_and_ncmc_metro": {
                            "net_cash_count": 208,
                            "net_cash_amount": 11560,
                            "net_upi_count": 138,
                            "net_upi_amount": 8340,
                            "net_ccdc_count": 0,
                            "net_ccdc_amount": 0,
                            "total_amount_count": 346,
                            "total_amount_amount": 19900
                        },
                        "ncmc_bank": {
                            "ncmc_bank_card_sale_cash_count": 0,
                            "ncmc_bank_card_sale_cash_amount": 0,
                            "ncmc_bank_card_sale_upi_count": 0,
                            "ncmc_bank_card_sale_upi_amount": 0,
                            "ncmc_bank_card_sale_ccdc_count": 0,
                            "ncmc_bank_card_sale_ccdc_amount": 0,
                            "ncmc_bank_card_replacement_cash_count": 0,
                            "ncmc_bank_card_replacement_cash_amount": 0,
                            "ncmc_bank_card_replacement_upi_count": 0,
                            "ncmc_bank_card_replacement_upi_amount": 0,
                            "ncmc_bank_card_replacement_card_count": 0,
                            "ncmc_bank_card_replacement_card_amount": 0,
                            "ncmc_bank_sv_topup_cash_count": 0,
                            "ncmc_bank_sv_topup_cash_amount": 0,
                            "ncmc_bank_sv_topup_upi_count": 0,
                            "ncmc_bank_sv_topup_upi_amount": 0,
                            "ncmc_bank_sv_topup_ccdc_count": 0,
                            "ncmc_bank_sv_topup_ccdc_amount": 0,
                            "ncmc_bank_sv_topup_balance_update_count": 0,
                            "ncmc_bank_sv_topup_balance_update_amount": 0
                        },
                        "net_collection_ncmc_bank": {
                            "net_cash_count": 0,
                            "net_cash_amount": 0,
                            "net_upi_count": 0,
                            "net_upi_amount": 0,
                            "net_ccdc_count": 0,
                            "net_ccdc_amount": 0,
                            "total_amount_count": 0,
                            "total_amount_amount": 0
                        },
                        "total_collection": {
                            "cash_mmrc_amount": 11560,
                            "upi_mmrc_amount": 8340,
                            "ccdc_mmrc_amount": 0,
                            "total_mmrc_amount": 19900,
                            "cash_bank_amount": 0,
                            "upi_bank_amount": 0,
                            "ccdc_bank_amount": 0,
                            "total_bank_amount": 0
                        }
                    }
                ],
                "message": "Success"
            }
        """.trimIndent().fromJson()
        val response = ApiManager.getCCService().getTomEosReport(tomEosRequest)
        if (!response.isSuccessful)
            throw Exception(response.errorBody()?.string() ?: "Failed to get tom report config from server")
        if (response.body() == null)
            throw Exception("Response body of TOM report is null")
        return response.body()!!
    }

    suspend fun getTvmEosReport(tvmEosRequest: TvmEosRequest): TvmEosResponse {
        return """
            {
                "data": [
                    {
                        "stn_id": "Aarey JVLR",
                        "eq_id": "010301",
                        "user_id": "74720",
                        "shift_id": 6,
                        "shift_start": "2025-09-26 00:00:00",
                        "shift_end": "2025-09-27 23:59:59",
                        "paper_qr": {
                            "paper_qr_sale_cash_count": 0,
                            "paper_qr_sale_cash_amount": 0,
                            "paper_qr_sale_upi_count": 0,
                            "paper_qr_sale_upi_amount": 0,
                            "paper_qr_sale_ccdc_count": 0,
                            "paper_qr_sale_ccdc_amount": 0,
                            "paper_qr_penalty_cash_count": 0,
                            "paper_qr_penalty_cash_amount": 0,
                            "paper_qr_penalty_upi_count": 0,
                            "paper_qr_penalty_upi_amount": 0,
                            "paper_qr_penalty_ccdc_count": 0,
                            "paper_qr_penalty_ccdc_amount": 0,
                            "paper_qr_refund_count": 0,
                            "paper_qr_refund_amount": 0
                        },
                        "ncmc_metro": {
                            "ncmc_metro_tp_sale_cash_count": 0,
                            "ncmc_metro_tp_sale_cash_amount": 0,
                            "ncmc_metro_tp_sale_upi_count": 0,
                            "ncmc_metro_tp_sale_upi_amount": 0,
                            "ncmc_metro_tp_sale_ccdc_count": 0,
                            "ncmc_metro_tp_sale_ccdc_amount": 0,
                            "ncmc_metro_tp_top_up_cash_count": 0,
                            "ncmc_metro_tp_top_up_cash_amount": 0,
                            "ncmc_metro_tp_top_up_upi_count": 0,
                            "ncmc_metro_tp_top_up_upi_amount": 0,
                            "ncmc_metro_tp_top_up_ccdc_count": 0,
                            "ncmc_metro_tp_top_up_ccdc_amount": 0,
                            "ncmc_metro_tp_refund_count": 0,
                            "ncmc_metro_tp_refund_amount": 0,
                            "ncmc_metro_tp_replace_count": 0,
                            "ncmc_metro_tp_replace_amount": 0,
                            "ncmc_metro_tp_penalty_cash_count": 0,
                            "ncmc_metro_tp_penalty_cash_amount": 0,
                            "ncmc_metro_tp_penalty_upi_count": 0,
                            "ncmc_metro_tp_penalty_upi_amount": 0,
                            "ncmc_metro_tp_penalty_ccdc_count": 0,
                            "ncmc_metro_tp_penalty_ccdc_amount": 0
                        },
                        "net_collection_pqr_and_ncmc_metro": {
                            "net_cash_count": 0,
                            "net_cash_amount": 0,
                            "net_upi_count": 0,
                            "net_upi_amount": 0,
                            "net_ccdc_count": 0,
                            "net_ccdc_amount": 0,
                            "total_amount_count": 0,
                            "total_amount_amount": 0
                        },
                        "ncmc_bank": {
                            "ncmc_bank_card_sale_cash_count": 0,
                            "ncmc_bank_card_sale_cash_amount": 0,
                            "ncmc_bank_card_sale_upi_count": 0,
                            "ncmc_bank_card_sale_upi_amount": 0,
                            "ncmc_bank_card_sale_ccdc_count": 0,
                            "ncmc_bank_card_sale_ccdc_amount": 0,
                            "ncmc_bank_card_replacement_cash_count": 0,
                            "ncmc_bank_card_replacement_cash_amount": 0,
                            "ncmc_bank_card_replacement_upi_count": 0,
                            "ncmc_bank_card_replacement_upi_amount": 0,
                            "ncmc_bank_card_replacement_card_count": 0,
                            "ncmc_bank_card_replacement_card_amount": 0,
                            "ncmc_bank_sv_topup_cash_count": 0,
                            "ncmc_bank_sv_topup_cash_amount": 0,
                            "ncmc_bank_sv_topup_upi_count": 0,
                            "ncmc_bank_sv_topup_upi_amount": 0,
                            "ncmc_bank_sv_topup_ccdc_count": 0,
                            "ncmc_bank_sv_topup_ccdc_amount": 0,
                            "ncmc_bank_sv_topup_balance_update_count": 0,
                            "ncmc_bank_sv_topup_balance_update_amount": 0
                        },
                        "net_collection_ncmc_bank": {
                            "net_cash_count": 0,
                            "net_cash_amount": 0,
                            "net_upi_count": 0,
                            "net_upi_amount": 0,
                            "net_ccdc_count": 0,
                            "net_ccdc_amount": 0,
                            "total_amount_count": 0,
                            "total_amount_amount": 0
                        },
                        "total_collection": {
                            "cash_mmrc_amount": 0,
                            "upi_mmrc_amount": 0,
                            "ccdc_mmrc_amount": 0,
                            "total_mmrc_amount": 0,
                            "cash_bank_amount": 0,
                            "upi_bank_amount": 0,
                            "ccdc_bank_amount": 0,
                            "total_bank_amount": 0
                        }
                    }
                ],
                "message": "Success"
            }
        """.trimIndent().fromJson()
        val response = ApiManager.getCCService().getTvmEosReport(tvmEosRequest)
        if (!response.isSuccessful)
            throw Exception(response.errorBody()?.string() ?: "Failed to get tvm report config from server")
        if (response.body() == null)
            throw Exception("Response body of TVM report is null")
        return response.body()!!
    }



    suspend fun getEquipmentsEOS(request: EndOfShiftsRequest): EndOfShiftsResponse {
        return """
            {
                "status": true,
                "message": "Shift summary fetched successfully.",
                "data": [
                    {
                        "shift_id": 87,
                        "stn_id": 10,
                        "eq_id": "100241",
                        "operator": "70742",
                        "start_date": "2025-11-16 06:21:23",
                        "end_date": "2025-11-16 06:23:25"
                    },
                    {
                        "shift_id": 88,
                        "stn_id": 10,
                        "eq_id": "100241",
                        "operator": "76832",
                        "start_date": "2025-11-16 06:34:19",
                        "end_date": "2025-11-16 14:12:42"
                    },
                    {
                        "shift_id": 89,
                        "stn_id": 10,
                        "eq_id": "100241",
                        "operator": "76085",
                        "start_date": "2025-11-16 15:32:41",
                        "end_date": "2025-11-16 20:38:15"
                    },
                    {
                        "shift_id": 90,
                        "stn_id": 10,
                        "eq_id": "100241",
                        "operator": "80244",
                        "start_date": "2025-11-16 22:16:51",
                        "end_date": "2025-11-16 22:56:43"
                    }
                ]
            }
        """.trimIndent().fromJson()
        /*val response = ApiManager.getCCService().getEquipmentsEOS(request)
        if (!response.isSuccessful)
            throw Exception(response.errorBody()?.string() ?: "Failed to get equipments EOS from server")
        if (response.body() == null)
            throw Exception("Response body of Equipments EOS is null")
        return response.body()!!*/
    }


}