package com.sd.src.stepcounterapp.model.wallet

class TokenModel {
    /**
     * status : 200
     * message : Success
     * data : {"totalSteps":111,"totalEarntokens":0}
     */

    var status: Int = 0
    var message: String? = null
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
