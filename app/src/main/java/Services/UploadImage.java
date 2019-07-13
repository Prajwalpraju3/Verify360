package Services;

import com.covert.verify360.BeanClasses.ResponseMessage;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface UploadImage {
    @Multipart
    @POST("Upload/user/PostUserImage")
    Call<ResponseMessage> uploadImage(@Part MultipartBody.Part image,
                                      @Part("case_id") RequestBody case_id,
                                      @Part("case_detailed_id") RequestBody case_detailed_id,
                                      @Part("document_for") RequestBody docFor,
                                      @Part("inserted_by") RequestBody working_by,
                                      @Part("latitude") RequestBody lat,
                                      @Part("longitude") RequestBody lon);
}
