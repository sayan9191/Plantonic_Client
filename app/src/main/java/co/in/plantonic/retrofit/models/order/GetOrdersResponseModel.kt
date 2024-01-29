package co.`in`.plantonic.retrofit.models.order

data class GetOrdersResponseModel(
    val current_page: Int,
    val `data`: List<Data>,
    val next_page: Int?,
    val prev_page: Int?,
    val total_page: Int,
    val total_count: Int
)