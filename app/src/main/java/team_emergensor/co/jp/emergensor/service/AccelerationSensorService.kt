package team_emergensor.co.jp.emergensor.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import android.util.Log


class AccelerationSensorService : Service(), SensorEventListener {

    private val sensorManager by lazy {
        getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }
    private val sensor by lazy {
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        handleStart()
        return START_STICKY
    }

    private fun handleStart() {
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val sensorX = event.values[0]
            val sensorY = event.values[1]
            val sensorZ = event.values[2]

            val strTmp = ("加速度センサー\n"
                    + " X: " + sensorX + "\n"
                    + " Y: " + sensorY + "\n"
                    + " Z: " + sensorZ)
            Log.d("acceleration sensor", strTmp)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    companion object {
        const val ACTION_KEY = "acceleration key"
        const val SERVICE_START = 0
        const val SERVICE_END = 1

        fun createIntent(context: Context): Intent {
            return Intent(context, AccelerationSensorService::class.java)
        }

        fun createStartIntent(context: Context): Intent {
            return Intent(context, AccelerationSensorService::class.java).apply {
                putExtra(ACTION_KEY, SERVICE_START)
            }
        }

        fun createStopIntent(context: Context): Intent {
            return Intent("AccelerationSensor")
        }
    }
}
