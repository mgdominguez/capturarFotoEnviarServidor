package uninorte.edu.py.myappenvioarchivosservidor;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uninorte.edu.py.myappenvioarchivosservidor.network.ApiService;
import uninorte.edu.py.myappenvioarchivosservidor.network.RetrofitClient;

public class MultipartUploadActivity extends AppCompatActivity {

    // Código de solicitud para el permiso de la cámara.
    private static final int REQUEST_CAMERA_PERMISSION = 3;
    // Vista para mostrar la imagen capturada.
    private ImageView imageView;
    // URI que apunta a la imagen capturada.
    private Uri fotoUri;
    // Archivo donde se guardará la imagen capturada.
    private File archivo;
    // Launcher para la actividad de la cámara que reemplaza al deprecated onActivityResult.
    private ActivityResultLauncher<Intent> cameraLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multipart_upload);

        imageView = findViewById(R.id.imageView);
        Button btnCamara = findViewById(R.id.btnCamara);
        Button btnEnviar = findViewById(R.id.btnEnviar);

        btnCamara.setOnClickListener(v -> requestCameraPermission());
        btnEnviar.setOnClickListener(v -> enviarFotoMultipart());

        // Inicializa el ActivityResultLauncher para manejar el resultado de la cámara.
        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                // Si la foto se tomó correctamente, la muestra en el ImageView.
                imageView.setImageURI(fotoUri);
            }
        });
    }

    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            abrirCamara();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                abrirCamara();
            } else {
                Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void abrirCamara() {
        // Define el archivo donde se guardará la foto.
        archivo = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "foto_multipart.jpg");
        // Obtiene la URI para el archivo, necesaria para que otras apps accedan a él de forma segura.
        fotoUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", archivo);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Pasa la URI del archivo a la cámara para que guarde la foto ahí.
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri);
        // Inicia la actividad de la cámara.
        cameraLauncher.launch(intent);
    }


    private void enviarFotoMultipart() {
        if (archivo == null || !archivo.exists()) {
            Toast.makeText(this, "Primero captura una foto", Toast.LENGTH_LONG).show();
            return;
        }

        // Crea un RequestBody a partir del archivo de imagen.
        RequestBody requestFile = RequestBody.create(archivo, MediaType.parse("image/jpeg"));
        // Crea una parte MultipartBody.Part a partir del RequestBody. "file" es el nombre del campo en el formulario.
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", archivo.getName(), requestFile);

        // Obtiene una instancia del servicio de la API con Retrofit.
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        // Realiza la llamada asíncrona para subir la imagen.
        apiService.subirImagen(body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(MultipartUploadActivity.this, "Foto enviada (Multipart)", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MultipartUploadActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}