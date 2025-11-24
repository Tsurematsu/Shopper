

# Documentación Helper `Fetch`

El helper `Fetch` es una clase utilitaria estática diseñada para simplificar las peticiones HTTP (REST API) en Android utilizando **OkHttp** y **Gson**. Gestiona automáticamente la serialización de objetos, la autenticación mediante Bearer Token y la subida de archivos multipart.

## 📋 Configuración Inicial

Antes de realizar cualquier petición, debes configurar la URL base y, opcionalmente, el token de autenticación. Se recomienda hacer esto en el `onCreate` de tu `Application` o tu `MainActivity`.

### 1. Permisos Requeridos
Asegúrate de tener el permiso de internet en tu `AndroidManifest.xml`:
```xml
<uses-permission android:name="android.permission.INTERNET" />
```

Basado en el código de tu clase `Fetch`, que es un cliente HTTP manual construido sobre **OkHttp** y que usa **Gson** para el manejo de JSON, he extraído únicamente las dependencias necesarias para que funcione.

He descartado librerías como `retrofit` (ya que tu clase `Fetch` hace manualmente el trabajo que haría Retrofit) y las de cámara/UI, ya que no afectan la lógica interna de la conexión.

Aquí tienes la documentación de implementación:

# 📦 Dependencias del Helper `Fetch`

Para que la clase `Fetch` funcione correctamente manejando peticiones HTTP (GET, POST, Multipart) y serialización JSON, solo necesitas implementar **OkHttp** y **Gson**.

## 1. Configuración en `libs.versions.toml`

Agrega o mantén únicamente estas líneas en tu archivo de catálogo de versiones.

```toml
[versions]
# Cliente HTTP (Motor de conexiones)
okhttp = "4.11.0"
# Convertidor JSON (Serialización de objetos)
gson = "2.10.1"

[libraries]
# Definición de la librería OkHttp
okhttp = { module = "com.squareup.okhttp3:okhttp", version.ref = "okhttp" }
# Definición de la librería Gson
gson = { module = "com.google.code.gson:gson", version.ref = "gson" }

converter-gson = { module = "com.squareup.retrofit2:converter-gson", version.ref = "retrofit" }

```

---

## 2. Implementación en `build.gradle.kts` (Module: app)

En el bloque de dependencias de tu archivo de construcción, implementa las referencias definidas anteriormente.

```kotlin
dependencies {
    // ... otras dependencias de UI (appcompat, material, etc.)

    // ✅ Dependencias Requeridas para Fetch.java
    implementation(libs.okhttp)
    implementation(libs.gson)
    implementation(libs.converter.gson)
    
    // NOTA: Aunque tenías 'retrofit' en tu lista, tu clase Fetch 
    // NO lo utiliza internamente, por lo que no es necesario para este helper.
}
```

---

## 📝 Explicación de los Componentes

| Librería | Versión | Uso en `Fetch.java` |
| :--- | :--- | :--- |
| **OkHttp** | `4.11.0` | Es el motor principal. Proporciona las clases `OkHttpClient`, `Request`, `RequestBody`, `MultipartBody` y maneja la conexión a internet, los timeouts y los hilos en background (`enqueue`). |
| **Gson** | `2.10.1` | Se utiliza en los métodos genéricos (`<T>`) para transformar automáticamente los Strings JSON que vienen del servidor en Objetos Java (`gson.fromJson`) y viceversa (`gson.toJson`). |

### ⚠️ Requisito de Java
Asegúrate de mantener la compatibilidad con Java 8 (versión 1.8) o superior, ya que tu helper utiliza `Consumer<T>` y Lambdas. Tu configuración actual ya lo cumple:

```kotlin
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
```

--

### 2. Inicialización
```java
// Configura la URL base de tu API (No olvides la barra al final si es necesario)
Fetch.urlAPI = "https://api.tu-servidor.com";

// (Opcional) Si el usuario ya hizo login, establece el token
Fetch.setAuthToken("eyJhbGciOiJIUzI1NiIsInR...");
```

