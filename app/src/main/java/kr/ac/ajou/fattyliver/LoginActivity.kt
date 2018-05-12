package kr.ac.ajou.fattyliver

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import kr.ac.ajou.fattyliver.R.id.login_button

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        login_button.setOnClickListener {
            Toast.makeText(this, "로그인!", Toast.LENGTH_LONG).show()
        }

    }


}