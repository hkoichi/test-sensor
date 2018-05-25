package team_emergensor.co.jp.emergensor.lib

/**
 * Created by koichihasegawa on 2018/05/25.
 */
object Utils {
    fun getNorm(data: Array<Float>): Double {
        var sum = 0.0
        data.forEach {
            sum += it * it
        }
        return Math.sqrt(sum)
    }
}