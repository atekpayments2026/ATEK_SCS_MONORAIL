package com.atek.scs.feature.domain.station_reporting.repo/*
package com.atek.scs.features.domain.station_reporting.repo

import com.google.gson.*
import java.lang.reflect.Type

class StationIdAdapter : JsonDeserializer<StationId>, JsonSerializer<StationId> {

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): StationId {
        return when {
            json.isJsonPrimitive && json.asJsonPrimitive.isNumber -> StationId.Number(json.asInt)
            json.isJsonObject -> {
                val obj = json.asJsonObject
                StationId.Operator(
                    operatorId = obj["operator_id"].asInt,
                    operatorName = obj["operator_name"].asString
                )
            }
            else -> throw JsonParseException("Invalid station_id format: $json")
        }
    }

    override fun serialize(src: StationId, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return when (src) {
            is StationId.Number -> JsonPrimitive(src.value)
            is StationId.Operator -> JsonObject().apply {
                addProperty("operator_id", src.operatorId)
                addProperty("operator_name", src.operatorName)
            }
        }
    }
}
*/
