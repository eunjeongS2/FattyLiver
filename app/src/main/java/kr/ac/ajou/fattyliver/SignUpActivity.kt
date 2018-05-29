package kr.ac.ajou.fattyliver

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import kotlinx.android.synthetic.main.activity_signup.*
import kr.ac.ajou.fattyliver.model.UserModel

class SignUpActivity : AppCompatActivity(), UserModel.OnSignUpListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val userModel = UserModel.instance
        userModel.onSignUpListener = this

        progressBar_signUp.visibility = View.INVISIBLE

        button_signUp.setOnClickListener {
            val name = editText_signUp_name.text.toString()
            val email = editText_signUp_id.text.toString()
            val password = editText_signUp_password.text.toString()

            progressBar_signUp.visibility = View.VISIBLE
            textView_signUp_error.visibility = View.INVISIBLE

            if (TextUtils.isEmpty(name)) {
                textView_signUp_error.text = "닉네임을 입력해주세요 !"
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(email)) {
                textView_signUp_error.text = "아이디를 입력해주세요 !"
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(password)) {
                textView_signUp_error.text = "비밀번호를 입력해주세요 !"
                return@setOnClickListener
            }

            if (password.length < 6) {
                textView_signUp_error.text = "비밀번호는 6자리 이상으로 설정해주세요 ! !"
                return@setOnClickListener
            }

            progressBar_signUp.visibility = View.VISIBLE
            userModel.signUp(name, email, password)
        }
    }

    override fun onSuccess() {
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onFail() {
        progressBar_signUp.visibility = View.INVISIBLE

    }
}
