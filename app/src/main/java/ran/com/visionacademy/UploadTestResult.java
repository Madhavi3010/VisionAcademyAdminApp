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

public class UploadTestResult extends AppCompatActivity {

    private CardView testAddPdf;
    private EditText testPdfTitle;
    private Button testUploadPdfBtn;
    private TextView testPdfTextView;
    private String testPdfName, testTitle;

    private Uri testPdfData;
    private String category;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    String downloadUrl = "";
    private ProgressDialog pd;

    private Spinner testCategory;
    private final int REQ = 1;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_test_result);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Test Result");
        storageReference = FirebaseStorage.getInstance().getReference().child("Test Result");

        pd = new ProgressDialog(this);

        testAddPdf = findViewById(R.id.testAddPdf);
        testPdfTitle = findViewById(R.id.testPdfTitle);
        testUploadPdfBtn = findViewById(R.id.testUploadPdfBtn);
        testPdfTextView = findViewById(R.id.testPdfTextView);
        testCategory = findViewById(R.id.testCategory);

        String[] items = new String[]{"Select Category", "XI SCIENCE", "XII SCIENCE", "ENGINEERING"};
        testCategory.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items));

        testCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = testCategory.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        testAddPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        testUploadPdfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testTitle = testPdfTitle.getText().toString();
                if (testTitle.isEmpty()) {
                    testPdfTitle.setError("Empty");
                    testPdfTitle.requestFocus();
                } else if (testPdfData == null) {
                    Toast.makeText(UploadTestResult.this, "Please Upload AttendanceSheet Pdf!!", Toast.LENGTH_SHORT).show();
                } else if(category.equals("Select Category")){
                    Toast.makeText(UploadTestResult.this, "Please select category!!", Toast.LENGTH_SHORT).show();
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
        StorageReference reference = storageReference.child("pdf/" + testPdfName + "-" + System.currentTimeMillis() + ".pdf");
        reference.putFile(testPdfData)
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
                Toast.makeText(UploadTestResult.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void uploadData(String downloadUrl) {
        databaseReference = databaseReference.child(category);
        String uniquekey = databaseReference.push().getKey();

        HashMap data = new HashMap();
        data.put("pdfTitle", testTitle);
        data.put("pdfUrl" , downloadUrl);

        databaseReference.child(uniquekey).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                Toast.makeText(UploadTestResult.this, "pdf uploaded successfully", Toast.LENGTH_SHORT).show();
                testPdfTitle.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(UploadTestResult.this, "Failed to upload pdf", Toast.LENGTH_SHORT).show();
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
            testPdfData = data.getData();

            if (testPdfData.toString().startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = UploadTestResult.this.getContentResolver().query(testPdfData, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        testPdfName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (testPdfData.toString().startsWith("file://")) {
                testPdfName = new File(testPdfData.toString()).getName();
            }

            testPdfTextView.setText(testPdfName);
        }
    }
}