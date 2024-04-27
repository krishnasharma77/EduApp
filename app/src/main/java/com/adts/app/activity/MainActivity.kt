package com.adts.app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adts.app.activity.*
import com.adts.app.adapter.AllCoursesAdapter
import com.adts.app.databinding.ActivityMainBinding
import com.adts.app.databinding.NavheaderBinding
import com.adts.app.model.Data
import com.adts.app.model.DataX
import com.adts.app.model.MainScreenData
import com.adts.app.model.MediaType
import com.adts.app.network.MyApp
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingDrawer: NavheaderBinding
    private lateinit var coursesAdapter: AllCoursesAdapter
    private lateinit var courseList: ArrayList<MainScreenData>
    var taxtname: TextView? = null
    private lateinit var data: Data
    private val myApp = MyApp(this)
    var actionBarDrawerToggle: ActionBarDrawerToggle? = null


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        bindingDrawer = NavheaderBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.img.setOnClickListener {
            binding.drawer.openDrawer(Gravity.LEFT)

        }


        val drawer = findViewById<View>(com.adts.app.R.id.drawer) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
            this,
            drawer,
            R.string.open,
            R.string.close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val navigationView = findViewById<View>(com.adts.app.R.id.navview) as NavigationView
        val headerView = navigationView.getHeaderView(0)
        val userName = MyApp(this).getSharedPrefString(MyApp.USER_NAME)
        val navUsername = headerView.findViewById<View>(R.id.txtname) as TextView
        val updateprofile = headerView.findViewById<View>(R.id.updateprofile) as TextView
        navUsername.text = userName
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_all_courses -> refresh()
                R.id.menu_Logout -> logout()
                R.id.menu_resources -> openResourses()
                R.id.menu_course -> openMyCourse()
            }
            true
        }
        binding.searchCourses.addTextChangedListener {
            coursesAdapter.filter.filter(it)
        }

        //Edit Profile
        updateprofile.setOnClickListener {
            startActivity(Intent(this, EditProfile::class.java))
            finish()
        }
        val video = MediaType.Video("https://example.com/video.mp4") // Should be accessible
        val image = MediaType.Image(R.drawable.pythone_traning) // Wrap resource ID in MediaType.Image

        courseList = ArrayList()
        courseList.add(
            MainScreenData(
                "The Java One-Shot Course provides a comprehensive introduction to Java programming in a condensed format. Covering key concepts like syntax, data structures, and object-oriented programming, it equips learners with essential skills for application development. This accelerated course enables quick proficiency in Java for efficient coding and problem-solving.\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "\n", "1 Month", 1, video, "Java One-Shot", "2100", "", false
            )
        )
        courseList.add(
            MainScreenData(
                "\n" +
                        "The Python Course offers a concise yet thorough exploration of Python programming. Participants gain hands-on experience with fundamental concepts, including syntax, data structures, and algorithm implementation. Designed for beginners, this course provides a solid foundation for leveraging Python's versatility in web development, data analysis, and automation.",
                "1 Month",
                2,
                image,
                " Python programming",
                "2100",
                "",
                false
            )
        )
        /*courseList.add(
            MainScreenData(
                "The Web Development Course immerses learners in the dynamic world of building responsive and interactive websites. Covering HTML, CSS, and JavaScript, participants acquire the skills to create visually appealing and functional web pages. This comprehensive course is essential for aspiring developers seeking expertise in front-end and basic back-end web development.",
                "1 Month",
                3,
                R.drawable.web_course,
                "Web Development",
                "2100",
                "",
                false
            )
        )

        courseList.add(
            MainScreenData(
                "The Java One-Shot Course provides a comprehensive introduction to Java programming in a condensed format. Covering key concepts like syntax, data structures, and object-oriented programming, it equips learners with essential skills for application development. This accelerated course enables quick proficiency in Java for efficient coding and problem-solving.\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "\n", "1 Month", 1, R.drawable.java_dev, "Java One-Shot", "2100", "", false
            )
        )
        courseList.add(
            MainScreenData(
                "\n" +
                        "The Python Course offers a concise yet thorough exploration of Python programming. Participants gain hands-on experience with fundamental concepts, including syntax, data structures, and algorithm implementation. Designed for beginners, this course provides a solid foundation for leveraging Python's versatility in web development, data analysis, and automation.",
                "1 Month",
                2,
                R.drawable.pythone_traning,
                " Python programming",
                "2100",
                "",
                false
            )
        )
        courseList.add(
            MainScreenData(
                "The Web Development Course immerses learners in the dynamic world of building responsive and interactive websites. Covering HTML, CSS, and JavaScript, participants acquire the skills to create visually appealing and functional web pages. This comprehensive course is essential for aspiring developers seeking expertise in front-end and basic back-end web development.",
                "1 Month",
                3,
                R.drawable.web_course,
                "Web Development",
                "2100",
                "",
                false
            )
        )
*/
        coursesAdapter =
            AllCoursesAdapter(this, courseList, object : AllCoursesAdapter.ClickListener {
                override fun clicked(view: View, posi: MainScreenData) {
                    startActivity(
                        Intent(
                            this@MainActivity,
                            CourseDetails::class.java
                        ).putExtra("courseDetail", posi)
                    )
                }
            })
        binding.searchCourses.text.clear()
        binding.coursesRv.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.coursesRv.adapter = coursesAdapter
        coursesAdapter.notifyDataSetChanged()


    }

    fun refresh() {
        binding.drawer.closeDrawers()
        onResume()

    }

    private fun openMyCourse() {
//        startActivity(Intent(this, MyCourses::class.java))
    }

    private fun openResourses() {
//        startActivity(Intent(this, AllResources::class.java))
    }

    private fun logout() {
        myApp.logout()
        this.startActivity(Intent(this, Login::class.java))
        Toast.makeText(this, "Logout Successfully", Toast.LENGTH_SHORT).show()
        this.finish()
        signOut()
    }

    private fun signOut() {
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        mGoogleSignInClient.signOut().addOnCompleteListener {

        }
        LoginManager.getInstance().logOut()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        TODO("Not yet implemented")
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle!!.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }


    /* override fun onSuccess(type: String, data: Any?) {
         myApp.dismisAlrtDialog()
         if (type == "allCourse") {
             val response: Response<GetAllCoursesModel> = data as Response<GetAllCoursesModel>
             if (response.isSuccessful) {
                 courseList = ArrayList()
                 courseList = response.body()!!.data as ArrayList<DataX>
                 coursesAdapter =
                     AllCoursesAdapter(this, courseList, object : AllCoursesAdapter.ClickListener {
                         override fun clicked(view: View, posi: DataX) {
                             startActivity(
                                 Intent(
                                     this@MainActivity,
                                     CourseDetails::class.java
                                 ).putExtra("courseDetail", posi)
                             )
                         }
                     })
                 binding.searchCourses.text.clear()
                 binding.coursesRv.layoutManager =
                     LinearLayoutManager(this, RecyclerView.VERTICAL, false)
                 binding.coursesRv.adapter = coursesAdapter
                 coursesAdapter.notifyDataSetChanged()
             }
         }
     }
 */
    /*  override fun onFailure(data: Any?) {
          myApp.dismisAlrtDialog()
      }*/

    override fun onResume() {
        super.onResume()
        val userId = MyApp(this).getSharedPrefInteger(MyApp.USER_ID).toString()
//        myApp.showAlrtDialg(this, "")
//        ApiCall.instance?.GetCourse(userId, this)
    }


}


