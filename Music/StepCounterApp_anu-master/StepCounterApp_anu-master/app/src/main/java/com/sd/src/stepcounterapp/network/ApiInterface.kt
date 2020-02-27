package com.sd.src.stepcounterapp.network

import com.google.gson.JsonElement
import com.sd.src.stepcounterapp.model.BasicInfoRequestObject
import com.sd.src.stepcounterapp.model.Deviceresponse.DashboardResponse
import com.sd.src.stepcounterapp.model.bmi.BMIinfoResponse
import com.sd.src.stepcounterapp.model.challenge.ChallengeResponse
import com.sd.src.stepcounterapp.model.challenge.ChallengeStartRequestModel
import com.sd.src.stepcounterapp.model.challenge.Challengetakenresponse.StartChallengeResponse
import com.sd.src.stepcounterapp.model.challenge.MyChallengeResponse
import com.sd.src.stepcounterapp.model.challenge.WeekendChallengeStartRequestModel
import com.sd.src.stepcounterapp.model.challenge.departmentchallengeresponse.DepartmentLeaderboardResponse
import com.sd.src.stepcounterapp.model.changepwd.ChangePasswordRequest
import com.sd.src.stepcounterapp.model.contactUs.ContactUsRequest
import com.sd.src.stepcounterapp.model.generic.BasicInfoResponse
import com.sd.src.stepcounterapp.model.generic.BasicRequest
import com.sd.src.stepcounterapp.model.generic.BasicUserRequest
import com.sd.src.stepcounterapp.model.image.ImageResponse
import com.sd.src.stepcounterapp.model.leaderboard.LeaderBoardRequest
import com.sd.src.stepcounterapp.model.leaderboard.LeaderBoardResponse
import com.sd.src.stepcounterapp.model.leaderboard.LeaderBoardTknRequest
import com.sd.src.stepcounterapp.model.login.LoginResponseJ
import com.sd.src.stepcounterapp.model.loginrequest.ForgetRequestObject
import com.sd.src.stepcounterapp.model.loginrequest.LoginRequestObject
import com.sd.src.stepcounterapp.model.marketplace.BasicSearchRequest
import com.sd.src.stepcounterapp.model.marketplace.MarketResponse
import com.sd.src.stepcounterapp.model.marketplace.PopularProducts
import com.sd.src.stepcounterapp.model.marketplace.walletInfo.WalletModelResponse
import com.sd.src.stepcounterapp.model.notification.BasicNotificationSettingsRequest
import com.sd.src.stepcounterapp.model.notification.NotificationResponse
import com.sd.src.stepcounterapp.model.notification.readnotification.NotificationReadRequest
import com.sd.src.stepcounterapp.model.notificatyionlist.NotificationListResponse
import com.sd.src.stepcounterapp.model.privacy.PrivacyResponse
import com.sd.src.stepcounterapp.model.profile.ProfileResponse
import com.sd.src.stepcounterapp.model.profile.UpdateProfileRequest
import com.sd.src.stepcounterapp.model.redeemnow.RedeemRequest
import com.sd.src.stepcounterapp.model.refreshToken.RefreshTokenResponse
import com.sd.src.stepcounterapp.model.rewards.AddRewardsRequestObject
import com.sd.src.stepcounterapp.model.rewards.MyRedeemedResponse
import com.sd.src.stepcounterapp.model.rewards.RewardsCategoriesResponse
import com.sd.src.stepcounterapp.model.rewards.selectedRewards.SelectedRewardCategories
import com.sd.src.stepcounterapp.model.survey.SurveyResponse
import com.sd.src.stepcounterapp.model.survey.mysurveyresponse.MySurveyResponseModel
import com.sd.src.stepcounterapp.model.survey.surveyrequest.SurveystartRequestModel
import com.sd.src.stepcounterapp.model.syncDevice.FetchDeviceDataRequest
import com.sd.src.stepcounterapp.model.syncDevice.SyncRequest
import com.sd.src.stepcounterapp.model.syncDevice.elcies.WidgetBody
import com.sd.src.stepcounterapp.model.transactionhistory.TransactionHistoryModel
import com.sd.src.stepcounterapp.model.updateresponse.UpdateProfileResponse
import com.sd.src.stepcounterapp.model.vendor.VendorDetailResponse
import com.sd.src.stepcounterapp.model.vendor.VendorRequest
import com.sd.src.stepcounterapp.model.wallet.TokenModel
import com.sd.src.stepcounterapp.model.wallet.purchase.PurchaseResponse
import com.sd.src.stepcounterapp.model.wallet.walletdetailresponse.WalletModel
import com.sd.src.stepcounterapp.model.wishlist.AddWishRequest
import com.sd.src.stepcounterapp.model.wishlist.GetWishListRequest
import com.sd.src.stepcounterapp.model.wishlist.WishListResponse
import com.sd.src.stepcounterapp.model.wishlist.basicwishlist.BasicWishListResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {

    @POST("user_challenges")
    fun getMyChallenges(
        @Body body: BasicRequest
    ): Call<MyChallengeResponse>


    @POST("myredeemed")
    fun getMyRedeemedRewards(
        @Body body: BasicRequest
    ): Call<MyRedeemedResponse>



    @POST("user_survey")
    fun getMySurveys(
        @Body body: BasicRequest
    ): Call<MySurveyResponseModel>


    @POST("contactus")
    fun postcontactus(
        @Body body: ContactUsRequest
    ): Call<BasicInfoResponse>


    @POST("auth/login")
    fun authenticate_user(
        @Body body: LoginRequestObject
    ): Call<LoginResponseJ>

    @POST("forgot_password")
    fun forget_password(
        @Body body: ForgetRequestObject
    ): Call<BasicInfoResponse>

    @PUT("add_basic_info")
    fun add_basic_info(
        @Body body: BasicInfoRequestObject
    ): Call<BMIinfoResponse>

    @PUT("profile")
    fun update_profile(
        @Body body: UpdateProfileRequest
    ): Call<UpdateProfileResponse>

    @Multipart
    @POST("upload_profile_pic")
    fun uploadImage(@Part("userId") userId: RequestBody, @Part imageFile: MultipartBody.Part):
            Call<ImageResponse>


    @FormUrlEncoded
    @POST("check_username")
    fun check_username(@Field("username") username: String): Call<BasicInfoResponse>


    @GET("categories")
    fun getRewardsCategories(): Call<RewardsCategoriesResponse>

    @POST("category_products")
    fun getCategoryProducts(@Body body: BasicRequest): Call<MarketResponse>


    @POST("category_products")
    fun searchCategoryProducts(@Body body: BasicSearchRequest): Call<MarketResponse>

    @POST("popular_products")
    fun getPopularityProducts(@Body body: BasicRequest): Call<PopularProducts>


    @POST("popular_products")
    fun searchPopularProducts(@Body body: BasicSearchRequest): Call<PopularProducts>

    @PUT("update_rewards")
    fun AddRewards(@Body body: AddRewardsRequestObject): Call<BasicInfoResponse>


    @POST("wishlist")
    fun WishList(
        @Body body: AddWishRequest
    ): Call<BasicInfoResponse>

    @POST("wallet")
    fun getWishList(
        @Body body: GetWishListRequest
    ): Call<WishListResponse>


    @POST("wishlist")
    fun addWishList(
        @Body body: AddWishRequest
    ): Call<BasicWishListResponse>

    @PUT("wishlist")
    fun removeWishList(
        @Body body: AddWishRequest
    ): Call<BasicWishListResponse>


    @PUT("wishlist")
    fun deleteWishList(
        @Body body: RedeemRequest
    ): Call<BasicWishListResponse>


    /*@POST("activity")
    fun syncWeableData(
        @Body body: SyncRequest
    ): Call<BasicInfoResponse>
*/
    @POST("multipleActivity")
    fun syncWeableData(
        @Body body: SyncRequest
    ): Call<BasicInfoResponse>


    @POST("dashboard")
    fun getSyncData(
        @Body body: FetchDeviceDataRequest
    ): Call<DashboardResponse>


    @POST("challenges")
    fun getChallenges(
        @Body body: BasicRequest
    ): Call<ChallengeResponse>

    @FormUrlEncoded
    @POST("stop_challenge")
    fun stopChallenges(
        @Field("userId") userId: String,
        @Field("challengeId") challengeId: String
    ): Call<BasicInfoResponse>

    @POST("join_challenge")
    fun startChallenge(@Body body: ChallengeStartRequestModel): Call<StartChallengeResponse>


    @POST("join_challenge")
    fun startWeekendChallenge(@Body body: WeekendChallengeStartRequestModel): Call<StartChallengeResponse>

    @POST("steps_to_token")
    fun steps_to_token(@Body body: BasicUserRequest): Call<TokenModel>

    @POST("survey")
    fun getsurvey(@Body body: BasicRequest): Call<SurveyResponse>

    @POST("attend_survey")
    fun takesurvey(@Body body: SurveystartRequestModel): Call<BasicInfoResponse>

    @POST("wallet")
    fun wallet(@Body body: BasicUserRequest): Call<WalletModel>

    @POST("wallet")
    fun walletData(@Body body: BasicRequest): Call<WalletModelResponse>

    @POST("redeem_now")
    fun redeemNow(@Body body: RedeemRequest): Call<PurchaseResponse>

//    @POST("/leaderboard")
    @POST("m1/leaderboard")
    fun getLeaderboard(@Body body: LeaderBoardRequest): Call<LeaderBoardResponse>


    @FormUrlEncoded
    @POST("department_leaderboard")
    fun getDepartmentLeaderboard(@Field("challengeId") ChallengeRequest:String): Call<DepartmentLeaderboardResponse>

    @POST("m1/challenge_leaderboard")
    fun getTknLeaderboard(@Body body: LeaderBoardTknRequest): Call<LeaderBoardResponse>


    @GET("v1/read-notifications/{userId}")
    fun readAllNotifications(@Path("userId") userId:String): Call<BasicInfoResponse>


    @POST("profile")
    fun getProfileData(
        @Body body: BasicUserRequest
    ): Call<ProfileResponse>


    @PUT("notification_setting")
    fun notificationSettings(
        @Body body: BasicNotificationSettingsRequest
    ): Call<NotificationResponse>

    @POST("my_transactions")
    fun getTransactionHistory(
        @Body body: BasicRequest
    ): Call<TransactionHistoryModel>

    @POST("change_password")
    fun change_password(
        @Body body: ChangePasswordRequest
    ): Call<BasicInfoResponse>


    @GET("pages/privacy_policy")
    fun getPrivacyPolicy(): Call<PrivacyResponse>

    @GET("pages/terms_conditions")
    fun gettnc(): Call<PrivacyResponse>


    @POST("notifications")
    fun getNotificationList(
        @Body body:com.sd.src.stepcounterapp.model.notification.BasicRequest
    ): Call<NotificationListResponse>


    @POST("categories")
    fun getSelectedRewardsCategories( @Body body: BasicUserRequest): Call<SelectedRewardCategories>

    @POST("notification_read")
    fun readNotification(
        @Body body: NotificationReadRequest
    ): Call<BasicInfoResponse>


    @POST("product_vendor")
    fun getVendorDetails(
        @Body body: VendorRequest
    ): Call<VendorDetailResponse>


    //Elcies APIs
//    https://api.elcies.com/api/UserActiveService?externalUserId=333
//    https://api-prd.kpn.com/data/elcies/health-data-aggregator/useractiveservice?externalUserId=
    @GET("useractiveservice")
    fun getConnectedDevices(@Query("externalUserId")  externalUsrId:Int): Call<JsonElement>

    @GET("v1/elicies_access_token")
    fun refreshToken():Call<RefreshTokenResponse>


//    ["name": widgetName, "status": status, Keys.userId: AppUserDefaults.value(forKey: Keys.userId) ?? 0]
    @POST("v1/elicies_connection")
    fun updateWidgetInfo(@Body body : WidgetBody):Call<BasicInfoResponse>


}