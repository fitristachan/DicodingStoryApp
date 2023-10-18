package com.dicoding.dicodingstoryapp.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.dicoding.dicodingstoryapp.R

class MyButton : AppCompatButton {

    private lateinit var enabledBackground: Drawable
    private lateinit var disabledBackground: Drawable
    private var txtColorLight: Int = 0
    private var txtColorDark: Int = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        background = if (isEnabled) enabledBackground else disabledBackground
        if (isEnabled) {
            background = enabledBackground
            setTextColor(txtColorLight)
        } else {
            background = disabledBackground
            setTextColor(txtColorDark)
        }
        textSize = 16f
        gravity = Gravity.CENTER

    }

    private fun init() {
        txtColorLight = ContextCompat.getColor(context, android.R.color.background_light)
        txtColorDark = ContextCompat.getColor(context, android.R.color.background_dark)
        enabledBackground =
            ContextCompat.getDrawable(context, R.drawable.bg_button_hovered) as Drawable
        disabledBackground = ContextCompat.getDrawable(context, R.drawable.bg_button) as Drawable
    }

}