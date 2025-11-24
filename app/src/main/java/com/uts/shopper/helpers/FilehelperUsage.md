# Documentación FileHelper - Ejemplos de Uso

## 1. Inicialización Básica

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    // Obtener instancia del helper
    FileHelper fileHelper = FileHelper.getInstance(this);
}
```

## 2. Configurar Carpeta Temporal Personalizada

```java
// Por defecto usa "files_temp", pero puedes cambiarlo
fileHelper.setTemporalDirectory("mis_archivos");
```

## 3. Solicitar Permisos Manualmente

```java
// Solicitar permisos antes de hacer cualquier operación
fileHelper.solicitarPermisos(concedido -> {
    if (concedido) {
        Log.d("TAG", "Usuario concedió permisos");
        // Ahora puedes pedir archivos
    } else {
        Toast.makeText(this, "Se necesitan permisos", Toast.LENGTH_SHORT).show();
    }
});
```

## 4. Seleccionar Imagen (Básico)

```java
Button btnImagen = findViewById(R.id.btnImagen);
btnImagen.setOnClickListener(v -> {
    fileHelper.requestImage(rutaAbsoluta -> {
        // Éxito - tienes la ruta del archivo
        Log.d("TAG", "Imagen guardada en: " + rutaAbsoluta);
    });
});
```

## 5. Seleccionar Imagen con Manejo de Errores

```java
fileHelper.requestImage(
    rutaAbsoluta -> {
        // Éxito
        Toast.makeText(this, "Imagen seleccionada", Toast.LENGTH_SHORT).show();
        Log.d("TAG", "Ruta: " + rutaAbsoluta);
    },
    error -> {
        // Error
        Toast.makeText(this, "Error: " + error, Toast.LENGTH_LONG).show();
        Log.e("TAG", "Error al seleccionar imagen: " + error);
    }
);
```

## 6. Mostrar Imagen Seleccionada en ImageView

```java
ImageView imageView = findViewById(R.id.imageView);

fileHelper.requestImage(rutaAbsoluta -> {
    File archivo = new File(rutaAbsoluta);
    if (archivo.exists()) {
        // Mostrar la imagen
        imageView.setImageURI(android.net.Uri.fromFile(archivo));
    }
});
```

## 7. Seleccionar Documento (PDF, Word, etc.)

```java
Button btnDocumento = findViewById(R.id.btnDocumento);
btnDocumento.setOnClickListener(v -> {
    fileHelper.requestDocument(
        rutaAbsoluta -> {
            Toast.makeText(this, "Documento seleccionado", Toast.LENGTH_SHORT).show();
            
            // Obtener información del archivo
            File archivo = new File(rutaAbsoluta);
            long tamañoKB = archivo.length() / 1024;
            String nombre = archivo.getName();
            
            Log.d("TAG", "Nombre: " + nombre);
            Log.d("TAG", "Tamaño: " + tamañoKB + " KB");
        },
        error -> {
            Toast.makeText(this, "Error: " + error, Toast.LENGTH_LONG).show();
        }
    );
});
```

## 8. Seleccionar Cualquier Tipo de Archivo

```java
Button btnArchivo = findViewById(R.id.btnArchivo);
btnArchivo.setOnClickListener(v -> {
    fileHelper.requestFile(
        rutaAbsoluta -> {
            File archivo = new File(rutaAbsoluta);
            String extension = obtenerExtension(archivo.getName());
            
            Toast.makeText(this, "Archivo: " + extension, Toast.LENGTH_SHORT).show();
        },
        error -> {
            Log.e("TAG", "Error: " + error);
        }
    );
});

// Método auxiliar para obtener extensión
private String obtenerExtension(String nombreArchivo) {
    int lastDot = nombreArchivo.lastIndexOf(".");
    if (lastDot != -1) {
        return nombreArchivo.substring(lastDot + 1);
    }
    return "";
}
```

## 9. Obtener Información de la Carpeta Temporal

```java
Button btnInfo = findViewById(R.id.btnInfo);
btnInfo.setOnClickListener(v -> {
    String carpetaTemp = fileHelper.getTemporalDirectory();
    
    File carpeta = new File(carpetaTemp);
    if (carpeta.exists()) {
        File[] archivos = carpeta.listFiles();
        int cantidad = archivos != null ? archivos.length : 0;
        
        Toast.makeText(this, 
            "Archivos temporales: " + cantidad, 
            Toast.LENGTH_SHORT).show();
    }
});
```

## 10. Limpiar Todos los Archivos Temporales

```java
Button btnLimpiar = findViewById(R.id.btnLimpiar);
btnLimpiar.setOnClickListener(v -> {
    fileHelper.limpiarArchivosTemporales();
    Toast.makeText(this, "Archivos eliminados", Toast.LENGTH_SHORT).show();
});
```

## 11. Eliminar un Archivo Específico

```java
String rutaArchivo = "/ruta/del/archivo.jpg";

