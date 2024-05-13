package com.doaamosallam.data.data


import com.doaamosallam.domain.model.products.AddOrDeleteFavoriteModel
import com.doaamosallam.domain.model.products.CartModel
import com.doaamosallam.domain.model.products.CartModelAddToCart
import com.doaamosallam.domain.model.products.CategoryDetailsModel
import com.doaamosallam.domain.model.products.CategoryModel
import com.doaamosallam.domain.model.products.EditQtyModel
import com.doaamosallam.domain.model.products.FavoritesModel
import com.doaamosallam.domain.model.products.LogoutModel
import com.doaamosallam.domain.model.products.RegisterModel
import com.doaamosallam.domain.model.products.SearchModel
import com.doaamosallam.domain.model.products.UserModel
import com.doaamosallam.domain.model.request.HomeDataResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/*This class interface request API*/
interface APIService {



    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
        @Header("lang") lang: String
    ): UserModel

    //@Headers("lang:en","Content-Type:application/json")
    @POST("register")
    suspend fun register(
        @Body model: RegisterModel,
        @Header("lang") lang: String,
    ): UserModel
    @GET("home")
    suspend fun getAllHomeData(
        @Header("lang") lang:String,
        @Header("Authorization") authorization:String
    ):HomeDataResponse
    @FormUrlEncoded
    @POST("favorites")
    suspend fun addOrDeleteFavorite(
        @Field("product_id") id: Int,
        @Header("lang") lang: String,
        @Header("Authorization") authorization: String
    ): AddOrDeleteFavoriteModel

    @FormUrlEncoded
    @POST("products/search")
    suspend fun searchProducts(
        @Field("text") text: String,
        @Header("lang") lang: String,
        @Header("Authorization") authorization: String
    ): SearchModel

    @GET("carts")
    suspend fun getCartData(
        @Header("lang") lang: String,
        @Header("Authorization") authorization: String
    ): CartModel

    @FormUrlEncoded
    @POST("carts")
    suspend fun addToCart(
        @Field("product_id") productId: Int,
        @Header("lang") lang: String,
        @Header("Authorization") authorization: String
    ): CartModelAddToCart

    @FormUrlEncoded
    @PUT("carts/{id}")
    suspend fun editQty(
        @Path("id") id: Int,
        @Field("quantity") qty: Int,
        @Header("lang") lang: String,
        @Header("Authorization") authorization: String
    ): EditQtyModel

    @GET("categories")
    suspend fun getCategoryData(@Header("lang") lang: String): CategoryModel

    @GET("categories/{id}")
    suspend fun getCategoryDetails(
        @Path("id") id: Int,
        @Header("lang") lang: String,
        @Header("Authorization") authorization: String
    ): CategoryDetailsModel

    @FormUrlEncoded
    @POST("logout")
    suspend fun logOut(
        @Field("fcm_token") fcmToken: String,
        @Header("lang") lang: String,
        @Header("Authorization") authorization: String
    ): LogoutModel

    @FormUrlEncoded
    @PUT("update-profile")
    suspend fun updateProfile(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("phone") phone: String,
        @Header("lang") lang: String,
        @Header("Authorization") authorization: String
    ): UserModel

    @GET("favorites")
    suspend fun getFavorites(
        @Header("lang") lang: String,
        @Header("Authorization") authorization: String
    ): FavoritesModel

    @FormUrlEncoded
    @POST("change-password")
    suspend fun changePass(
        @Field("current_password") currentPass: String,
        @Field("new_password") newPass: String,
        @Header("lang") lang: String,
        @Header("Authorization") authorization: String
    ): LogoutModel
}