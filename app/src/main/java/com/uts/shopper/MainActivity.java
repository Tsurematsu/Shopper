package com.uts.shopper;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private LinearLayout contenedorItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
//        setContentView(R.layout.home_view_product_activity);
        setContentView(R.layout.home_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        /*
        contenedorItems = findViewById(R.id.contenedorItems);

        // Agregar items dinámicamente
        agregarItem("Producto 1", "Descripción del producto 1");
        agregarItem("Producto 2", "Descripción del producto 2");
        agregarItem("Producto 3", "Descripción del producto 3");
        */
    }


    /*
    private void agregarItem(String titulo, String descripcion) {
        // Inflar el layout del item
        View itemView = getLayoutInflater().inflate(R.layout.item_producto, contenedorItems, false);

        // Obtener referencias a los elementos
        TextView tvTitulo = itemView.findViewById(R.id.tvTitulo);
        TextView tvDescripcion = itemView.findViewById(R.id.tvDescripcion);

        // Configurar los datos
        tvTitulo.setText(titulo);
        tvDescripcion.setText(descripcion);

        // Agregar click listener si lo necesitas
        itemView.setOnClickListener(v -> {
            Toast.makeText(this, "Clicked: " + titulo, Toast.LENGTH_SHORT).show();
        });

        // Agregar el item al contenedor
        contenedorItems.addView(itemView);
    }
     */

}