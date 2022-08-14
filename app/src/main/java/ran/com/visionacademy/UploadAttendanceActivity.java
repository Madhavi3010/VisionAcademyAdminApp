package ran.com.visionacademy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;

public class UploadAttendanceActivity extends AppCompatActivity {

    private CardView attendanceAddPdf;
    private EditText attendancePdfTitle;
    private Button attendanceUploadPdfBtn;
    private TextView attendancePdfTextView;
    private String attendancePdfName, attendanceTitle;

    private Uri attendancePdfData;
    private String category;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    String downloadUrl = "";
    private ProgressDialog pd;

    private Spinner attendanceCategory;
    private final int REQ = 1;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_attendance);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Attendance Pdf");
        storageReference = FirebaseStorage.getInstance().getReference().child("Attendance Pdf");

        pd = new ProgressDialog(this);

        attendanceAddPdf = findViewById(R.id.attendanceAddPdf);
        attendancePdfTitle = findViewById(R.id.attendancePdfTitle);
        attendanceUploadPdfBtn = findViewById(R.id.attendanceUploadPdfBtn);
        attendancePdfTextView = findViewById(R.id.attendancePdfTextView);
        attendanceCategory = findViewById(R.id.attendanceCategory);

        String[] items = new String[]{"Select Category", "XI SCIENCE", "XII SCIENCE", "ENGINEERING"};
        attendanceCategory.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items));

        attendanceCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = attendanceCategory.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        attendanceAddPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        attendanceUploadPdfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attendanceTitle = attendancePdfTitle.getText().toString();
                if (attendanceTitle.isEmpty()) {
                    attendancePdfTitle.setError("Empty");
                    attendancePdfTitle.requestFocus();
                } else if (attendancePdfData == null) {
                    Toast.makeText(UploadAttendanceActivity.this, "Please Upload AttendanceSheet Pdf!!", Toast.LENGTH_SHORT).show();
                } else if(category.equals("Select Category")){
                    Toast.makeText(UploadAttendanceActivity.this, "Please select category!!", Toast.LENGTH_SHORT).show();
                }else {
                    uploadPdf();
                }
            }
        });
    }

    private void uploadPdf() {
        pd.setTitle("Please wait...");
        pd.setMessage("Uploading pdf");
        pd.show();
        StorageReference reference = storageReference.child("pdf/" + attendancePdfName + "-" + System.currentTimeMillis() + ".pdf");
        reference.putFile(attendancePdfData)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete()) ;
                        Uri uri = uriTask.getResult();
                        uploadData(String.valueOf(uri));

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(UploadAttendanceActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadData(String downloadUrl) {
        databaseReference = databaseReference.child(category);
        String uniquekey = databaseReference.push().getKey();

        HashMap data = new HashMap();
        data.put("pdfTitle", attendanceTitle);
        data.put("pdfUrl" , downloadUrl);

        databaseReference.child(uniquekey).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                Toast.makeText(UploadAttendanceActivity.this, "pdf uploaded successfully", Toast.LENGTH_SHORT).show();
                attendancePdfTitle.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(UploadAttendanceActivity.this, "Failed to upload pdf", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf File"), REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ && resultCode == RESULT_OK) {
            attendancePdfData = data.getData();

            if (attendancePdfData.toString().startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = UploadAttendanceActivity.this.getContentResolver().query(attendancePdfData, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        attendancePdfName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (attendancePdfData.toString().startsWith("file://")) {
                attendancePdfName = new File(attendancePdfData.toString()).getName();
            }

            attendancePdfTextView.setText(attendancePdfName);
        }
    }
}