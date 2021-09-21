package com.github.vrvs.githubapp.data.datasource.remote.interceptor

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.MockitoAnnotations.openMocks
import org.mockito.kotlin.any
import org.mockito.kotlin.capture
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
@Config(minSdk = Build.VERSION_CODES.LOLLIPOP_MR1, maxSdk = Build.VERSION_CODES.P)
class CacheInterceptorTest {

    @Mock lateinit var context: Context
    @Mock lateinit var connectivityManager: ConnectivityManager
    @Mock lateinit var networkCapabilities: NetworkCapabilities
    @Mock lateinit var network: Network
    @Mock lateinit var networkInfo: NetworkInfo
    @Mock lateinit var chain: Interceptor.Chain
    @Mock lateinit var response: Response
    private lateinit var cacheInterceptor: CacheInterceptor
    private lateinit var request: Request
    @Captor lateinit var captor: ArgumentCaptor<Request>

    @Before
    fun setUp() {
        openMocks(this)
        whenever(context.getSystemService(any())).thenReturn(null)
        if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.M) {
            whenever(connectivityManager.activeNetwork).thenReturn(network)
            whenever(connectivityManager.getNetworkCapabilities(any())).thenReturn(networkCapabilities)
            whenever(networkCapabilities.hasTransport(any())).thenReturn(true)
        } else {
            whenever(connectivityManager.activeNetworkInfo).thenReturn(null)
            whenever(networkInfo.isConnected).thenReturn(true)
        }
        request = Request.Builder().url("https://api.github.com/").build()
        whenever(chain.request()).thenReturn(request)
        whenever(chain.proceed(any())).thenReturn(response)
    }

    @Test
    fun `git hub interceptor should add not connected header`() {
        make()
        assertEquals(
            expected = response,
            actual = cacheInterceptor.intercept(chain)
        )
        verify(chain).proceed(
            capture(captor)
        )
        val request = captor.value
        assertEquals(
            expected = "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7,
            actual = request.header("Cache-Control"),
        )
    }

    @Test
    fun `git hub interceptor should add connected header`() {
        whenever(context.getSystemService(any())).thenReturn(connectivityManager)
        whenever(connectivityManager.activeNetworkInfo).thenReturn(networkInfo)
        make()
        assertEquals(
            expected = response,
            actual = cacheInterceptor.intercept(chain)
        )
        verify(chain).proceed(
            capture(captor)
        )
        val request = captor.value
        assertEquals(
            expected = "public, max-age=" + 5,
            actual = request.header("Cache-Control"),
        )
    }

    private fun make() {
        cacheInterceptor = CacheInterceptor(context)
    }
}
