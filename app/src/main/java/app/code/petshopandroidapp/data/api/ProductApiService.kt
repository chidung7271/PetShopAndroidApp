package app.code.petshopandroidapp.data.api


import app.code.petshopandroidapp.data.model.CreateProductResponse
import app.code.petshopandroidapp.data.model.ProductResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path


interface ProductApiService {
    @GET("product")
    suspend fun getAllProducts(): Response<List<ProductResponse>>

    @Multipart
    @POST("product")
    suspend fun addProduct(
        @Part image: MultipartBody.Part,
        @Part("name") name: RequestBody,
        @Part("category") category: RequestBody,
        @Part("quantity") quantity: RequestBody,
        @Part("price") price: RequestBody,
        @Part("des") des: RequestBody

    ): Response<CreateProductResponse>

    @Multipart
    @PATCH("product/{id}")
    suspend fun updateProduct(
        @Path("id") id: String,
        @Part image: MultipartBody.Part?,
        @Part("name") name: RequestBody?,
        @Part("category") category: RequestBody?,
        @Part("quantity") quantity: RequestBody?,
        @Part("price") price: RequestBody?,
        @Part("des") des: RequestBody?
    ): Response<CreateProductResponse>

    @DELETE("product/{id}")
    suspend fun deleteProduct(@Path("id") id: String): Response<CreateProductResponse>

    @GET("product/{id}")
    suspend fun getProductById(@Path("id") id: String): Response<ProductResponse>

}