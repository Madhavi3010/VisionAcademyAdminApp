package ran.com.visionacademy.Notification;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ran.com.visionacademy.R;

public class NotifyActivity extends AppCompatActivity {

    private final int REQ = 1;
    private EditText notificationTitle , notificationContent;
    private Button uploadNotificationBtn;
    private DatabaseReference reference;
    private StorageReference storageReference;
    private ProgressDialog pd;

    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);

        reference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        pd = new ProgressDialog(this);

        notificationTitle = findViewById(R.id.notificationTitle);
        notificationContent = findViewById(R.id.notificationContent);
        uploadNotificationBtn = findViewById(R.id.uploadNotificationBtn);

        uploadNotificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(notificationTitle.getText().toString().isEmpty()){
                    notificationTitle.setError("Empty");
                    notificationTitle.requestFocus();
                }else if(bitmap == null){
                    uploadData();
                }
            }
        });


    }

    private void uploadData() {
        pd.setMessage("Uploading...");
        pd.show();

        reference = reference.child("Notification");
        final String uniqueKey = reference.push().getKey();

        String title = notificationTitle.getText().toString();
        String content = notificationContent.getText().toString();

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yy");
        String date = currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        String time = currentTime.format(calForTime.getTime());

        NotificationData notificationData = new NotificationData(title,content,date,time,uniqueKey);

        reference.child(uniqueKey).setValue(notificationData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                pd.dismiss();
                Toast.makeText(NotifyActivity.this, "Notification sent", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(NotifyActivity.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
            }
        });

    }


}