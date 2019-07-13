package Services;

import com.covert.verify360.BeanClasses.ResponseMessage;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface MFormSubmissionService {

    /*@POST("FormSubmitiondetails")
    @FormUrlEncoded
    Call<ResponseMessage> submitForm(
            @Field("case_id")String case_id,
            @Field("case_detailed_id")String case_detailed_id,
            @Field("s")String idValue,
            @Field("inserted_by")String working_by,
            @Field("TRANS") String trans*/

    @POST("FormSubmitiondetails")
    @FormUrlEncoded
    Call<ResponseMessage> submitForm(
            @Field("case_id")String case_id,
            @Field("case_detailed_id")String case_detailed_id,
            @Field("s")String s,
            @Field("inserted_by")String working_by,
            @Field("TRANS") int trans
    );
}
