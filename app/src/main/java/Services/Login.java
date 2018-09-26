package Services;

import com.covert.verify360.BeanClasses.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Login {
    @FormUrlEncoded
    @POST("login_user")
    Call<LoginResponse> login(
            @Field("user_login_id") String username,
            @Field("password") String Password
    );
}
