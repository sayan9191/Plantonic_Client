package co.`in`.plantonic.retrofit.models.pincode

data class PinCodeAvailableResponseModel(
    val detail: String,
    val is_delivery_possible: Boolean,
    val location_data : List<LocationData>
)