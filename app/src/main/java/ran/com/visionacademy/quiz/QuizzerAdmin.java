package ran.com.visionacademy.quiz;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import ran.com.visionacademy.R;

public class QuizzerAdmin extends AppCompatActivity {

    private RecyclerView recyclerView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    public static List<CategoryModel> list;

    private Dialog loadingDialog, categoryDialog;
    private EditText category_name;
    private Button add_category;
    private CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizzer_admin);
        Toolbar toolbar = findViewById(R.id.t1);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Categories");

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corner));
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);

        setCategoryDialog();

        recyclerView = findViewById(R.id.rv);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        list = new ArrayList<>();
        adapter = new CategoryAdapter(list, new CategoryAdapter.DeleteListener() {
            @Override
            public void onDelete(final String key, final int position) {

                new AlertDialog.Builder(QuizzerAdmin.this, R.style.Theme_AppCompat_Light_Dialog)
                        .setTitle("Delete Category")
                        .setMessage("Are you sure you want to delete this category?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                myRef.child("Categories").child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            for (String setIds : list.get(position).getSets()) {
                                                myRef.child("SETS").child(setIds).removeValue();
                                            }
                                            list.remove(position);
                                            adapter.notifyDataSetChanged();
                                            loadingDialog.dismiss();
                                        } else {
                                            Toast.makeText(QuizzerAdmin.this, "Failed to delete !!", Toast.LENGTH_SHORT).show();
                                            loadingDialog.dismiss();
                                        }

                                    }
                                });
                            }
                        })
                        .setNegativeButton("cancel", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        recyclerView.setAdapter(adapter);

        loadingDialog.show();
        myRef.child("Categories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    List<String> sets = new ArrayList<>();
                    for (DataSnapshot dataSnapshot2 : dataSnapshot1.child("sets").getChildren()) {
                        sets.add(dataSnapshot2.getKey());
                    }
                    list.add(new CategoryModel(dataSnapshot1.child("name").getValue().toString(),
                            sets,
                            dataSnapshot1.getKey()
                    ));
                }
                adapter.notifyDataSetChanged();
                loadingDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(QuizzerAdmin.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.addBtn) {
            //dialog show
            categoryDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setCategoryDialog() {
        categoryDialog = new Dialog(this);
        categoryDialog.setContentView(R.layout.add_category_dialog);
        categoryDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corner));
        categoryDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        categoryDialog.setCancelable(true);

        category_name = categoryDialog.findViewById(R.id.category_name);
        add_category = categoryDialog.findViewById(R.id.add_category);


        add_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (category_name.getText() == null || category_name.getText().toString().isEmpty()) {
                    category_name.setError("Required...");
                    return;
                }
                for (CategoryModel model : list) {
                    if (category_name.getText().toString().equals(model.getName())) {
                        category_name.setError("Category Name Already Exist !!");
                        return;
                    }
                }
                categoryDialog.dismiss();
                uploadCategoryName();
            }
        });
    }

    private void uploadCategoryName() {

        Map<String, Object> map = new HashMap<>();
        map.put("name", category_name.getText().toString());
        map.put("sets", 0);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        final String id = UUID.randomUUID().toString();

        database.getReference().child("Categories").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    list.add(new CategoryModel(category_name.getText().toString(), new ArrayList<String>(), id));
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(QuizzerAdmin.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }
        });
    }
}