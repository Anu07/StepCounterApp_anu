package com.sd.src.stepcounterapp.network

import com.sd.src.stepcounterapp.model.BaseModel
import com.sd.src.stepcounterapp.model.BasicInfoRequestObject
import com.sd.src.stepcounterapp.model.DeviceResponse.DashboardResponse
import com.sd.src.stepcounterapp.model.challenge.ChallengeResponse
import com.sd.src.stepcounterapp.model.generic.BasicInfoResponse
import com.sd.src.stepcounterapp.model.generic.BasicRequest
import com.sd.src.stepcounterapp.model.image.ImageResponse
import com.sd.src.stepcounterapp.model.leaderboard.LeaderBoardResponse
import com.sd.src.stepcounterapp.model.login.LoginResponseJ
import com.sd.src.stepcounterapp.model.loginrequest.LoginRequestObject
import com.sd.src.stepcounterapp.model.marketplace.BasicSearchRequest
import com.sd.src.stepcounterapp.model.marketplace.MarketResponse
import com.sd.src.stepcounterapp.model.marketplace.PopularProducts
import com.sd.src.stepcounterapp.model.profile.ProfileResponse
import com.sd.src.stepcounterapp.model.profile.UpdateProfileRequest
import com.sd.src.stepcounterapp.model.rewards.AddRewardsRequestObject
import com.sd.src.stepcounterapp.model.rewards.RewardsCategoriesResponse
import com.sd.src.stepcounterapp.model.survey.Data
import com.sd.src.stepcounterapp.model.survey.SurveyListResponse
import com.sd.src.stepcounterapp.model.syncDevice.FetchDeviceDataRequest
import com.sd.src.stepcounterapp.model.syncDevice.SyncRequest
import com.sd.src.stepcounterapp.model.updateresponse.UpdateProfileResponse
import com.sd.src.stepcounterapp.model.wallet.TokenModel
import com.sd.src.stepcounterapp.model.wallet.WalletModel
import com.sd.src.stepcounterapp.model.wishList.AddWishRequest
import com.sd.src.stepcounterapp.model.wishList.GetWishListRequest
import com.sd.src.stepcounterapp.model.wishList.WishListResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {

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
    ): Call<BasicInfoResponse>

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
    @POST("api/stop_challenge")
    fun stopChallenges(
        @Field("userId") userId: String,
        @Field("challengeId") challengeId: String
    ): Call<BaseModel>

    @POST("api/join_challenge")
    fun startChallenge(@Body body: com.sd.src.stepcounterapp.model.challenge.Data): Call<BaseModel>

    @POST("api/steps_to_token")
    fun steps_to_token(@Body body: BasicRequest): Call<TokenModel>

    @GET("api/survey")
    fun getsurvey(): Call<SurveyListResponse>

    @POST("/api/wallet")
    fun wallet(@Body body: BasicRequest): Call<WalletModel>

    @POST("/api/leaderboard")
    fun getLeaderboard(@Body body: BasicRequest): Call<LeaderBoardResponse>



    @POST("api/profile")
    fun getProfileData(
        @Body body: BasicRequest
    ): Call<ProfileResponse>
}