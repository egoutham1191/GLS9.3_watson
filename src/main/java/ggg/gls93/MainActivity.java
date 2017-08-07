package ggg.gls93;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneInputStream;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    Button send;
    Button record;
    EditText textToSend;
    TextView output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        send = (Button)findViewById(R.id.button);
        record = (Button) findViewById(R.id.button2);
        textToSend = (EditText)findViewById(R.id.editText);
        output = (TextView)findViewById(R.id.textView);
        final Context con = this;
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Watson w = new Watson(con);
                w.record();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Watson w = new Watson(con);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                         w.speak("I am clearly working, u dumbasrse");
                    }
                }).start();
            }
        });
    }

    public MainActivity getActivity() {
        return this;
    }
}
