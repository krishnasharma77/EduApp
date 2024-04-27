package com.adts.app.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.adts.app.MainActivity
import com.adts.app.databinding.ActivityEditProfileBinding
import com.adts.app.model.RegistrationModel
import com.adts.app.model.UserDetailModel
import com.adts.app.network.ApiCall
import com.adts.app.network.ApiCallback
import com.adts.app.network.MyApp
import retrofit2.Response

class EditProfile : AppCompatActivity(), ApiCallback {
    val myApp by lazy { MyApp(this) }
    private lateinit var binding: ActivityEditProfileBinding
    var fullname = ""
    var email = ""
    var password = ""
    var mobile = ""
    var newpassword = ""
    var userId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userId = MyApp(this).getSharedPrefInteger(MyApp.USER_ID).toString()
        myApp.showAlrtDialg(this, "")
        ApiCall.instance?.getuserdetail(userId, this)

        binding.useremail.isEnabled = false
        binding.userno.isEnabled = false


        binding.Cancel.setOnClickListener {
            onBackPressed()
        }

        if (myApp.getSharedPrefString(MyApp.LOGIN_FROM).equals("Social")) {
            binding.password.visibility = View.GONE
            binding.npassword.visibility = View.GONE
            binding.userno.visibility = View.GONE
        }



        binding.Save.setOnClickListener {
            fullname = binding.username.text.toString()
            email = binding.useremail.text.toString()
            password = binding.password.text.toString()
            mobile = binding.userno.text.toString()
            newpassword = binding.npassword.text.toString()

            if (password.isNotEmpty()) {
                if (fullname.isNotEmpty()) {
                    if (newpassword.isNotEmpty()) {

                        callUpdateApi()

                    } else {
                        binding.npassword.error = "Enter Vaild Password"
                    }
                } else {
                    binding.username.error = "Enter Vaild Name"
                }

            } else {
                if (fullname.isNotEmpty()) {
                    callUpdateApi()
                } else {
                    binding.username.error = "Enter Vaild Name"
                }

            }
            //            Toast.makeText(this, "Update User Details", Toast.LENGTH_SHORT).show()
        }
    }

    private fun callUpdateApi() {
        myApp.showAlrtDialg(this, "")
        ApiCall.instance?.register(
            userId,
            fullname,
            email,
            password,
            mobile,
            newpassword,
            this
        )
    }

    override fun onBackPressed() {
        super.onBackPressed()
       startMainActivity()
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onSuccess(type: String, data: Any?) {
        myApp.dismisAlrtDialog()
        if (type == "userdetail") {
            val response: Response<UserDetailModel> = data as Response<UserDetailModel>
            if (response.isSuccessful) {
                if (response.body()!!.message == "success") {
                    val userdetails = response.body()!!.data
                    binding.username.setText(userdetails.UserName)
                    binding.useremail.setText(userdetails.UserEmail)
                    binding.userno.setText(userdetails.Mobile)
                }
            }
        } else if (type == "signup") {
            val response: Response<RegistrationModel> = data as Response<RegistrationModel>
            if (response.isSuccessful) {
                if (response.body()!!.message == "success") {
                    Toast.makeText(this, "Update User Details", Toast.LENGTH_SHORT).show()
                    MyApp(this).setSharedPrefString(
                        MyApp.USER_NAME,
                        response.body()!!.data.UserName
                    )
                    startMainActivity()
                } else {
                    Toast.makeText(this, response.body()!!.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onFailure(data: Any?) {
        myApp.dismisAlrtDialog()
    }
}