---

## 🚀 Uso de Métodos GET

### 1. GET Simple (Respuesta String)
Útil cuando no necesitas mapear la respuesta a un objeto o quieres inspeccionar el JSON crudo.

```java
Fetch.GET("/productos", (response) -> {
    if (response != null) {
        Log.d("API", "Respuesta cruda: " + response);
        // Recuerda: Esto corre en hilo secundario
    } else {
        Log.e("API", "Error en la petición");
    }
});
```

### 2. GET Tipado (Respuesta Objeto)
Convierte automáticamente la respuesta JSON a una clase Java usando Gson.

```java
// Supongamos que tienes una clase Producto.java
Fetch.GET("/productos/1", (producto) -> {
    if (producto != null) {
        Log.d("API", "Producto: " + producto.getNombre());
    }
}, Producto.class);
```

---

## 📤 Uso de Métodos POST

### 1. POST Simple (Enviar Objeto, Recibir String)
Envía un objeto serializado a JSON y recibe la respuesta cruda.

```java
LoginRequest loginData = new LoginRequest("usuario", "123456");

Fetch.POST(loginData, "/auth/login", (response) -> {
    if (response != null) {
        Log.d("API", "Login exitoso: " + response);
    }
});
```

### 2. POST Tipado (Enviar Objeto, Recibir Objeto)
Envía un objeto y espera que el servidor responda con un JSON que coincida con una clase específica.

```java
Usuario nuevoUsuario = new Usuario("Juan", "Perez");

Fetch.POST(nuevoUsuario, "/usuarios", (usuarioCreado) -> {
    if (usuarioCreado != null) {
        Log.d("API", "ID asignado: " + usuarioCreado.getId());
    }
}, Usuario.class);
```

---

## 📁 Subida de Archivos (Multipart)

Este método está diseñado para trabajar en conjunto con un selector de archivos que retorne una ruta absoluta (como `FileHelper`). Envía el archivo como `multipart/form-data`.

### Parámetros
1. **EndPoint**: La ruta del servidor (ej. `/upload`).
2. **KeyName**: El nombre del campo que el Backend espera (ej. `archivo`, `image`, `avatar`).
3. **FilePath**: La ruta absoluta del archivo local.
4. **Callback**: La respuesta del servidor.

### Ejemplo de Uso

```java
String rutaAbsoluta = "/data/user/0/com.app/cache/files_temp/foto.jpg";

Fetch.upload("/usuarios/avatar", "avatar", rutaAbsoluta, (response) -> {
    // Volver al hilo principal para actualizar UI
    runOnUiThread(() -> {
        if (response != null) {
            Toast.makeText(this, "Foto subida correctamente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al subir foto", Toast.LENGTH_SHORT).show();
        }
    });
});
```

---

## ⚠️ Notas Importantes sobre Hilos (Threading)

La clase `Fetch` utiliza `OkHttp.enqueue`, lo que significa que **todas las respuestas (`onResponse`) se ejecutan en un hilo secundario (Background Thread)**.

Si intentas modificar la interfaz de usuario (UI) directamente dentro del callback, la aplicación se cerrará con un error `CalledFromWrongThreadException`.

**Forma correcta de actualizar la UI:**

```java
Fetch.GET("/algo", (response) -> {
    // Hilo secundario (Procesar datos, guardar en BD, etc.)
    
    runOnUiThread(() -> {
        // Hilo principal (Actualizar TextViews, ocultar Loaders, etc.)
        miTextView.setText(response);
    });
});
```

---

## 🔒 Manejo de Autenticación

*   **`Fetch.setAuthToken(String token)`**: Guarda el token en memoria estática.
*   **`Fetch.getAuthToken()`**: Recupera el token actual.
*   Una vez establecido el token, **todas** las peticiones siguientes (GET, POST, Upload) añadirán automáticamente el header:
    `Authorization: Bearer <token>`

Para cerrar sesión (dejar de enviar el token):
```java
Fetch.setAuthToken(null);
```



