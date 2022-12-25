package com.Sadraafzar.Messenger.WebService;



import com.Sadraafzar.Messenger.Image.ServerResponseUpload;
import com.Sadraafzar.Messenger.Model.LoginLimitViewModel;
import com.Sadraafzar.Messenger.Model.OfferPriceViewModel;
import com.Sadraafzar.Messenger.Model.ProductGroupViewModel;
import com.Sadraafzar.Messenger.Model.ProductRequestProviderViewModel;
import com.Sadraafzar.Messenger.Model.ProductRequestShowViewModel;
import com.Sadraafzar.Messenger.Model.ReceiveAllViewModel;
import com.Sadraafzar.Messenger.Model.UserPicViewModel;
import com.Sadraafzar.Messenger.Model.UserViewModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface APIInterface {


    //image
    @Multipart
    @POST("/api1/upload")
    Call<ServerResponseUpload> uploadFile(@Part MultipartBody.Part file, @Part("file") RequestBody name);

    @GET()
    Call<String> getStringResponse(@Url String url);


    @GET("GetUserPicName")
    Call<UserPicViewModel> GetUserPicName(@Query("userId") int userId);


    //

    //region Versrsion code
    @PUT("GetLoadVersion")
    Call<LoginLimitViewModel> GetLoadVersion();

    //endregion
    //region Register
    @PUT("Register")
    Call<ResponseBody> RegisterUser(@Query("username") String username, @Query("type") int type, @Query("ip4") String ip4, @Query("appVersion") int appVersion);

    @PUT("ValidVerificationCode")
    Call<ResponseBody> ValidVerificationCode(@Query("userId") int userId, @Query("verificationCode") int verificationCode, @Query("publicKey") String publicKey,@Query("tokenFireBase") String tokenFireBase);

    @PUT("RegisterUserInfo")
    Call<ResponseBody> RegisterUserInfo(@Body UserViewModel user);

    @PUT("ChangeTokenFireBase")
    Call<ResponseBody> ChangeTokenFireBase(@Query("userId")String userid,@Query("tokenFireBase") String tokenFireBase);

    @GET("GetUserInfo")
    Call<UserViewModel> GetUserInfo(@Query("userId") int userId);
    //endregion
    //region ProductRequest

    @GET("GetProductGroup")
    Call<List<ProductGroupViewModel>> GetProductGroup();

    @PUT("RegisterProductRequest")
    Call<ResponseBody> RegisterProductRequest(@Query("userId") int userId, @Query("productGroupId") int productGroupId, @Query("comment") String comment, @Query("address") String address);

    @GET("GetProductRequest")
    Call<List<ProductRequestShowViewModel>> GetProductRequest(@Query("userId") int userId);

    @GET("GetProductRequestById")
    Call<ProductRequestShowViewModel> GetProductRequestById(@Query("userId") int userId, @Query("productRequestId") int productRequestId);

    @PUT("ProductRequestUpdate")
    Call<ResponseBody> ProductRequestUpdate(@Query("userId") int userId, @Query("productRequestId") int productRequestId, @Query("productGroupId") int productGroupId, @Query("comment") String comment, @Query("address") String address);



    //endregion
    //region OfferPrice

    @GET("GetProductRequestProvider")
    Call<List<ProductRequestProviderViewModel>> GetProductRequestProvider(@Query("userId") int userId);

    @PUT("RegisterOfferPrice")
    Call<ResponseBody> RegisterOfferPrice(@Query("userId") int userId, @Query("productRequestId") int productRequestId, @Query("price") int price, @Query("ship") int ship, @Query("offerPriceComment") String offerPriceComment);

    @GET("GetOfferPriceById")
    Call<OfferPriceViewModel> GetOfferPriceById(@Query("userId") int userId, @Query("productRequestId") int productRequestId);

    @PUT("OfferPriceUpdate")
    Call<ResponseBody> OfferPriceUpdate(@Query("userId") int userId, @Query("price") int price, @Query("ship") int ship, @Query("comment") String comment, @Query("offerPriceId") int offerPriceId);

    @GET("GetOfferList")
    Call<List<OfferPriceViewModel>> GetOfferList(@Query("userId") int userId, @Query("productRequestId") int productRequestId);

    @PUT("OfferChangeStatus")
    Call<ResponseBody> OfferChangeStatus(@Query("userId") int userId, @Query("offerPriceId") int offerPriceId, @Query("offerStatus") int offerStatus);
    //endregion

    //region Message
    @GET("GetReceiveAll")
    Call<ReceiveAllViewModel> GetReceiveAll(@Query("userIdString") String userId, @Query("number") String number, @Query("changeDate") String changeDate);

    @PUT("UpdateDateTime")
    Call<String> UpdateDateTime(@Query("pkfMessageString") String pkfMessageString, @Query("type") int type);

    //endregion

    @FormUrlEncoded
    @POST("deleteStudent")
    Call<ResponseBody> deletePeronInfo(@Field("id") int id);


    /*@POST("/signin")
    void signIn(@Body SignInRequestModel signInRequestModel, Callback<AuthorizeResponseModel> responseModelCallback);*/
}
