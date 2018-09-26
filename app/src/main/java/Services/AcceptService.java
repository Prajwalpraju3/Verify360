package Services;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AcceptService {
    @FormUrlEncoded
    @POST("STATUS_TRANSACTION_DETAILS")
    Call<ResponseBody> accept(
            @Field("working_by") String working_by,
            @Field("CASE_DETAILED_ID") String activity_id,
            @Field("CASE_ID") String case_id,
            @Field("status_id") String status
    );
}

