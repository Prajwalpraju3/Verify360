package Services;

import com.covert.verify360.BeanClasses.AssignedCasesResponse;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IAssignedCases {
    @FormUrlEncoded
    @POST("AssignedCases")
    Observable<AssignedCasesResponse> getCasesFromNetwork(
            @Field("working_by") String Eid
    );
}
