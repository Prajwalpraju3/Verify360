package Services;

import android.database.Observable;

import com.covert.verify360.BeanClasses.CompletedCasesResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface CompletedCaseDetails {

    @FormUrlEncoded
    @POST("CompletedCaseslist")
    Call<CompletedCasesResponse> getCompletedCases(@Field("working_by") String Eid);

}
