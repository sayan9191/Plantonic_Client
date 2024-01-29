package co.`in`.plantonic.retrofit.models.order

data class Data(
    val actual_order_quantity: Int,
    val bd_order_id: String,
    val created_at: String,
    val customer_address_type: String,
    val customer_full_address: String,
    val customer_id: Int,
    val customer_name: String,
    val customer_payment_method: String,
    val customer_phone_number: String,
    val customer_pincode: String,
    val delivery_charge: String,
    val merchant_id: String,
    val order_id: Int,
    val order_quantity: Int,
    val payable: String,
    val product_id: String,
    val product_listed_price: String,
    val related_to_order_id: String,
    val special_instructions: String,
    val transaction_id: String,
    val updated_at: Any
)