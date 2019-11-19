package co.edu.javeriana.wow_guau.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import co.edu.javeriana.wow_guau.R;
import co.edu.javeriana.wow_guau.adapters.MensajeAdapter;
import co.edu.javeriana.wow_guau.model.Mensaje;
import co.edu.javeriana.wow_guau.utils.FirebaseUtils;
import co.edu.javeriana.wow_guau.utils.Permisos;

public class ChatActivity extends AppCompatActivity {
    private ImageButton btn_send;
    private RecyclerView mMessageRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ProgressBar mProgressBar;
    private EditText et_mensaje;
    private ImageButton ib_add_mensaje;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String uidPaseo;
    private String uidChat;
    private List<Mensaje> mensajes;
    private MensajeAdapter mensajeAdapter;

    private String TAG = "CHAT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mAuth = FirebaseAuth.getInstance();

        btn_send = findViewById(R.id.btn_send);
        mProgressBar = findViewById(R.id.progressBar);
        et_mensaje = findViewById(R.id.et_mensaje);
        ib_add_mensaje = findViewById(R.id.ib_add_mensaje);
        mMessageRecyclerView = findViewById(R.id.messageRecyclerView);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);

        uidPaseo = getIntent().getStringExtra("uidPaseo");
        uidChat = uidPaseo;

        db.collection("Chats")
                .whereEqualTo("id", uidChat)
                .orderBy("time", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot snapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e(TAG, "Listen failed.", e);
                            return;
                        }

                        mensajes = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : snapshots) {
                            mensajes.add(
                                    new Mensaje(
                                            doc.getString("text"),
                                            doc.getString("imageUrl"),
                                            doc.getString("senderId")
                                    )
                            );
                            Log.i(TAG, doc.getString("text")+" "+doc.getString("imageUrl")+" "+doc.getString("senderId"));
                        }
                        updateUI();
                    }
                });

        et_mensaje.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    btn_send.setEnabled(true);
                } else {
                    btn_send.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mensaje mensaje = new Mensaje(et_mensaje.getText().toString(), null, mAuth.getUid());
                FirebaseUtils.sendMessage(mensaje, uidChat);
                et_mensaje.setText("");
            }
        });
        ib_add_mensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, Permisos.IMAGE_PICKER_REQUEST);
            }
        });
    }

    private void updateUI() {
        mensajeAdapter = new MensajeAdapter(mAuth.getUid(), mensajes);
        mMessageRecyclerView.setAdapter(mensajeAdapter);
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            startActivity(new Intent(ChatActivity.this, LoginActivity.class));
        }
    }

    @Override
    public void onPause() {
        //mFirebaseAdapter.stopListening();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        //mFirebaseAdapter.startListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == Permisos.IMAGE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    try {
                        final Uri uri = data.getData();
                        InputStream imageStream = getContentResolver().openInputStream(uri);
                        Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        Log.d("CHAT", "Uri: " + uri.toString());

                        Mensaje tempMessage = new Mensaje(null, "Chat/photo_"+uidChat+"_"+mensajes.size()+".jpg", mAuth.getUid());
                        FirebaseUtils.sendMessage(tempMessage, uidChat);
                        FirebaseUtils.subirFoto("Chat/photo_"+uidChat+"_"+mensajes.size()+".jpg", selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
