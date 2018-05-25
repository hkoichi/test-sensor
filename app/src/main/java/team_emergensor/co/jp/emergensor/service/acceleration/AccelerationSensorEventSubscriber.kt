package team_emergensor.co.jp.emergensor.service.acceleration

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import team_emergensor.co.jp.emergensor.Entity.Message
import team_emergensor.co.jp.emergensor.Entity.SensorValue
import team_emergensor.co.jp.emergensor.lib.filter.VectorPeriodicSampleFilter


class AccelerationSensorEventSubscriber : SensorEventListener {

    /**
     * publish subject
     *
     * apply Vector filter
     */
    // sensor dataを受け取るpublish subject. periodic filterにpublishする
    private val sensorEventSubject = PublishSubject.create<Message>()

    /**
     * filters
     */
    private val vectorPereiodicSampleFilter = VectorPeriodicSampleFilter(1 * 1000 * 1000 / 100)

    /**
     * init
     */
    init {
        // observableをsetしてスタート
        vectorPereiodicSampleFilter.setObservableSource(sensorEventSubject)
                .flatMap {
                    Observable.fromCallable { "きた" }
                }
    }

    /**
     * event listener
     */
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        val sensorEvent = event ?: return
        if (sensorEvent.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val value = SensorValue.AccelerationSensorValue(sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2])
            val message = Message(event.timestamp, value)
            sensorEventSubject.onNext(message)
        }
    }

}