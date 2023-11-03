package com.basit.workout_library.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.util.AttributeSet

internal class VerticalTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyle) {

    private var _width = 0
    private var _height = 0
    private val _bounds = Rect()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        _height = measuredWidth;
        _width = measuredHeight;
        setMeasuredDimension(_width, _height);
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()

        canvas.translate(_width.toFloat(), _height.toFloat())
        canvas.rotate(-90F)

        val paint = paint
        paint.color = textColors.defaultColor

        val text: String = text()

        paint.getTextBounds(text, 0, text.length, _bounds)
        canvas.drawText(
            text,
            compoundPaddingLeft.toFloat(), ((_bounds.height() - _width) / 2).toFloat(), paint
        )

        canvas.restore()
    }

    private fun text(): String {
        return super.getText().toString()
    }

}