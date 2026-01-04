package app.code.petshopandroidapp.data.model

data class InventoryTransaction(
    val id: String,
    val itemType: String,
    val itemId: String,
    val itemName: String,
    val type: String,
    val quantity: Int,
    val previousQuantity: Int,
    val newQuantity: Int,
    val reason: String?,
    val note: String?,
    val orderId: String?,
    val performedBy: String?,
    val createdAt: String
)

data class CreateInventoryTransactionRequest(
    val itemType: String,
    val itemId: String,
    val type: String,
    val quantity: Int,
    val reason: String? = null,
    val note: String? = null,
    val orderId: String? = null,
    val performedBy: String? = null
)

data class AdjustInventoryRequest(
    val itemType: String,
    val itemId: String,
    val newQuantity: Int,
    val reason: String? = null,
    val note: String? = null
)

data class InventoryAlert(
    val id: String,
    val name: String,
    val category: String,
    val currentQuantity: Int,
    val minThreshold: Int,
    val itemType: String
)

data class InventoryStats(
    val totalImports: Int,
    val totalExports: Int,
    val totalSales: Int,
    val totalAdjustments: Int,
    val importQuantity: Int,
    val exportQuantity: Int,
    val salesQuantity: Int
)
