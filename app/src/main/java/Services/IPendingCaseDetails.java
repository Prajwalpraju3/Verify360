package Services;

import com.covert.verify360.BeanClasses.PendingCaseDetails;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IPendingCaseDetails {
    @FormUrlEncoded
    @POST("PendingCaseDetails")
    Call<PendingCaseDetails> getPendingCases(
            @Field("working_by") String working_by,
            @Field("case_id") String case_id,
            @Field("case_detailed_id") String activity_id
    );
}
