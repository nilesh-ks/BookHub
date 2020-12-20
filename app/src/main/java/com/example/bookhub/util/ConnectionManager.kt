package com.example.bookhub.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class ConnectionManager{
    //Since connectivity manager provides the information at some context, we need to put context here

    fun checkConnectivity(context: Context): Boolean{
        val connectivityManager=context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        //gives info of the currently active network
        //need to be used in all apps for the internet check
    //To check if the network is active or not(not concerned with the Internet connection)
        val activeNetwork: NetworkInfo?=connectivityManager.activeNetworkInfo
        return if(activeNetwork?.isConnected!=null)
            activeNetwork.isConnected
        else false
    }

}