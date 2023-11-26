package com.cencen.storyu.customelement

import android.content.Context
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import com.cencen.storyu.R

class ETPassword : AppCompatEditText {

    private var isPasswordTrue: Boolean = false

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

        transformationMethod = PasswordTransformationMethod.getInstance()

        addTextChangedListener(onTextChanged = { p0: CharSequence?, p1: Int, p2: Int, p3: Int ->
            val memberPass = text?.trim()
            when {
                memberPass.isNullOrEmpty() -> {
                    isPasswordTrue = false
                    error = resources.getString(R.string.pass_here)
                }

                memberPass.length < 8 -> {
                    isPasswordTrue = false
                    error = resources.getString(R.string.pass_error)
                }

                else -> {
                    isPasswordTrue = true
                }
            }
        })
    }

}