boolean eliminado = fileHelper.eliminarArchivo(rutaArchivo);
if (eliminado) {
    Toast.makeText(this, "Archivo eliminado", Toast.LENGTH_SHORT).show();
} else {
    Toast.makeText(this, "No se pudo eliminar", Toast.LENGTH_SHORT).show();
}
```

## 12. Manejo de Permisos (Requerido)

```java
@Override
public void onRequestPermissionsResult(int requestCode, 
                                       @NonNull String[] permissions, 
                                       @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    
    // Delegar al helper para que maneje los permisos
    fileHelper.onRequestPermissionsResult(requestCode, grantResults);
}
```

## 13. Ejemplo Completo: Seleccionar y Subir Imagen

```java
Button btnSubir = findViewById(R.id.btnSubir);
btnSubir.setOnClickListener(v -> {
    fileHelper.requestImage(
        rutaAbsoluta -> {
            // Mostrar preview
            ImageView preview = findViewById(R.id.imagePreview);
            preview.setImageURI(android.net.Uri.fromFile(new File(rutaAbsoluta)));
            
            // Simular subida
            subirImagenAlServidor(rutaAbsoluta);
        },
        error -> {
            Toast.makeText(this, "Error al seleccionar", Toast.LENGTH_SHORT).show();
        }
    );
});

private void subirImagenAlServidor(String rutaLocal) {
    File archivo = new File(rutaLocal);
    
    // Aquí irían tus llamadas de red (Retrofit, etc.)
    Log.d("TAG", "Subiendo archivo: " + archivo.getName());
    
    // Después de subir, eliminar el temporal
    fileHelper.eliminarArchivo(rutaLocal);
}
```

## 14. Validar Tamaño de Archivo Antes de Procesar

```java
fileHelper.requestImage(rutaAbsoluta -> {
    File archivo = new File(rutaAbsoluta);
    long tamañoMB = archivo.length() / (1024 * 1024);
    
    if (tamañoMB > 5) {
        Toast.makeText(this, 
            "La imagen es muy grande (max 5MB)", 
            Toast.LENGTH_LONG).show();
        
        // Eliminar archivo
        fileHelper.eliminarArchivo(rutaAbsoluta);
    } else {
        // Procesar imagen
        procesarImagen(rutaAbsoluta);
    }
});
```

## 15. Ejemplo con Múltiples Imágenes

```java
private List<String> imagenesSeleccionadas = new ArrayList<>();

Button btnAgregarImagen = findViewById(R.id.btnAgregarImagen);
btnAgregarImagen.setOnClickListener(v -> {
    fileHelper.requestImage(rutaAbsoluta -> {
        imagenesSeleccionadas.add(rutaAbsoluta);
        
        Toast.makeText(this, 
            "Imágenes: " + imagenesSeleccionadas.size(), 
            Toast.LENGTH_SHORT).show();
    });
});

Button btnSubirTodas = findViewById(R.id.btnSubirTodas);
btnSubirTodas.setOnClickListener(v -> {
    for (String ruta : imagenesSeleccionadas) {
        subirImagenAlServidor(ruta);
    }
    imagenesSeleccionadas.clear();
});
```

## 16. Limpiar al Cerrar la App

```java
@Override
protected void onDestroy() {
    super.onDestroy();
    
    // Opcional: limpiar archivos temporales al cerrar
    if (fileHelper != null) {
        fileHelper.limpiarArchivosTemporales();
    }
}
```

## 17. Permisos en AndroidManifest.xml (Requerido)

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android">
    
    <!-- Para Android 12 y anteriores -->
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" 
        android:maxSdkVersion="32" />
    
    <!-- Para Android 13+ -->
    <uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />
    
    <application>
        <!-- ... -->
    </application>
</manifest>
```

---

## Notas Importantes

- **Siempre** llama a `fileHelper.onRequestPermissionsResult()` en tu Activity
- Los archivos se guardan en el almacenamiento interno de la app
- Recuerda limpiar archivos temporales cuando ya no los necesites
- La carpeta temporal por defecto es `files_temp`

¿Necesitas algún ejemplo adicional o tienes dudas sobre alguno?