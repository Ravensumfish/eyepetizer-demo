package com.example.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.home.api.DiscoveryApi

class CategoryDetailFragment: Fragment() {
    private var mId=0
    private var mName=""
    private var mHeaderImage=""
    private var mDescription=""
    private var mBgpicture=""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            mId=it.getInt("id")
            mName=it.getString("name","")
            mHeaderImage=it.getString("headerImage","")
            mDescription=it.getString("description","")
            mBgpicture=it.getString("bgPicture","")
        }
        getCategoryVideos()
    }
}