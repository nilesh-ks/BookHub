package com.example.bookhub.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.bookhub.R
import com.example.bookhub.adapter.DashboardRecyclerAdapter
import com.example.bookhub.model.Book
import com.example.bookhub.util.ConnectionManager
import org.json.JSONException
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap


class DashboardFragment : Fragment() {
lateinit var recyclerDashboard: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
   lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar


    /* val bookList= arrayListOf(
        "P.S. I love You",
        "The Great Gatsby",
        "Anna Karenina",
        "Madame Bovary",
        "War and Peace",
        "Lolita",
        "Middlemarch",
        "The Adventure of Huckleberry Finn",
        "Moby-Dick",
        "The Lord of the Rings"
    )*/
//Now, declare and initialise a variable for the adapter
    lateinit var recyclerAdapter: DashboardRecyclerAdapter

    val bookInfoList = arrayListOf<Book>()
    val ratingComparator= Comparator<Book>{
        book1,book2->
        if(book1.bookRating.compareTo(book2.bookRating,true)==0)
        {
            //sort according to name if the rating is same
            book1.bookName.compareTo(book2.bookRating,true)
        }else {
            book1.bookRating.compareTo(book2.bookRating, true)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_dashboard, container, false)//we put fragment layout inside the container
        // that the activity provides for us
        //container is a ViewGroup provided by the activity
        //attachToRoot is false because we don't want one fragment to attach to the root activity.
        // Instead, several fragments would be operational;it is kept false mostly
        recyclerDashboard=view.findViewById(R.id.recyclerDashboard)

        setHasOptionsMenu(true)//used only for adding menu to a fragment
        //for an activity, it is automatically created by onCreateOptionsMenu

        layoutManager=LinearLayoutManager(activity)//since we're working inside the fragment, we need to use activity instead of this.

        progressLayout=view.findViewById(R.id.progressLayout)
        progressBar=view.findViewById(R.id.progressBar)

        progressLayout.visibility=View.VISIBLE//makes the layout visible when the fragment is being loaded






        recyclerAdapter= DashboardRecyclerAdapter(activity as Context, bookInfoList)//typecasting application context as context will always succeed

        //now, we need to set them up with the RecyclerView
        recyclerDashboard.adapter=recyclerAdapter
        recyclerDashboard.layoutManager=layoutManager

        /*recyclerDashboard.addItemDecoration(// to remove the divider line
            DividerItemDecoration(
                recyclerDashboard.context,
                (layoutManager as LinearLayoutManager).orientation
            )
        )*/
        val queue= Volley.newRequestQueue(activity as Context)
        val url="http://13.235.250.119/v1/book/fetch_books/"
        if(ConnectionManager().checkConnectivity(activity as Context)) {
            val jsonObjectRequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                    //Here we will handle the response
                    try{
                        progressLayout.visibility=View.GONE//After getting the layout, this will hide the layout
                    val success = it.getBoolean("success")//if it's a success
                    if (success) {
                        val data = it.getJSONArray("data")//data will be fetched from the JSONArray
                        for (i in 0 until data.length()) {
                            val bookJsonObject = data.getJSONObject(i)
                            //parsing each object into a book object
                            val bookObject = Book(
                                bookJsonObject.getString("book_id"),
                                bookJsonObject.getString("name"),
                                bookJsonObject.getString("author"),
                                bookJsonObject.getString("rating"),
                                bookJsonObject.getString("price"),
                                bookJsonObject.getString("image")


                            )
                            bookInfoList.add(bookObject)
                            //sending data to the adapter
                           recyclerAdapter = DashboardRecyclerAdapter(
                                activity as Context,
                                bookInfoList
                            )//typecasting application context as context will always succeed

                            //now, we need to set them up with the RecyclerView
                            recyclerDashboard.adapter = recyclerAdapter
                            recyclerDashboard.layoutManager = layoutManager

                            /*recyclerDashboard.addItemDecoration(// to remove the divider line
                                DividerItemDecoration(
                                    recyclerDashboard.context,
                                    (layoutManager as LinearLayoutManager).orientation
                                )
                            )*/
                        }

                    } else {
                        Toast.makeText(activity as Context, "Some Error Occurred!", Toast.LENGTH_SHORT).show()
                    }
                }catch (e: JSONException){
                        e.printStackTrace()
                        Toast.makeText(activity as Context,"Some unexpected error occurred!!!",Toast.LENGTH_SHORT).show()
                    }

                }, Response.ErrorListener {
                    //Here we will handle the errors
                    //println("Error is $it")
                    if(activity!=null) {
                        Toast.makeText(
                            activity as Context,
                            "Volley error occurred!!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers =
                            HashMap<String, String>()//two parameters here are used for sending key and values
                        headers["Content-type"] = "application/json"
                        headers["token"] = "82443d9edf0a08"
                        return headers
                    }
                }
            queue.add(jsonObjectRequest)
       }else{
            //Internet is not available
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection is not found")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                val settingsIntent=Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish() // it refreshes the list once we come back from the settings
            }
            dialog.setNegativeButton("Exit"){text, Listener->
                ActivityCompat.finishAffinity(activity as Activity)//ActivityCompat class finishActivity takes the current activity as the parameter,
                // it closes all the running instances of the app
            }
            dialog.create()
            dialog.show()

        }

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.menu_dashboard,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item?.itemId
        if(id==R.id.action_sort)
        {
            Collections.sort(bookInfoList,ratingComparator)
            bookInfoList.reverse()
        }
        recyclerAdapter.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
    }
}