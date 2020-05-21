package com.whx.baselib.animaion_dsl

import android.animation.Animator
import android.animation.AnimatorSet

/**
 * a Container for [Anim] just like [AnimatorSet], but it could reverse itself without API level limitation.
 * In addition, it is easy to build mush shorter and readable animation code like the following:
 *
 * animSet {
 *      valueAnim {
 *          values = floatArrayOf(1.0f, 1.4f)
 *          action = { value -> tv.scaleX = (value as Float) }
 *      } with valueAnim {
 *          values = floatArrayOf(0f, -200f)
 *          action = { value -> tv.translationY = (value as Float) }
 *      }
 *      duration = 200L
 * }
 *
 * if you want to run animation with several properties on one Object,
 * using [ObjectAnim] is more efficient than [ValueAnim], like the following:
 *
 * animSet {
 *      objectAnim {
 *          target = tvTitle
 *          translationX = floatArrayOf(0f, 200f)
 *          alpha = floatArrayOf(1.0f, 0.3f)
 *          scaleX = floatArrayOf(1.0f, 1.3f)
 *      }
 *      duration = 100L
 * }
 *
 */
class AnimSet : Anim() {
    override var animator = AnimatorSet() as Animator
    private val animatorSet
        get() = animator as AnimatorSet

    private val anims by lazy { mutableListOf<Anim>() }

    private var isAtStartPoint = true

    private var hasReverse = true

    fun valueAnim(animCreation: ValueAnim.() -> Unit): Anim = ValueAnim().apply(animCreation)
        .also { it.addListener() }
        .also { anims.add(it) }

    fun objectAnim(action: ObjectAnim.() -> Unit): Anim = ObjectAnim().apply(action)
        .also { it.setPropertyValueHolder() }
        .also { it.addListener() }
        .also { anims.add(it) }

    fun start() {
        if (animatorSet.isRunning) return
        anims.takeIf { hasReverse }?.forEach { it.reverse() }.also { hasReverse = false }
        if (anims.size == 1) animatorSet.play(anims.first().animator)
        if (isAtStartPoint) {
            animatorSet.start()
            isAtStartPoint = false
        }
    }

    override fun reverse() {
        if (animatorSet.isRunning) return
        anims.takeIf { !hasReverse }?.forEach { it.reverse() }.also { hasReverse = true }
        if (!isAtStartPoint) {
            animatorSet.start()
            isAtStartPoint = true
        }
    }

    override fun toBeginning() {
        anims.forEach { it.toBeginning() }
    }

    fun getAnim(index: Int) = anims.takeIf { index in anims.indices }?.let { it[index] }

    fun cancel() {
        animatorSet.cancel()
    }

    infix fun Anim.before(anim: Anim): Anim {
        animatorSet.play(animator).before(anim.animator).let { this.builder = it }
        return anim
    }

    infix fun Anim.with(anim: Anim): Anim {
        if (builder == null) builder = animatorSet.play(animator).with(anim.animator)
        else builder?.with(anim.animator)
        return anim
    }

    infix fun Anim.after(anim: Anim): Anim {
        animatorSet.play(animator).after(anim.animator).let { this.builder = it }
        return anim
    }

    fun animSet(creation: AnimSet.() -> Unit) = AnimSet().apply { creation() }.also { it.addListener() }
}