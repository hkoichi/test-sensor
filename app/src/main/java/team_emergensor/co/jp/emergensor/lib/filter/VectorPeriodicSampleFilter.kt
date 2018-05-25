package team_emergensor.co.jp.emergensor.lib.filter

import team_emergensor.co.jp.emergensor.Entity.Message
import team_emergensor.co.jp.emergensor.Entity.SensorValue

class VectorPeriodicSampleFilter(private val period: Long)
    : One2ManyFilter() {

    private var lastMessage: Message? = null
    private var lastTime: Long = 0

    override fun filtering(message: Message) {

        lastMessage?.let { lastMessage ->
            while (lastTime + period <= message.timeStamp) {
                lastTime += period

                val a = message.timeStamp - lastMessage.timeStamp
                val b = lastTime - lastMessage.timeStamp
                val ratio = b.toFloat() / a.toFloat()

                publish(Message(lastTime, SensorValue.AccelerationSensorValue(message.value.periodicSampleFilter(ratio))))
            }
            log("vectorsamplefilter", message.value.data.map { it.toString() }.toString())
            this.lastMessage = message
            return
        }

        lastMessage = message
        lastTime = message.timeStamp
        publish(message)
    }

    private fun SensorValue.periodicSampleFilter(ratio: Float): Array<Float> {
        val lastMessage = lastMessage!!

        return this.data.mapIndexed({ index, fr ->
            lastMessage.value.data[index] * (1 - ratio) + fr * ratio
        }).toTypedArray()
    }
}