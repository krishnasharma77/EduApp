package com.adts.app.network


import com.adts.app.model.*
import com.adts.app.network.ApiClient.client
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field

/**
 * Created by Logictrix on 22-Oct-21.
 */
class ApiCall<Req, Res> private constructor() {


    companion object {
        var apiCall: ApiCall<*, *>? = null
        var service: ApiInterface? = null
        val instance: ApiCall<*, *>?
            get() {
                if (apiCall == null) {
                    apiCall = ApiCall<Any?, Any?>()
                    service = client
                }
                return apiCall
            }
    }

    fun register(
        id: String,
        userName: String,
        userEmail: String,
        password: String,
        mobile: String,
        NewPassword: String,
        iApiCallback: ApiCallback
    ) {
        val call: Call<RegistrationModel> =
            service!!.register(id,userName,userEmail,password,mobile,NewPassword)!!
        call.enqueue(object : Callback<RegistrationModel> {
            override fun onResponse(
                call: Call<RegistrationModel>,
                response: Response<RegistrationModel>
            ) {
                iApiCallback.onSuccess("signup", response)
            }

            override fun onFailure(call: Call<RegistrationModel>, t: Throwable) {
                iApiCallback.onFailure(t)
            }

        })
    }

    fun login(
        userEmail: String,
        password: String,
        deviceId: String,
        deviceType: String,
        iApiCallback: ApiCallback
    ) {
        val call: Call<RegistrationModel> =
            service!!.login(userEmail,password,deviceId,deviceType)!!
        call.enqueue(object : Callback<RegistrationModel> {
            override fun onResponse(
                call: Call<RegistrationModel>,
                response: Response<RegistrationModel>
            ) {
                iApiCallback.onSuccess("login", response)
            }

            override fun onFailure(call: Call<RegistrationModel>, t: Throwable) {
                iApiCallback.onFailure(t)
            }
        })

    }
    fun GetCourse(
        userId: String,
        iApiCallback: ApiCallback
    ) {
        val call: Call<GetAllCoursesModel> =
            service!!.getCourse(userId)!!
        call.enqueue(object : Callback<GetAllCoursesModel> {
            override fun onResponse(
                call: Call<GetAllCoursesModel>,
                response: Response<GetAllCoursesModel>
            ) {
                iApiCallback.onSuccess("allCourse", response)
            }

            override fun onFailure(call: Call<GetAllCoursesModel>, t: Throwable) {
                iApiCallback.onFailure(t)
            }

        })
    }

    fun getResources(
        iApiCallback: ApiCallback
    ) {
        val call: Call<GetAllResourcesModel> =
            service!!.getResources()!!
        call.enqueue(object : Callback<GetAllResourcesModel> {
            override fun onResponse(
                call: Call<GetAllResourcesModel>,
                response: Response<GetAllResourcesModel>
            ) {
                iApiCallback.onSuccess("allresourses", response)
            }

            override fun onFailure(call: Call<GetAllResourcesModel>, t: Throwable) {
                iApiCallback.onFailure(t)
            }

        })
    }

    fun applyCourse(
        userId: String,
        courseId: String,
        iApiCallback: ApiCallback
    ) {
        val call: Call<RegistrationModel> =
            service!!.applyCourse(userId,courseId)!!
        call.enqueue(object : Callback<RegistrationModel> {
            override fun onResponse(
                call: Call<RegistrationModel>,
                response: Response<RegistrationModel>
            ) {
                iApiCallback.onSuccess("buyCourse", response)
            }

            override fun onFailure(call: Call<RegistrationModel>, t: Throwable) {
                iApiCallback.onFailure(t)
            }

        })
    }


    fun PurchaseCourse(
        RazorpayPaymentId: String,
        PaymentId: String,
        LocalOrderId: String,
        Amount: String,
        PaymentStatus: String,
        CourseId: String,
        UserId: String,
        Reason: String,
        iApiCallback: ApiCallback
    ) {
        val call: Call<PurchaseCourseModel> =
            service!!.PurchaseCourse(RazorpayPaymentId,PaymentId,LocalOrderId,Amount,PaymentStatus,CourseId,UserId,Reason)!!
        call.enqueue(object : Callback<PurchaseCourseModel> {
            override fun onResponse(
                call: Call<PurchaseCourseModel>,
                response: Response<PurchaseCourseModel>
            ) {
                iApiCallback.onSuccess("Purchase_Course", response)
            }

            override fun onFailure(call: Call<PurchaseCourseModel>, t: Throwable) {
                iApiCallback.onFailure(t)
            }

        })
    }


    fun getArticle(
        iApiCallback: ApiCallback
    ) {
        val call: Call<GetArticleModel> =
            service!!.getArticle()!!
        call.enqueue(object : Callback<GetArticleModel> {
            override fun onResponse(
                call: Call<GetArticleModel>,
                response: Response<GetArticleModel>
            ) {
                iApiCallback.onSuccess("getArticle", response)
            }

            override fun onFailure(call: Call<GetArticleModel>, t: Throwable) {
                iApiCallback.onFailure(t)
            }

        })
    }


    fun myappliedcourse(
        userId: String,
        iApiCallback: ApiCallback
    ) {
        val call: Call<AppliedCoursesModel> =
            service!!.myappliedcourse(userId)!!
        call.enqueue(object : Callback<AppliedCoursesModel> {
            override fun onResponse(
                call: Call<AppliedCoursesModel>,
                response: Response<AppliedCoursesModel>
            ) {
                iApiCallback.onSuccess("myAppliedCourse", response)
            }

            override fun onFailure(call: Call<AppliedCoursesModel>, t: Throwable) {
                iApiCallback.onFailure(t)
            }

        })
    }


    fun googleFacebookLoginSignUp(
        userName: String,
        userEmail: String,
        roleName:String ,
        tokenId: String,
        deviceId:String,
        deviceType:String,
        provider:String,

        iApiCallback: ApiCallback
    ) {
        val call: Call<GFLoginModel> =
            service!!.googleFacebookLoginSignUp(userName,userEmail,roleName,tokenId,deviceId,deviceType,provider)!!
        call.enqueue(object : Callback<GFLoginModel> {
            override fun onResponse(
                call: Call<GFLoginModel>,
                response: Response<GFLoginModel>
            ) {
                iApiCallback.onSuccess("gflogin", response)
            }

            override fun onFailure(call: Call<GFLoginModel>, t: Throwable) {
                iApiCallback.onFailure(t)
            }
        })

    }


    fun getuserdetail(
        userId: String,
        iApiCallback: ApiCallback
    ) {
        val call: Call<UserDetailModel> =
            service!!.getuserdetail(userId)!!
        call.enqueue(object : Callback<UserDetailModel> {
            override fun onResponse(
                call: Call<UserDetailModel>,
                response: Response<UserDetailModel>
            ) {
                iApiCallback.onSuccess("userdetail", response)
            }

            override fun onFailure(call: Call<UserDetailModel>, t: Throwable) {
                iApiCallback.onFailure(t)
            }

        })
    }


    fun ForgotPassword(
        email: String,
        iApiCallback: ApiCallback
    ) {
        val call: Call<RegistrationModel> = service!!.ForgotPassword(email)!!
        call.enqueue(object : Callback<RegistrationModel> {
            override fun onResponse(
                call: Call<RegistrationModel>,
                response: Response<RegistrationModel>
            ) {
                iApiCallback.onSuccess("ForgotPassword", response)
            }

            override fun onFailure(call: Call<RegistrationModel>, t: Throwable) {
                iApiCallback.onFailure(t)
            }

        })
    }







    }