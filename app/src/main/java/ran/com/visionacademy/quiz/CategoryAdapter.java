package ran.com.visionacademy.quiz;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ran.com.visionacademy.Notification.DeleteNotificationActivity;
import ran.com.visionacademy.R;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.viewholder> {

    private List<CategoryModel> categoryModelList;
    private DeleteListener deleteListener;

    public CategoryAdapter(List<CategoryModel> categoryModelList,DeleteListener deleteListener) {
        this.categoryModelList = categoryModelList;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {

        holder.setData(categoryModelList.get(position).getName(),categoryModelList.get(position).getKey(),position);

    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }

    class viewholder extends RecyclerView.ViewHolder{


        private TextView c_title;
        private ImageButton deleteCategory;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            c_title = itemView.findViewById(R.id.c_title);
            deleteCategory = itemView.findViewById(R.id.deleteCategory);
        }

        private void setData(final String c_title, final String key,final int position){

            this.c_title.setText(c_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent setIntent = new Intent(itemView.getContext(),SetsActivity.class);
                    setIntent.putExtra("title",c_title);
                    setIntent.putExtra("position",position);
                    setIntent.putExtra("key",key);
                    itemView.getContext().startActivity(setIntent);
                }
            });

            deleteCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteListener.onDelete(key,position);
                }
            });
        }
    }

    public interface DeleteListener{
        public void onDelete(String key, int position);
    }



}
