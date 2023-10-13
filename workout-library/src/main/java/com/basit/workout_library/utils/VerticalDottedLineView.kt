package com.basit.workout_library.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathDashPathEffect
import android.util.AttributeSet
import android.view.View


internal class VerticalDottedLineView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
)  : View(context, attrs, defStyle) {

    init {
        initView()
    }

    private var mPaint: Paint? = null

    private fun initView(){
        //val res = resources
        mPaint = Paint()

        mPaint?.color = Color.parseColor("#838383")
        val size = 4.0F
        val gap = 24.0F
        mPaint?.style = Paint.Style.FILL


        val path = Path()
        path.addCircle(0F, 0F, size, Path.Direction.CW)

        mPaint?.pathEffect = PathDashPathEffect(
            path,
            gap,
            0f,
            PathDashPathEffect.Style.ROTATE
        )
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawLine(
            (width / 2).toFloat(), 0F,
            (width / 2).toFloat(), height.toFloat(), mPaint!!
        )
    }

}