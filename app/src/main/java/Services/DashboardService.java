package Services;

import com.covert.verify360.BeanClasses.CancelledCasesResponse;
import com.covert.verify360.BeanClasses.Dashboard;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface DashboardService {
    @FormUrlEncoded
    @POST("Dashboard")
    Call<Dashboard> getDashboardData(@Field("working_by") String Eid);
}
