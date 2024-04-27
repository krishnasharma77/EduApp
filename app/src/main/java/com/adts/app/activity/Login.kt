package com.adts.app.activity

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.adts.app.MainActivity
import com.adts.app.R
import com.adts.app.SignUp
import com.adts.app.databinding.ActivityLoginBinding
import com.adts.app.model.GFLoginModel
import com.adts.app.model.RegistrationModel
import com.adts.app.network.ApiCall
import com.adts.app.network.ApiCallback
import com.adts.app.network.MyApp
import com.facebook.AccessToken
import com.facebook.BuildConfig
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.facebook.LoggingBehavior
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Response


class Login : AppCompatActivity(), ApiCallback {
    private lateinit var binding: ActivityLoginBinding
    private val myApp = MyApp(this)
    val RC_SIGN_IN = 123
    lateinit var callbackManager: CallbackManager

    var id = ""
    var firstName = ""
    var lastName = ""
    var picture = ""
    var email = ""
    var accessToken = ""
    lateinit var aler: AlertDialog
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance();

//        getFirebaseToken()
        callbackManager = CallbackManager.Factory.create()

        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        if (isLoggedIn()) {
            Log.d("LoggedIn? :", "YES")
            LoginManager.getInstance().logOut()

        } else {
            Log.d("LoggedIn? :", "NO")
        }

        binding.loginGoogle.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
        binding.loginFacebook.setOnClickListener {
            LoginManager.getInstance()
                .logInWithReadPermissions(this, listOf("public_profile", "email"))
        }

