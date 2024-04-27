package com.adts.app.network

import com.adts.app.model.*
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by Logictrix on 22-Oct-21.
 */
interface ApiInterface {

    @FormUrlEncoded
    @POST("Register")
    fun register(
        @Field("Id") id: String?,
        @Field("UserName") userName: String?,
        @Field("UserEmail") userEmail: String?,
        @Field("Password") password: String?,
        @Field("Mobile") mobile: String?,
        @Field("NewPassword") NewPassword : String?): Call<RegistrationModel>?

    @FormUrlEncoded
    @POST("Login")
        fun login(
        @Field("Mobile") mobile : String?,
        @Field("Password") password : String?,
        @Field("DeviceId")deviceId: String,
        @Field("DeviceType") devicetype : String? ): Call<RegistrationModel>?

    @FormUrlEncoded
    @POST("ForgotPassword")
    fun ForgotPassword(
        @Field("Email") email : String) : Call<RegistrationModel>?


    @FormUrlEncoded
    @POST("GoogleFacebookLoginSignUp")
    fun googleFacebookLoginSignUp(
            @Field("UserName") userName: String?,
            @Field("UserEmail") userEmail: String?,
            @Field("RoleName") roleName: String?,
            @Field("TokenId") tokenId: String?,
            @Field("DeviceId") deviceId:String?,
            @Field("DeviceType") deviceType:String?,
            @Field("Provider") provider:String?):Call<GFLoginModel>?


    @FormUrlEncoded
    @POST("PurchaseCourse")
    fun PurchaseCourse(
            @Field("RazorpayPaymentId") RazorpayPaymentId: String?,
            @Field("PaymentId") PaymentId: String?,
            @Field("LocalOrderId") LocalOrderId: String?,
            @Field("Amount") Amount: String?,
            @Field("PaymentStatus") PaymentStatus: String?,
            @Field("CourseId") CourseId:String?,
            @Field("UserId") UserId:String?,
            @Field("Reason") Reason:String?):Call<PurchaseCourseModel>?



    @GET("GetCourse")
    fun getCourse(@Query("userId") userId : String?) : Call<GetAllCoursesModel>?

    @GET("GetResources")
    fun getResources(): Call<GetAllResourcesModel>?

    @FormUrlEncoded
    @POST("ApplyCourse")
    fun applyCourse(
        @Field("UserId") userId : String?,
        @Field("CourseId") courseId : String?): Call<RegistrationModel>?

    @GET("GetArticle")
    fun getArticle(): Call<GetArticleModel>?

    @GET("MyAppliedCourse")
    fun myappliedcourse(
    @Query("UserId") userId : String?) : Call<AppliedCoursesModel>?

@GET("GetUserDetail")
fun getuserdetail(
    @Query("UserId") userId : String?) : Call<UserDetailModel>?


}