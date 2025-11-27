package com.uts.shopper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.uts.shopper.Controllers.ControllerAdmin;
import com.uts.shopper.helpers.Fetch;
import com.uts.shopper.helpers.FileHelper;

import java.util.Map;

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
        Gson gson = new Gson();
        ImageView btn_imagen = findViewById(R.id.loadImage);
        fileHelper = FileHelper.getInstance(this);

        btn_imagen.setOnClickListener(e->{
            fileHelper.requestImage(path->{
                Fetch.upload("/api/upload", "archivo", path, response->{
                    Map<String, Object> map = gson.fromJson(json, new TypeToken<Map<String, Object>>(){}.getType());
                    Log.d("APP_API_DEBUG", "Response server ->" + response);
                });
                runOnUiThread(() -> {
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    btn_imagen.setImageBitmap(bitmap);

                    /*
                    Glide.with(this)
                            .load(url)
                            .placeholder(R.drawable.icon_download)
                            .error(R.drawable.icon_download)
                            .centerCrop()
                            .into(imgProducto);
                     */
                });
                Log.d("APP_API_DEBUG", path);
            });
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        fileHelper.onRequestPermissionsResult(requestCode, grantResults);
    }
}