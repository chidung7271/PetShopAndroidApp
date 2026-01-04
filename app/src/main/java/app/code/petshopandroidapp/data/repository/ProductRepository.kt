package app.code.petshopandroidapp.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import app.code.petshopandroidapp.data.api.AuthApiService
import app.code.petshopandroidapp.data.api.ProductApiService
import app.code.petshopandroidapp.data.model.CreateProductResponse
import app.code.petshopandroidapp.data.model.LoginRequest
import app.code.petshopandroidapp.data.model.LoginResponse
import app.code.petshopandroidapp.data.model.ProductResponse
import app.code.petshopandroidapp.ui.model.Product
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productApiService: ProductApiService
) {
    suspend fun getAllProducts(): List<ProductResponse> {
        val response: Response<List<ProductResponse>> = productApiService.getAllProducts()
        return if (response.isSuccessful) {
            response.body() ?: emptyList()
        } else {
            emptyList()
        }

    }
    private fun prepareFilePart(uri: Uri, context: Context): MultipartBody.Part {
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(uri) ?: throw IllegalArgumentException("Cannot open input stream for URI")

        val tempFile = File.createTempFile("upload", ".jpg", context.cacheDir)
        tempFile.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream)
        }

        val requestFile = tempFile.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("image", tempFile.name, requestFile)
    }

    suspend fun addProduct(
        imageUri: Uri,
        name: String,
        category: String,
        quantity: Int,
        price: Int,
        des: String,
        context: Context
    ): CreateProductResponse {
        return try{
        val imagePart = prepareFilePart(imageUri, context)
        val response = productApiService.addProduct(
            image = imagePart,
            name = name.toRequestBody(),
            category = category.toRequestBody(),
            quantity = quantity.toString().toRequestBody(),
            price = price.toString().toRequestBody(),
            des = des.toRequestBody()
        )
        if (response.isSuccessful) {
             response.body()!!
        } else {
             CreateProductResponse(false, "Error: ${response.code()}")
        }
        }
        catch (e: Exception) {
             CreateProductResponse(false, "Exception: ${e.message}")
        }


    }

    suspend fun updateProduct(
        id: String,
        imageUri: Uri?, // nullable
        name: String?,
        category: String?,
        quantity: Int?,
        price: Int?,
        des: String?,
        context: Context
    ): CreateProductResponse {
        return try {
            // Chỉ chuẩn bị part nếu imageUri có
            val imagePart = imageUri?.let { prepareFilePart(it, context) }

            // Tạo multipart body chỉ khi dữ liệu có
            val response = productApiService.updateProduct(
                id = id,
                image = imagePart, // Có thể null
                name = name?.toRequestBody(), // Có thể null
                category = category?.toRequestBody(),
                quantity = quantity?.toString()?.toRequestBody(),
                price = price?.toString()?.toRequestBody(),
                des = des?.toRequestBody()
            )

            if (response.isSuccessful) {
                Log.d("ProductRepository", "updateProduct: ${response.body()}")
                response.body()!!
            } else {
                Log.d("ProductRepository", "updateProduct: ${response.code()}")
                CreateProductResponse(false, "Error: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.d("ProductRepository", "updateProduct: ${e.message}")
            CreateProductResponse(false, "Exception: ${e.message}")
        }
    }

    suspend fun deleteProduct(id: String): CreateProductResponse {
        val response = productApiService.deleteProduct(id)
        return if (response.isSuccessful) {
            Log.d("ProductRepository", "deleteProduct: ${response.body()}")
            response.body()!!
        } else {
            Log.d("ProductRepository", "deleteProduct: ${response.code()}")
            CreateProductResponse(false, "Error: ${response.code()}")
        }
    }

    suspend fun getProductById(id: String): ProductResponse {
        val response = productApiService.getProductById(id)
        return if (response.isSuccessful) {
            Log.d("ProductRepository", "getProductById: ${response.body()}")
            response.body()!!
        } else {
            Log.d("ProductRepository", "getProductById: ${response.code()}")
            ProductResponse("", "", "", 0, 0, "", "", "")
        }
    }

}