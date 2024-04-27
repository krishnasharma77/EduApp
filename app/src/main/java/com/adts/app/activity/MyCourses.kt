package com.adts.app.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adts.app.adapter.AppliedCoursesAdapter
import com.adts.app.databinding.ActivityMyCoursesBinding
import com.adts.app.model.*
import com.adts.app.network.ApiCall
import com.adts.app.network.ApiCallback
import com.adts.app.network.MyApp
import retrofit2.Response

class MyCourses : AppCompatActivity(), ApiCallback {
    private lateinit var binding: ActivityMyCoursesBinding
    private val myapp = MyApp(this)
    private lateinit var appliedCoursesAdapter: AppliedCoursesAdapter
    private lateinit var  appliedCourseList:ArrayList<DataXXXX>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyCoursesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inIt()
        val userId =MyApp(this).getSharedPrefInteger(MyApp.USER_ID).toString()
        MyApp(this).showAlrtDialg(this, "")
        ApiCall.instance?.myappliedcourse(userId,this)

    }

    private fun inIt() {
        binding.back.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onSuccess(type: String, data: Any?) {
        myapp.dismisAlrtDialog()
        if (type == "myAppliedCourse"){
            val response:Response<AppliedCoursesModel> = data as Response<AppliedCoursesModel>
            if (response.isSuccessful) {
                appliedCourseList = ArrayList()
                appliedCourseList = response.body()?.data as ArrayList<DataXXXX>
                appliedCoursesAdapter = AppliedCoursesAdapter(this,appliedCourseList,object : AppliedCoursesAdapter.ClickListener{
                    override fun clicked(view: View, posi: DataXXXX) {
                        startActivity(Intent(this@MyCourses, MyCourseDetails::class.java).putExtra("myCourseDetail",posi))

                    }

                })
                if (appliedCourseList.isNotEmpty()){
                    binding.myCoursesRv.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
                    binding.myCoursesRv.adapter = appliedCoursesAdapter
                    appliedCoursesAdapter.notifyDataSetChanged()
                }
                else{
                    binding.tvCours.visibility =View.VISIBLE
                }



            }
        }
    }

    override fun onFailure(data: Any?) {
        myapp.dismisAlrtDialog()
    }
}