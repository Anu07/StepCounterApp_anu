package com.sd.src.stepcounterapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.sd.src.stepcounterapp.R


class NavArrayAdapter(val mContext: Context, list: Array<String>, var nCount:Int) :
    ArrayAdapter<String>(mContext, 0, list) {
    private var itemList:Array<String> = list


    override fun getCount(): Int {
        return itemList.size
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItem: View? = convertView
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.menu_textview, parent, false)

        val currentItem = itemList[position]
        val notificationBadge = listItem!!.findViewById(R.id.notificationBadge) as TextView

        val name = listItem!!.findViewById(R.id.menuItemText) as TextView
        name.text = currentItem

        if(currentItem == "Notifications" && nCount>0){
            notificationBadge.visibility = View.VISIBLE
            notificationBadge.text = nCount.toString()
        }else{
            notificationBadge.visibility = View.GONE
        }

        return listItem
    }
}