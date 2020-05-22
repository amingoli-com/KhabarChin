package ir.goliforoshani.sms.ui.intro;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.goliforoshani.sms.R;

public class IntroAdapter extends RecyclerView.Adapter<IntroAdapter.ViewHolder>{

  private List<IntroModel> itemIntroList;
  private LayoutInflater mInflater;
  private Context context;

  // data is passed into the constructor
  public IntroAdapter(Context context, List<IntroModel> itemIntroList) {
    this.context = context;
    this.mInflater = LayoutInflater.from(context);
    this.itemIntroList = itemIntroList;
  }

  // inflates the row layout from xml when needed
  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = mInflater.inflate(R.layout.item_intro, parent, false);
    return new ViewHolder(view);
  }

  // binds the data to the TextView in each row
  @Override
  public void onBindViewHolder(ViewHolder holder, final int position) {
    IntroModel item = itemIntroList.get(position);

    if (item.getTitle()!=null){
      holder.title.setVisibility(View.VISIBLE);
      holder.title.setText(item.getTitle());
      holder.title.setTextColor(Color.parseColor(item.getColot_title()));
    }else if (item.getSubTitle()!=null){
      holder.subTitle.setVisibility(View.VISIBLE);
      holder.subTitle.setText(item.getSubTitle());
      holder.subTitle.setTextColor(Color.parseColor(item.getColot_title()));
    }
    if (item.getDesc()!=null){
      holder.desc.setVisibility(View.VISIBLE);
      holder.desc.setText(item.getDesc());
      holder.desc.setTextColor(Color.parseColor(item.getColot_desc()));
    }
  }

  // total number of rows
  @Override
  public int getItemCount() {
    return itemIntroList.size();
  }


  // stores and recycles views as they are scrolled off screen
  public class ViewHolder extends RecyclerView.ViewHolder{
    TextView title,subTitle,desc;

    ViewHolder(View itemView) {
      super(itemView);
//      item_intro_1
      title = itemView.findViewById(R.id.title_intro1);
      subTitle = itemView.findViewById(R.id.sub_title_intro1);
      desc = itemView.findViewById(R.id.desc_intro1);
    }

  }


  boolean urlIsTrue(String url){
    if (url!=null &&
        url.length()>3 &&
        url.startsWith("http")){
      return true;
    }
    return false;
  }
}
