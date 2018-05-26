package team_emergensor.co.jp.emergensor.lib.filter

import team_emergensor.co.jp.emergensor.Entity.Message
import team_emergensor.co.jp.emergensor.lib.filter.function.Utils
import team_emergensor.co.jp.emergensor.lib.filter.base.FunctionFilter

/**
 * Created by koichihasegawa on 2018/05/25.
 */
class NormFunctionFilter : FunctionFilter<Array<Double>, Double>() {
    override fun getResult(body: Message.Body<Array<Double>>): Double {
        return Utils.getNorm(body.data)
    }
}