package com.uts.shopper;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.uts.shopper.Models.Producto;

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

        Producto producto = (Producto) getIntent().getSerializableExtra("PRODUCTO", Producto.class);
        if (producto != null) {
            ((TextView) findViewById(R.id.tileProduct)).setText(producto.titulo);
            Glide.with(this)
                    .load(producto.imagenUrl)
                    .placeholder(R.drawable.test_puente_h)
                    .error(R.drawable.test_puente_h)
                    .centerCrop()
                    .into(((ImageView) findViewById(R.id.imageProduct)));

            //System.out.println(usuarioRecibido.getNombre());
        }
    }
}