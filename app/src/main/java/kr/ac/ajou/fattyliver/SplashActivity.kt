package kr.ac.ajou.fattyliver

import android.animation.ValueAnimator
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : AppCompatActivity() {

    companion object {
        const val SPLASH_DISPLAY_LENGTH = 2500
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val animator: ValueAnimator = ValueAnimator.ofFloat(0f, 1f)
        animator.addUpdateListener { p0 -> imageView_splash_liver.alpha= p0?.animatedValue as Float }

        animator.duration = 2500
        animator.repeatMode = ValueAnimator.REVERSE
        animator.repeatCount = -1
        animator.start()

        val animation1 : Animation = AnimationUtils.loadAnimation(this, R.anim.animation_title)
        animation1.startOffset = 300
        animation1.duration = 150

        val animation2 : Animation = AnimationUtils.loadAnimation(this, R.anim.animation_title)
        animation2.startOffset = 500
        animation2.duration = 150

        val animation3 : Animation = AnimationUtils.loadAnimation(this, R.anim.animation_title)
        animation3.startOffset = 700
        animation3.duration = 150

        val animation4 : Animation = AnimationUtils.loadAnimation(this, R.anim.animation_title)
        animation4.startOffset = 900
        animation4.duration = 150

        val animation5 : Animation = AnimationUtils.loadAnimation(this, R.anim.animation_title)
        animation5.startOffset = 1100
        animation5.duration = 150

        val animation6 : Animation = AnimationUtils.loadAnimation(this, R.anim.animation_title)
        animation6.startOffset = 1300
        animation6.duration = 150

        imageView_splash_title1?.startAnimation(animation1)
        imageView_splash_title2?.startAnimation(animation2)
        imageView_splash_title3?.startAnimation(animation3)
        imageView_splash_title4?.startAnimation(animation4)
        imageView_splash_title5?.startAnimation(animation5)
        imageView_splash_title6?.startAnimation(animation6)


        Handler().postDelayed({
            val mainIntent = Intent(this@SplashActivity, LoginActivity::class.java)
            this@SplashActivity.startActivity(mainIntent)
            this@SplashActivity.finish()
        }, SPLASH_DISPLAY_LENGTH.toLong())

    }
}
