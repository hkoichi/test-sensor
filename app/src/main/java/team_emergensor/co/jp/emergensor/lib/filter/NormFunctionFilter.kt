package team_emergensor.co.jp.emergensor.lib.filter

import team_emergensor.co.jp.emergensor.Entity.Message
import team_emergensor.co.jp.emergensor.lib.Utils
import team_emergensor.co.jp.emergensor.lib.filter.base.FunctionFilter

/**
 * Created by koichihasegawa on 2018/05/25.
 */
class NormFunctionFilter : FunctionFilter<Array<Float>, Double>() {
    override fun getResult(body: Message.Body<Array<Float>>): Double {
        return Utils.getNorm(body.data)
    }
}