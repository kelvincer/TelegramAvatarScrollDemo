package com.home.myapplicationtelegramguideline

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlin.random.Random

class AnotherAdapter(private val context: Context) : RecyclerView.Adapter<AnotherAdapter.ViewHolder>() {

    private val s = AnotherAdapter::class.java.simpleName
    private val colors = mutableListOf(
        "#984536", "#129845", "#128900", "#67ffaa",
        "#56bbbb", "#998877", "#aaaaaa", "#990011", "#12eeee", "#aabbcc"
    )
    private val names =
        mutableListOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_another, parent, false)
        val height = Random.nextInt(100, 600)

        view.layoutParams.height = height.toFloat().toDips(context).toInt()
        Log.d(s, "onCreateViewHolder: ${height.toFloat().toDips(context).toInt()}")
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(names[position], colors[position])
    }

    override fun getItemCount() = names.size

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bindItem(name: String, color: String) {
            val tvName = view.findViewById<TextView>(R.id.tvName)
            tvName.text = name
            val ivImage = view.findViewById<ImageView>(R.id.ivAvatar)
            ivImage.setColorFilter(Color.parseColor(color))
        }
    }
}

fun Float.toDips(context: Context) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics)

fun dpToPx(activity: Activity, dp: Int): Int {
    val r: Resources = activity.resources
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        r.displayMetrics
    ).toInt()
}