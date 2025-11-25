package com.uts.shopper;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.uts.shopper.Models.Producto;
import com.uts.shopper.helpers.TextHelper;

public class HomeViewProductActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.home_view_product_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Manejar el botón atrás moderno
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
                overridePendingTransition(0, 0);
            }
        });
        ImageView volver = findViewById(R.id.volver);
        volver.setOnClickListener(v->{
            finish();
            overridePendingTransition(0, 0);
        });

        Producto producto = (Producto) getIntent().getSerializableExtra("PRODUCTO", Producto.class);
        if (producto != null) {
           try {
               ((TextView) findViewById(R.id.tileProduct)).setText(producto.titulo);
               Glide.with(this)
                       .load(producto.imagenUrl)
                       .placeholder(R.drawable.test_puente_h)
                       .error(R.drawable.test_puente_h)
                       .centerCrop()
                       .into(((ImageView) findViewById(R.id.imageProduct)));
               String formatPricing = "$" + TextHelper.formatearNumero(String.valueOf(producto.precioUnitairo));
               ((TextView) findViewById(R.id.precioUnitario)).setText(formatPricing);
               ((TextView) findViewById(R.id.calificacion)).setText(String.valueOf(producto.calificacion));
               ((TextView) findViewById(R.id.textDescription)).setText(producto.descripcion);

           } catch (Exception e) {
               Log.d("APP_API_DEBUG", e.getMessage());
           }

        }
    }
}