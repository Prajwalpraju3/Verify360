package Services;

import io.reactivex.annotations.Nullable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface StatusTranscationService {

    @FormUrlEncoded
    @POST("STATUS_TRANSACTION_DETAILS")
    Call<StatusTxnResponse> getCancelledReasons(
            @Field("CASE_ID") String caseId,
            @Field("CASE_DETAILED_ID") String caseDetailId,
            @Field("WORKING_BY") String workingBy,
            @Field("status_id") String statusId,
            @Field("cancelled_reason_id") String reasonId,
            @Nullable@Field("REASON") String reasonStr);

}
