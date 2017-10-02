package com.avalon.app.seajude;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//Classes Firebase
import com.avalon.app.seajude.Model.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class CadastroActivity extends AppCompatActivity {

    //Invocando itens do Firebase
    FirebaseDatabase database;
    DatabaseReference users;

    CircularProgressButton cadastrobtn;
    EditText campoemail, campousuario, camposenha;
    TextView jatemconta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        //Iniciando Firebase
        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        cadastrobtn = (CircularProgressButton)findViewById(R.id.cadastrobtn);
        campoemail = (EditText)findViewById(R.id.campoemail);
        campousuario = (EditText)findViewById(R.id.campousuario);
        camposenha = (EditText)findViewById(R.id.camposenha);
        jatemconta = (TextView)findViewById(R.id.jatemconta);
        jatemconta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
            }
        });

        cadastrobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final User user = new User(campousuario.getText().toString(),
                        campoemail.getText().toString(),
                        camposenha.getText().toString());
                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(user.getUsuario()).exists()){
                            Toast.makeText(CadastroActivity.this, "O nome de usuário ja existe!", Toast.LENGTH_SHORT).show();
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        }else{
                            users.child(user.getUsuario()).setValue(user);
                            Toast.makeText(CadastroActivity.this, "Registrado com sucesso!", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //Código livre
                    }
                });
                cadastrobtn.startAnimation();
            }
        });
    }
}
