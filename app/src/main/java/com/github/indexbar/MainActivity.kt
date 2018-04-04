package com.github.indexbar

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.caijinglong.library.IndexAble
import com.github.caijinglong.library.IndexArray
import com.github.caijinglong.library.KIndexBar

class MainActivity : AppCompatActivity(), KIndexBar.IndexBarOnTouchObserver {
    val kIndexBar: KIndexBar by lazy {
        findViewById<KIndexBar>(R.id.indexBar)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        KIndexBar.DEBUG_BORDER = true
        kIndexBar.indexArray = object : IndexArray {
            override fun indexArray(): List<IndexAble> {
                val list = ArrayList<IndexAble>()
                list.add(Index("10~20", "1"))
                list.add(Index("20~20", "1"))
                list.add(Index("30~20", "1"))
                list.add(Index("40~20", "1"))
                list.add(Index("50~20", "1"))
                list.add(Index("60~20", "1"))
                return list
            }
        }

        kIndexBar.onTouchObserver = this
    }

    override fun onCurrentTouch(currentTouch: IndexAble) {
        println(currentTouch.indexString())
    }

    class Index(val string: String, val id: String) : IndexAble {
        override fun indexString(): String {
            return string
        }

        override fun indexId(): String {
            return id
        }

    }
}
