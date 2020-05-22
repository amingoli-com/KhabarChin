package ir.goliforoshani.sms.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.goliforoshani.sms.R;

public class AdapterListNumber extends RecyclerView.Adapter<AdapterListNumber.ViewHolder> {


    private List<String> itemIntroList;
    private listener listener;
    Context context;

    public AdapterListNumber(List<String> itemIntroList, AdapterListNumber.listener listener, Context context) {
        this.itemIntroList = itemIntroList;
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterListNumber.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_number, parent, false);
        return new AdapterListNumber.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterListNumber.ViewHolder holder, final int position) {
        holder.number.setText(itemIntroList.get(position));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemIntroList.size()>=1){
                    listener.result(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemIntroList.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView number;
        ImageView delete;
        ViewHolder(View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.number);
            delete = itemView.findViewById(R.id.delete);
        }

    }

    public interface listener{
        void result(int pos);
    }
}
