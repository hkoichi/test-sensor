package team_emergensor.co.jp.emergensor.service.acceleration

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.util.Log
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import team_emergensor.co.jp.emergensor.Entity.Message
import team_emergensor.co.jp.emergensor.lib.filter.*


class AccelerationSensorEventSubscriber : SensorEventListener {

    private val compositeDisposable = CompositeDisposable()

    /**
     * publish subject
     *
     */
    private val sensorEventSubject = PublishSubject.create<Message<Array<Float>>>()

    /**
     * filters
     */
    private val vectorPeriodicSampleFilter = VectorPeriodicSampleFilter(1 * 1000 * 1000 / 100)
    private val normFunctionFilter = NormFunctionFilter()
    private val bufferFilter = BufferFilter(256)
    private val meanFunctionFilter = MeanFunctionFilter()
    private val varianceFunctionFilter = VarianceFunctionFilter()

    /**
     * init (subscribe data(subject))
     */
    init {
        val disposable = sensorEventSubject
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap {
                    vectorPeriodicSampleFilter.filter(it)
                }.map {
                    normFunctionFilter.filter(it)
                }.flatMap {
                    bufferFilter.filter(it)
                }.map {
                    arrayOf(
                            meanFunctionFilter.filter(it).body.data,
                            varianceFunctionFilter.filter(it).body.data
                    )
                }.subscribe({
                    Log.d("data cominngggg", "平均${it[0]}, 分散${it[1]}")
                })

        compositeDisposable.add(disposable)
    }

    /**
     * event listener (listen sensor and publish data)
     */
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        val sensorEvent = event ?: return
        if (sensorEvent.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val body = Message.Body(3, arrayOf(sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]))
            val message = Message(event.timestamp, body)
            sensorEventSubject.onNext(message) // publish
        }
    }

    fun dispose() {
        if (!compositeDisposable.isDisposed) compositeDisposable.dispose()
    }

}