package com.whx.baselib.animaion_dsl

import android.animation.Animator
import android.animation.ValueAnimator

class ValueAnim : Anim() {
    override var animator: Animator = ValueAnimator()
    private val valueAnimator
        get() = animator as ValueAnimator

    var values: Any? = null
        set(value) {
            field = value
            value?.let {
                when(it) {
                    is FloatArray -> valueAnimator.setFloatValues(*it)
                    is IntArray -> valueAnimator.setIntValues(*it)
                    else -> throw IllegalArgumentException("future support")
                }
            }
        }

    var action: ((Any) -> Unit)? = null
        set(value) {
            field = value
            valueAnimator.addUpdateListener { valueAnim ->
                valueAnim.animatedValue.let {
                    value?.invoke(it)
                }
            }
        }

    var repeatCount
        get() = 0
        set(value) {
            valueAnimator.repeatCount = value
        }

    /**
     * the repeat mode of [ValueAnim]
     * the available value is [ValueAnimator.RESTART] or [ValueAnimator.REVERSE]
     */
    var repeatMode
        get() = ValueAnimator.RESTART
        set(value) {
            valueAnimator.repeatMode = value
        }

    /**
     * reverse the value of [ValueAnimator]
     */
    override fun reverse() {
        values?.let {
            when (it) {
                is FloatArray -> {
                    it.reverse()
                    valueAnimator.setFloatValues(*it)
                }
                is IntArray -> {
                    it.reverse()
                    valueAnimator.setIntValues(*it)
                }
                else -> throw IllegalArgumentException("unsupported type of value")
            }
        }
    }

    override fun toBeginning() {
    }
}