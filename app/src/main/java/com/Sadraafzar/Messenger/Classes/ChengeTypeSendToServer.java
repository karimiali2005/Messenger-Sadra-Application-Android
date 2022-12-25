package com.Sadraafzar.Messenger.Classes;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.Sadraafzar.Messenger.Model.Changetype;
import com.Sadraafzar.Messenger.WebService.APIClient;
import com.Sadraafzar.Messenger.WebService.APIInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChengeTypeSendToServer {

   public void Send(Context context)
    {
        try {
            db_query dq=new db_query();
          List<Changetype> lc =dq.selectChangetype(context);

          for(Changetype l:lc)
          {

                      String messagestring=app.AesEncryptionAlgorithm.EncryptServiceUserSend(String.valueOf(l.idmessage));
                      APIInterface apiInterface = APIClient.getClient(app.Communication.RegisterApiVersion).create(APIInterface.class);
                      Call<String> call = apiInterface.UpdateDateTime(messagestring,l.idtype);
                      call.enqueue(new Callback<String>() {
                          @Override
                          public void onResponse(Call<String> call, Response<String> response) {
                              if (response.isSuccessful())
                              {
                                  dq.del_Changetype(context,l.idmessage);
                              }else
                              {
                                  app.Info.ischeang=true;
                              }
                          }

                          @Override
                          public void onFailure(Call<String> call, Throwable t) {
                              app.Info.ischeang=true;
                          }
                      });

          }
          lc=dq.selectChangetype(context);

        }
        catch (Exception ex)
        {
            app.Info.ischeang=true;
        }
    }
}
