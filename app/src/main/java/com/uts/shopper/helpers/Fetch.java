package com.uts.shopper.helpers;

import android.util.Log;

import androidx.annotation.NonNull;


import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
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

    /**
     * Sube un archivo usando Multipart/form-data.
     *
     * @param endPoint La ruta del endpoint (ej: "/subir-foto")
     * @param keyName  El nombre del campo que espera el backend (ej: "archivo" o "image")
     * @param filePath La ruta absoluta del archivo (que te da el FileHelper)
     * @param onResponse Callback con la respuesta del servidor
     */
    public static void upload(String endPoint, String keyName, String filePath, Consumer<String> onResponse) {
        // 1. Validar que la ruta no sea nula
        if (filePath == null || filePath.isEmpty()) {
            Log.e("Fetch", "La ruta del archivo es nula o vacía");
            onResponse.accept(null);
            return;
        }

        // 2. Crear el archivo desde la ruta
        File file = new File(filePath);

        // 3. Validar que el archivo exista físicamente
        if (!file.exists()) {
            Log.e("Fetch", "El archivo no existe en la ruta: " + filePath);
            onResponse.accept(null);
            return;
        }

        // 4. Intentar adivinar el tipo de archivo (Mime Type) o usar default
        String mimeType = URLConnection.guessContentTypeFromName(file.getName());
        if (mimeType == null) {
            mimeType = "application/octet-stream"; // Tipo genérico binario
        }
        MediaType fileMediaType = MediaType.parse(mimeType);

        // 5. Construir el cuerpo de la petición (Multipart)
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM) // Importante para que sea form-data
                // "keyName" es el nombre del campo, file.getName() es el nombre del fichero, fileMediaType es el contenido
                .addFormDataPart(keyName, file.getName(), RequestBody.create(file, fileMediaType))
                .build();

        // 6. Construir la petición
        Request.Builder requestBuilder = new Request.Builder()
                .url(urlAPI + endPoint)
                .post(requestBody);

        // 7. Agregar Auth Token si existe
        if (authToken != null && !authToken.isEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = requestBuilder.build();

        // 8. Ejecutar
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
                    // Puedes loguear el error del body si quieres ver por qué falló el back
                    Log.e("Fetch", "Error upload: " + response.code());
                    onResponse.accept(null);
                }
            }
        });
    }
}