package team_emergensor.co.jp.emergensor.lib.filter.function

/**
 * Created by koichihasegawa on 2018/05/25.
 */
object Utils {
    fun getNorm(data: Array<Double>): Double {
        var sum = 0.0
        data.forEach {
            sum += it * it
        }
        return Math.sqrt(sum)
    }
}