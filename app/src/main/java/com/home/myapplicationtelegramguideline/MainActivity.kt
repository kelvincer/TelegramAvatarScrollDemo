package com.home.myapplicationtelegramguideline

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs


class MainActivity : AppCompatActivity() {

    val TAG = MainActivity::class.java.simpleName
    lateinit var rvMain: RecyclerView
    lateinit var viewQ: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvMain = findViewById<RecyclerView>(R.id.rvMain)
        rvMain.adapter = AnotherAdapter(this)
        rvMain.addItemDecoration(
            DividerItemDecoration(
                applicationContext,
                DividerItemDecoration.VERTICAL
            )
        )
        rvMain.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var percentFirst = 0
            var lastPosition = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = rvMain.layoutManager as LinearLayoutManager
                val firstPosition = layoutManager.findFirstVisibleItemPosition()
                lastPosition = layoutManager.findLastVisibleItemPosition()
                val rvRect = Rect()
                rvMain.getGlobalVisibleRect(rvRect)
                for (i in firstPosition until lastPosition) {
                    val rowRect = Rect()
                    layoutManager.findViewByPosition(i)!!.getGlobalVisibleRect(rowRect)
                    var percentFirst: Int
                    percentFirst = if (rowRect.bottom >= rvRect.bottom) {
                        val visibleHeightFirst: Int = rvRect.bottom - rowRect.top
                        visibleHeightFirst * 100 / layoutManager.findViewByPosition(i)!!.height
                    } else {
                        val visibleHeightFirst: Int = rowRect.bottom - rvRect.top
                        visibleHeightFirst * 100 / layoutManager.findViewByPosition(i)!!.height
                    }
                    if (percentFirst > 100) percentFirst = 100
                    val motionLay = layoutManager.findViewByPosition(i)
                        ?.findViewById<MotionLayout>(R.id.motion_layout)
                    motionLay?.progress = abs(1f - 1) //percentFirst / 100f
                }

                val rowRect = Rect()
                layoutManager.findViewByPosition(lastPosition)!!.getGlobalVisibleRect(rowRect)
                var percentFirst: Float
                percentFirst = if (rowRect.bottom >= rvRect.bottom) {
                    val visibleHeightFirst: Int = rvRect.bottom - rowRect.top
                    visibleHeightFirst / layoutManager.findViewByPosition(lastPosition)!!.height.toFloat()
                } else {
                    val visibleHeightFirst: Int = rowRect.bottom - rvRect.top
                    visibleHeightFirst / layoutManager.findViewByPosition(lastPosition)!!.height.toFloat()
                }
                if (percentFirst > 1) percentFirst = 1f
                val motionLay = layoutManager.findViewByPosition(lastPosition)
                    ?.findViewById<MotionLayout>(R.id.motion_layout)

                val value = abs(1f - percentFirst)
                val couple = 1 - dpToPx(this@MainActivity, 50) / (layoutManager.findViewByPosition(
                    lastPosition
                )!!.height.toFloat() - dpToPx(this@MainActivity, 10))
                if (dy < 0) {
                    // Scrolling down
                    if (value <= couple)
                        motionLay?.progress = value
                    else
                        motionLay?.progress = couple
                } else {
                    // Scrolling up
                    if (value >= couple)
                        motionLay?.progress = abs(couple)
                    else {
                        motionLay?.progress = value
                    }
                }

                Log.d(
                    TAG,
                    "onScrolled progress: ${motionLay?.progress}, percent: $percentFirst, couple: $couple," +
                            " position: $lastPosition, height: ${
                                layoutManager.findViewByPosition(
                                    lastPosition
                                )!!.height.toFloat()
                            }"
                )
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        println("The RecyclerView is not scrolling")
                    }
                    RecyclerView.SCROLL_STATE_DRAGGING -> {
                        println("Scrolling now")
                        rvMain.invalidate()
                    }
                    RecyclerView.SCROLL_STATE_SETTLING -> {
                        println("Scroll Settling")

                    }
                }
            }
        })
    }
}