package com.uts.shopper.helpers;

import android.util.Log;

import androidx.annotation.NonNull;


import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Fetch {
    private static String authToken = null;
    private static OkHttpClient client = new OkHttpClient();
    private static MediaType JSON_TYPE = MediaType.get("application/json; charset=utf-8");
    private static Gson gson = new Gson();
    public static String urlAPI = "";

    // Metodo para establecer el token
    public static void setAuthToken(String token) {
        authToken = token;
    }

    // Metodo para obtener el token actual
    public static String getAuthToken() {
        return authToken;
    }

    public static void GET(String endPoint, Consumer<String> onResponse){
        Request.Builder requestBuilder = new Request.Builder()
                .url(urlAPI + endPoint);

        // Agregar el header de autorización si existe el token
        if (authToken != null && !authToken.isEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = requestBuilder.build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    onResponse.accept(responseBody);
                }
            }
        });
    }

    public static <T> void GET(String endPoint, Consumer<T> onResponse, Class<T> classObject) {
        Request.Builder requestBuilder = new Request.Builder()
                .url(urlAPI + endPoint);

        if (authToken != null && !authToken.isEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = requestBuilder.build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                onResponse.accept(null);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    try {
                        T obj = gson.fromJson(responseBody, classObject);
                        onResponse.accept(obj);
                    } catch (Exception e) {
                        e.printStackTrace();
                        onResponse.accept(null);
                    }
                } else {
                    onResponse.accept(null);
                }
            }
        });
    }

    public static void POST(Object objeto, String endPoint, Consumer<String> onResponse){
        String jsonString = gson.toJson(objeto);
        RequestBody body = RequestBody.create(jsonString, JSON_TYPE);

        Request.Builder requestBuilder = new Request.Builder()
                .url(urlAPI + endPoint)
                .post(body);

        if (authToken != null && !authToken.isEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = requestBuilder.build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    onResponse.accept(responseBody);
                }
            }
        });
    }

    public static <T> void POST(Object objeto, String endPoint, Consumer<T> onResponse, Class<T> classObject){
        String jsonString = gson.toJson(objeto);
        RequestBody body = RequestBody.create(jsonString, JSON_TYPE);

        Request.Builder requestBuilder = new Request.Builder()
                .url(urlAPI + endPoint)
                .post(body);

        if (authToken != null && !authToken.isEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = requestBuilder.build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    try {
                        T obj = gson.fromJson(responseBody, classObject);
                        onResponse.accept(obj);
                    } catch (Exception e) {
                        e.printStackTrace();
                        onResponse.accept(null);
                    }
                } else {
                    onResponse.accept(null);
                }
            }
        });
    }

    // Método para subir archivos (Multipart)
    public static void uploadFile(String endPoint, String paramName, File file, Consumer<String> onResponse) {
        if (file == null || !file.exists()) {
            Log.e("Fetch", "El archivo no existe o es nulo");
            onResponse.accept(null);
            return;
        }

        // Determinar el tipo de contenido del archivo (o usar stream por defecto)
        MediaType MEDIA_TYPE = MediaType.parse("application/octet-stream");

        // Construimos el cuerpo Multipart
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(paramName, file.getName(), RequestBody.create(file, MEDIA_TYPE))
                .build();

        Request.Builder requestBuilder = new Request.Builder()
                .url(urlAPI + endPoint)
                .post(requestBody);

        if (authToken != null && !authToken.isEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = requestBuilder.build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                onResponse.accept(null);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    onResponse.accept(responseBody);
                } else {
                    onResponse.accept(null);
                }
            }
        });
    }
}