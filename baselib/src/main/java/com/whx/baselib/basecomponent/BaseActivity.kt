package com.whx.baselib.basecomponent

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gyf.immersionbar.ImmersionBar

/**
 * 所有Activity 都应该继承
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setImmersiveSticky()
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onPause() {
        super.onPause()
    }

    // 设置沉浸式
    fun setImmersiveSticky(isDarkFont: Boolean = false) {
        ImmersionBar.with(this).statusBarDarkFont(isDarkFont).init()
    }

    /**
     * 是否上报Activity 的onResume 和onPause，默认上报
     */
    protected fun shouldReportAction(): Boolean {
        return true
    }
}