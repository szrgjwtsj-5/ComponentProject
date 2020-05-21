package com.whx.baselib.network

enum class Env(val url: String) {
    TEST("https://wavetest.yy.com/"),
    RELEASE("https://api.iwaveapp.com");
}