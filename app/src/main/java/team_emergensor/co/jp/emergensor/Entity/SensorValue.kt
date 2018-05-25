package team_emergensor.co.jp.emergensor.Entity

/**
 * Created by koichihasegawa on 2018/05/24.
 */
sealed class SensorValue(val data: Array<Float>) {
    fun size(): Int = data.size

    class AccelerationSensorValue(array: Array<Float>) : SensorValue(array) {
        constructor(x: Float, y: Float, z: Float) : this(arrayOf(x, y, z))
    }
}