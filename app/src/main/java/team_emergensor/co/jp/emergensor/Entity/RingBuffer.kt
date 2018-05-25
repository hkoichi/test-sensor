package team_emergensor.co.jp.emergensor.Entity

import java.util.*

class RingBuffer<T>(val size: Int) {

    val data = PriorityQueue<T>()

    var latestIndex: Int = 0
    var isFilled = false

    fun append(body: T): List<T> {
        if (isFilled) {
            data.poll()
            data.offer(body)
        } else {
            data.offer(body)
            latestIndex++
            if (latestIndex + 1 >= size) isFilled = true
        }
        return data.toMutableList()
    }

    fun obtainResult(body: T) = data.toMutableList()

}