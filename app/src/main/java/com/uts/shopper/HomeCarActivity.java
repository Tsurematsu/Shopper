package com.uts.shopper;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.uts.shopper.Acopladores.AcopladorCar;
import com.uts.shopper.Acopladores.AcopladorMain;
import com.uts.shopper.App.AppSessionCarritoManager;
import com.uts.shopper.Models.ModelCarrito;
import com.uts.shopper.helpers.TextHelper;

import java.util.ArrayList;
import java.util.function.Consumer;

public class HomeCarActivity extends AppCompatActivity {
    AppSessionCarritoManager appSessionCarritoManager = new AppSessionCarritoManager(this);
    private final AcopladorCar acopladorCar = new AcopladorCar(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.home_car_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            //ArrayList<ModelCarrito> carritoList = appSessionCarritoManager.getCarritoList();
            ArrayList<ModelCarrito> carritoList = new ArrayList<>();
            ModelCarrito item1 = new ModelCarrito(
                    "0",
                    "Arbol De Navidad Home Sale 2.10 Cm Robusto 1250 Ramas Verde",
                    3,
                    11000,
                    3000,
                    "https://http2.mlstatic.com/D_NQ_NP_2X_985872-MLA97585537885_112025-F.webp"
            );

            carritoList.add(item1);

            TextView carLayoutNumProductos = findViewById(R.id.carLayoutNumProductos);
            String template_carLayoutNumProductos = String.valueOf(carLayoutNumProductos.getText());

            TextView text_sumSubProductos = findViewById(R.id.text_sumSubProductos);

            TextView carLayoutNumEnvio = findViewById(R.id.carLayoutNumEnvio);
            String template_carLayoutNumEnvio = String.valueOf(carLayoutNumEnvio.getText());
            Runnable loadPricingData = ()->{

                String count_items = String.valueOf(carritoList.size());
                String typed_carLayoutNumProductos = template_carLayoutNumProductos.replaceAll("x", count_items);
                carLayoutNumProductos.setText(typed_carLayoutNumProductos);

                double sumaProductos_subtotal = 0.0;
                double sumaProductos_envios = 0.0;
                for (ModelCarrito producto : carritoList) {
                    sumaProductos_subtotal += (producto.precioUnitairo * producto.cantidadSeleccionada);
                    sumaProductos_envios += producto.costoEnvio;
                }

                String typed_text_sumSubProductos = "$" + TextHelper.formatearNumero(String.valueOf(sumaProductos_subtotal));
                text_sumSubProductos.setText(typed_text_sumSubProductos);


            };

            Consumer<ModelCarrito> ActionClick = producto->{
                Log.d("APP_API_DEBUG", "-> click");
            };

            Consumer<ModelCarrito> RemoveClick = producto->{
                Log.d("APP_API_DEBUG", "-> removed");
            };

            Consumer<ArrayList<ModelCarrito>> UpdateElement = productos->{
                loadPricingData.run();
            };

            acopladorCar.AddLayoutCarrito(carritoList, ActionClick, RemoveClick, UpdateElement);
            loadPricingData.run();
        } catch (Exception e) {
            Log.e("APP_API_DEBUG", e.getMessage());
        }
    }
}