package org.monora.uprotocol.client.android.view

import android.content.*
import android.content.res.TypedArray
import android.util.AttributeSet
import android.widget.RelativeLayout
import org.monora.uprotocol.client.android.R

/**
 * created by: Veli
 * date: 27.03.2018 22:32
 */
class ComparativeRelativeLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    private var alwaysUseWidth = true

    private var baseOnSmaller = false

    private var tallerExtraLength = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Set a proportional layout.
        var width = widthMeasureSpec
        var height = heightMeasureSpec
        if (baseOnSmaller) {
            if (width > height) {
                width = height + tallerExtraLength
            } else if (height > width) {
                height = width + tallerExtraLength
            }
        } else if (alwaysUseWidth) {
            height = width + tallerExtraLength
        } else {
            width = height + tallerExtraLength
        }
        super.onMeasure(width, height)
    }

    init {
        val typedAttributes: TypedArray = context.theme
            .obtainStyledAttributes(attrs, R.styleable.ComparativeRelativeLayout, defStyleAttr, 0)
        baseOnSmaller = typedAttributes.getBoolean(
            R.styleable.ComparativeRelativeLayout_baseOnSmallerLength, baseOnSmaller
        )
        tallerExtraLength = typedAttributes.getDimensionPixelSize(
            R.styleable.ComparativeRelativeLayout_tallerLengthExtra, tallerExtraLength
        )
        alwaysUseWidth = typedAttributes.getBoolean(
            R.styleable.ComparativeRelativeLayout_alwaysUseWidth, alwaysUseWidth
        )
    }
}