

# Documentación Helper `Fetch`

El helper `Fetch` es una clase utilitaria estática diseñada para simplificar las peticiones HTTP (REST API) en Android utilizando **OkHttp** y **Gson**. Gestiona automáticamente la serialización de objetos, la autenticación mediante Bearer Token y la subida de archivos multipart.

## 📋 Configuración Inicial

Antes de realizar cualquier petición, debes configurar la URL base y, opcionalmente, el token de autenticación. Se recomienda hacer esto en el `onCreate` de tu `Application` o tu `MainActivity`.

### 1. Permisos Requeridos
Asegúrate de tener el permiso de internet en tu `AndroidManifest.xml`:
```xml
<uses-permission android:name="android.permission.INTERNET" />
```

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