package Services;

import com.covert.verify360.BeanClasses.LoginResponse;
import com.covert.verify360.BeanClasses.LogoutResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Logout {
    @FormUrlEncoded
    @POST("Logout")
    Call<LogoutResponse> logout(
            @Field("Working_by") String workingBy,
            @Field("latitude") Double lat,
            @Field("longitude") Double lon
    );
}
