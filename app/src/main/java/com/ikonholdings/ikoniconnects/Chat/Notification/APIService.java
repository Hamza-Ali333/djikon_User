package com.ikonholdings.ikoniconnects.Chat.Notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers({
                    "Content-Type:application/json",
                    "Authorization:key=AAAArtlB-KE:APA91bFVLEUan-vwsJjo_3U-Ajt32d--asVQV2RWg8b2Yz0OoMxjGLLozM94nA-XMkXQZtqRLEJvtfCXEzQH4DXHqTEx-yt-MIIElnKS6Arq4F5J1PkOZQ5jZAZHgxjHeuTqaxxoJBHE"
            })
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
