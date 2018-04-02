# KIndexBar
a simple IndexBar[![](https://jitpack.io/v/CaiJingLong/KIndexBar.svg)](https://jitpack.io/#CaiJingLong/KIndexBar)

## install 
see 
https://jitpack.io/#CaiJingLong/KIndexBar

## Sample

```kotlin
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
```

```xml
<?xml version="1.0" encoding="utf-8"?>
<android.support.constraint.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="com.github.indexbar.MainActivity">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Hello World!"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent"/>

    <com.github.caijinglong.library.KIndexBar
        android:id="@+id/indexBar"
        android:layout_width="80dp"
        android:layout_height="match_parent"
        android:background="#ff0"
        app:layout_constraintRight_toRightOf="parent"/>

</android.support.constraint.ConstraintLayout>

```

## use 

impletation IndexArray  
```kotlin
class MyArray: IndexArray {
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
```

let your bean impletion IndexAble  

```kotlin
class Index(val string: String, val id: String) : IndexAble {
        override fun indexString(): String {
            return string
        }

        override fun indexId(): String {
            return id
        }

    }
```

set your indexarry to IndexBar

## LICENSE
MIT
