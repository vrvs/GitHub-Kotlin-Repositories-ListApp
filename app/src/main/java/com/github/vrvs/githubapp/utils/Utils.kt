package com.github.vrvs.githubapp.utils

object Utils {

    fun processUrl(url: String): String {
        if ("https" in url)
            return url
        return url.replace("http", "https")
    }

    inline fun <T1: Any, T2: Any, T3: Any, R: Any> safeLet(p1: T1?, p2: T2?, p3: T3?, block: (T1, T2, T3)->R?): R? {
        return if (p1 != null && p2 != null && p3 != null) block(p1, p2, p3) else null
    }
}