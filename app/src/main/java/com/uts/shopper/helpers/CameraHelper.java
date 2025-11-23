package com.uts.shopper.helpers;

import android.Manifest;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.FocusMeteringAction;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.MeteringPoint;
import androidx.camera.core.Preview;
import androidx.camera.core.SurfaceOrientedMeteringPointFactory;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;

public class CameraHelper {
    private ExecutorService cameraExecutor;
    private PreviewView previewView;
    private AppCompatActivity parent;
    private static final int CAMERA_PERMISSION_REQUEST = 100;

    public Consumer<String> onRawCode = code -> Log.d("AppCamera", "Valor detectado: " + code);

    public CameraHelper(PreviewView previewView, AppCompatActivity parent){
        this.previewView = previewView;
        this.cameraExecutor = Executors.newSingleThreadExecutor();
        this.parent = parent;
        int statusNowPermission = ContextCompat.checkSelfPermission(parent, Manifest.permission.CAMERA);
        if (statusNowPermission == PackageManager.PERMISSION_GRANTED) { startCamera(); return; }
        ActivityCompat.requestPermissions(parent, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length == 0) return;
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) return;
            startCamera();
        }
    }

    private void startCamera(){
        Log.d("AppCamera", "iniciando camara");
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(parent);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder().setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build();

                imageAnalysis.setAnalyzer(cameraExecutor, this::processImageProxy);

                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

                cameraProvider.unbindAll(); // libera cualquier cámara anterior

                Camera camera = cameraProvider.bindToLifecycle(parent, cameraSelector, preview, imageAnalysis);

                SurfaceOrientedMeteringPointFactory factory =  new SurfaceOrientedMeteringPointFactory(previewView.getWidth(), previewView.getHeight());
                MeteringPoint point = factory.createPoint(previewView.getWidth() / 2f, previewView.getHeight() / 2f);
                FocusMeteringAction action = new FocusMeteringAction.Builder(point).build();
                camera.getCameraControl().startFocusAndMetering(action);


            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(parent));
    }

    @OptIn(markerClass = ExperimentalGetImage.class)
    private void processImageProxy(ImageProxy imageProxy) {
        @androidx.camera.core.ExperimentalGetImage
        ImageProxy.PlaneProxy plane = imageProxy.getPlanes()[0];

        if (imageProxy.getImage() != null) {
            InputImage inputImage = InputImage.fromMediaImage(
                    imageProxy.getImage(),
                    imageProxy.getImageInfo().getRotationDegrees()
            );

            BarcodeScanner scanner = BarcodeScanning.getClient();
            scanner.process(inputImage)
                    .addOnSuccessListener(barcodes -> {
                        for (Barcode barcode : barcodes) {
                            String rawValue = barcode.getRawValue();
                            onRawCode.accept(rawValue);
                        }
                    })
                    .addOnFailureListener(e -> e.printStackTrace())
                    .addOnCompleteListener(task -> imageProxy.close()); // cerrar frame
        } else {
            imageProxy.close();
        }
    }

    public void onDestroy() {
        cameraExecutor.shutdown();
    }

}
