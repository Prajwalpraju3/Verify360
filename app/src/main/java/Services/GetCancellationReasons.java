package Services;

import android.content.Context;

import com.covert.verify360.BeanClasses.CancelledReasonsList;
import com.covert.verify360.BeanClasses.GetCancelledReasonsResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetCancellationReasons {

    Context context;
    PassCancelReasons passCancelReasons;

    public GetCancellationReasons(Context context, PassCancelReasons passCancelReasons) {
        this.context = context;
        this.passCancelReasons = passCancelReasons;
    }

    public void getAllReasons(){

        CancelledCasesReasonService casesService = FactoryService.createService(CancelledCasesReasonService.class);
        Call<GetCancelledReasonsResponse> responseCall = null;

        //System.out.println("***###@@@ someone called my name !!..");
        responseCall = casesService.getCancelledReasons("4");

        responseCall.enqueue(new Callback<GetCancelledReasonsResponse>() {
            @Override
            public void onResponse(Call<GetCancelledReasonsResponse> call, Response<GetCancelledReasonsResponse> response) {

                //System.out.println("****$$$#### response "+response.code());
                if(response.code() == 200 && response.body().getError().equals("false")){
                    //System.out.println("****$$$#### response getUser "+response.body().getUser().size());
                    passCancelReasons.ReasonsObtained((ArrayList<CancelledReasonsList>) response.body().getUser());

                }else{
                    passCancelReasons.Error("");
                }
            }

            @Override
            public void onFailure(Call<GetCancelledReasonsResponse> call, Throwable t) {
                //System.out.println("****$$$#### response "+t.getMessage());
                t.printStackTrace();
                passCancelReasons.Error(t.getMessage());
            }
        });

    }

}
