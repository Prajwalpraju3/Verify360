package Services;

import com.covert.verify360.BeanClasses.ResponseMessage;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface FinalRemarks {
    @POST("FinalRemarkDetails")
    @FormUrlEncoded
    Call<ResponseMessage> submitFinalRemarks(
            @Field("final_remark_type") String remarkType,
            @Field("Recommendation_Remarks")String remarks,
            @Field("case_id")String case_id,
            @Field("case_detailed_id")String case_detailed_id,
            @Field("inserted_by")String working_by
    );
}
