package team_emergensor.co.jp.emergensor.lib.filter

import io.reactivex.Observable
import team_emergensor.co.jp.emergensor.Entity.Message
import team_emergensor.co.jp.emergensor.lib.filter.base.Filter

class VectorPeriodicSampleFilter(private val period: Long) : Filter.FlatMap<Array<Float>, Array<Float>>() {

    private var lastMessage: Message<Array<Float>>? = null
    private var lastTime: Long = 0

    override fun filter(message: Message<Array<Float>>): Observable<Message<Array<Float>>> {
        return Observable.create<Message<Array<Float>>> {
            lastMessage?.let { lastMessage ->
                while (lastTime + period <= message.timeStamp) {
                    lastTime += period

                    val a = message.timeStamp - lastMessage.timeStamp
                    val b = lastTime - lastMessage.timeStamp
                    val ratio = b.toFloat() / a.toFloat()

                    it.onNext(Message(lastTime, Message.Body(lastMessage.body.size, message.body.periodicSampleFilter(ratio))))

                }
                this.lastMessage = message
                it.onComplete()
            }

            lastMessage = message
            lastTime = message.timeStamp
            it.onNext(message)
            it.onComplete()
        }
    }


    private fun Message.Body<Array<Float>>.periodicSampleFilter(ratio: Float): Array<Float> {
        val lastMessage = lastMessage!!

        return this.data.mapIndexed({ index, fr ->
            lastMessage.body.data[index] * (1 - ratio) + fr * ratio
        }).toTypedArray()
    }
}