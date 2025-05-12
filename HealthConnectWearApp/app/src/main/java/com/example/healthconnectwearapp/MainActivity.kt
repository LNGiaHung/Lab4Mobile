package com.example.healthconnectwearapp

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var statusText: TextView
    private val TAG = "HealthConnectWearApp"
    private lateinit var generateFakeDataButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: Starting")
        try {
            Log.d(TAG, "onCreate: Setting content view")
            setContentView(R.layout.activity_main)
            Log.d(TAG, "onCreate: Content view set")

            Log.d(TAG, "onCreate: Finding status_text")
            statusText = findViewById(R.id.status_text)
            Log.d(TAG, "onCreate: status_text found")

            Log.d(TAG, "onCreate: Finding generate_fake_data_button")
            generateFakeDataButton = findViewById(R.id.generate_fake_data_button)
            Log.d(TAG, "onCreate: generate_fake_data_button found")

            statusText.text = "Press button to generate and send fake health data"

            Log.d(TAG, "onCreate: Setting click listeners")
            generateFakeDataButton.setOnClickListener {
                Log.d(TAG, "Generate fake data button clicked")
                generateAndSendFakeData()
            }
            Log.d(TAG, "onCreate: Click listeners set")
        } catch (e: Exception) {
            Log.e(TAG, "onCreate error: ${e.message}", e)
            try {
                setContentView(R.layout.activity_main)
                statusText = findViewById(R.id.status_text)
                statusText.text = "Initialization error: ${e.message}"
            } catch (fallbackException: Exception) {
                Log.e(TAG, "Fallback error: ${fallbackException.message}", fallbackException)
            }
        }
    }

    private fun generateAndSendFakeData() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                Log.d(TAG, "Generating fake health data")

                // Generate random values within realistic ranges
                val fakeSteps = Random.nextLong(3000, 15000)
                val fakeCalories = Random.nextDouble(100.0, 800.0)

                statusText.text = "Generated Fake Data:\nSteps: $fakeSteps\nCalories: ${String.format("%.2f", fakeCalories)} kcal"
                Log.d(TAG, "Fake health data generated: Steps=$fakeSteps, Calories=$fakeCalories")

                sendHealthDataToWear(fakeSteps, fakeCalories)
            } catch (e: Exception) {
                statusText.text = "Error generating fake data: ${e.message}"
                Log.e(TAG, "Error generating fake data: ${e.message}", e)
            }
        }
    }

    private fun sendHealthDataToWear(steps: Long, calories: Double) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d(TAG, "Sending health data to Wear: Steps=$steps, Calories=$calories")
                val dataClient = Wearable.getDataClient(this@MainActivity)
                val putDataMapReq = com.google.android.gms.wearable.PutDataMapRequest.create("/health_data")
                putDataMapReq.dataMap.putLong("steps", steps)
                putDataMapReq.dataMap.putDouble("calories", calories)
                putDataMapReq.dataMap.putLong("timestamp", System.currentTimeMillis())
                putDataMapReq.dataMap.putBoolean("is_fake", true)
                val putDataReq = putDataMapReq.asPutDataRequest()
                putDataReq.setUrgent()
                dataClient.putDataItem(putDataReq).await()

                runOnUiThread {
                    statusText.text = statusText.text.toString() + "\n\nData sent to Wear successfully!"
                }
                Log.d(TAG, "Health data sent to Wear successfully")
            } catch (e: Exception) {
                runOnUiThread {
                    statusText.text = "Error sending data to Wear: ${e.message}"
                }
                Log.e(TAG, "Error sending data to Wear: ${e.message}", e)
            }
        }
    }
}