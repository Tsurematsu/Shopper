package com.uts.shopper.helpers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken; // Importación necesaria

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type; // Importación necesaria
import java.net.URLConnection;
import java.util.List;
import java.util.Map; // Importación necesaria
import java.util.concurrent.TimeUnit;
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

    // =========================================================================
    // METODOS ORIGINALES (Intactos)
    // =========================================================================

    public static void GET(String endPoint, Consumer<String> onResponse){
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
                Log.e("APP_API_DEBUG", "Error POST string ->\n"+  e.getMessage());
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

    // =========================================================================
    // NUEVOS METODOS AGREGADOS (GetMap y PostMap)
    // =========================================================================

    /**
     * Realiza una petición GET y retorna la respuesta parseada como un Map.
     * Útil para acceder a claves dinámicas sin crear una clase POJO.
     */
    public static void GetMap(String endPoint, Consumer<Map<String, Object>> onResponse) {
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
                        // Usamos TypeToken para decirle a Gson que queremos un Map<String, Object>
                        Type mapType = new TypeToken<Map<String, Object>>(){}.getType();
                        Map<String, Object> map = gson.fromJson(responseBody, mapType);
                        onResponse.accept(map);
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
     * Realiza una petición POST enviando un objeto y retorna la respuesta parseada como un Map.
     */
    public static void PostMap(Object objeto, String endPoint, Consumer<Map<String, Object>> onResponse) {
        try {
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
                    onResponse.accept(null);
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful() && response.body() != null) {
                        String responseBody = response.body().string();
                        try {
                            // Usamos TypeToken para decirle a Gson que queremos un Map<String, Object>
                            Type mapType = new TypeToken<Map<String, Object>>(){}.getType();
                            Map<String, Object> map = gson.fromJson(responseBody, mapType);
                            onResponse.accept(map);
                        } catch (Exception e) {
                            e.printStackTrace();
                            onResponse.accept(null);
                        }
                    } else {
                        onResponse.accept(null);
                    }
                }
            });
        } catch (Exception e) {
            Log.e("APP_API_DEBUG", "(Fetch PostMap)\n" + e.getMessage());
        }
    }

    // =========================================================================
    // RESTO DE METODOS (Intactos)
    // =========================================================================

    public static void upload(String endPoint, String keyName, String filePath, Consumer<String> onResponse) {
        if (filePath == null || filePath.isEmpty()) {
            Log.e("Fetch", "La ruta del archivo es nula o vacía");
            onResponse.accept(null);
            return;
        }

        File file = new File(filePath);

        if (!file.exists()) {
            Log.e("Fetch", "El archivo no existe en la ruta: " + filePath);
            onResponse.accept(null);
            return;
        }

        String mimeType = URLConnection.guessContentTypeFromName(file.getName());
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }
        MediaType fileMediaType = MediaType.parse(mimeType);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(keyName, file.getName(), RequestBody.create(file, fileMediaType))
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
                    Log.e("Fetch", "Error upload: " + response.code());
                    onResponse.accept(null);
                }
            }
        });
    }

    public static void checkHealth(Consumer<Boolean> onResult) {
        if (urlAPI == null || urlAPI.isEmpty()) {
            onResult.accept(false);
            return;
        }

        OkHttpClient shortTimeoutClient = client.newBuilder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url(urlAPI)
                .head()
                .build();

        shortTimeoutClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                onResult.accept(false);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                response.close();
                onResult.accept(true);
            }
        });
    }

    public static void findWorkingHost(String[] urls, Consumer<String> onFound, Runnable onFailure) {
        if (urls == null || urls.length == 0) {
            onFailure.run();
            return;
        }
        checkUrlRecursive(urls, 0, onFound, onFailure);
    }

    private static void checkUrlRecursive(String[] urls, int index, Consumer<String> onFound, Runnable onFailure) {
        if (index >= urls.length) {
            onFailure.run();
            return;
        }

        String candidateUrl = urls[index];

        OkHttpClient fastClient = client.newBuilder()
                .connectTimeout(2, TimeUnit.SECONDS)
                .readTimeout(2, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url(candidateUrl)
                .head()
                .build();

        fastClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.w("Fetch", "Fallo al conectar con: " + candidateUrl);
                checkUrlRecursive(urls, index + 1, onFound, onFailure);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                response.close();
                urlAPI = candidateUrl;
                Log.d("Fetch", "Servidor encontrado y establecido: " + urlAPI);
                onFound.accept(candidateUrl);
            }
        });
    }


    // =========================================================================
    // NUEVOS METODOS PARA LISTAS (Arrays JSON)
    // =========================================================================

    /**
     * Realiza una petición GET esperando un Array JSON [{}, {}]
     * Retorna una Lista de Mapas.
     */
    public static void GetList(String endPoint, Consumer<List<Map<String, Object>>> onResponse) {
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
                        // Usamos TypeToken para List<Map<String, Object>>
                        Type listType = new TypeToken<List<Map<String, Object>>>(){}.getType();
                        List<Map<String, Object>> list = gson.fromJson(responseBody, listType);
                        onResponse.accept(list);
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
     * Realiza una petición POST esperando un Array JSON como respuesta.
     */
    public static void PostList(Object objeto, String endPoint, Consumer<List<Map<String, Object>>> onResponse) {
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
                onResponse.accept(null);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    try {
                        Type listType = new TypeToken<List<Map<String, Object>>>(){}.getType();
                        List<Map<String, Object>> list = gson.fromJson(responseBody, listType);
                        onResponse.accept(list);
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
}