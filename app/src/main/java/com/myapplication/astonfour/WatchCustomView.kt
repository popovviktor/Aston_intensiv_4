package com.myapplication.astonfour

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import java.util.*


class WatchCustomView
    @JvmOverloads constructor(
        context: Context,
        attrs:AttributeSet? = null,
        defStyleAttr:Int = 0):View(context,attrs,defStyleAttr) {
    private var colorCircle:Int? = null
    private var colorLineSecond:Int? = null
    private var colorLineMinute:Int? = null
    private var colorLineHour:Int? = null
    init {
        val typedArray = context.obtainStyledAttributes(
            attrs,R.styleable.WatchCustomView,
            defStyleAttr,0)
        colorCircle = typedArray.getColor(R.styleable.WatchCustomView_colorCicle,Color.CYAN)
        colorLineHour = typedArray.getColor(R.styleable.WatchCustomView_colorLineHour,Color.RED)
        colorLineMinute = typedArray.getColor(R.styleable.WatchCustomView_colorLineMinute,Color.YELLOW)
        colorLineSecond = typedArray.getColor(R.styleable.WatchCustomView_colorLineSecond,Color.GREEN)
        typedArray.recycle()
    }
    private val textSizeForDigitsOfHours by lazy {
        context.dip2px(30)
    }
    private val widthStrokeForHourLine by lazy {
        context.dip2px(5)
    }
    private val widthStrokeForMinuteLine by lazy {
        context.dip2px(5)
    }
    private val widthStrokeForSecondLine by lazy {
        context.dip2px(5)
    }
    private val paintForOutCircle by lazy {
        Paint().apply {
            if (colorCircle==null){
                color = Color.GREEN
            }else{
                color = colorCircle!!
            }
            isAntiAlias = true
        }
    }
    private val widthScreenCenter by lazy {
        if (width<=height){
            (this.width / 2).toFloat()
        }else{
            (this.height / 2).toFloat()}
    }
    private val heightScreenCenter by lazy {
        (this.height / 2).toFloat()
    }
    private val radiusLineSecondForEndXYCoords by lazy {
        (widthScreenCenter - context.dip2px(30)).toFloat()
    }
    private val radiusLineMinuteForEndXYCoords by lazy {
        (widthScreenCenter - context.dip2px(60)).toFloat()
    }
    private val radiusLineHourForEndXYCoords by lazy {
        (widthScreenCenter - context.dip2px(90)).toFloat()
    }
    private val radiusForDigitsOfHourForEndXYCoords by lazy {
        (widthScreenCenter - context.dip2px(80)).toFloat()
    }
    private val pathOutCircle by lazy {
        Path().apply {
            addCircle(widthScreenCenter, heightScreenCenter, widthScreenCenter, Path.Direction.CW)
        }
    }
    private val pathCutFromOutCircleInnerCircle by lazy {
        Path().apply {
            addCircle(
                widthScreenCenter,
                heightScreenCenter,
                widthScreenCenter * 0.9F,
                Path.Direction.CW
            )
            op(pathOutCircle, Path.Op.REVERSE_DIFFERENCE)
        }
    }
    private val paintLineSecond by lazy {
        Paint().apply {
            if (colorLineSecond==null){
                color = Color.YELLOW
            }else{
                color = colorLineSecond!!
            }
            strokeWidth = widthStrokeForSecondLine
        }
    }
    private val paintLineMinute by lazy {
        Paint().apply {
            if (colorLineMinute==null){
                color = Color.GREEN
            }else{
                color = colorLineMinute!!
            }
            isAntiAlias = true
            strokeWidth = widthStrokeForMinuteLine
        }
    }
    private val paintLineHour by lazy {
        Paint().apply {
            if (colorLineHour==null){
                color = Color.RED
            }else{
                color = colorLineHour!!
            }
            isAntiAlias = true
            strokeWidth = widthStrokeForHourLine
        }
    }
    private val paintMarksSecondAndHour by lazy {
        Paint().apply {
            color = Color.BLUE
            isAntiAlias = true
            strokeWidth = widthStrokeForSecondLine
        }
    }
    private val marksForHour by lazy {
        context.dip2px(30).toFloat()
    }
    private val marksForSecondAndMinute by lazy {
        context.dip2px(15).toFloat()
    }
    private val PaintScaleHourNumber by lazy {
        Paint().apply {
            color = Color.RED
            isAntiAlias = true
            textSize = textSizeForDigitsOfHours
            setAntiAlias(true)
            setTextAlign(Paint.Align.CENTER)
            setStrokeCap(Paint.Cap.ROUND);
        }
    }
    private val timer by lazy {
        Timer()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val second = Calendar.getInstance().get(Calendar.SECOND)
        val minute = Calendar.getInstance().get(Calendar.MINUTE)
        val hour = Calendar.getInstance().get(Calendar.HOUR)
        drawAllMarksAndDigitsHour(canvas)
        canvas.drawPath(pathCutFromOutCircleInnerCircle, paintForOutCircle)
        drawLineSecond(canvas,second)
        drawLineMinute(canvas, minute)
        drawLineHour(canvas,hour,minute)
        startWatch()
    }
    fun startWatch(){
        val timerTask = object :TimerTask(){
            override fun run() {
                invalidate()
            }}
        timer.schedule(timerTask,0,1000)
    }

    fun drawLineMinute(canvas: Canvas, minuteNow: Int) {
        val PiValueEndXYCoords: Double = minuteNow * 6 * Math.PI / 180
        val sin1 = Math.sin(PiValueEndXYCoords)
        val cos1 = Math.cos(PiValueEndXYCoords)
        val xEnd: Float = (radiusLineMinuteForEndXYCoords * sin1.toFloat()) + widthScreenCenter
        val yEnd: Float = -(radiusLineMinuteForEndXYCoords * cos1.toFloat()) + heightScreenCenter
        canvas.drawLine(widthScreenCenter, heightScreenCenter, xEnd, yEnd, paintLineMinute)
    }

    fun drawLineHour(canvas: Canvas, hourNow: Int,minuteNow: Int) {
        val minInHour = Math.floor(minuteNow.toDouble()/60*100)/100
        val nowTimeInHourPlusMin = (hourNow + minInHour)
        val PiValueEndXYCoords: Double = (nowTimeInHourPlusMin) * 30 * Math.PI / 180
        val sin1 = Math.sin(PiValueEndXYCoords)
        val cos1 = Math.cos(PiValueEndXYCoords)
        val xEnd: Float = (radiusLineHourForEndXYCoords * sin1.toFloat()) + widthScreenCenter
        val yEnd: Float = -(radiusLineHourForEndXYCoords * cos1.toFloat()) + heightScreenCenter
        canvas.drawLine(widthScreenCenter, heightScreenCenter, xEnd, yEnd, paintLineHour)
    }

    fun drawLineSecond(canvas: Canvas, nowSecond: Int) {
        val hudu: Double = nowSecond * 6 * Math.PI / 180
        val sin1 = Math.sin(hudu)
        val cos1 = Math.cos(hudu)
        val xEnd: Float = (radiusLineSecondForEndXYCoords * sin1.toFloat()) + widthScreenCenter
        val yEnd: Float = -(radiusLineSecondForEndXYCoords * cos1.toFloat()) + heightScreenCenter
        canvas.drawLine(widthScreenCenter, heightScreenCenter, xEnd, yEnd, paintLineSecond)
    }
    fun drawMarksHour(canvas: Canvas, iterator:Int){
        val PiValueForEndCoords: Double = iterator * 6 * Math.PI / 180
        val sin1 = Math.sin(PiValueForEndCoords)
        val cos1 = Math.cos(PiValueForEndCoords)
        val xStart: Float = (((radiusLineSecondForEndXYCoords - marksForHour) * sin1.toFloat())
                + widthScreenCenter)
        val yStart: Float = (-((radiusLineSecondForEndXYCoords - marksForHour) * cos1.toFloat())
                + heightScreenCenter)
        val xEnd: Float = (radiusLineSecondForEndXYCoords * sin1.toFloat()) + widthScreenCenter
        val yEnd: Float = -(radiusLineSecondForEndXYCoords * cos1.toFloat()) + heightScreenCenter
        canvas.drawLine(xStart, yStart, xEnd, yEnd, paintMarksSecondAndHour)
    }
    fun drawDigitsForHour(canvas: Canvas, iterator: Int){
        val PiValueForEndCoords: Double = (iterator) * 6 * Math.PI / 180
        val sin1HourText = Math.sin(PiValueForEndCoords)
        val cos1HourText = Math.cos(PiValueForEndCoords)
        val hour = (iterator / 5).toString()
        val xEndClock: Float = ((radiusForDigitsOfHourForEndXYCoords) * sin1HourText.toFloat()
                + widthScreenCenter)
        val yEndClock: Float = (-(radiusForDigitsOfHourForEndXYCoords * cos1HourText.toFloat())
                + heightScreenCenter + textSizeForDigitsOfHours / 3)
        canvas.drawText(hour, xEndClock, yEndClock, PaintScaleHourNumber)
    }
    fun drawMarksForSecondAndMinute(canvas: Canvas,itetator: Int){
        val PiValueForEndCoords: Double = itetator * 6 * Math.PI / 180
        val sin1 = Math.sin(PiValueForEndCoords)
        val cos1 = Math.cos(PiValueForEndCoords)
        val xStart: Float = (((radiusLineSecondForEndXYCoords - marksForSecondAndMinute)
                * sin1.toFloat())+ widthScreenCenter)
        val yStart: Float = (-((radiusLineSecondForEndXYCoords - marksForSecondAndMinute)
                * cos1.toFloat())+ heightScreenCenter)
        val xEnd: Float = (radiusLineSecondForEndXYCoords * sin1.toFloat()) + widthScreenCenter
        val yEnd: Float = -(radiusLineSecondForEndXYCoords * cos1.toFloat()) + heightScreenCenter
        canvas.drawLine(xStart, yStart, xEnd, yEnd, paintMarksSecondAndHour)
    }
    fun drawAllMarksAndDigitsHour(canvas: Canvas) {
        for (i in 0..60) {
            if (i % 5 == 0) {
                drawMarksHour(canvas,i)
                if (i != 0) {
                    drawDigitsForHour(canvas,i)
                }
            } else {
                drawMarksForSecondAndMinute(canvas,i)
            }
        }
    }
    fun changeColorStyleOne(){
        colorCircle = Color.BLUE
        paintForOutCircle.color = colorCircle!!
        colorLineHour = Color.RED
        paintLineHour.color = colorLineHour!!
        colorLineMinute = Color.GREEN
        paintLineMinute.color = colorLineMinute!!
        colorLineSecond = Color.YELLOW
        paintLineSecond.color = colorLineSecond!!
        invalidate()
    }
    fun changeColorStyleTwo(){
        colorCircle = Color.GREEN
        paintForOutCircle.color = colorCircle!!
        colorLineHour = Color.MAGENTA
        paintLineHour.color = colorLineHour!!
        colorLineMinute = Color.BLACK
        paintLineMinute.color = colorLineMinute!!
        colorLineSecond = Color.CYAN
        paintLineSecond.color = colorLineSecond!!
        invalidate()
    }
}