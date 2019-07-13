package com.covert.verify360.BeanClasses;

import android.content.Context;

import Services.FactoryService;
import Services.StatusTranscationService;
import Services.StatusTxnDetailsResponse;
import Services.StatusTxnResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatusTxnDetailsPost {

    StatusTxnDetailsResponse statusTxnDetailsResponse;
    Context context;

    public StatusTxnDetailsPost(StatusTxnDetailsResponse statusTxnDetailsResponse, Context context) {
        this.statusTxnDetailsResponse = statusTxnDetailsResponse;
        this.context = context;
    }


    public void postStatusTxndetailsResponse(String caseId,
                                              String caseDetailId,
                                              String workingBy,
                                              String statusId,
                                              String reasonId,
                                              String reasonStr){


        StatusTranscationService  service = FactoryService.createService(StatusTranscationService.class);
        Call<StatusTxnResponse> call = null;
        call = service.getCancelledReasons(caseId,caseDetailId,workingBy,"13",reasonId,reasonStr);

        call.enqueue(new Callback<StatusTxnResponse>() {
            @Override
            public void onResponse(Call<StatusTxnResponse> call, Response<StatusTxnResponse> response) {
                //System.out.println("****###@@@@ statusTxnResponse response "+response.code()+" "+response.body().getError());
                if(response.code() ==200 && response.body().getError().equals("false")){
                    statusTxnDetailsResponse.responseDetails(response.body());
                }else{
                    statusTxnDetailsResponse.errorResponse("error");
                }

            }

            @Override
            public void onFailure(Call<StatusTxnResponse> call, Throwable t) {

                statusTxnDetailsResponse.errorResponse(t.getMessage());
            }
        });
    }
}
