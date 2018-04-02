package com.github.caijinglong.library

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * Created by cai on 2018/3/30.
 */
class KIndexBar @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    var indexArray: IndexArray = defaultIndexArray()
        set(value) {
            field = value
            invalidate()
        }

    private val _height = 30

    private fun defaultIndexArray(): IndexArray {
        return object : IndexArray {
            override fun indexArray(): List<IndexAble> {
                val arr = ArrayList<IndexAble>()
                for (v in 'A'..'Z') {
                    arr.add(Index(v.toString(), v.toString()))
                }
                return arr
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) {
            return
        }
        val count = indexArray.indexArray().count()

        _rect.left = 0
        _rect.right = width

        val h = height / count

        for (index in 0 until count) {
            _rect.top = height / count * index
            _rect.bottom = _rect.top + h
            drawInRect(canvas, _rect, indexArray.indexArray()[index].indexString(), 40F)
        }

    }

    private val _rect = Rect()

    companion object {
        var DEBUG_BORDER = false
    }

    private fun drawInRect(canvas: Canvas, rect: Rect, text: String, textSize: Float = 30F) {
        if (DEBUG_BORDER) {
            val p = Paint()
            p.color = Color.RED
            p.style = Paint.Style.STROKE
            canvas.drawRect(rect, p)
        }

        val x = rect.centerX()
        val y = rect.centerY() + textSize / 2
        _paint.textSize = textSize
        canvas.drawText(text, x.toFloat(), y, _paint)
    }

    private val _paint = Paint()

    init {
        _paint.color = Color.BLACK
        _paint.isAntiAlias = true
        _paint.textAlign = Paint.Align.CENTER
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                // 计算坐标,获取当前
                val per = height / indexArray.indexArray().count()
                val index = (event.y / per).toInt()
                onTouchObserver?.onCurrentTouch(indexArray.indexArray()[index])
                return true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                // 取消相关提示信息
                return true
            }
            else -> {
            }
        }

        return super.onTouchEvent(event)
    }

    private class Index(var index: String, var id: String) : IndexAble {

        override fun indexString(): String {
            return index
        }

        override fun indexId(): String {
            return id
        }
    }

    var onTouchObserver: IndexBarOnTouchObserver? = null

    interface IndexBarOnTouchObserver {

        fun onCurrentTouch(currentTouch: IndexAble)

    }
}