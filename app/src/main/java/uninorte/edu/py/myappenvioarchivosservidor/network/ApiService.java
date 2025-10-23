package uninorte.edu.py.myappenvioarchivosservidor.network;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("a3390aae-ec03-48b3-a3aa-0f895421777f")
    Call<ResponseBody> subirBase64(@Body RequestBody body);
}
