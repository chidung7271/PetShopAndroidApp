package app.code.petshopandroidapp.data.api

import app.code.petshopandroidapp.data.model.CreatePetResponse
import app.code.petshopandroidapp.data.model.PetResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface PetApiService {
    @GET("pet")
    suspend fun getAllPets(): Response<List<PetResponse>>

    @Multipart
    @POST("pet")
    suspend fun createPet(
        @Part image: MultipartBody.Part,
        @Part("ownerId") ownerId: RequestBody,
        @Part("name") name: RequestBody,
        @Part("type") type: RequestBody,
        @Part("breed") breed: RequestBody,
        @Part("weight") weight: RequestBody,
        @Part("des") des: RequestBody,
        @Part("isActive") isActive: RequestBody,
    ): Response<CreatePetResponse>

    @DELETE("pet/{id}")
    suspend fun deletePet(@Path("id") id: String): Response<CreatePetResponse>
    
}