package co.`in`.plantonic.retrofit.models.track

data class Shipment(
    val Destination: String,
    val Origin: String,
    val Scans: Scans,
    val Status: String,
    val StatusDate: String,
    val StatusTime: String,
    val StatusType: String,
    val WaybillNo: String
)