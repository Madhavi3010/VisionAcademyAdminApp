package ran.com.visionacademy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import ran.com.visionacademy.Notification.DeleteNotificationActivity;
import ran.com.visionacademy.Notification.NotifyActivity;
import ran.com.visionacademy.faculty.UpdateFaculty;
import ran.com.visionacademy.notice.DeleteNoticeActivity;
import ran.com.visionacademy.notice.UploadNotice;
import ran.com.visionacademy.quiz.QuizzerAdmin;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    CardView uploadNotice, addGalleryImage, addEbook, faculty, deleteNotice, logout, uploadNotification, deleteNotification, addAttendance, uploadTestMark , quiz;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sharedPreferences = this.getSharedPreferences("login", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (sharedPreferences.getString("isLogin", "false").equals("false")) {
            openLogin();
        }

        uploadNotice = findViewById(R.id.addNotice);
        addGalleryImage = findViewById(R.id.addGalleryImage);
        addEbook = findViewById(R.id.addEbook);
        faculty = findViewById(R.id.faculty);
        deleteNotice = findViewById(R.id.deleteNotice);
        logout = findViewById(R.id.logout);
        uploadNotification = findViewById(R.id.notification);
        deleteNotification = findViewById(R.id.deleteNotification);
        addAttendance = findViewById(R.id.addAttendance);
        uploadTestMark = findViewById(R.id.uploadTestMark);
        quiz = findViewById(R.id.quiz);


        uploadNotice.setOnClickListener(this);
        addGalleryImage.setOnClickListener(this);
        addEbook.setOnClickListener(this);
        faculty.setOnClickListener(this);
        deleteNotice.setOnClickListener(this);
        logout.setOnClickListener(this);
        uploadNotification.setOnClickListener(this);
        deleteNotification.setOnClickListener(this);
        addAttendance.setOnClickListener(this);
        uploadTestMark.setOnClickListener(this);
        quiz.setOnClickListener(this);

    }

    private void openLogin() {
        startActivity(new Intent(MainActivity.this, loginActivity.class));
        finish();
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.addNotice:
                intent = new Intent(MainActivity.this, UploadNotice.class);
                startActivity(intent);
                break;

            case R.id.addGalleryImage:
                intent = new Intent(MainActivity.this, UploadImage.class);
                startActivity(intent);
                break;

            case R.id.addEbook:
                intent = new Intent(MainActivity.this, UploadPdfActivity.class);
                startActivity(intent);
                break;

            case R.id.addAttendance:
                intent = new Intent(MainActivity.this, UploadAttendanceActivity.class);
                startActivity(intent);
                break;

            case R.id.faculty:
                intent = new Intent(MainActivity.this, UpdateFaculty.class);
                startActivity(intent);
                break;

            case R.id.deleteNotice:
                intent = new Intent(MainActivity.this, DeleteNoticeActivity.class);
                startActivity(intent);
                break;

            case R.id.uploadTestMark:
                intent = new Intent(MainActivity.this, UploadTestResult.class);
                startActivity(intent);
                break;

            case R.id.logout:
                editor.putString("isLogin", "false");
                editor.commit();
                openLogin();
                break;

            case R.id.notification:
                intent = new Intent(MainActivity.this, NotifyActivity.class);
                startActivity(intent);
                break;

            case R.id.deleteNotification:
                intent = new Intent(MainActivity.this, DeleteNotificationActivity.class);
                startActivity(intent);
                break;

            case R.id.quiz:
                intent = new Intent(MainActivity.this, QuizzerAdmin.class);
                startActivity(intent);
                break;


        }

    }
}