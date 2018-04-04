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

    private var needRefresh = false

    var indexArray: IndexArray = defaultIndexArray()
        set(value) {
            field = value
            needInvalidate()
        }

    private fun needInvalidate() {
        //用于在绘制过程中触发重绘,从而导致的array修改异常
        if (!isDrawing) {
            invalidate()
        } else {
            needRefresh = true
        }
    }

    private var _height = 40

    /** item height */
    var itemHeight = _height
        set(value) {
            field = value
            _height = value
            needInvalidate()
        }

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

    private var isDrawing = false

    override fun onDraw(canvas: Canvas?) {
        // 记录正在drawing的状态
        isDrawing = true
        super.onDraw(canvas)
        if (canvas == null) {
            isDrawing = false
            return
        }


        val array = indexArray.indexArray().toList()

        val count = array.count()

        _rect.left = 0
        _rect.right = width

        var paddingTop = 0
        var h = height / count

        if (count * _height < height) {  // 如果当目前实际高度大于绘制高度时,用于控制绘制区域
            paddingTop = (height - _height * count) / 2
            h = _height
        }

        mPaddingTop = paddingTop
        perChildHeight = h

        for (index in 0 until count) {
            _rect.top = h * index + paddingTop
            _rect.bottom = _rect.top + h
            drawInRect(canvas, _rect, array[index].indexString(), textSize)
        }

        isDrawing = false

        if (needRefresh) { //如果被标记为需要刷新,则重新绘制一次
            needRefresh = false
            invalidate()
        }
    }

    private var mPaddingTop = 0
    private var perChildHeight = 0

    private val _rect = Rect()

    var textSize = 30F //文字高度,不应大于itemHeight
        set(value) {
            field = value
            needRefresh
        }

    private val _paint = Paint()

    init {
        _paint.color = Color.BLACK
        _paint.isAntiAlias = true
        _paint.textAlign = Paint.Align.CENTER
    }

    var textColor = Color.BLACK
        set(value) {
            field = value
            _paint.color = value
        }

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
        val y = rect.centerY() + textSize / 4
        _paint.textSize = textSize
        canvas.drawText(text, x.toFloat(), y.toFloat(), _paint)
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                // 计算坐标,获取当前
                val index = getCurrentIndex(event.y)
                if (index < 0 || index >= indexArray.indexArray().count()) {
                    return true
                } else {
                    onTouchObserver?.onCurrentTouch(indexArray.indexArray()[index])
                }
                return true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                // 取消相关提示信息
                onTouchObserver?.onTouchCancelOrUp()
                return true
            }
            else -> {
            }
        }

        return super.onTouchEvent(event)
    }

    private fun getCurrentIndex(y: Float): Int {
//        val count = indexArray.indexArray().count()
        if (y < mPaddingTop || y > height - mPaddingTop) {
            return -1
        }
//        val h = height - mPaddingTop * 2 //真实的可点击区域高度 ps:240
        val realY = y - mPaddingTop // y的真实坐标  ps:22
        //期望 index 0  22/40 = 0   50/40 = 1 so
        return (realY / perChildHeight).toInt()
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

        fun onTouchCancelOrUp() {}
    }
}