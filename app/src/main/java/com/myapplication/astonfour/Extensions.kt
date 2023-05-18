package com.myapplication.astonfour

import android.content.Context

fun Context.dip2px(value: Int):Float = resources.displayMetrics.density*value