package com.uts.shopper;

import android.Manifest;
import android.app.DownloadManager;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PayFacturaction extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.pay_facturaction_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Solicitar permiso si es necesario
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {

            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1
                );
            }
        }

        WebView webView = findViewById(R.id.webview);
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                // Crear la solicitud de descarga
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

                // Obtener el nombre del archivo
                String filename = URLUtil.guessFileName(url, contentDisposition, mimetype);

                // Permitir que se descargue con metered networks (datos móviles)
                request.setAllowedOverMetered(true);
                request.setAllowedOverRoaming(true);

                // Mostrar notificación durante y después de la descarga
                request.setNotificationVisibility(
                        DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
                );

                // IMPORTANTE: Guardar en la carpeta Downloads pública
                request.setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOWNLOADS,
                        filename
                );
                request.setTitle(filename);
                request.setDescription("Descargando archivo");
                request.setMimeType(mimetype);

                // Obtener el DownloadManager y encolar la descarga
                DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                downloadManager.enqueue(request);

                // Mostrar mensaje al usuario
                Toast.makeText(getApplicationContext(),
                        "Descargando: " + filename,
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}