package kr.ac.ajou.fattyliver

import android.animation.ValueAnimator
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_splash.*
import android.R.attr.animation



class SplashActivity : AppCompatActivity() {

    private val SPLASH_DISPLAY_LENGTH = 2500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

//        val animation : Animation = AnimationUtils.loadAnimation(this, R.anim.animation_scale)
//        imageView_splash_liver?.startAnimation(animation)

        val animator: ValueAnimator = ValueAnimator.ofFloat(0f, 1f)
        animator.addUpdateListener { p0 -> imageView_splash_liver.alpha= p0?.animatedValue as Float }

        animator.duration = 2500
        animator.repeatMode = ValueAnimator.REVERSE
        animator.repeatCount = -1
        animator.start()

        Handler().postDelayed({
            val mainIntent = Intent(this@SplashActivity, LoginActivity::class.java)
            this@SplashActivity.startActivity(mainIntent)
            this@SplashActivity.finish()
        }, SPLASH_DISPLAY_LENGTH.toLong())

    }
}
