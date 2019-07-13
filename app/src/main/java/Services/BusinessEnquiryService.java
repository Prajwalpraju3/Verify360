package Services;

import com.covert.verify360.BeanClasses.ResponseMessage;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface BusinessEnquiryService {
    @FormUrlEncoded
    @POST("CaseEnqueryDetails")
    Call<ResponseMessage> submitEnquiryDetails(
            @Field("case_id")String case_id,
            @Field("case_detailed_id")String case_detailed_id,
            @Field("Met_with")String met_with,
            @Field("Relationship_with_Applicant") String relation_with_applicant,
            @Field("owned")String owned,
            @Field("Year_of_ops_in_Current_location") String yof_ops_in_location,
            @Field("Nature_of_Business")String nature_of_business,
            @Field("No_Of_employees")String no_of_employees,
            @Field("Designation")String designation,
            @Field("Remarks")String otherRemarks,
            @Field("inserted_by")String working_by

    );
}
