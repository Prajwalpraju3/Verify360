package Services;

import com.covert.verify360.BeanClasses.CancelledCasesResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface CanclledCasesService {

    @FormUrlEncoded
    @POST("cancelledCaseList")
    Call<CancelledCasesResponse> getCancelledCases(@Field("working_by") String Eid);
}
