package com.sd.src.stepcounterapp.model.wallet

import com.sd.src.stepcounterapp.model.BaseModel
import java.util.*

class WalletModel : BaseModel() {

    /**
     * status : 200
     * message : Success
     * data : {"totalEarnings":3256,"updatedAt":"2019-08-07T11:31:00.000Z","steps":25600,"days":3,"totalGenerated":25,"wishlist":[{"_id":"5d303584a32be4235d74dd3c","vendorId":"5d2af632a7918160365a8b9f","name":"the himalayan","shortDesc":"50% Off Wellness Retreat","description":"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.","token":3000,"rewardId":"5d244a20dc18c5d737a6a7aa","image":"/public/uploads/product/104665-1563440516333.jpg"},{"_id":"5d3035c9a32be4235d74dd3d","vendorId":"5d2af632a7918160365a8b9f","name":"cafe organic","shortDesc":"Free Americano","description":"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.","token":30,"rewardId":"5d244a16dc18c5d737a6a72c","image":"/public/uploads/product/106300-1563440585186.jpg"},{"_id":"5d30365da32be4235d74dd40","vendorId":"5d2af632a7918160365a8b9f","name":"the woodcraft","shortDesc":"Free Dine for Two","description":"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.","token":1200,"rewardId":"5d244a16dc18c5d737a6a72c","image":"/public/uploads/product/107167-1563440733437.jpg"},{"_id":"5d303686a32be4235d74dd41","vendorId":"5d2af632a7918160365a8b9f","name":"body center","shortDesc":"Free Week Pass","description":"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.","token":1300,"rewardId":"5d303acc2c35df2cbef205a5","image":"/public/uploads/product/107727-1563440774621.jpg"},{"_id":"5d3036b8a32be4235d74dd42","vendorId":"5d2af632a7918160365a8b9f","name":"garden of life","shortDesc":"Buy one Get one Free","description":"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.","token":20,"rewardId":"5d2449eddc18c5d737a6a4ec","image":"/public/uploads/product/102834-1563440824698.jpg"}],"redeemed":[]}
     */
    var data: DataBean? = null

    class DataBean {
        /**
         * totalEarnings : 3256
         * updatedAt : 2019-08-07T11:31:00.000Z
         * steps : 25600
         * days : 3
         * totalGenerated : 25
         * wishlist : [{"_id":"5d303584a32be4235d74dd3c","vendorId":"5d2af632a7918160365a8b9f","name":"the himalayan","shortDesc":"50% Off Wellness Retreat","description":"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.","token":3000,"rewardId":"5d244a20dc18c5d737a6a7aa","image":"/public/uploads/product/104665-1563440516333.jpg"},{"_id":"5d3035c9a32be4235d74dd3d","vendorId":"5d2af632a7918160365a8b9f","name":"cafe organic","shortDesc":"Free Americano","description":"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.","token":30,"rewardId":"5d244a16dc18c5d737a6a72c","image":"/public/uploads/product/106300-1563440585186.jpg"},{"_id":"5d30365da32be4235d74dd40","vendorId":"5d2af632a7918160365a8b9f","name":"the woodcraft","shortDesc":"Free Dine for Two","description":"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.","token":1200,"rewardId":"5d244a16dc18c5d737a6a72c","image":"/public/uploads/product/107167-1563440733437.jpg"},{"_id":"5d303686a32be4235d74dd41","vendorId":"5d2af632a7918160365a8b9f","name":"body center","shortDesc":"Free Week Pass","description":"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.","token":1300,"rewardId":"5d303acc2c35df2cbef205a5","image":"/public/uploads/product/107727-1563440774621.jpg"},{"_id":"5d3036b8a32be4235d74dd42","vendorId":"5d2af632a7918160365a8b9f","name":"garden of life","shortDesc":"Buy one Get one Free","description":"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.","token":20,"rewardId":"5d2449eddc18c5d737a6a4ec","image":"/public/uploads/product/102834-1563440824698.jpg"}]
         * redeemed : []
         */

        var totalEarnings: Int = 0
        var updatedAt: String? = null
        var steps: Int = 0
        var days: Int = 0
        var totalGenerated: Int = 0
        var wishlist: ArrayList<WishlistBean>? = null
        var redeemed: ArrayList<RedeemlistBean>? = null

        class WishlistBean {
            /**
             * _id : 5d303584a32be4235d74dd3c
             * vendorId : 5d2af632a7918160365a8b9f
             * name : the himalayan
             * shortDesc : 50% Off Wellness Retreat
             * description : Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
             * token : 3000
             * rewardId : 5d244a20dc18c5d737a6a7aa
             * image : /public/uploads/product/104665-1563440516333.jpg
             */

            var _id: String? = null
            var vendorId: String? = null
            var name: String? = null
            var shortDesc: String? = null
            var description: String? = null
            var token: Int = 0
            var rewardId: String? = null
            var image: String? = null
        }

        class RedeemlistBean {
            /**
             * _id : 5d303584a32be4235d74dd3c
             * vendorId : 5d2af632a7918160365a8b9f
             * name : the himalayan
             * shortDesc : 50% Off Wellness Retreat
             * description : Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
             * token : 3000
             * rewardId : 5d244a20dc18c5d737a6a7aa
             * image : /public/uploads/product/104665-1563440516333.jpg
             */

            var _id: String? = null
            var vendorId: String? = null
            var name: String? = null
            var shortDesc: String? = null
            var description: String? = null
            var token: Int = 0
            var rewardId: String? = null
            var image: String? = null
        }
    }
}
