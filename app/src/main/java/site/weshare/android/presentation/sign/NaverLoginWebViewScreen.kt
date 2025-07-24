package site.weshare.android.presentation.sign.login

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.webkit.*
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun NaverLoginWebViewScreen(
    context: Context,
    onLoginSuccess: () -> Unit
) {
    AndroidView(factory = { webContext ->

        // ✅ 1. WebView 세션 초기화
        CookieManager.getInstance().apply {
            removeAllCookies(null)
            flush()
        }
        WebStorage.getInstance().deleteAllData()

        // ✅ 2. WebView 생성
        val webView = WebView(webContext).apply {
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
            }

            val cookieManager = CookieManager.getInstance()
            cookieManager.setAcceptCookie(true)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cookieManager.setAcceptThirdPartyCookies(this, true)
            }

            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    if (url != null && url.startsWith("http://10.0.2.2:8080/login/oauth2/code/naver")) {
                        onLoginSuccess()
                    }
                }

                @Suppress("DEPRECATION")
                override fun onReceivedError(
                    view: WebView?, errorCode: Int, description: String?, failingUrl: String?
                ) {
                    Toast.makeText(context, "로그인 오류: $description", Toast.LENGTH_SHORT).show()
                }

                override fun onReceivedError(
                    view: WebView, request: WebResourceRequest, error: WebResourceError
                ) {
                    Toast.makeText(context, "로그인 오류: ${error.description}", Toast.LENGTH_SHORT).show()
                }
            }

            loadUrl("http://10.0.2.2:8080/oauth2/authorization/naver")
        }

        webView
    })
}