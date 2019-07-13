package Services;

import com.covert.verify360.BeanClasses.ResponseMessage;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface NeighbourCheckService {
    @FormUrlEncoded
    @POST("CaseNeighbourDetails")
    Call<ResponseMessage> submitNeighbourCheck(
            @Field("case_id")String case_id,
            @Field("case_detailed_id")String case_detailed_id,
            @Field("Neighbour_name")String Neighbour_name,
            @Field("Address")String Address,
            @Field("Remarks")String remarks,
            @Field("Adverse")boolean adverse,
            @Field("Confirmed_stay")boolean confirmed_stay,
            @Field("Political_contact")boolean political_contact,
            @Field("Rowdism")boolean rowdism,
            @Field("inserted_by")String working_by,
            @Field("trans")String trans

    );
}
