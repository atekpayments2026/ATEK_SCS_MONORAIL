package com.atek.scs.feature.domain.station_reporting.end_of_shift_report.configuration

import kotlinx.serialization.Serializable

@Serializable
data class TomEosResponse(
    val data: List<ShiftReport>,
    val message: String
) {
    @Serializable
    data class ShiftReport(
        val stn_id: String,
        val eq_id: String,
        val user_id: String,
        val shift_id: Long,
        val shift_start: String,
        val shift_end: String,
        val paper_qr: PaperQr,
        val ncmc_metro: NcmcMetro,
        val net_collection_pqr_and_ncmc_metro: NetCollectionPqrMetro,
        val ncmc_bank: NcmcBank,
        val net_collection_ncmc_bank: NetCollectionNcmcBank,
        val total_collection: TotalCollection
    )

    @Serializable
    data class PaperQr(
        val paper_qr_sale_cash_count: String,
        val paper_qr_sale_cash_amount: String,
        val paper_qr_sale_upi_count: String,
        val paper_qr_sale_upi_amount: String,
        val paper_qr_sale_ccdc_count: String,
        val paper_qr_sale_ccdc_amount: String,
        val paper_qr_penalty_cash_count: String,
        val paper_qr_penalty_cash_amount: String,
        val paper_qr_penalty_upi_count: String,
        val paper_qr_penalty_upi_amount: String,
        val paper_qr_penalty_ccdc_count: String,
        val paper_qr_penalty_ccdc_amount: String,
        val paper_qr_refund_count: String,
        val paper_qr_refund_amount: String
    )

    @Serializable
    data class NcmcMetro(
        val ncmc_metro_tp_sale_cash_count: String,
        val ncmc_metro_tp_sale_cash_amount: String,
        val ncmc_metro_tp_sale_upi_count: String,
        val ncmc_metro_tp_sale_upi_amount: String,
        val ncmc_metro_tp_sale_ccdc_count: String,
        val ncmc_metro_tp_sale_ccdc_amount: String,
        val ncmc_metro_tp_top_up_cash_count: String,
        val ncmc_metro_tp_top_up_cash_amount: String,
        val ncmc_metro_tp_top_up_upi_count: String,
        val ncmc_metro_tp_top_up_upi_amount: String,
        val ncmc_metro_tp_top_up_ccdc_count: String,
        val ncmc_metro_tp_top_up_ccdc_amount: String,
        val ncmc_metro_tp_refund_count: String,
        val ncmc_metro_tp_refund_amount: String,
        val ncmc_metro_tp_replace_count: String,
        val ncmc_metro_tp_replace_amount: String,
        val ncmc_metro_tp_penalty_cash_count: String,
        val ncmc_metro_tp_penalty_cash_amount: String,
        val ncmc_metro_tp_penalty_upi_count: String,
        val ncmc_metro_tp_penalty_upi_amount: String,
        val ncmc_metro_tp_penalty_ccdc_count: String,
        val ncmc_metro_tp_penalty_ccdc_amount: String
    )

    @Serializable
    data class NetCollectionPqrMetro(
        val net_cash_count: String,
        val net_cash_amount: String,
        val net_upi_count: String,
        val net_upi_amount: String,
        val net_ccdc_count: String,
        val net_ccdc_amount: String,
        val total_amount_count: String,
        val total_amount_amount: String
    )

    @Serializable
    data class NcmcBank(
        val ncmc_bank_card_sale_cash_count: String,
        val ncmc_bank_card_sale_cash_amount: String,
        val ncmc_bank_card_sale_upi_count: String,
        val ncmc_bank_card_sale_upi_amount: String,
        val ncmc_bank_card_sale_ccdc_count: String,
        val ncmc_bank_card_sale_ccdc_amount: String,
        val ncmc_bank_card_replacement_cash_count: String,
        val ncmc_bank_card_replacement_cash_amount: String,
        val ncmc_bank_card_replacement_upi_count: String,
        val ncmc_bank_card_replacement_upi_amount: String,
        val ncmc_bank_card_replacement_card_count: String,
        val ncmc_bank_card_replacement_card_amount: String,
        val ncmc_bank_sv_topup_cash_count: String,
        val ncmc_bank_sv_topup_cash_amount: String,
        val ncmc_bank_sv_topup_upi_count: String,
        val ncmc_bank_sv_topup_upi_amount: String,
        val ncmc_bank_sv_topup_ccdc_count: String,
        val ncmc_bank_sv_topup_ccdc_amount: String,
        val ncmc_bank_sv_topup_balance_update_count: String,
        val ncmc_bank_sv_topup_balance_update_amount: String
    )

    @Serializable
    data class NetCollectionNcmcBank(
        val net_cash_count: String,
        val net_cash_amount: String,
        val net_upi_count: String,
        val net_upi_amount: String,
        val net_ccdc_count: String,
        val net_ccdc_amount: String,
        val total_amount_count: String,
        val total_amount_amount: String
    )

    @Serializable
    data class TotalCollection(
        val cash_mmrc_amount: String,
        val upi_mmrc_amount: String,
        val ccdc_mmrc_amount: String,
        val total_mmrc_amount: String,
        val cash_bank_amount: String,
        val upi_bank_amount: String,
        val ccdc_bank_amount: String,
        val total_bank_amount: String
    )

    fun getTomPaperData(index: Int): MutableMap<String, Pair<String, String>> {
        val paperData = data[index].paper_qr
        val data = mutableMapOf<String, Pair<String, String>>()

        data["Sale - Cash"] = paperData.paper_qr_sale_cash_count to paperData.paper_qr_sale_cash_amount
        data["Sale - UPI"] = paperData.paper_qr_sale_upi_count to paperData.paper_qr_sale_upi_amount
        data["Sale - DC/CC"] = paperData.paper_qr_sale_ccdc_count to paperData.paper_qr_sale_ccdc_amount

        data["Penalty - Cash"] = paperData.paper_qr_penalty_cash_count to paperData.paper_qr_penalty_cash_amount
        data["Penalty - UPI"] = paperData.paper_qr_penalty_upi_count to paperData.paper_qr_penalty_upi_amount
        data["Penalty - DC/CC"] = paperData.paper_qr_penalty_ccdc_count to paperData.paper_qr_penalty_ccdc_amount

        data["Refund"] = "-${paperData.paper_qr_refund_count}" to "-${paperData.paper_qr_refund_amount}"

        return data
    }

    fun getTomNcmcMetroData(index: Int): MutableMap<String, Pair<String, String>> {
        val ncmcData = data[index].ncmc_metro
        val data = mutableMapOf<String, Pair<String, String>>()

        data["TP Sale - Cash"] = ncmcData.ncmc_metro_tp_sale_cash_count to ncmcData.ncmc_metro_tp_sale_cash_amount
        data["TP Sale - UPI"] = ncmcData.ncmc_metro_tp_sale_upi_count to ncmcData.ncmc_metro_tp_sale_upi_amount
        data["TP Sale - DC/CC"] = ncmcData.ncmc_metro_tp_sale_ccdc_count to ncmcData.ncmc_metro_tp_sale_ccdc_amount

        data["TP Topup - Cash"] = ncmcData.ncmc_metro_tp_top_up_cash_count to ncmcData.ncmc_metro_tp_top_up_cash_amount
        data["TP Topup - UPI"] = ncmcData.ncmc_metro_tp_top_up_upi_count to ncmcData.ncmc_metro_tp_top_up_upi_amount
        data["TP Topup - DC/CC"] = ncmcData.ncmc_metro_tp_top_up_ccdc_count to ncmcData.ncmc_metro_tp_top_up_ccdc_amount

        data["TP Refund"] = "-${ncmcData.ncmc_metro_tp_refund_count}" to "-${ncmcData.ncmc_metro_tp_refund_amount}"
        data["TP Replace"] = ncmcData.ncmc_metro_tp_replace_count to ncmcData.ncmc_metro_tp_replace_amount

        data["Penalty - Cash"] = ncmcData.ncmc_metro_tp_penalty_cash_count to ncmcData.ncmc_metro_tp_penalty_cash_amount
        data["Penalty - UPI"] = ncmcData.ncmc_metro_tp_penalty_upi_count to ncmcData.ncmc_metro_tp_penalty_upi_amount
        data["Penalty - DC/CC"] = ncmcData.ncmc_metro_tp_penalty_ccdc_count to ncmcData.ncmc_metro_tp_penalty_ccdc_amount

        return data
    }

    fun getTomPaperAndNcmcNetColletion(index: Int): MutableMap<String, Pair<String, String>> {
        val paperNcmcNet = data[index].net_collection_pqr_and_ncmc_metro
        val data = mutableMapOf<String, Pair<String, String>>()

        data["Net Cash"] = paperNcmcNet.net_cash_count to paperNcmcNet.net_cash_amount
        data["Net UPI"] = paperNcmcNet.net_upi_count to paperNcmcNet.net_upi_amount
        data["Net DC/CC"] = paperNcmcNet.net_ccdc_count to paperNcmcNet.net_ccdc_amount
        data["Total Amount"] = paperNcmcNet.total_amount_count to paperNcmcNet.total_amount_amount

        return data
    }

    fun getTomNcmcBankData(index: Int): MutableMap<String, Pair<String, String>> {
        val ncmcBankData = data[index].ncmc_bank
        val data = mutableMapOf<String, Pair<String, String>>()

        data["CSC Sale - Cash"] =
            ncmcBankData.ncmc_bank_card_sale_cash_count to ncmcBankData.ncmc_bank_card_sale_cash_amount
        data["CSC Sale - UPI"] =
            ncmcBankData.ncmc_bank_card_sale_upi_count to ncmcBankData.ncmc_bank_card_sale_upi_amount
        data["CSC Sale - DC/CC"] =
            ncmcBankData.ncmc_bank_card_sale_ccdc_count to ncmcBankData.ncmc_bank_card_sale_ccdc_amount

        data["CSC Replace - Cash"] =
            ncmcBankData.ncmc_bank_card_replacement_cash_count to ncmcBankData.ncmc_bank_card_replacement_cash_amount
        data["CSC Replace - UPI"] =
            ncmcBankData.ncmc_bank_card_replacement_upi_count to ncmcBankData.ncmc_bank_card_replacement_upi_amount
        data["CSC Replace - DC/CC"] =
            ncmcBankData.ncmc_bank_card_replacement_card_count to ncmcBankData.ncmc_bank_card_replacement_card_amount

        data["SV Topup - Cash"] =
            ncmcBankData.ncmc_bank_sv_topup_cash_count to ncmcBankData.ncmc_bank_sv_topup_cash_amount
        data["SV Topup - UPI"] = ncmcBankData.ncmc_bank_sv_topup_upi_count to ncmcBankData.ncmc_bank_sv_topup_upi_amount
        data["SV Topup - DC/CC"] =
            ncmcBankData.ncmc_bank_sv_topup_ccdc_count to ncmcBankData.ncmc_bank_sv_topup_ccdc_amount

        data["SU Bal Update*"] =
            ncmcBankData.ncmc_bank_sv_topup_balance_update_count to ncmcBankData.ncmc_bank_sv_topup_balance_update_amount

        return data
    }


    fun getTomNetCollectionNcmcBank(index: Int): MutableMap<String, Pair<String, String>> {
        val ncmcNet = data[index].net_collection_ncmc_bank
        val data = mutableMapOf<String, Pair<String, String>>()

        data["Net Cash"] = ncmcNet.net_cash_count to ncmcNet.net_cash_amount
        data["Net UPI"] = ncmcNet.net_upi_count to ncmcNet.net_upi_amount
        data["Net DC/CC"] = ncmcNet.net_ccdc_count to ncmcNet.net_ccdc_amount
        data["Total Amount"] = ncmcNet.total_amount_count to ncmcNet.total_amount_amount

        return data
    }

    fun getTomTotalCollection(index: Int): MutableMap<String, Pair<String, String>> {
        val total = data[index].total_collection
        val data = mutableMapOf<String, Pair<String, String>>()

        data["Cash"] = total.cash_mmrc_amount to total.cash_bank_amount
        data["UPI"] = total.upi_mmrc_amount to total.upi_bank_amount
        data["DC/CC"] = total.ccdc_mmrc_amount to total.ccdc_bank_amount
        data["Total Amount"] = total.total_mmrc_amount to total.total_bank_amount

        return data
    }

}