package team_emergensor.co.jp.emergensor.lib.filter

import io.reactivex.Observable
import team_emergensor.co.jp.emergensor.Entity.Message
import team_emergensor.co.jp.emergensor.Entity.SensorValue

class VectorPeriodicSampleFilter(private val period: Long)
    : Filter.OneToMany<Message, Message>() {

    private var lastMessage: Message? = null
    private var lastTime: Long = 0

    override fun filter(message: Message) = Observable.create<Message> {
        lastMessage?.let { lastMessage ->
            while (lastTime + period <= message.timeStamp) {
                lastTime += period

                val a = message.timeStamp - lastMessage.timeStamp
                val b = lastTime - lastMessage.timeStamp
                val ratio = b.toFloat() / a.toFloat()

                it.onNext(Message(lastTime, SensorValue.AccelerationSensorValue(message.value.periodicSampleFilter(ratio))))

            }
            this.lastMessage = message
            it.onComplete()
        }

        lastMessage = message
        lastTime = message.timeStamp
        it.onNext(message)
        it.onComplete()
    }


    private fun SensorValue.periodicSampleFilter(ratio: Float): Array<Float> {
        val lastMessage = lastMessage!!

        return this.data.mapIndexed({ index, fr ->
            lastMessage.value.data[index] * (1 - ratio) + fr * ratio
        }).toTypedArray()
    }
}