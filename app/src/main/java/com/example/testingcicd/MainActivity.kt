package com.example.testingcicd

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.performance
import com.google.firebase.perf.trace
import java.io.DataOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //manualNetworkTrace()
    }

    @Throws(Exception::class)
    fun manualNetworkTrace() {
        val data = "badgerbadgerbadgerbadgerMUSHROOM!".toByteArray()

        // [START perf_manual_network_trace]
        val url = URL("https://www.google.com")
        val metric = Firebase.performance.newHttpMetric(
            "https://www.google.com",
            FirebasePerformance.HttpMethod.GET,
        )
        metric.trace {
            val conn = url.openConnection() as HttpURLConnection
            conn.doOutput = true
            conn.setRequestProperty("Content-Type", "application/json")
            try {
                val outputStream = DataOutputStream(conn.outputStream)
                outputStream.write(data)
            } catch (ignored: IOException) {
            }

            // Set HttpMetric attributes
            setRequestPayloadSize(data.size.toLong())
            setHttpResponseCode(conn.responseCode)

            println(conn.inputStream)

            conn.disconnect()
        }
        // [END perf_manual_network_trace]
    }
}