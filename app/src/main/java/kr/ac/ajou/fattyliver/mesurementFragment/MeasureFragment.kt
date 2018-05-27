package kr.ac.ajou.fattyliver.mesurementFragment


import android.content.Context
import android.graphics.Color
import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.hoho.android.usbserial.driver.UsbSerialDriver
import com.hoho.android.usbserial.driver.UsbSerialPort
import com.hoho.android.usbserial.driver.UsbSerialProber
import kr.ac.ajou.fattyliver.R
import kr.ac.ajou.fattyliver.model.AlcoholModel
import java.io.IOException
import java.util.*

class MeasureFragment : Fragment() {
    private var timerTask: TimerTask? = null
    private var timerWriteTask: TimerTask? = null

    private var connection : UsbDeviceConnection? = null
    private var port : UsbSerialPort? = null
    private lateinit var measureExplainText : TextView
    private lateinit var measureText : TextView
    private lateinit var startButton: Button

    private var numBytesRead: Int = 0

    private var timer : Timer? = null
    private var newTimer : Timer? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_measure, container, false)

        measureExplainText = view.findViewById(R.id.textView_measure_explain)
        measureText = view.findViewById(R.id.textView_measure)
        startButton = view.findViewById(R.id.button_measure_start)

        initArduino()

        startButton.setOnClickListener {

            port?.let {
                setTimerTask()
            }
        }

        return view
    }

    private fun setTimerTask(){
        if(timerTask != null){
            timerTask?.cancel()
            timerTask = null
        }

        if(timerWriteTask != null){
            timerWriteTask?.cancel()
            timerWriteTask = null
        }

        if(timer != null){
            timer?.cancel()
            timer = null
        }

        if(newTimer != null){
            newTimer?.cancel()
            newTimer = null
        }

        timerTask = object : TimerTask() {

            override fun run() {
                commandWrite("Start")

                activity?.runOnUiThread {
                    readData()
                    startButton.isEnabled = false
                }
            }
        }

        timerWriteTask = object : TimerTask(){
            override fun run() {
                try{
                    port?.purgeHwBuffers(true, true)
                }catch (e: IOException){
                    println(e)
                }
                timer?.cancel()
                timer = null
                timerTask?.cancel()
                timerTask = null
                commandWrite("*")
                activity?.runOnUiThread {
                    startButton.isEnabled = true
                    AlcoholModel().addAlcohol("", measureText.text.toString().toDouble())
                }
            }
        }

        timer = Timer()
        timer?.schedule(timerTask, 0, 300)

        newTimer = Timer()
        newTimer?.schedule(timerWriteTask, 3500)
    }


    private fun initArduino(){
        try {
            val manager : UsbManager = activity?.getSystemService(Context.USB_SERVICE) as UsbManager
            val availableDrivers : List<UsbSerialDriver> = UsbSerialProber.getDefaultProber().findAllDrivers(manager)
            if (availableDrivers.isEmpty()){
                return
            }

            val driver: UsbSerialDriver = availableDrivers[0]
            connection = manager.openDevice(driver.device) ?: return

            port = driver.ports[0]
            port?.open(connection)
            port?.setParameters(9600, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE)
            port?.purgeHwBuffers(true, true)

            if(port == null){
                measureExplainText.text =  "드라이버가 연결되지 않았습니다"
            }else {
                measureExplainText.text = "드라이버가 연결되었습니다. 힘껏 불어주세요 ~!"
                measureExplainText.setTextColor(Color.BLACK)
            }
        } catch (e: IOException){
            println(e)
        }

    }

    private fun readData(){
        try {
            val buffer = ByteArray(120)
            port?.read(buffer, 50)?.let { numBytesRead = it }
            val str = String(buffer)

            if(str.contains("Start")){
                val start = str.indexOf("Start")
                val subString = str.substring(start+5, start+9)

                val saveString = subString.substring(0,1)+"."+subString.substring(1,4)

                measureText.text = saveString

            }
        }catch (e: IOException){
            println(e)
        }
    }

    private fun commandWrite(command: String){
        try{
            val comm = command.toByteArray()
            port?.write(comm, 100)
        }catch (e: IOException){
            println(e)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if(connection != null){
            try {
                port?.close()
            }catch (e: IOException){
                println(e)
            }
        }
    }
}
