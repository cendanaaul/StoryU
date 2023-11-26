package com.cencen.storyu.customelement

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import com.cencen.storyu.R

class ETName : AppCompatEditText {

    private var isNameRight: Boolean = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {

        val typeface = ResourcesCompat.getFont(context, R.font.regular)
        setTypeface(typeface)

        addTextChangedListener(onTextChanged = { p0: CharSequence?, p1: Int, p2: Int, p3: Int ->
            val nameMember = text?.trim()
            if (nameMember.isNullOrEmpty()) {
                isNameRight = false
                error = resources.getString(R.string.name_right)
            } else {
                isNameRight = true
            }
        })
    }

}