        binding.SignUp.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
        }

        binding.next.setOnClickListener {
            val email = ""
            val password = binding.password.text.toString()
            val mobile = binding.phone.text.toString()


            when {
                email.isEmpty() -> {
                    Toast.makeText(this, R.string.useremail_required, Toast.LENGTH_SHORT).show()
                }

                password.isEmpty() -> {
                    Toast.makeText(this, R.string.userpass_required, Toast.LENGTH_SHORT).show()
                }

                else -> {
                    mAuth!!.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
//                                toast("created account successfully !")
//                                sendEmailVerification()
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            } else {
                                Toast.makeText(
                                    this@Login,
                                    "Failed to Authenticate !",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    /* val deviceId = myApp.getSharedPrefString(MyApp.DEVICE_ID)
                     val deviceType = "android"
                     myApp.showAlrtDialg(this, "")
                     myApp.setSharedPrefBoolean(MyApp.isLogin, true)
                     startActivity(Intent(this, MainActivity::class.java))
                     finishAffinity()*/
//                    ApiCall.instance?.login(mobile, password, deviceId, deviceType, this)
                }
            }
        }

        // Callback registration
        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    Log.d("TAG", "Success Login")
                    getUserProfile(result.accessToken, result.accessToken.userId)
                }

                override fun onCancel() {
                    Toast.makeText(this@Login, "Login Cancelled", Toast.LENGTH_LONG).show()
                }

                override fun onError(error: FacebookException) {
                    Toast.makeText(this@Login, error.message, Toast.LENGTH_LONG).show()
                }
            })


        binding.forgotPass.setOnClickListener {
            showdialog()

        }
    }

    private fun showdialog() {
        var builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        val inflater: LayoutInflater = layoutInflater
        val dialogLayout: View = inflater.inflate(R.layout.forgot_password, null)
        builder.setView(dialogLayout)
        val editText: EditText = dialogLayout.findViewById(R.id.enter_email)
        val okButton: AppCompatButton = dialogLayout.findViewById(R.id.ok)
        val crossbutton: ImageView = dialogLayout.findViewById(R.id.cross)


        aler = builder.create()
        aler.show()

        okButton.setOnClickListener { v ->
            val email = editText.text.trim().toString()

            if (email.isEmpty()) {
                editText.error = resources.getString(R.string.useremail_required)
            } else {
                myApp.showAlrtDialg(this, "")
                ApiCall.instance?.ForgotPassword(email.trim(), this)
            }

        }

        crossbutton.setOnClickListener {
            aler.dismiss()

        }
    }


    @SuppressLint("LongLogTag")
    fun getUserProfile(token: AccessToken?, userId: String?) {

        val parameters = Bundle()
        parameters.putString(
            "fields",
            "id, first_name, middle_name, last_name, name, picture, email"
        )
        GraphRequest(token,
            "/$userId/",
            parameters,
            HttpMethod.GET,
            GraphRequest.Callback { response ->
                val jsonObject = response.jsonObject

                // Facebook Access Token
                // You can see Access Token only in Debug mode.
                // You can't see it in Logcat using Log.d, Facebook did that to avoid leaking user's access token.
                if (BuildConfig.DEBUG) {
                    FacebookSdk.setIsDebugEnabled(true)
                    FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS)
                }
                accessToken = token.toString()

                // Facebook Id
                if (jsonObject?.has("id") == true) {
                    val facebookId = jsonObject.getString("id")
                    Log.i("Facebook Id: ", facebookId.toString())
                    id = facebookId.toString()
                } else {
                    Log.i("Facebook Id: ", "Not exists")
                    id = "Not exists"
                }


                // Facebook First Name
                if (jsonObject != null) {
                    if (jsonObject.has("first_name")) {
                        val facebookFirstName = jsonObject.getString("first_name")
                        Log.i("Facebook First Name: ", facebookFirstName)
                        firstName = facebookFirstName
                    } else {
                        Log.i("Facebook First Name: ", "Not exists")
                        firstName = "Not exists"
                    }
                }

                // Facebook Profile Pic URL
                if (jsonObject?.has("picture") == true) {
                    val facebookPictureObject = jsonObject.getJSONObject("picture")
                    if (facebookPictureObject.has("data")) {
                        val facebookDataObject = facebookPictureObject.getJSONObject("data")
                        if (facebookDataObject.has("url")) {
                            val facebookProfilePicURL = facebookDataObject.getString("url")
                            Log.i("Facebook Profile Pic URL: ", facebookProfilePicURL)
                            picture = facebookProfilePicURL
                        }
                    }
                } else {
                    Log.i("Facebook Profile Pic URL: ", "Not exists")
                    picture = "Not exists"
                }

                // Facebook Email
                if (jsonObject?.has("email") == true) {
                    val facebookEmail = jsonObject.getString("email")
                    Log.i("Facebook Email: ", facebookEmail)
                    email = facebookEmail
                } else {
                    Log.i("Facebook Email: ", "Not exists")
                    email = ""
                }

                facebookLogin()
            }).executeAsync()
    }


    fun isLoggedIn(): Boolean {
        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired
        return isLoggedIn
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                handleSignInResult(task)
            }
        }
    }


    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        val account = completedTask.getResult(ApiException::class.java)

        if (account != null) {
            val roleName = ""
            val deviceType = "android"
            val tokenId = account.id!!
            val deviceId = myApp.getSharedPrefString(MyApp.DEVICE_ID)
            val personEmail = account.email
            val username = account.email!!.replace("@gmail.com", "")

            myApp.showAlrtDialg(this, "")
            ApiCall.instance?.googleFacebookLoginSignUp(
                username,
                personEmail.toString(),
                roleName,
                tokenId,
                deviceId,
                deviceType,
                "Google",
                this
            )
        }
    }

    override fun onBackPressed() {

        val builder =
            AlertDialog.Builder(this)
        builder.setMessage("Are you sure ?")
            .setCancelable(true)
            .setPositiveButton("Yes",
                DialogInterface.OnClickListener { dialogInterface, i ->

                    finishAffinity()

                })
            .setNegativeButton(
                "No",
                DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.cancel() })


        val alertDialog = builder.create()
        alertDialog.setTitle("Do you want to exit ")
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.show()

    }

    override fun onSuccess(type: String, data: Any?) {
        myApp.dismisAlrtDialog()
        if (type == "login") {
            val response: Response<RegistrationModel> = data as Response<RegistrationModel>
            if (response.isSuccessful) {
                if (response.body()!!.message == "success") {
                    myApp.setSharedPrefInteger(MyApp.USER_ID, response.body()!!.data.Id)
                    myApp.setSharedPrefBoolean(MyApp.isLogin, true)
                    myApp.setSharedPrefString(MyApp.USER_NAME, response.body()!!.data.UserName)
                    myApp.setSharedPrefString(MyApp.USER_Email, response.body()!!.data.UserEmail)
                    myApp.setSharedPrefString(MyApp.USER_Number, response.body()!!.data.Mobile)
                    myApp.setSharedPrefString(MyApp.LOGIN_FROM, "Normal")


//                    myApp.setSharedPrefString("userName",name)
                    startActivity(Intent(this, MainActivity::class.java))
                    finishAffinity()
                } else {
                    Toast.makeText(this, response.body()!!.message, Toast.LENGTH_SHORT).show()
                }
            }
        } else if (type == "gflogin") {
            val response: Response<GFLoginModel> = data as Response<GFLoginModel>
            if (response.isSuccessful) {
                if (response.body()?.message == "success") {
                    startActivity(Intent(this, MainActivity::class.java))
                    finishAffinity()
                    myApp.setSharedPrefBoolean(MyApp.isLogin, true)
                    myApp.setSharedPrefInteger(MyApp.USER_ID, response.body()!!.data.Id)
//                    if (response.body()!!.data.UserEmail.isNotEmpty()){
//                        myApp.setSharedPrefString(MyApp.USER_Email, response.body()!!.data.UserEmail)
//                    }
//                    else{
//                        myApp.setSharedPrefString(MyApp.USER_Email, "")
//                    }
                    myApp.setSharedPrefString(MyApp.USER_NAME, response.body()!!.data.UserName)
                    myApp.setSharedPrefString(MyApp.USER_Email, "")
                    myApp.setSharedPrefString(MyApp.USER_Number, "")
                    myApp.setSharedPrefString(MyApp.LOGIN_FROM, "Social")
                } else {
                    Toast.makeText(this, response.body()!!.message, Toast.LENGTH_SHORT).show()

                    val gso =
                        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestEmail().build()
                    val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
                    mGoogleSignInClient.signOut().addOnCompleteListener {

                    }
                    LoginManager.getInstance().logOut()
                }

            }

        } else if (type == "ForgotPassword") {
            val response: Response<RegistrationModel> = data as Response<RegistrationModel>
            if (response.isSuccessful) {
                if (response.body()?.message == "success") {
                    aler.dismiss()
                    Toast.makeText(this, "New Password Send On Your Email", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(this, response.body()?.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    override fun onFailure(data: Any?) {
        myApp.dismisAlrtDialog()
        Toast.makeText(this, R.string.error_in_registeration, Toast.LENGTH_SHORT).show()
        Toast.makeText(this, data.toString(), Toast.LENGTH_SHORT).show()
    }


    fun facebookLogin() {
        id
        firstName
        lastName
        picture
        email
        accessToken

        val roleName = ""
        val deviceType = "android"
        val tokenId = id
        val deviceId = myApp.getSharedPrefString(MyApp.DEVICE_ID)
        val personEmail = email

        ApiCall.instance?.googleFacebookLoginSignUp(
            firstName,
            personEmail,
            roleName,
            tokenId,
            deviceId,
            deviceType,
            "Facebook",
            this
        )
        myApp.showAlrtDialg(this, "")
    }

    private fun getFirebaseToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(
                        "ttttt",
                        "Fetching FCM registration token failed",
                        task.getException()
                    )
                    return@addOnCompleteListener
                }

                // Get new FCM registration token
                val token: String = task.result
                myApp.setSharedPrefString(MyApp.DEVICE_ID, token)

                // Log and toast

                val msg = "FCM console token $token"
                Log.d("ttttt", msg)
            }
    }


}

