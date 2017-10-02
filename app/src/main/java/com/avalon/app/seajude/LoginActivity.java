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

public class LoginActivity extends AppCompatActivity {

    //Invocando itens do Firebase
    FirebaseDatabase database;
    DatabaseReference users;

    TextView cadastrese;
    CircularProgressButton loginbtn;
    EditText campousuario, camposenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //iniciando Firebase
        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        loginbtn = (CircularProgressButton)findViewById(R.id.loginbtn);
        campousuario = (EditText)findViewById(R.id.campousuario);
        camposenha = (EditText)findViewById(R.id.camposenha);
        cadastrese = (TextView)findViewById(R.id.cadastrese);
        cadastrese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), CadastroActivity.class);
                startActivity(i);
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entrar(campousuario.getText().toString(),
                        camposenha.getText().toString());
                loginbtn.startAnimation();
            }
        });
    }

    private void entrar(final String usuario, final String senha) {
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(usuario).exists()){
                    if(!usuario.isEmpty()){
                        User login = dataSnapshot.child(usuario).getValue(User.class);
                        if(login.getSenha().equals(senha)){
                            Toast.makeText(LoginActivity.this, "Logado com sucesso!", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(i);
                            finish();
                        }else{
                            Toast.makeText(LoginActivity.this, "Senha incorreta!", Toast.LENGTH_SHORT).show();
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        }
                    }else{
                        Toast.makeText(LoginActivity.this, "Usuário não registrado!", Toast.LENGTH_SHORT).show();
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Código livre
            }
        });
    }
}