Este método es muy útil para llamarlo en el onResume de tu Activity o antes de intentar un login.
code
```java
// Ejemplo en tu MainActivity

@Override
protected void onResume() {
super.onResume();

    // Verificar conexión con el servidor
    Fetch.checkHealth(isServerUp -> {
        runOnUiThread(() -> {
            if (isServerUp) {
                Log.d("API", "El servidor está ONLINE");
                // Poner un icono verde, habilitar botones, etc.
                findViewById(R.id.statusIndicator).setBackgroundColor(Color.GREEN);
            } else {
                Log.e("API", "El servidor no responde o no hay internet");
                // Mostrar alerta, deshabilitar botones, etc.
                Toast.makeText(this, "Sin conexión al servidor", Toast.LENGTH_LONG).show();
                findViewById(R.id.statusIndicator).setBackgroundColor(Color.RED);
            }
        });
    });
}

```
Explicación técnica de por qué funciona así:
client.newBuilder(): Reutiliza la configuración de tu cliente principal (pool de conexiones, interceptores) pero nos permite cambiar solo el tiempo de espera.
.head(): Si tu API es http://mi-api.com/api, una petición HEAD verifica que la máquina responda. Incluso si la ruta /api devuelve un error 404 o 401 (No autorizado), eso es bueno en este caso, porque confirma que el servidor existe y te está respondiendo "No te conozco" o "No encontrado", pero está vivo.
onFailure: Aquí es donde cae si el dominio no existe, si el usuario no tiene WiFi/Datos, o si el servidor está apagado/fuego bloqueando la conexión.


---

## 🔍 Selección Automática de Host (Discovery)

El método `findWorkingHost` permite probar una lista de direcciones URL secuencialmente hasta encontrar una que responda. Esto es ideal para entornos de desarrollo donde la IP local cambia frecuentemente, o para tener una configuración híbrida (Local vs Producción).

### Comportamiento
1.  Prueba las URLs en el orden del array.
2.  Usa un **timeout corto (2s)** para no bloquear la app.
3.  Si una conecta, asigna automáticamente `Fetch.urlAPI` y ejecuta el callback de éxito.
4.  Si ninguna conecta, ejecuta el callback de error.

### Ejemplo de Implementación

```java
// 1. Definir la lista de posibles servidores por prioridad
String[] posiblesServidores = {
    "http://192.168.80.23:8080",  // Prioridad 1: IP Red A
    "http://192.168.1.50:8080",   // Prioridad 2: IP Red B
    "https://api.miproyecto.com"  // Prioridad 3: Servidor Nube (Fallback)
};

// 2. Iniciar la búsqueda
Fetch.findWorkingHost(posiblesServidores, 
    (urlConectada) -> {
        // ✅ ÉXITO: Se encontró un servidor activo
        // Nota: El callback se ejecuta en hilo secundario, usa runOnUiThread para UI
        runOnUiThread(() -> {
            Log.d("API_DEBUG", "Conectado a: " + urlConectada);
            Toast.makeText(this, "Servidor establecido: " + urlConectada, Toast.LENGTH_SHORT).show();
            
            // Ya es seguro realizar peticiones
            hacerPeticionDePrueba();
        });
    }, 
    () -> {
        // ❌ ERROR: Ninguna URL respondió
        runOnUiThread(() -> {
            Log.e("API_DEBUG", "Ningún servidor respondió.");
            Toast.makeText(this, "Error de conexión: Verifique su red", Toast.LENGTH_LONG).show();
        });
    }
);
```

### ⚠️ Notas Importantes
*   **Actualización Automática:** No necesitas hacer `Fetch.urlAPI = url` manualmente dentro del éxito; el helper ya lo hace por ti.
*   **Localhost en Android:** Recuerda que `localhost` dentro del emulador se refiere al propio emulador. Para referirte a tu PC usa `10.0.2.2`.
*   **Hilos de UI:** Al igual que los otros métodos, los callbacks corren en background. Siempre usa `runOnUiThread` si vas a mostrar Toasts o cambiar vistas.