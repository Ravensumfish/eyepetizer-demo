package com.example.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object TimeUtils {
    fun formatDuration(seconds:Int) : String{
        val sec = seconds % 60
        val min = seconds / 60
        var s1:String = min.toString()
        var s2:String = sec.toString()
        if (min < 10){
            s1 = "0$min"
        }

        if (sec<10){
            s2 = "0$sec"
        }

        return "$s1:$s2"
    }

    fun transToDate(time:Long):String{
        val s = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        val date = Date(time)
        return s.format(date)
    }

}