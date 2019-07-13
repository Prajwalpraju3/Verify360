package Services;

import com.covert.verify360.BeanClasses.AssignedCasesResponse;
import com.covert.verify360.BeanClasses.PendingCasesResponse;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IPendingCases {
    @FormUrlEncoded
    @POST("PendingCases")
    Observable<PendingCasesResponse> getCasesFromNetwork(
            @Field("working_by") String Eid
    );
}
