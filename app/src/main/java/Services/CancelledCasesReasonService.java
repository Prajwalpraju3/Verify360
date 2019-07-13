package Services;

import com.covert.verify360.BeanClasses.CancelledCasesResponse;
import com.covert.verify360.BeanClasses.GetCancelledReasonsResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface CancelledCasesReasonService {

    @FormUrlEncoded
    @POST("CancelledCasesReason")
    Call<GetCancelledReasonsResponse> getCancelledReasons(@Field("trans") String Eid);



}
