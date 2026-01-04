package app.code.petshopandroidapp.data.api

import app.code.petshopandroidapp.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface InventoryApiService {
    @GET("inventory/transactions")
    suspend fun getAllTransactions(
        @Query("itemType") itemType: String? = null,
        @Query("itemId") itemId: String? = null,
        @Query("type") type: String? = null,
        @Query("limit") limit: Int? = 100
    ): Response<List<InventoryTransaction>>

    @GET("inventory/transactions/{itemType}/{itemId}")
    suspend fun getTransactionsByItem(
        @Path("itemType") itemType: String,
        @Path("itemId") itemId: String
    ): Response<List<InventoryTransaction>>

    @POST("inventory/import")
    suspend fun importStock(
        @Body request: CreateInventoryTransactionRequest
    ): Response<InventoryTransaction>

    @POST("inventory/export")
    suspend fun exportStock(
        @Body request: CreateInventoryTransactionRequest
    ): Response<InventoryTransaction>

    @PATCH("inventory/adjust")
    suspend fun adjustStock(
        @Body request: AdjustInventoryRequest
    ): Response<InventoryTransaction>

    @GET("inventory/alerts")
    suspend fun getLowStockAlerts(
        @Query("threshold") threshold: Int? = 10
    ): Response<List<InventoryAlert>>

    @GET("inventory/stats")
    suspend fun getInventoryStats(
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null
    ): Response<InventoryStats>
}
