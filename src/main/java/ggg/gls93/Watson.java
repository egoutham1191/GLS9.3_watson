package ggg.gls93;

import android.app.Activity;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneInputStream;
import com.ibm.watson.developer_cloud.android.library.audio.StreamPlayer;
import com.ibm.watson.developer_cloud.http.HttpMediaType;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.BaseRecognizeCallback;
import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.AudioFormat;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;
import com.ibm.watson.developer_cloud.text_to_speech.v1.util.WaveUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Jk on 8/4/2017.
 */

public class Watson {
    private static final String _WS2TUsername = "0868f213-25ee-4235-afaa-09a31b2c2488";
    private static final String _WS2TPassword = "7V2e4cZfLAvu";
    private static final String _WT2SUsername = "c53f197a-798e-4835-8c7c-55f723184263";
    private static final String _WT2SPassword = "i85dvGq4EuRE";

    Context context;
    public Watson(Context context) {
        this.context = context;
    }

    public void record() {
        final Activity ma = (Activity)context;
        final EditText textToSend =(EditText) ma.findViewById(R.id.editText);
        final Button record_button = (Button) ma.findViewById(R.id.button2);
        ma.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                record_button.setBackgroundColor(10);
                textToSend.setText("");
            }
        });
        String result = "";
        SpeechToText speechService = new SpeechToText();
        speechService.setUsernameAndPassword(_WS2TUsername, _WS2TPassword);
        speechService.setEndPoint("https://stream.watsonplatform.net/speech-to-text/api");
        int sampleRate = 16000;
        RecognizeOptions ro = new RecognizeOptions.Builder()
                .continuous(true)
                .interimResults(true)
                .inactivityTimeout(1)
                .model("en-US_BroadbandModel")
                .contentType(HttpMediaType.AUDIO_RAW + "; rate=" + sampleRate)
                .build();
        speechService.recognizeUsingWebSocket(new MicrophoneInputStream(), ro, new BaseRecognizeCallback() {
            @Override
            public void onTranscription(SpeechResults sr) {
                final String result = sr.getResults().get(0).getAlternatives().get(0).getTranscript();
                System.out.println(result);
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textToSend.append(result);
                    }
                });
            }

            @Override public void onDisconnected() {
                ma.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        record_button.setBackgroundColor(32);
                    }
                });
            }
        });
    }

    public void speak(String s) {
        TextToSpeech textToSpeech = new TextToSpeech();
        textToSpeech.setUsernameAndPassword(_WT2SUsername, _WT2SPassword);
        textToSpeech.setEndPoint("https://stream.watsonplatform.net/text-to-speech/api");
        try {
            InputStream stream = textToSpeech.synthesize(s, Voice.EN_LISA, AudioFormat.OGG).execute();
            InputStream in = WaveUtils.reWriteWaveHeader(stream);
            StreamPlayer sp = new StreamPlayer();
            sp.playStream(in);
            in.close();
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
