package com.home.myapplicationtelegramguideline

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    val s = MainActivity::class.java.simpleName
    lateinit var rvMain: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvMain = findViewById(R.id.rvMain)
        rvMain.adapter = AnotherAdapter(this)
        rvMain.addItemDecoration(
            DividerItemDecoration(
                applicationContext,
                DividerItemDecoration.VERTICAL
            )
        )
        rvMain.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var lastPosition = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = rvMain.layoutManager as LinearLayoutManager
                val firstPosition = layoutManager.findFirstVisibleItemPosition()
                lastPosition = layoutManager.findLastVisibleItemPosition()
                val rvRect = Rect()
                rvMain.getGlobalVisibleRect(rvRect)
                for (i in firstPosition until lastPosition) {
                    val motionLay = layoutManager.findViewByPosition(i)
                        ?.findViewById<MotionLayout>(R.id.motion_layout)
                    motionLay?.progress = 0f
                }

                val rowRect = Rect()
                layoutManager.findViewByPosition(lastPosition)!!.getGlobalVisibleRect(rowRect)
                var percentLast: Float = if (rowRect.bottom >= rvRect.bottom) {
                    val visibleHeightFirst: Int = rvRect.bottom - rowRect.top
                    visibleHeightFirst / layoutManager.findViewByPosition(lastPosition)!!.height.toFloat()
                } else {
                    val visibleHeightFirst: Int = rowRect.bottom - rvRect.top
                    visibleHeightFirst / layoutManager.findViewByPosition(lastPosition)!!.height.toFloat()
                }
                if (percentLast > 1) percentLast = 1f

                val motionLay = layoutManager.findViewByPosition(lastPosition)
                    ?.findViewById<MotionLayout>(R.id.motion_layout)

                val factor =
                    dpToPx(this@MainActivity, 20 + 30) / layoutManager.findViewByPosition(
                        lastPosition
                    )!!.height.toFloat()

                if (percentLast < factor)
                    motionLay?.progress  = 1f
                else
                    motionLay?.progress  =
                        -1 / (1 - factor) * percentLast + (1 + factor / (1 - factor))//1.2998f//-1.2998f*percentLast + 1.2998f

                Log.d(
                    s,
                    "onScrolled progress: ${motionLay?.progress}, percentLast $percentLast," +
                            " position: $lastPosition, height: ${
                                layoutManager.findViewByPosition(
                                    lastPosition
                                )!!.height.toFloat()
                            }"
                )
            }
        })
    }
}