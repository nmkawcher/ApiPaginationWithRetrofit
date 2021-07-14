package kawcher.websheba.apipagination;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {

    NestedScrollView nestedScrollView;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    ArrayList<MainData> dataArrayList = new ArrayList<MainData>();
    MainAdapter adapter;

    int page = 1, limit = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nestedScrollView = findViewById(R.id.scroll_view);
        recyclerView = findViewById(R.id.rv);
        progressBar = findViewById(R.id.progressbar);


        adapter = new MainAdapter(dataArrayList, MainActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        recyclerView.setAdapter(adapter);

        //create get data method

        getData(page, limit);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY==v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()){
                    //when reach last item position
                    //increase page size
                    page++;
                    progressBar.setVisibility(View.VISIBLE);
                    getData(page,limit);
                }
            }
        });
    }

    private void getData(int page, int limit) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://picsum.photos/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        MainInterface mainInterface=retrofit.create(MainInterface.class);

        Call<String>call=mainInterface.STRING_CALL(page,limit);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //check condition
                if(response.isSuccessful()&&response.body()!=null){
                    //when response is successful and not empty
                    //hide progressbar
                    progressBar.setVisibility(View.GONE);
                    //initialize json array
                    try {
                        JSONArray jsonArray=new JSONArray(response.body());
                        parseResult(jsonArray);

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                  }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }

    private void parseResult(JSONArray jsonArray) {

        for (int i=0;i<jsonArray.length();i++){

            try {
                JSONObject object=jsonArray.getJSONObject(i);
                //
                MainData data=new MainData();
                data.setImage(object.getString("download_url"));

                //set name
                data.setName(object.getString("author"));
                dataArrayList.add(data);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            //initialize adapter
            adapter=new MainAdapter(dataArrayList,MainActivity.this);

            recyclerView.setAdapter(adapter);
        }

    }
}