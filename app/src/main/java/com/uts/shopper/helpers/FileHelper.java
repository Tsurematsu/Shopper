package com.uts.shopper.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Consumer;

public class FileHelper {
    private Consumer<Boolean> permisoCallback = null;
    private String accionPendiente = null; // "image", "document", "file"
    private static final String TAG = "FileHelper";
    private static final int STORAGE_PERMISSION_REQUEST = 100;

    private static FileHelper instance = null;
    private AppCompatActivity parent = null;
    private String temporalDirectory = "files_temp";

    private Consumer<String> onResult = url -> Log.d(TAG, "Resultado: " + url);
    private Consumer<String> onError = error -> Log.e(TAG, "Error: " + error);

    private ActivityResultLauncher<Intent> imageLauncher;
    private ActivityResultLauncher<Intent> documentLauncher;
    private ActivityResultLauncher<Intent> fileLauncher;

    // Constructor privado para Singleton
    private FileHelper() {}

    /**
     * Obtiene la instancia del helper y la inicializa con la Activity
     */
    public static FileHelper getInstance(AppCompatActivity parent) {
        if (instance == null) {
            instance = new FileHelper();
        }
        instance.parent = parent;
        instance.initializeLaunchers();
        return instance;
    }

    /**
     * Inicializa los launchers para cada tipo de archivo
     */
    private void initializeLaunchers() {
        if (parent == null) return;

        // Launcher para imágenes
        imageLauncher = parent.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        if (uri != null) {
                            String rutaAbsoluta = copiarArchivoATemporal(uri, "imagen");
                            if (rutaAbsoluta != null) {
                                onResult.accept(rutaAbsoluta);
                            } else {
                                onError.accept("Error al copiar la imagen");
                            }
                        }
                    } else {
                        onError.accept("No se seleccionó ninguna imagen");
                    }
                }
        );

        // Launcher para documentos
        documentLauncher = parent.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        if (uri != null) {
                            String rutaAbsoluta = copiarArchivoATemporal(uri, "documento");
                            if (rutaAbsoluta != null) {
                                onResult.accept(rutaAbsoluta);
                            } else {
                                onError.accept("Error al copiar el documento");
                            }
                        }
                    } else {
                        onError.accept("No se seleccionó ningún documento");
                    }
                }
        );

        // Launcher para archivos generales
        fileLauncher = parent.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        if (uri != null) {
                            String rutaAbsoluta = copiarArchivoATemporal(uri, "archivo");
                            if (rutaAbsoluta != null) {
                                onResult.accept(rutaAbsoluta);
                            } else {
                                onError.accept("Error al copiar el archivo");
                            }
                        }
                    } else {
                        onError.accept("No se seleccionó ningún archivo");
                    }
                }
        );
    }

    /**
     * Solicita una imagen de la galería
     */
    public void requestImage(Consumer<String> callback) {
        requestImage(callback, error -> Log.e(TAG, error));
    }

    /**
     * Solicita una imagen con manejo de errores
     */
    public void requestImage(Consumer<String> callback, Consumer<String> errorCallback) {
        this.onResult = callback;
        this.onError = errorCallback;

        if (checkAndRequestPermission("image")) {
            openImagePicker();
        }
    }

    /**
     * Solicita un documento (PDF, Word, etc.)
     */
    public void requestDocument(Consumer<String> callback) {
        requestDocument(callback, error -> Log.e(TAG, error));
    }

    /**
     * Solicita un documento con manejo de errores
     */
    public void requestDocument(Consumer<String> callback, Consumer<String> errorCallback) {
        this.onResult = callback;
        this.onError = errorCallback;

        if (checkAndRequestPermission("document")) {
            openDocumentPicker();
        }
    }

    /**
     * Solicita cualquier tipo de archivo
     */
    public void requestFile(Consumer<String> callback) {
        requestFile(callback, error -> Log.e(TAG, error));
    }

    /**
     * Solicita un archivo con manejo de errores
     */
    public void requestFile(Consumer<String> callback, Consumer<String> errorCallback) {
        this.onResult = callback;
        this.onError = errorCallback;

        if (checkAndRequestPermission("file")) {
            openFilePicker();
        }
    }

    /**
     * Verifica y solicita permisos según la versión de Android
     */
    private boolean checkAndRequestPermission(String accion) {
        if (parent == null) {
            Log.e(TAG, "Parent activity es null");
            return false;
        }

        String permission;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ (API 33)
            permission = Manifest.permission.READ_MEDIA_IMAGES;
        } else {
            // Android 12 y anteriores
            permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        }

        if (ContextCompat.checkSelfPermission(parent, permission)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            accionPendiente = accion;
            ActivityCompat.requestPermissions(
                    parent,
                    new String[]{permission},
                    STORAGE_PERMISSION_REQUEST
            );
            return false;
        }
    }

    /**
     * Abre el selector de imágenes
     */
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imageLauncher.launch(intent);
    }

    /**
     * Abre el selector de documentos
     */
    private void openDocumentPicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        String[] mimeTypes = {"application/pdf", "application/msword",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "text/plain"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        documentLauncher.launch(intent);
    }

    /**
     * Abre el selector de archivos general
     */
    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        fileLauncher.launch(intent);
    }

    /**
     * Copia el archivo seleccionado a una carpeta temporal
     * @return Ruta absoluta del archivo copiado
     */
    private String copiarArchivoATemporal(Uri uri, String prefijo) {
        if (parent == null) return null;

        try {
            // Crear carpeta temporal
            File carpetaTemp = new File(parent.getFilesDir(), temporalDirectory);
            if (!carpetaTemp.exists()) {
                carpetaTemp.mkdirs();
            }

            // Obtener nombre del archivo o generar uno
            String nombreArchivo = prefijo + "_" + System.currentTimeMillis();
            String extension = obtenerExtension(uri);
            if (extension != null && !extension.isEmpty()) {
                nombreArchivo += "." + extension;
            }

            File archivoDestino = new File(carpetaTemp, nombreArchivo);

            // Copiar contenido
            InputStream inputStream = parent.getContentResolver().openInputStream(uri);
            if (inputStream == null) return null;

            OutputStream outputStream = new FileOutputStream(archivoDestino);

            byte[] buffer = new byte[8192];
            int bytesLeidos;
            while ((bytesLeidos = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesLeidos);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();

            Log.d(TAG, "Archivo copiado exitosamente: " + archivoDestino.getAbsolutePath());
            return archivoDestino.getAbsolutePath();

        } catch (Exception e) {
            Log.e(TAG, "Error al copiar archivo: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtiene la extensión del archivo desde la Uri
     */
    private String obtenerExtension(Uri uri) {
        if (parent == null) return null;

        String mimeType = parent.getContentResolver().getType(uri);
        if (mimeType != null) {
            switch (mimeType) {
                case "image/jpeg": return "jpg";
                case "image/png": return "png";
                case "image/gif": return "gif";
                case "image/webp": return "webp";
                case "application/pdf": return "pdf";
                case "application/msword": return "doc";
                case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
                    return "docx";
                case "text/plain": return "txt";
                default: return "bin";
            }
        }
        return null;
    }

    /**
     * Método para manejar el resultado de permisos
     * Debe ser llamado desde onRequestPermissionsResult de la Activity
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permiso concedido");
                // El usuario debe volver a hacer click en el botón
                // O puedes almacenar la última acción solicitada y ejecutarla aquí

                if (permisoCallback != null) {
                    permisoCallback.accept(true);
                    permisoCallback = null;
                }

                // Ejecutar la acción pendiente
                if (accionPendiente != null) {
                    switch (accionPendiente) {
                        case "image":
                            openImagePicker();
                            break;
                        case "document":
                            openDocumentPicker();
                            break;
                        case "file":
                            openFilePicker();
                            break;
                    }
                    accionPendiente = null;
                }
            } else {
                Log.e(TAG, "Permiso denegado");

                if (permisoCallback != null) {
                    permisoCallback.accept(false);
                    permisoCallback = null;
                }


                onError.accept("Permiso denegado por el usuario");
            }
        }
    }

    /**
     * Limpia todos los archivos temporales
     */
    public void limpiarArchivosTemporales() {
        if (parent == null) return;

        File carpetaTemp = new File(parent.getFilesDir(), temporalDirectory);
        if (carpetaTemp.exists() && carpetaTemp.isDirectory()) {
            File[] archivos = carpetaTemp.listFiles();
            if (archivos != null) {
                for (File archivo : archivos) {
                    boolean eliminado = archivo.delete();
                    Log.d(TAG, "Archivo eliminado: " + archivo.getName() + " - " + eliminado);
                }
            }
        }
    }

    /**
     * Elimina un archivo específico por ruta
     */
    public boolean eliminarArchivo(String rutaAbsoluta) {
        File archivo = new File(rutaAbsoluta);
        if (archivo.exists()) {
            return archivo.delete();
        }
        return false;
    }

    /**
     * Obtiene la carpeta temporal
     */
    public String getTemporalDirectory() {
        if (parent == null) return null;
        return new File(parent.getFilesDir(), temporalDirectory).getAbsolutePath();
    }

    /**
     * Configura el nombre de la carpeta temporal
     */
    public void setTemporalDirectory(String directoryName) {
        this.temporalDirectory = directoryName;
    }

    /**
     * Solicita permisos de almacenamiento manualmente
     * Útil para pedir permisos antes de intentar seleccionar archivos
     */
    public void solicitarPermisos(Consumer<Boolean> callback) {
        if (parent == null) {
            Log.e(TAG, "Parent activity es null");
            callback.accept(false);
            return;
        }

        String permission;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permission = Manifest.permission.READ_MEDIA_IMAGES;
        } else {
            permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        }

        // Verificar si ya tiene el permiso
        if (ContextCompat.checkSelfPermission(parent, permission)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permiso ya concedido");
            callback.accept(true);
            return;
        }

        // Guardar callback para después
        Consumer<Boolean> permisoCallback = callback;

        // Solicitar permiso
        ActivityCompat.requestPermissions(
                parent,
                new String[]{permission},
                STORAGE_PERMISSION_REQUEST
        );
    }
}