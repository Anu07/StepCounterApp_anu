package com.sd.src.stepcounterapp.network

import com.sd.src.stepcounterapp.model.BasicInfoRequestObject
import com.sd.src.stepcounterapp.model.DeviceResponse.DashboardResponse
import com.sd.src.stepcounterapp.model.bmi.BMIinfoResponse
import com.sd.src.stepcounterapp.model.challenge.ChallengeResponse
import com.sd.src.stepcounterapp.model.challenge.ChallengeStartRequestModel
import com.sd.src.stepcounterapp.model.challenge.ChallengeTakenResponse.StartChallengeResponse
import com.sd.src.stepcounterapp.model.challenge.MyChallengeResponse
import com.sd.src.stepcounterapp.model.changepwd.ChangePasswordRequest
import com.sd.src.stepcounterapp.model.contactUs.ContactUsRequest
import com.sd.src.stepcounterapp.model.generic.BasicInfoResponse
import com.sd.src.stepcounterapp.model.generic.BasicRequest
import com.sd.src.stepcounterapp.model.generic.BasicUserRequest
import com.sd.src.stepcounterapp.model.image.ImageResponse
import com.sd.src.stepcounterapp.model.leaderboard.LeaderBoardRequest
import com.sd.src.stepcounterapp.model.leaderboard.LeaderBoardResponse
import com.sd.src.stepcounterapp.model.login.LoginResponseJ
import com.sd.src.stepcounterapp.model.loginrequest.LoginRequestObject
import com.sd.src.stepcounterapp.model.marketplace.BasicSearchRequest
import com.sd.src.stepcounterapp.model.marketplace.MarketResponse
import com.sd.src.stepcounterapp.model.marketplace.PopularProducts
import com.sd.src.stepcounterapp.model.marketplace.walletInfo.WalletModelResponse
import com.sd.src.stepcounterapp.model.notification.BasicNotificationSettingsRequest
import com.sd.src.stepcounterapp.model.notification.NotificationResponse
import com.sd.src.stepcounterapp.model.notification.read_notification.NotificationReadRequest
import com.sd.src.stepcounterapp.model.notificatyionlist.NotificationListResponse
import com.sd.src.stepcounterapp.model.privacy.PrivacyResponse
import com.sd.src.stepcounterapp.model.profile.ProfileResponse
import com.sd.src.stepcounterapp.model.profile.UpdateProfileRequest
import com.sd.src.stepcounterapp.model.redeemnow.RedeemRequest
import com.sd.src.stepcounterapp.model.rewards.AddRewardsRequestObject
import com.sd.src.stepcounterapp.model.rewards.MyRedeemedResponse
import com.sd.src.stepcounterapp.model.rewards.RewardsCategoriesResponse
import com.sd.src.stepcounterapp.model.rewards.selectedRewards.SelectedRewardCategories
import com.sd.src.stepcounterapp.model.survey.SurveyResponse
import com.sd.src.stepcounterapp.model.survey.mysurveyresponse.MySurveyResponseModel
import com.sd.src.stepcounterapp.model.survey.surveyrequest.SurveystartRequestModel
import com.sd.src.stepcounterapp.model.syncDevice.FetchDeviceDataRequest
import com.sd.src.stepcounterapp.model.syncDevice.SyncRequest
import com.sd.src.stepcounterapp.model.transactionhistory.TransactionHistoryModel
import com.sd.src.stepcounterapp.model.updateresponse.UpdateProfileResponse
import com.sd.src.stepcounterapp.model.vendor.VendorDetailResponse
import com.sd.src.stepcounterapp.model.vendor.VendorRequest
import com.sd.src.stepcounterapp.model.wallet.TokenModel
import com.sd.src.stepcounterapp.model.wallet.purchase.PurchaseResponse
import com.sd.src.stepcounterapp.model.wallet.walletDetailResponse.WalletModel
import com.sd.src.stepcounterapp.model.wishList.AddWishRequest
import com.sd.src.stepcounterapp.model.wishList.GetWishListRequest
import com.sd.src.stepcounterapp.model.wishList.WishListResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {

    @POST("api/user_challenges")
    fun getMyChallenges(
        @Body body: BasicRequest
    ): Call<MyChallengeResponse>


    @POST("api/myredeemed")
    fun getMyRedeemedRewards(
        @Body body: BasicRequest
    ): Call<MyRedeemedResponse>



    @POST("api/user_survey")
    fun getMySurveys(
        @Body body: BasicRequest
    ): Call<MySurveyResponseModel>


    @POST("api/contactus")
    fun postcontactus(
        @Body body: ContactUsRequest
    ): Call<BasicInfoResponse>


    @POST("api/auth/login")
    fun authenticate_user(
        @Body body: LoginRequestObject
    ): Call<LoginResponseJ>

    @POST("api/forgot_password")
    fun forget_password(
        @Body body: LoginRequestObject
    ): Call<BasicInfoResponse>

    @PUT("api/add_basic_info")
    fun add_basic_info(
        @Body body: BasicInfoRequestObject
    ): Call<BMIinfoResponse>

    @PUT("api/profile")
    fun update_profile(
        @Body body: UpdateProfileRequest
    ): Call<UpdateProfileResponse>

    @Multipart
    @POST("api/upload_profile_pic")
    fun uploadImage(@Part("userId") userId: RequestBody, @Part imageFile: MultipartBody.Part):
            Call<ImageResponse>


    @FormUrlEncoded
    @POST("api/check_username")
    fun check_username(@Field("username") username: String): Call<BasicInfoResponse>


    @GET("api/categories")
    fun getRewardsCategories(): Call<RewardsCategoriesResponse>

    @POST("api/category_products")
    fun getCategoryProducts(@Body body: BasicRequest): Call<MarketResponse>


    @POST("api/category_products")
    fun searchCategoryProducts(@Body body: BasicSearchRequest): Call<MarketResponse>

    @POST("api/popular_products")
    fun getPopularityProducts(@Body body: BasicRequest): Call<PopularProducts>


    @POST("api/popular_products")
    fun searchPopularProducts(@Body body: BasicSearchRequest): Call<PopularProducts>

    @PUT("api/update_rewards")
    fun AddRewards(@Body body: AddRewardsRequestObject): Call<BasicInfoResponse>


    @POST("api/wishlist")
    fun WishList(
        @Body body: AddWishRequest
    ): Call<BasicInfoResponse>

    @POST("api/wallet")
    fun getWishList(
        @Body body: GetWishListRequest
    ): Call<WishListResponse>


    @POST("api/wishlist")
    fun addWishList(
        @Body body: AddWishRequest
    ): Call<BasicInfoResponse>

    @PUT("api/wishlist")
    fun removeWishList(
        @Body body: AddWishRequest
    ): Call<BasicInfoResponse>


    @PUT("api/wishlist")
    fun deleteWishList(
        @Body body: RedeemRequest
    ): Call<BasicInfoResponse>


    @POST("api/activity")
    fun syncWeableData(
        @Body body: SyncRequest
    ): Call<BasicInfoResponse>

    @POST("api/dashboard")
    fun getSyncData(
        @Body body: FetchDeviceDataRequest
    ): Call<DashboardResponse>


    @POST("api/challenges")
    fun getChallenges(
        @Body body: BasicRequest
    ): Call<ChallengeResponse>

    @FormUrlEncoded
    @POST("/api/stop_challenge")
    fun stopChallenges(
        @Field("userId") userId: String,
        @Field("challengeId") challengeId: String
    ): Call<BasicInfoResponse>

    @POST("/api/join_challenge")
    fun startChallenge(@Body body: ChallengeStartRequestModel): Call<StartChallengeResponse>

    @POST("api/steps_to_token")
    fun steps_to_token(@Body body: BasicUserRequest): Call<TokenModel>

    @POST("api/survey")
    fun getsurvey(@Body body: BasicRequest): Call<SurveyResponse>

    @POST("api/attend_survey")
    fun takesurvey(@Body body: SurveystartRequestModel): Call<BasicInfoResponse>

    @POST("/api/wallet")
    fun wallet(@Body body: BasicUserRequest): Call<WalletModel>

    @POST("/api/wallet")
    fun walletData(@Body body: BasicRequest): Call<WalletModelResponse>

    @POST("/api/redeem_now")
    fun redeemNow(@Body body: RedeemRequest): Call<PurchaseResponse>

    @POST("/api/leaderboard")
    fun getLeaderboard(@Body body: LeaderBoardRequest): Call<LeaderBoardResponse>



    @POST("api/profile")
    fun getProfileData(
        @Body body: BasicUserRequest
    ): Call<ProfileResponse>


    @POST("api/notification_setting")
    fun notificationSettings(
        @Body body: BasicNotificationSettingsRequest
    ): Call<NotificationResponse>

    @POST("api/my_transactions")
    fun getTransactionHistory(
        @Body body: BasicRequest
    ): Call<TransactionHistoryModel>

    @POST("api/change_password")
    fun change_password(
        @Body body: ChangePasswordRequest
    ): Call<BasicInfoResponse>


    @GET("api/pages/privacy_policy")
    fun getPrivacyPolicy(): Call<PrivacyResponse>

    @GET("api/pages/terms_conditions")
    fun gettnc(): Call<PrivacyResponse>


    @POST("api/notifications")
    fun getNotificationList(
        @Body body:com.sd.src.stepcounterapp.model.notification.BasicRequest
    ): Call<NotificationListResponse>


    @POST("api/categories")
    fun getSelectedRewardsCategories( @Body body: BasicUserRequest): Call<SelectedRewardCategories>

    @POST("api/notification_read")
    fun readNotification(
        @Body body: NotificationReadRequest
    ): Call<BasicInfoResponse>


    @POST("api/product_vendor")
    fun getVendorDetails(
        @Body body: VendorRequest
    ): Call<VendorDetailResponse>
}