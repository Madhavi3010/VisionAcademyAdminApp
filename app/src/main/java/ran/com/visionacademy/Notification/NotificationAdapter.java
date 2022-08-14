package ran.com.visionacademy.Notification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ran.com.visionacademy.R;
import ran.com.visionacademy.notice.NoticeData;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewAdapter> {
    private Context context;
    private ArrayList<NotificationData> list1;
    public NotificationAdapter(Context context, ArrayList<NotificationData> list1) {
        this.context = context;
        this.list1 = list1;
    }
    @NonNull
    @Override
    public NotificationViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_item_layout,parent,false);
        return new NotificationViewAdapter(view);
    }
    @Override
    public void onBindViewHolder(@NonNull NotificationViewAdapter holder, final int position) {
        final NotificationData currentItem = list1.get(position);
        holder.deleteNotificationTitle.setText(currentItem.getTitle());
        holder.deleteNotificationContent.setText(currentItem.getContent());
        holder.deleteNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Notification");
                reference.child(currentItem.getKey()).removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
                notifyItemRemoved(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list1.size();
    }

    public class NotificationViewAdapter extends RecyclerView.ViewHolder {

        private Button deleteNotification;
        private TextView deleteNotificationTitle;
        private TextView deleteNotificationContent;

        public NotificationViewAdapter(@NonNull View itemView) {
            super(itemView);
            deleteNotification = itemView.findViewById(R.id.deleteNotification);
            deleteNotificationTitle = itemView.findViewById(R.id.deleteNotificationTitle);
            deleteNotificationContent = itemView.findViewById(R.id.deleteNotificationContent);
        }
    }

}
