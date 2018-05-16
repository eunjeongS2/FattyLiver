package kr.ac.ajou.fattyliver


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import kr.ac.ajou.fattyliver.R
import org.json.JSONObject
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL


class Tab1Fragment : Fragment() {

    private val TAG = "Test"


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_tab1, container, false)



        val button: Button = view.findViewById(R.id.button)
        button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {



            }

        })

        return view
    }




}
