package com.uts.shopper;

import android.os.Bundle;
import android.util.Log;
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

    private class FormInputs{
        public EditText tituloProducto;
        public EditText descripcionProducto;
        public EditText precioUnitario;
        public EditText costoEnvio;
        public EditText cantidad;
        public EditText calificacion;
        public ImageView btn_imagen;
        public FormInputs(){
            this.tituloProducto = findViewById(R.id.tituloProducto);
            this.descripcionProducto = findViewById(R.id.descripcionProducto);
            this.precioUnitario = findViewById(R.id.precioUnitario);
            this.costoEnvio = findViewById(R.id.costoEnvio);
            this.cantidad = findViewById(R.id.cantidad);
            this.calificacion = findViewById(R.id.calificacion);
            this.btn_imagen = findViewById(R.id.loadImage);
        }
    }
    private FormInputs formInputs;
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

        formInputs = new FormInputs();

        if (getIntent().hasExtra("ID_PRODUCTO")){
            String idProd = (String) getIntent().getStringExtra("ID_PRODUCTO");
            onUpdate(idProd);
        }else{
            onCreate();
        }

        TextView cancelarButton = findViewById(R.id.cancelarButton);
        ImageView volver = findViewById(R.id.volver);
        cancelarButton.setOnClickListener(e->this.finish());
        volver.setOnClickListener(e->this.finish());

    }
    private AtomicReference<String> uploadImage(){
        fileHelper = FileHelper.getInstance(this);
        AtomicReference<String> imagenUrl = new AtomicReference<>("");
        formInputs.btn_imagen.setOnClickListener(e->{
            controllerAdmin.uploadImage(fileHelper, formInputs.btn_imagen, this, imagenUrl::set);
        });
        return imagenUrl;
    }
    private ModelProducto getDataForm(AtomicReference<String> imagenUrl){
        String text_tituloProducto = String.valueOf(formInputs.tituloProducto.getText());
        String text_descripcionProducto = String.valueOf(formInputs.descripcionProducto.getText());
        String text_precioUnitario = String.valueOf(formInputs.precioUnitario.getText());
        String text_costoEnvio = String.valueOf(formInputs.costoEnvio.getText());
        String text_cantidad = String.valueOf(formInputs.cantidad.getText());
        String text_calificacion = String.valueOf(formInputs.calificacion.getText());
        return new ModelProducto(
                text_tituloProducto,
                text_descripcionProducto,
                imagenUrl.get(),
                Double.parseDouble(text_precioUnitario),
                Double.parseDouble(text_costoEnvio),
                Integer.parseInt(text_cantidad),
                Double.parseDouble(text_calificacion)
        );

    }

    private void onUpdate(String id){
        TextView titlePanelAddProduct = findViewById(R.id.titlePanelAddProduct);
        titlePanelAddProduct.setText("Admin Edit product");


        AtomicReference<String> imagenUrl = new AtomicReference<>("");

        controllerAdmin.getProduct(id, producto->{
            runOnUiThread(()->{
                try {
                    formInputs.tituloProducto.setText(producto.titulo);
                    formInputs.descripcionProducto.setText(producto.descripcion);
                    controllerAdmin.setImageUrl(producto.imagenUrl, formInputs.btn_imagen, AdminPanelAddProductActivity.this);
                    formInputs.precioUnitario.setText(String.valueOf(producto.precioUnitairo));
                    formInputs.costoEnvio.setText(String.valueOf(producto.costoEnvio));
                    formInputs.cantidad.setText(String.valueOf(producto.cantidad));
                    formInputs.calificacion.setText(String.valueOf(producto.calificacion));
                    imagenUrl.set(producto.imagenUrl);
                } catch (Exception e) {
                    Log.e("APP_API_DEBUG", "Error Admin panel add product (Edit)\n" + e.getMessage());
                }
            });
        });

        TextView addToProduct = findViewById(R.id.addToProduct);
        addToProduct.setText("Aplicar cambios");
        addToProduct.setOnClickListener(e->{
            ModelProducto updateProducto = getDataForm(imagenUrl);
            controllerAdmin.updateProduct(id, updateProducto, this::finish);
        });

    }

    private void onCreate(){
        AtomicReference<String> imagenUrl = uploadImage();
        TextView addToProduct = findViewById(R.id.addToProduct);
        addToProduct.setOnClickListener(e->{
            ModelProducto newProducto = getDataForm(imagenUrl);
            controllerAdmin.addProduct(newProducto, this::finish);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        fileHelper.onRequestPermissionsResult(requestCode, grantResults);
    }
}