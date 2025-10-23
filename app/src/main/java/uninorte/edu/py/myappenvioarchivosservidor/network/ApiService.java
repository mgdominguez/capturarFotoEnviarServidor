package uninorte.edu.py.myappenvioarchivosservidor.network;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
    @POST("a3390aae-ec03-48b3-a3aa-0f895421777f")
    Call<ResponseBody> subirBase64(@Body RequestBody body);

    @Multipart
    @POST("a3390aae-ec03-48b3-a3aa-0f895421777f")
    Call<ResponseBody> subirImagen(@Part MultipartBody.Part file);
}
