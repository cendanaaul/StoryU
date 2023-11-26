package com.cencen.storyu.customelement

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import com.cencen.storyu.R

class ETEmailCustom : AppCompatEditText {

    private var isEmailTrueFormat: Boolean = false

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
            val mails = text?.trim()
            if (mails.isNullOrEmpty()) {
                isEmailTrueFormat = false
                error = resources.getString(R.string.email_here)
            } else if (!Patterns.EMAIL_ADDRESS.matcher(mails).matches()) {
                isEmailTrueFormat = false
                error = resources.getString(R.string.email_error)
            } else {
                isEmailTrueFormat = true
            }
        })
    }
}