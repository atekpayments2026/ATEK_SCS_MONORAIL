package com.atek.scs.feature.domain.station_reporting.end_of_shift_report.configuration

import kotlinx.serialization.Serializable

@Serializable
data class TvmEosResponse(
    val data: List<Data>,
    val message: String
) {
    @Serializable
    data class Data(
        val eq_id: String,
        val ncmc_bank: NcmcBank,
        val ncmc_metro: NcmcMetro,
        val net_collection_ncmc_bank: NetCollectionNcmcBank,
        val net_collection_pqr_and_ncmc_metro: NetCollectionPqrAndNcmcMetro,
        val paper_qr: PaperQr,
        val shift_end: String,
        val shift_id: Long,
        val shift_start: String,
        val stn_id: String,
        val total_collection: TotalCollection,
        val user_id: String
    )

    @Serializable
    data class NcmcBank(
        val ncmc_bank_card_replacement_card_amount: String,
        val ncmc_bank_card_replacement_card_count: String,
        val ncmc_bank_card_replacement_cash_amount: String,
        val ncmc_bank_card_replacement_cash_count: String,
        val ncmc_bank_card_replacement_upi_amount: String,
        val ncmc_bank_card_replacement_upi_count: String,
        val ncmc_bank_card_sale_cash_amount: String,
        val ncmc_bank_card_sale_cash_count: String,
        val ncmc_bank_card_sale_ccdc_amount: String,
        val ncmc_bank_card_sale_ccdc_count: String,
        val ncmc_bank_card_sale_upi_amount: String,
        val ncmc_bank_card_sale_upi_count: String,
        val ncmc_bank_sv_topup_balance_update_amount: String,
        val ncmc_bank_sv_topup_balance_update_count: String,
        val ncmc_bank_sv_topup_cash_amount: String,
        val ncmc_bank_sv_topup_cash_count: String,
        val ncmc_bank_sv_topup_ccdc_amount: String,
        val ncmc_bank_sv_topup_ccdc_count: String,
        val ncmc_bank_sv_topup_upi_amount: String,
        val ncmc_bank_sv_topup_upi_count: String
    )

    @Serializable
    data class NcmcMetro( //
        val ncmc_metro_tp_penalty_cash_amount: String,
        val ncmc_metro_tp_penalty_cash_count: String,
        val ncmc_metro_tp_penalty_ccdc_amount: String,
        val ncmc_metro_tp_penalty_ccdc_count: String,
        val ncmc_metro_tp_penalty_upi_amount: String,
        val ncmc_metro_tp_penalty_upi_count: String,
        val ncmc_metro_tp_refund_amount: String,
        val ncmc_metro_tp_refund_count: String,
        val ncmc_metro_tp_replace_amount: String,
        val ncmc_metro_tp_replace_count: String,
        val ncmc_metro_tp_sale_cash_amount: String,
        val ncmc_metro_tp_sale_cash_count: String,
        val ncmc_metro_tp_sale_ccdc_amount: String,
        val ncmc_metro_tp_sale_ccdc_count: String,
        val ncmc_metro_tp_sale_upi_amount: String,
        val ncmc_metro_tp_sale_upi_count: String,
        val ncmc_metro_tp_top_up_cash_amount: String,
        val ncmc_metro_tp_top_up_cash_count: String,
        val ncmc_metro_tp_top_up_ccdc_amount: String,
        val ncmc_metro_tp_top_up_ccdc_count: String,
        val ncmc_metro_tp_top_up_upi_amount: String,
        val ncmc_metro_tp_top_up_upi_count: String
    ) //

    @Serializable
    data class NetCollectionNcmcBank( //
        val net_cash_amount: String,
        val net_cash_count: String,
        val net_ccdc_amount: String,
        val net_ccdc_count: String,
        val net_upi_amount: String,
        val net_upi_count: String,
        val total_amount_amount: String,
        val total_amount_count: String
    ) //

    @Serializable
    data class NetCollectionPqrAndNcmcMetro( //
        val net_cash_amount: String,
        val net_cash_count: String,
        val net_ccdc_amount: String,
        val net_ccdc_count: String,
        val net_upi_amount: String,
        val net_upi_count: String,
        val total_amount_amount: String,
        val total_amount_count: String
    ) //

    @Serializable
    data class PaperQr( //
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
    ) //

    @Serializable
    data class TotalCollection( //
        val cash_bank_amount: String,
        val cash_mmrc_amount: String,
        val ccdc_bank_amount: String,
        val ccdc_mmrc_amount: String,
        val total_bank_amount: String,
        val total_mmrc_amount: String,
        val upi_bank_amount: String,
        val upi_mmrc_amount: String
    ) //

    fun getTvmPaperQrData(index: Int): MutableMap<String, Pair<String, String>> {
        val paper = data[index].paper_qr
        val data = mutableMapOf<String, Pair<String, String>>()

        data["Sale - Cash"] = paper.paper_qr_sale_cash_count to paper.paper_qr_sale_cash_amount
        data["Sale - UPI"] = paper.paper_qr_sale_upi_count to paper.paper_qr_sale_upi_amount
        data["Sale - DC/CC"] = paper.paper_qr_sale_ccdc_count to paper.paper_qr_sale_ccdc_amount

        data["Penalty - Cash"] = paper.paper_qr_penalty_cash_count to paper.paper_qr_penalty_cash_amount
        data["Penalty - UPI"] = paper.paper_qr_penalty_upi_count to paper.paper_qr_penalty_upi_amount
        data["Penalty - DC/CC"] = paper.paper_qr_penalty_ccdc_count to paper.paper_qr_penalty_ccdc_amount

        data["Refund"] = "-${paper.paper_qr_refund_count}" to "-${paper.paper_qr_refund_amount}"

        return data
    }

    fun getTvmNcmcMetroData(index: Int): MutableMap<String, Pair<String, String>> {
        val metro = data[index].ncmc_metro
        val data = mutableMapOf<String, Pair<String, String>>()
        data["TP Sale - Cash"] = metro.ncmc_metro_tp_sale_cash_count to metro.ncmc_metro_tp_sale_cash_amount
        data["TP Sale - UPI"] = metro.ncmc_metro_tp_sale_upi_count to metro.ncmc_metro_tp_sale_upi_amount
        data["TP Sale - DC/CC"] = metro.ncmc_metro_tp_sale_ccdc_count to metro.ncmc_metro_tp_sale_ccdc_amount

        data["TP Topup - Cash"] = metro.ncmc_metro_tp_top_up_cash_count to metro.ncmc_metro_tp_top_up_cash_amount
        data["TP Topup - UPI"] = metro.ncmc_metro_tp_top_up_upi_count to metro.ncmc_metro_tp_top_up_upi_amount
        data["TP Topup - DC/CC"] = metro.ncmc_metro_tp_top_up_ccdc_count to metro.ncmc_metro_tp_top_up_ccdc_amount

        data["TP Refund"] = "-${metro.ncmc_metro_tp_refund_count}" to "-${metro.ncmc_metro_tp_refund_amount}"
        data["TP Replace"] = metro.ncmc_metro_tp_replace_count to metro.ncmc_metro_tp_replace_amount

        data["TP Penalty - Cash"] = metro.ncmc_metro_tp_penalty_cash_count to metro.ncmc_metro_tp_penalty_cash_amount
        data["TP Penalty - UPI"] = metro.ncmc_metro_tp_penalty_upi_count to metro.ncmc_metro_tp_penalty_upi_amount
        data["TP Penalty - DC/CC"] = metro.ncmc_metro_tp_penalty_ccdc_count to metro.ncmc_metro_tp_penalty_ccdc_amount


        return data
    }

    fun getTvmNetCollectionPqrAndMetro(index: Int): MutableMap<String, Pair<String, String>> {
        val net = data[index].net_collection_pqr_and_ncmc_metro
        val data = mutableMapOf<String, Pair<String, String>>()

        data["Net Cash"] = net.net_cash_count to net.net_cash_amount
        data["Net UPI"] = net.net_upi_count to net.net_upi_amount
        data["Net DC/CC"] = net.net_ccdc_count to net.net_ccdc_amount
        data["Total"] = net.total_amount_count to net.total_amount_amount

        return data
    }

    fun getTvmNcmcBankData(index: Int): MutableMap<String, Pair<String, String>> {
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

        data["Balance Update*"] =
            ncmcBankData.ncmc_bank_sv_topup_balance_update_count to ncmcBankData.ncmc_bank_sv_topup_balance_update_amount

        return data
    }


    fun getTvmNetCollectionNcmcBank(index: Int): MutableMap<String, Pair<String, String>> {
        val net = data[index].net_collection_ncmc_bank
        val data = mutableMapOf<String, Pair<String, String>>()

        data["Net Cash"] = net.net_cash_count to net.net_cash_amount
        data["Net UPI"] = net.net_upi_count to net.net_upi_amount
        data["Net DC/CC"] = net.net_ccdc_count to net.net_ccdc_amount
        data["Total"] = net.total_amount_count to net.total_amount_amount

        return data
    }

    fun getTvmTotalCollection(index: Int): MutableMap<String, Pair<String, String>> {
        val total = data[index].total_collection
        val data = mutableMapOf<String, Pair<String, String>>()

        data["Cash"] = total.cash_mmrc_amount to total.cash_bank_amount
        data["UPI - Bank"] = total.upi_mmrc_amount to total.upi_bank_amount
        data["DC/CC"] = total.ccdc_mmrc_amount to total.ccdc_bank_amount

        data["Total - Amount"] = total.total_mmrc_amount to total.total_bank_amount

        return data
    }


}