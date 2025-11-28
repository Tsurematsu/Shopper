package com.uts.shopper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.uts.shopper.Acopladores.AcopladorAdmin;
import com.uts.shopper.App.AppSessionUserManager;
import com.uts.shopper.Components.ComponentNavbar;
import com.uts.shopper.Controllers.ControllerAdmin;
import com.uts.shopper.Models.ModelProducto;
import com.uts.shopper.Models.ParamActionContinue;

import java.util.function.Consumer;

public class AdminPanelActivity extends AppCompatActivity {
    AppSessionUserManager appSessionUserManager;
    private final ControllerAdmin controllerAdmin = new ControllerAdmin();
    private final AcopladorAdmin acopladorAdmin = new AcopladorAdmin(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        appSessionUserManager = new AppSessionUserManager(this);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.admin_panel_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ComponentNavbar.apply(this, "mas");

        ImageView adminButton = findViewById(R.id.addproduct);
        adminButton.setOnClickListener(e->{
            Intent intent = new Intent(AdminPanelActivity.this, AdminPanelAddProductActivity.class);
            startActivity(intent);
        });

        ImageView logout = findViewById(R.id.logout);
        logout.setOnClickListener(e->{
            appSessionUserManager.clearUserSession();
            finish();
        });
        ImageView volver = findViewById(R.id.volver);
        volver.setOnClickListener(e->{
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        controllerAdmin.getProductos(listaProductos->{
            Consumer<ModelProducto> ActionClick = producto->{

            };
            Consumer<ParamActionContinue> ActionDelete = actionContinue->{
                new AlertDialog.Builder(this)
                        .setTitle("Confirmar acción")
                        .setMessage("¿Estás seguro de que deseas continuar?")
                        .setPositiveButton("Sí", (dialog, which) -> {
                            controllerAdmin.removeProduct(actionContinue.producto.id, ()->{
                                runOnUiThread(() -> {
                                    Toast.makeText(this, "Elemento eliminado", Toast.LENGTH_SHORT).show();
                                    actionContinue.continueAction.run();
                                });
                            });
                        })
                        .setNegativeButton("No", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();
            };
            acopladorAdmin.loadProducts(listaProductos, ActionClick, ActionDelete);
        });
    }


}