package com.example.bookhub.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.bookhub.*
import com.example.bookhub.fragment.AboutAppFragment
import com.example.bookhub.fragment.DashboardFragment
import com.example.bookhub.fragment.FavoritesFragment
import com.example.bookhub.fragment.ProfileFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView: NavigationView

    var previousMenuItem: MenuItem?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout=findViewById(R.id.drawerLayout)
        coordinatorLayout=findViewById(R.id.coordinatorLayout)
        toolbar=findViewById(R.id.toolbar)
        frameLayout=findViewById(R.id.frame)
        navigationView=findViewById(R.id.navigationView)
        setUpToolbar()

   openDashboard()

        val actionBarDrawerToggle=ActionBarDrawerToggle(this@MainActivity,drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        //making hamburger icon functional
        drawerLayout.addDrawerListener(actionBarDrawerToggle)//enables the drawer layout to listen to the action on toggle
        actionBarDrawerToggle.syncState()//synchronizing state of the toggle with the state of navigation drawer
        //it changes the hamburger icon to an arrow & vice-versa

navigationView.setNavigationItemSelectedListener {

    if(previousMenuItem!=null)
    {
        previousMenuItem?.isChecked=false

    }
    it.isCheckable=true
    it.isChecked=true
    previousMenuItem=it
    when(it.itemId){
        R.id.dashboard ->{
           openDashboard()
drawerLayout.closeDrawers()
        }
        R.id.favorites ->{
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.frame,
                    FavoritesFragment()
                )
                .commit()//commit applies the transaction

            supportActionBar?.title="Favorites"
            drawerLayout.closeDrawers()
        }
        R.id.profile ->{
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.frame,
                    ProfileFragment()
                )
                .commit()//commit applies the transaction

            supportActionBar?.title="Profile"
            drawerLayout.closeDrawers()

        }
        R.id.aboutApp ->{
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.frame,
                    AboutAppFragment()
                )
                .commit()//commit applies the transaction

            supportActionBar?.title="About App"
            drawerLayout.closeDrawers()

        }
    }
    return@setNavigationItemSelectedListener true;
}
    }
    fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title="Toolbar Title"
        supportActionBar?.setHomeButtonEnabled(true);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId//extracting the id of the item & storing it inside id
         if(id==android.R.id.home)//id.home is the id for the hamburger button
         {
             drawerLayout.openDrawer(GravityCompat.START);
         }
        return super.onOptionsItemSelected(item)
    }
    fun openDashboard(){
        val fragment= DashboardFragment()
        val transaction=supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame,fragment)
        transaction.commit()//commit applies the transaction
        supportActionBar?.title="Dashboard"
        navigationView.setCheckedItem(R.id.dashboard)
    }

    override fun onBackPressed() {
        val frag=supportFragmentManager.findFragmentById(R.id.frame)//holds the fragment that is currently displayed inside the frame
        when(frag){
            !is DashboardFragment ->openDashboard()// if the user isn't inside the dashboard, then he'll be redirected to the dashboard fragment

            else-> super.onBackPressed()//else back button(by default) will close the app
        }

    }
}