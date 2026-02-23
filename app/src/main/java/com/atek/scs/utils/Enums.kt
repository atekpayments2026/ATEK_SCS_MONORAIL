package com.atek.scs.utils

import cafe.adriel.voyager.core.screen.Screen
import com.atek.scs.feature.domain.station_reporting.end_of_shift_report.model.TomEosInputScreen
import com.atek.scs.feature.domain.station_reporting.end_of_shift_report.model.TvmEosInputScreen
import com.atek.scs.feature.domain.station_reporting.end_of_shifts.EndOfShiftsInputScreen


enum class EquipmentType(val id: Long) {
    AG(1),
    TOM(2),
    TVM(3),
    SCS(4),
    TR(5);
    companion object {
        fun get(value: Long): EquipmentType {
            return entries.find { it.id == value }
                ?: throw IllegalArgumentException("Unknown equipment type")
        }
    }
}

enum class AuditOperations(val id: Long) {

    // USER OPERATIONS
    LOGIN(2),
    LOGOUT(3),

    // LAYOUT OPERATIONS
    CHANGE_LAYOUT(1),

    // CHANGE EQUIPMENT MODE
    EQUIPMENT_MODE_CHANGE(4),

    // SPECIAL MODE OPERATIONS
    IN_SERVICE(5),
    OUT_OF_SERVICE(6),
    EMERGENCY_START(7),
    EMERGENCY_END(8),
    STATION_CLOSE(9),
    MAINTENANCE(10),

    // OVERRIDE OPTIONS
    OVERRIDE_APPLIED(11);

    companion object {
        fun get(value: String) = entries.find { it.name == value } ?: throw IllegalArgumentException("Unknown audit operation type!")
        fun get(id: Long) = entries.find { it.id == id } ?: throw IllegalArgumentException("Unknown audit operation type!")
    }

}


/**
 * Enum representing the mode of a gate.
 * Modes include ENTRY, EXIT, BI_DI (Bi-directional), and EMERGENCY.
 * Each mode is associated with a Long value for internal processing and a display name.
 */
enum class GateMode(val id: Long, val displayName: String) {
    ENTRY(1, "ENTRY"),                      // Entry gate mode
    EXIT(2, "EXIT"),                        // Exit gate mode
    BI_DI(3, "BI DI");                      // Bi-directional gate mode
    companion object {
        /**
         * Retrieves a [GateMode] by its associated [mode] value.
         *
         * @param mode The [Long] value representing the gate mode.
         * @return The corresponding [GateMode] or null if not found.
         */
        fun get(mode: Long): GateMode? = entries.find { it.id == mode }
        fun get(mode: String): GateMode? = entries.find { it.name == mode }
    }
}

/*enum class ServiceMode(val mode: Long) {
    IN_SERVICE(7),
    OUT_OF_SERVICE(5),
    EMERGENCY_START(6),
    MAINTENANCE(8);
    companion object {

        fun get(mode: Long): ServiceMode? = entries.find { it.mode == mode }
        fun get(mode: String): ServiceMode? = entries.find { it.name == mode }
    }
}*/

/**
 * Enum representing media types associated with ticketing.
 * Media types include OL (Open Loop), CL (Close Loop), MQR (Mobile Ticket), PQR (Paper Ticket), and WQR (WhatsApp Ticket).
 */
enum class MediaType(val id: Long, val displayName: String) {
    OL(1, "OPEN LOOP"),
    CL(2, "CLOSE LOOP"),
    MQR(3, "MOBILE TICKET"),
    PQR(4, "PAPER TICKET"),
    WQR(5, "WHATSAPP TICKET");

    companion object {
        /**
         * Retrieves a [MediaType] by its associated [id] value.
         *
         * @param id The [Long] value representing the media type.
         * @return The corresponding [MediaType] or null if not found.
         */
        fun get(id: Long) = entries.find { it.id == id }
    }
}


/**
 * Enum representing different types of gates.
 * Supported types are INDRA and GUNEBO, which are mapped to specific gate hardware manufacturers.
 */
enum class GateType {
    INDRA,  // Gate type: INDRA
    GUNEBO, // Gate type: GUNEBO
    PULOON;

    companion object {
        /**
         * Retrieves a [GateType] by its string representation.
         *
         * @param type The [String] representing the gate type.
         * @return The corresponding [GateType] or null if not found.
         */
        fun get(type: String): GateType? = entries.find { it.name == type }
    }
}


enum class ServiceMode(val id: Long) {
    IN_SERVICE(1),
    OUT_OF_SERVICE(2),
    EMERGENCY_START(3),
    EMERGENCY_STOP(4),
    MAINTENANCE(5),
    STATION_CLOSED(6);
    companion object {
        fun get(mode: Long): ServiceMode? = entries.find { it.id == mode }
        fun get(mode: String): ServiceMode? = entries.find { it.name == mode }
    }
}

enum class SelectedScreen(val id: Int, val displayName: String, val onClick: Screen) {

    TVM(3, "TVM END OF SHIFT", TvmEosInputScreen()),
    TOM(4, "TOM/EFO END OF SHIFT REPORT", TomEosInputScreen()),
    EOS(5, "EQUIPMENTS END OF SHIFTS", EndOfShiftsInputScreen())
}