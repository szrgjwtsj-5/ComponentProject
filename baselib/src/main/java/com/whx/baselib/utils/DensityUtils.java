package com.whx.baselib.utils;


import com.whx.baselib.RuntimeInfo;

public class DensityUtils {

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    public static int sp2px(float spValue) {
        final float fontScale = RuntimeInfo.INSTANCE.getSApplicationContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @return
     */
    public static int px2sp(float pxValue) {
        final float fontScale = RuntimeInfo.INSTANCE.getSApplicationContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }


    public static int dip2px(float dpValue) {
        return dp2px(dpValue);
    }

    public static int dp2px(float dpValue) {
        final float scale = RuntimeInfo.INSTANCE.getSApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static float dp2PxF(float dp) {
        final float scale = RuntimeInfo.INSTANCE.getSApplicationContext().getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public static int px2dp(float pxValue) {
        final float scale = RuntimeInfo.INSTANCE.getSApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getHeightPx() {
        return RuntimeInfo.INSTANCE.getSApplicationContext().getResources().getDisplayMetrics().heightPixels;
    }


    public static int getWidthPx() {
        return RuntimeInfo.INSTANCE.getSApplicationContext().getResources().getDisplayMetrics().widthPixels;
    }

}