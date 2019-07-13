package Services;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import Utils.BaseUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FactoryService {
    private static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);
    private static OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();

    private static Retrofit retrofit = new Retrofit.Builder()
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BaseUrl.BASE_URL)
            .build();

    private static String mURL = BaseUrl.BASE_URL;
    private static HttpLoggingInterceptor interceptor2 = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);
    private static OkHttpClient okHttpClient2 = new OkHttpClient.Builder().addInterceptor(interceptor2).build();
    private static Retrofit retrofitImage = new Retrofit.Builder()
            .client(okHttpClient2)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(mURL)
            .build();

    public static <T> T createService(Class<T> serviceClass) {
        return retrofit.create(serviceClass);
    }

    public static <T> T createFileService(Class<T> serviceClass) {
        return retrofitImage.create(serviceClass);
    }



/*    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
logging.setLevel(Level.BODY);

    */
}
