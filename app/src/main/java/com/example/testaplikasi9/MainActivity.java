package com.example.testaplikasi9;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    final static String ChanelId = "Info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MediaPlayer music = MediaPlayer.create(this,R.raw.alec);
        music.start();
        textView = findViewById(R.id.statusdownload);
    }

    public void startServiceBtn(View view) {
        startService(new Intent(this,MyService.class));
    }

    public void stopServiceBtn(View view) {
        stopService(new Intent(this,MyService.class));
    }

    public void DownloadBtn(View view) {
        downloadfile task = new downloadfile();
        task.execute(4);
    }

    public class downloadfile extends AsyncTask<Integer,Integer,Void> {

        @Override
        protected Void doInBackground(Integer... integers) {
            int file = integers[0];
            for (int i=0;i<file; i++){
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(i+1);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int value = values[0];
            String info = String.format("Downloaded %d/4",value);
            textView.setText(info);
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            textView.setText("Download Finished");
            NotificationCompat.Builder mbuilder = new NotificationCompat.Builder(MainActivity.this,ChanelId).setSmallIcon(R.drawable.ic_launcher_background).setContentTitle("Notification Download").setContentText("DOwnload Finished");
            Notification notification = mbuilder.build();
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                NotificationChannel channel = new NotificationChannel(ChanelId,"Download Notfiation", NotificationManager.IMPORTANCE_HIGH);
                        notificationManager.createNotificationChannel(channel);
            }
            notificationManager.notify(1,notification);
        }
    }
}