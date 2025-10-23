package uninorte.edu.py.myappenvioarchivosservidor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Button btnBase64, btnMultipart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnBase64 = findViewById(R.id.btnBase64);
        btnMultipart = findViewById(R.id.btnMultipart);

        btnBase64.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, Base64UploadActivity.class));
        });

        btnMultipart.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, MultipartUploadActivity.class));
        });


    }
}