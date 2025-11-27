package com.uts.shopper;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.uts.shopper.Controllers.ControllerAdmin;
import com.uts.shopper.Models.ModelProducto;
import com.uts.shopper.helpers.FileHelper;

import java.util.concurrent.atomic.AtomicReference;

public class AdminPanelAddProductActivity extends AppCompatActivity {
    private  final ControllerAdmin controllerAdmin = new ControllerAdmin();
    private FileHelper fileHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.admin_panel_add_product_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageView btn_imagen = findViewById(R.id.loadImage);
        fileHelper = FileHelper.getInstance(this);
        AtomicReference<String> imagenUrl = new AtomicReference<>("");
        btn_imagen.setOnClickListener(e->{
            controllerAdmin.uploadImage(fileHelper, btn_imagen, this, imagenUrl::set);
        });

        TextView addToProduct = findViewById(R.id.addToProduct);
        addToProduct.setOnClickListener(e->{
            EditText tituloProducto = findViewById(R.id.tituloProducto);
            EditText descripcionProducto = findViewById(R.id.descripcionProducto);
            EditText precioUnitario = findViewById(R.id.PrecioUnitario);
            EditText costoEnvio = findViewById(R.id.costoEnvio);
            EditText cantidad = findViewById(R.id.cantidad);
            EditText calificacion = findViewById(R.id.calificacion);

            String text_tituloProducto = String.valueOf(tituloProducto.getText());
            String text_descripcionProducto = String.valueOf(descripcionProducto.getText());
            String text_precioUnitario = String.valueOf(precioUnitario.getText());
            String text_costoEnvio = String.valueOf(costoEnvio.getText());
            String text_cantidad = String.valueOf(cantidad.getText());
            String text_calificacion = String.valueOf(calificacion.getText());

            ModelProducto newProducto = new ModelProducto(
                    text_tituloProducto,
                    text_descripcionProducto,
                    imagenUrl.get(),
                    Double.parseDouble(text_precioUnitario),
                    Double.parseDouble(text_costoEnvio),
                    Integer.parseInt(text_cantidad),
                    Double.parseDouble(text_calificacion)
            );

            controllerAdmin.addProduct(newProducto);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        fileHelper.onRequestPermissionsResult(requestCode, grantResults);
    }
}