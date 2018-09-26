package Services;

import com.covert.verify360.BeanClasses.ResponseMessage;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface FormSubmissionService {
    @POST("FormSubmitiondetails")
    @FormUrlEncoded
    Call<ResponseMessage> submitForm(
            @Field("TRANS") String trans,
            @Field("case_id")String case_id,
            @Field("case_detailed_id")String case_detailed_id,
            @Field("form_element_id")String form_element_id,
            @Field("Remarks")String Remarks,
            @Field("inserted_by")String working_by
            );

}
