package com.example.zia.login;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.zia.login.R.id.player;

/**
 * Created by jzl on 2016/11/24.
 */

public class MyActivity extends AppCompatActivity {
    TextView pulse;
    TextView go_web;
    MediaPlayer Mediaplayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        getSupportActionBar().hide();

        pulse = (TextView) findViewById(player);
        go_web = (TextView) findViewById(R.id.go_web);

        Mediaplayer = new MediaPlayer();
        Mediaplayer = MediaPlayer.create(MyActivity.this, R.raw.bravesong);
        Mediaplayer.start();

        //设置音乐暂停
        pulse.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View view) {
                Mediaplayer.pause();
            }
        });

        //设置跳转按钮启动浏览器
        go_web.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.jianshu.com/p/a0bf389680ad"));
                startActivity(intent);
                MyActivity.this.onPause();
            }
        });
    }

            //设置再按一次结束程序
        private long exitTime = 0;
        @Override
        public boolean onKeyDown ( int keyCode, KeyEvent event){
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
                } else {
                    finish();
                    System.exit(0);
                }
                return true;
            }
            return super.onKeyDown(keyCode, event);
        }
    }

