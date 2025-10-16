package uninorte.edu.py.myappenvioarchivosservidor.network;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("f444df44-ecf8-4315-b594-6b44cd55d93d")
    Call<ResponseBody> subirBase64(@Body RequestBody body);
}
