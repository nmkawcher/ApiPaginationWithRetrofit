package kawcher.websheba.apipagination;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {


    private ArrayList<MainData>list;
    private Activity activity;


    public MainAdapter(ArrayList<MainData> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(activity).inflate(R.layout.list_row,parent,false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  MainAdapter.ViewHolder holder, int position) {
MainData data=list.get(position);
        Glide.with(activity).load(data.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.image);

        holder.textView.setText(data.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
      ImageView image;
      TextView textView;
        public ViewHolder(@NonNull  View itemView) {
            super(itemView);

            image=itemView.findViewById(R.id.image);
            textView=itemView.findViewById(R.id.textview);

        }
    }
}
