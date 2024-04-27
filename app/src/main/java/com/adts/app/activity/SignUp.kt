package com.adts.app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.adts.app.activity.Login
import com.adts.app.databinding.ActivitySignUpBinding
import com.adts.app.model.GFLoginModel
import com.adts.app.model.RegistrationModel
import com.adts.app.network.ApiCall
import com.adts.app.network.ApiCallback
import com.adts.app.network.MyApp
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Response

class SignUp : AppCompatActivity(), ApiCallback {
    private lateinit var binding: ActivitySignUpBinding
    val RC_SIGN_IN = 123
    lateinit var callbackManager: CallbackManager
    private var loginManager: LoginManager? = null
    var id = ""
    var firstName = ""
    var lastName = ""
    var picture = ""
    var email = ""
    var accessToken = ""
    private var mAuth: FirebaseAuth? = null

    private val myApp = MyApp(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        callbackManager = CallbackManager.Factory.create()
        mAuth = FirebaseAuth.getInstance();

       /* val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        FacebookSdk.sdkInitialize(this)
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

        }*/

        binding.loginTv.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }

        binding.next.setOnClickListener {
            val fullname = binding.userName.text.toString()
            val email = binding.userEmail.text.toString()
            val password = binding.password.text.toString()
            val mobile = binding.phn.text.toString()

            when {
                fullname.isEmpty() -> {
                    Toast.makeText(this, R.string.useremail_required, Toast.LENGTH_SHORT).show()
                }
                email.isEmpty() -> {
                    Toast.makeText(this, R.string.useremail_required, Toast.LENGTH_SHORT).show()
                }
                password.isEmpty() -> {
                    Toast.makeText(this, R.string.userpass_required, Toast.LENGTH_SHORT).show()
                }

                else -> {
                    if (email != null && password != null && fullname != null) {

                        mAuth!!.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { signIn ->
                                if (signIn.isSuccessful) {
                                    startActivity(Intent(this, Login::class.java))
                                    finish()
                                } else {
                                    Toast.makeText(this,"sign in failed",Toast.LENGTH_SHORT).show()
                                }
                            }
                       /* mAuth!!.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(
                                        applicationContext,
                                        "Login successful!!",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()

                                    // hide the progress bar
                                    //                                            progressBar.setVisibility(View.GONE)

                                    // if sign-in is successful
                                    // intent to home activity
                                    val intent = Intent(
                                        this@SignUp,
                                        Login::class.java
                                    )
                                    startActivity(intent)
                                } else {

                                    // sign-in failed
                                    Toast.makeText(
                                        applicationContext,
                                        "Login failed!!",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()

                                    // hide the progress bar
                                    //                                            progressbar.setVisibility(View.GONE)
                                }
                            }
*/

//                        MyApp(this).showAlrtDialg(this, "")
//                        myApp.setSharedPrefBoolean(MyApp.isLogin, true)
//                        startActivity(Intent(this, MainActivity::class.java))
//                        finishAffinity()
//                        ApiCall.instance?.register("0", fullname, email, password, mobile, "",this)
                    } else {
                        Toast.makeText(
                            this,
                            "Please enter a valid Number/password",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        }

        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    Log.d("TAG", "Success Login")
                    getUserProfile(result.accessToken, result.accessToken.userId)
                }

                override fun onCancel() {
                    Toast.makeText(this@SignUp, "Login Cancelled", Toast.LENGTH_LONG).show()
                }

                override fun onError(error: FacebookException) {
                    Toast.makeText(this@SignUp, error.message, Toast.LENGTH_LONG).show()
                }
            })
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
                    email = "Not exists"
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
            val personName = account.displayName
            val roleName = ""
            val deviceType = "android"
            val tokenId = account.id!!
            val deviceId = myApp.getSharedPrefString(MyApp.DEVICE_ID)
            val personEmail = account.email
            val username = account.email!!.replace("@gmail.com", "")

            MyApp(this).showAlrtDialg(this, "")
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


    override fun onSuccess(type: String, data: Any?) {
        myApp.dismisAlrtDialog()
        if (type == "signup") {
            val response: Response<RegistrationModel> = data as Response<RegistrationModel>
            if (response.isSuccessful) {
                if (response.body()?.message == "success") {
                    Toast.makeText(this, "Account created Successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, Login::class.java))
                    finishAffinity()
                } else {

                    Toast.makeText(this, response.body()!!.message, Toast.LENGTH_SHORT).show()
                }
            }
        } else if (type == "gflogin") {
            val response: Response<GFLoginModel> = data as Response<GFLoginModel>
            if (response.isSuccessful) {
                if (response.body()?.message == "success") {
                    Toast.makeText(this, "Successfully Login", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finishAffinity()
                    myApp.setSharedPrefBoolean(MyApp.isLogin, true)
                    myApp.setSharedPrefInteger(MyApp.USER_ID, response.body()!!.data.Id)
                    myApp.setSharedPrefString(MyApp.USER_NAME, response.body()!!.data.UserName)
                    myApp.setSharedPrefString(MyApp.USER_Email, response.body()!!.data.UserEmail)
                    myApp.setSharedPrefString(MyApp.USER_Number, "")
                    myApp.setSharedPrefString(MyApp.LOGIN_FROM, "Social")
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
        val split = personEmail.split("@").toTypedArray()
        val firstSubString = split[0]
        val secondSubString = split[1]
        val username = firstSubString
        ApiCall.instance?.googleFacebookLoginSignUp(
            username,
            personEmail.toString(),
            roleName,
            tokenId,
            deviceId,
            deviceType,
            "Facebook",
            this
        )
        myApp.showAlrtDialg(this, "")
    }
}