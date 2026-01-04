package app.code.petshopandroidapp.data.repository

import android.util.Log
import app.code.petshopandroidapp.data.api.PetApiService
import app.code.petshopandroidapp.data.model.CreatePetResponse
import app.code.petshopandroidapp.data.model.PetResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class PetRepository @Inject constructor(
    private val petApiService: PetApiService
) {
    suspend fun getAllPets(): Result<List<PetResponse>> {
        return try {
            Log.d("PetRepository", "Calling getAllPets API")
            val response = petApiService.getAllPets()
            Log.d("PetRepository", "getAllPets response: isSuccessful=${response.isSuccessful}, code=${response.code()}")
            
            if (response.isSuccessful) {
                val pets = response.body() ?: emptyList()
                Log.d("PetRepository", "getAllPets success: ${pets.size} pets received")
                Result.success(pets)
            } else {
                Log.d("PetRepository", "getAllPets failed: ${response.errorBody()?.string()}")
                Result.failure(Exception("Lỗi khi tải danh sách thú cưng"))
            }
        } catch (e: Exception) {
            Log.e("PetRepository", "getAllPets error", e)
            Result.failure(e)
        }
    }

    suspend fun createPet(
        name: String,
        ownerId: String,
        type: String,
        breed: String,
        weight: Double,
        des: String,
        isActive: Boolean = true,
        imageFile: File
    ): Result<CreatePetResponse> {
        return try {
            val nameBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
            val ownerIdBody = ownerId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val typeBody = type.toRequestBody("text/plain".toMediaTypeOrNull())
            val breedBody = breed.toRequestBody("text/plain".toMediaTypeOrNull())
            val weightBody = weight.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val desBody = des.toRequestBody("text/plain".toMediaTypeOrNull())
            val isActiveBody = isActive.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val imageRequestBody = imageFile.readBytes().toRequestBody("image/*".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, imageRequestBody)

            val response = petApiService.createPet(
                name = nameBody,
                ownerId = ownerIdBody,
                type = typeBody,
                breed = breedBody,
                weight = weightBody,
                des = desBody,
                isActive = isActiveBody,
                image = imagePart
            )

            if (response.isSuccessful) {
                Result.success(response.body() ?: CreatePetResponse(true,"Thêm thú cưng thành công"))
            } else {
                Result.failure(Exception("Lỗi khi thêm thú cưng"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deletePet(id: String): Result<CreatePetResponse> {
        return try {
            val response = petApiService.deletePet(id)
            if (response.isSuccessful) {
                Result.success(response.body() ?: CreatePetResponse(true,"Xóa thú cưng thành công"))
            } else {
                Result.failure(Exception("Lỗi khi xóa thú cưng"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 