package com.sd.src.stepcounterapp.model.survey

import java.util.ArrayList

class SurveyModel {

    /**
     * status : 200
     * message : Success
     * data : [{"_id":"5d35904708cc29292ea6936b","name":"travel preferences","expireOn":"2020-11-21T04:32:18.758Z","earningToken":10,"products":[{"_id":"5d35a8c4ea9c4b2fc26aa2d1","quesType":"checkbox","question":"test123456789","option1":"opt1","option2":"opt2","option3":"opt3","option4":"opt4","answer":["opt1","opt3"]},{"_id":"5d35a8c4ea9c4b2fc26aa2d2","quesType":"radio","question":"test123","option1":"opt1","option2":"opt2","option3":"opt3","option4":"opt4","answer":["opt1"]}]},{"_id":"5d35905708cc29292ea6936c","name":"app feedback","expireOn":"2020-11-21T04:32:18.758Z","earningToken":10,"products":[{"_id":"5d35a8c1ea9c4b2fc26aa2d0","quesType":"checkbox","question":"test1234567890","option1":"opt1","option2":"opt2","option3":"opt3","option4":"opt4","answer":["opt1","opt3"]},{"_id":"5d35a8c8ea9c4b2fc26aa2d2","quesType":"checkbox","question":"test12345678","option1":"opt1","option2":"opt2","option3":"opt3","option4":"opt4","answer":["opt1","opt3"]}]}]
     */

    var status: Int = 0
    var message: String? = null
    var data: ArrayList<DataBean>? = null

    class DataBean {
        /**
         * _id : 5d35904708cc29292ea6936b
         * name : travel preferences
         * expireOn : 2020-11-21T04:32:18.758Z
         * earningToken : 10
         * products : [{"_id":"5d35a8c4ea9c4b2fc26aa2d1","quesType":"checkbox","question":"test123456789","option1":"opt1","option2":"opt2","option3":"opt3","option4":"opt4","answer":["opt1","opt3"]},{"_id":"5d35a8c4ea9c4b2fc26aa2d2","quesType":"radio","question":"test123","option1":"opt1","option2":"opt2","option3":"opt3","option4":"opt4","answer":["opt1"]}]
         */

        var _id: String? = null
        var name: String? = null
        var expireOn: String? = null
        var earningToken: Int = 0
        var products: List<ProductsBean>? = null

        class ProductsBean {
            /**
             * _id : 5d35a8c4ea9c4b2fc26aa2d1
             * quesType : checkbox
             * question : test123456789
             * option1 : opt1
             * option2 : opt2
             * option3 : opt3
             * option4 : opt4
             * answer : ["opt1","opt3"]
             */

            var _id: String? = null
            var quesType: String? = null
            var question: String? = null
            var option1: String? = null
            var option2: String? = null
            var option3: String? = null
            var option4: String? = null
            var answer: List<String>? = null
        }
    }
}
