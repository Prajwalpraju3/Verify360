package Services;

import com.covert.verify360.BeanClasses.ResponseMessage;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface EnquiryDetailsService {
    @FormUrlEncoded
    @POST("CaseEnqueryDetails")
    Call<ResponseMessage> submitEnquiryDetails(
            @Field("case_id")String case_id,
            @Field("case_detailed_id")String case_detailed_id,
            @Field("Met_with")String met_with,
            @Field("Relationship_with_Applicant") String relation_with_applicant,
            @Field("owned")String owned,
            @Field("Marital_Status") String marital_status,
            @Field("Year_of_ops_in_Current_location") String yof_ops_in_location,
            @Field("no_of_family_members")String no_of_family_members,
            @Field("No_of_members_working")String no_of_working,
            @Field("company_Name")String company_name,
            @Field("Designation")String designation,
            @Field("year_of_exp")String years_of_exp,
            @Field("inserted_by")String working_by
    );
}
