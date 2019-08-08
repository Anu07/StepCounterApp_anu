package com.sd.src.stepcounterapp.model.wallet

import com.sd.src.stepcounterapp.model.BaseModel

class TokenModel : BaseModel() {
    /**
     * data : {"totalSteps":111,"totalEarntokens":0}
     */

    var data: DataBean? = null
    class DataBean {
        /**
         * totalSteps : 111
         * totalEarntokens : 0
         */
        var totalSteps: Int = 0
        var totalEarntokens: Int = 0
    }
}
