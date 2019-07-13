package Services;

import com.covert.verify360.BeanClasses.ResponseMessage;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface PaySlipenquiryService {
    @FormUrlEncoded
    @POST("CaseEnqueryDetails")
    Call<ResponseMessage> submitEnquiryDetails(
            @Field("case_id")String case_id,
            @Field("case_detailed_id")String case_detailed_id,
            @Field("Met_with")String met_with,
            @Field("Designation") String designation,
            @Field("applicant_designation")String applicant_designation,
            @Field("employee_since") String emp_since,
            @Field("salary_slips_status")String salary_slip_status,
            @Field("Nature_of_Business")String nature_of_business,
            @Field("Remarks")String explaination,
            @Field("inserted_by")String working_by

    );
}
