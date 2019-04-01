package com.example.suzzang.network_socket_chat.intro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.suzzang.network_socket_chat.MainActivity;
import com.example.suzzang.network_socket_chat.R;

public class SettingPortIPActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_port_ip);
        final EditText ip = findViewById(R.id.ip_adress);
        final EditText port = findViewById(R.id.port_num);
        TextView btn_gochat = findViewById(R.id.btn_gochat);

        btn_gochat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingPortIPActivity.this,MainActivity.class);
                intent.putExtra("ip",ip.getText().toString());
                intent.putExtra("port", Integer.valueOf(port.getText().toString()));
                startActivity(intent);
            }
        });
    }
}
