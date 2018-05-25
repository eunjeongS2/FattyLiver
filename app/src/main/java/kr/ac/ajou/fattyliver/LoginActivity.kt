package kr.ac.ajou.fattyliver

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_login.*
import kr.ac.ajou.fattyliver.model.UserModel

class LoginActivity : AppCompatActivity(), UserModel.OnLoginListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        progressBar_login.visibility = View.INVISIBLE

        val animation : Animation = AnimationUtils.loadAnimation(this, R.anim.animation_scale)
        imageView_login_liver?.startAnimation(animation)

        val userModel = UserModel.instance
        userModel.onLoginListener = this

        button_login.setOnClickListener {
            progressBar_login.visibility = View.VISIBLE
            textView_error.visibility = View.INVISIBLE

            val email = editText_login_id.text.toString()
            val password = editText_login_password.text.toString()

            userModel.login(email, password)

        }

        signup_textView.setOnClickListener {
            val intent = Intent(applicationContext, SignUpActivity::class.java)
            startActivity(intent)
        }

    }
    override fun onSuccess() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onFail() {
        progressBar_login.visibility = View.INVISIBLE
        textView_error.visibility = View.VISIBLE
        textView_error.text = "존재하지 않는 아이디나 비빌번호입니다 !"
    }
}