package com.whx.commonui.layout

import android.content.Context
import android.graphics.Canvas
import android.graphics.Region
import android.util.AttributeSet
import android.util.SparseIntArray
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IntDef
import com.whx.commonui.R

open class FlowLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ViewGroup(context, attrs, defStyleAttr) {

    companion object {

        const val TOP = 0
        const val CENTER_VERTICAL = 1
        const val BOTTOM = 2

        private const val DEFAULT_CHILD_GRAVITY = TOP
    }

    @IntDef(TOP, CENTER_VERTICAL, BOTTOM)
    annotation class ChildGravity

    private val lineLastViewIndexArray = SparseIntArray()
    private val lineMaxHeightArray = SparseIntArray()

    @ChildGravity
    private var childGravity = DEFAULT_CHILD_GRAVITY
    private var lineSpace: Int = 0
    private var childMargin = 0
    private var canExpand = false

    private var iconWidth = 0
    private var firstLineLastViewIndex = 0
    private var isFold = true
    private var isFirstTime = true
    private val removedViews = arrayListOf<View>()

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout)

        childGravity = a.getInt(R.styleable.FlowLayout_childGravity, DEFAULT_CHILD_GRAVITY)
        lineSpace = a.getDimensionPixelSize(R.styleable.FlowLayout_lineSpace, 0)
        childMargin = a.getDimensionPixelOffset(R.styleable.FlowLayout_childMargin, 0)
        canExpand = a.getBoolean(R.styleable.FlowLayout_canExpand, false)

        iconWidth = if (canExpand) context.resources.getDimensionPixelSize(R.dimen.dp_20) else 0

        a.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //ViewGroup的测量模式和大小
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        //最大宽度
        var maxWidth = 0

        // reset array
        lineLastViewIndexArray.clear()
        lineMaxHeightArray.clear()

        // for-measure
        var firstShowChild = true//当前child是否为第一个要展示的child
        var currentLine = 0
        val initWidth = paddingLeft + paddingRight
        var curLineWidth = initWidth
        var curLineHeight = 0
        val childCount = childCount
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child == null || child.visibility == View.GONE) {//GONE不参与计算
                continue
            }
            // 根据margin测量child
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0)
            // child的占用空间
            val lp = child.layoutParams as ViewGroup.MarginLayoutParams
            val childWidth = child.measuredWidth + lp.leftMargin + lp.rightMargin
            val childHeight = child.measuredHeight + lp.topMargin + lp.bottomMargin

            // 加入line
            if (curLineWidth + childWidth + childMargin > widthSize - iconWidth && !firstShowChild) {// 换行展示:如果第一个要展示的child就大于widthSize,则将其加入第一行---与整体逻辑保持一致
                // to new line
                ++currentLine
                curLineWidth = initWidth
                curLineHeight = 0
            } else {    // 本行展示
                firstShowChild = false
                curLineWidth += childMargin
            }
            // put this child to line
            curLineWidth += childWidth
            curLineHeight = Math.max(curLineHeight, childHeight)    // 更新本行最大高度
            //upd record
            lineLastViewIndexArray.put(currentLine, i)  // 当前行的view截至到i
            if (currentLine == 0) {
                firstLineLastViewIndex = i
            }
            lineMaxHeightArray.put(currentLine, curLineHeight)  // 当前行最大高度
            maxWidth = Math.max(maxWidth, curLineWidth) // 更新最大宽度
        }

        // 高度
        var height = paddingTop + paddingBottom
        val lineCount = if (lineMaxHeightArray.size() > 1 && canExpand && isFirstTime && isFold) 1 else lineMaxHeightArray.size()
        for (i in 0 until lineCount) {
            height += lineMaxHeightArray.valueAt(i)
        }
        height += Math.max(0, lineCount - 1) * lineSpace

        //set dimension
        setMeasuredDimension(if (widthMode == View.MeasureSpec.EXACTLY) widthSize else maxWidth,
            if (heightMode == View.MeasureSpec.EXACTLY) heightSize else height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        //for-lines
        val lines = if (lineLastViewIndexArray.size() > 1 && canExpand && isFirstTime && isFold) 1 else lineLastViewIndexArray.size()
        val childCount = childCount
        var startIndex = 0
        var top = paddingTop    // top
        var lineMaxHeight: Int  // 当前行高度
        for (lineIndex in 0 until lines) {
            lineMaxHeight = lineMaxHeightArray.get(lineIndex)
            var left = paddingLeft  // left
            val lastViewIndex = lineLastViewIndexArray.get(lineIndex)
            var i = startIndex

            while (i <= lastViewIndex && i < childCount) {
                val child = getChildAt(i)
                if (child == null || child.visibility == View.GONE) {
                    i++
                    continue
                }
                val lp = child.layoutParams as MarginLayoutParams

                //计算childView的left,top,right,bottom
                val childLeft = if (i == startIndex) left + lp.leftMargin else left + lp.leftMargin + childMargin
                val childRight = childLeft + child.measuredWidth

                val childTop = when (childGravity) {
                    CENTER_VERTICAL -> top + (lineMaxHeight - child.measuredHeight) / 2
                    BOTTOM -> top + lineMaxHeight - child.measuredHeight - lp.bottomMargin
                    else -> top + lp.topMargin
                }

                val childBottom = childTop + child.measuredHeight
                //layout
                child.layout(childLeft, childTop, childRight, childBottom)

                //更新当前行的left
                left += if (i == startIndex) {
                    child.measuredWidth + lp.rightMargin + lp.leftMargin
                } else {
                    child.measuredWidth + lp.rightMargin + lp.leftMargin + childMargin
                }
                i++
            }
            startIndex = lastViewIndex + 1  // 更新startIndex
            val needSpace = lines > 1 && lineIndex != lines - 1     // 非最后一行需要算入行间距
            top += lineMaxHeight + if (needSpace) lineSpace else 0  // 更新top
        }
    }

    override fun dispatchDraw(canvas: Canvas?) {
        if (isFirstTime && canExpand) {
            for (i in childCount - 1 downTo firstLineLastViewIndex + 1) {
                removedViews.add(getChildAt(i))
                removeViewAt(i)
            }
            isFirstTime = false
        }

        if (removedViews.size > 0 && canvas != null) {
            val drawable = context.resources.getDrawable(if (isFold) R.drawable.expand_more else R.drawable.expand_less)
            val left = width - iconWidth - paddingRight
            val top = 0
            val right = left + iconWidth
            val bottom = top + iconWidth
            drawable.setBounds(left, top, right, bottom)
            drawable.draw(canvas)
        }

        super.dispatchDraw(canvas)
    }

    override fun removeAllViews() {
        isFirstTime = true
        isFold = true
        removedViews.clear()
        super.removeAllViews()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action) {
            MotionEvent.ACTION_DOWN -> {
                val region = Region(width - iconWidth, 0, width, iconWidth)
                if (canExpand && region.contains(event.x.toInt(), event.y.toInt())) {
                    isFold = if (isFold) {
                        for (i in removedViews.size - 1 downTo 0) {
                            addView(removedViews[i])
                        }
                        false
                    } else {
                        removedViews.clear()
                        for (i in childCount - 1 downTo firstLineLastViewIndex + 1) {
                            removedViews.add(getChildAt(i))
                            removeViewAt(i)
                        }
                        true
                    }
                    return true
                }
            }
        }
        return super.onTouchEvent(event)
    }

    override fun generateLayoutParams(attrs: AttributeSet): MarginLayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun generateDefaultLayoutParams(): MarginLayoutParams {

        return MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

    override fun generateLayoutParams(p: LayoutParams): MarginLayoutParams {
        return MarginLayoutParams(p)
    }
}