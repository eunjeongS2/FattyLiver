package kr.ac.ajou.fattyliver

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

class MyFirebaseInstanceIDService: FirebaseInstanceIdService() {

    private val TAG = "IDService"

    // 토큰 재생성
    override fun onTokenRefresh() {
        // 설치할때 여기서 토큰을 자동으로 생성
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.d(TAG, "Refreshed token: "+refreshedToken)

        sendRegistrationToServer(refreshedToken)

    }

    // 갱신된 토큰 서버에 보냄
    private fun sendRegistrationToServer(token: String?) {

    }
}