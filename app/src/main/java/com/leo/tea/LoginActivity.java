package com.leo.tea;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    private int time=3;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){

                case 999:

                    login_time.setText(time+"秒后自动进入");
                    if(time<=1){

                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                        LoginActivity.this.startActivity(intent);

                    }
                    time--;

                     mHandler.sendEmptyMessageDelayed(999,1000);
                    break;


            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        mHandler.sendEmptyMessageDelayed(999,1000);
    }
    private TextView login_time;
    private void initView() {
        login_time= (TextView) findViewById(R.id.login_time);
    }
}
