package in.madhaw.voiceChanger;

import android.content.Intent;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    File file = new File(Environment.getExternalStorageDirectory(), "test.pcm");
    public static Boolean recording;
    private Spinner spFrequency;
    ImageView microphone, playback, startRec;
    ArrayAdapter<String> adapter;
    AudioTrack audioTrack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] arrayofString = new String[8];
        arrayofString[0] = "Ghost";
        arrayofString[1] = "Slow Motion";
        arrayofString[2] = "Robot";
        arrayofString[3] = "Normal";
        arrayofString[4] = "Chipmunk";
        arrayofString[5] = "Funny";
        arrayofString[6] = "Bee";
        arrayofString[7] = "Elephant";

        startRec = findViewById(R.id.startRec);
        microphone = findViewById(R.id.imgMic);
        playback = findViewById(R.id.play);
        spFrequency = findViewById(R.id.frequency);

        startRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RecordingDialog.class));

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        recording = true;
                        try {
                            startRecord();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        playback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (file.exists()) {
                    try {
                        playRecord();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayofString);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFrequency.setAdapter(adapter);
        microphone.setVisibility(View.VISIBLE);


    }

    private void playRecord() throws IOException {
        int i = 0;
        String str = (String) spFrequency.getSelectedItem();
        int shortSizeInBytes = Short.SIZE / Byte.SIZE;
        int bufferedSizeInBytes = (int) (file.length() / shortSizeInBytes);
        short[] audioData = new short[bufferedSizeInBytes];
        InputStream inputStream = new FileInputStream(file);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        DataInputStream dataInputStream = new DataInputStream(bufferedInputStream);
        int j = 0;
        while (dataInputStream.available() > 0) {
            audioData[j] = dataInputStream.readShort();
            j++;
        }
        dataInputStream.close();
        if (str.equals("Ghost")) {
            i = 5000;
        }
        if (str.equals("Slow Motion")) {
            i = 6050;
        }
        if (str.equals("Robot")) {
            i = 8500;
        }
        if (str.equals("Normal")) {
            i = 11025;
        }
        if (str.equals("Chipmunk")) {
            i = 16000;
        }
        if (str.equals("Funny")) {
            i = 22050;
        }
        if (str.equals("Bee")) {
            i = 41000;
        }
        if (str.equals("Elephant")) {
            i = 30000;
        }
        audioTrack = new AudioTrack(3, i, 2, 2, bufferedSizeInBytes, 1);
        audioTrack.play();
        audioTrack.write(audioData, 0, bufferedSizeInBytes);

    }

    private void startRecord() throws IOException {
        File myFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "test.pcm");
        myFile.createNewFile();
        OutputStream outputStream = new FileOutputStream(myFile);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
        DataOutputStream dataOutputStream = new DataOutputStream(bufferedOutputStream);
        int minBufferSize = AudioRecord.getMinBufferSize(11025, 2, 2);
        short[] audioData = new short[minBufferSize];
        AudioRecord audioRecord = new AudioRecord(1, 11025, 2, 2, minBufferSize);
        audioRecord.startRecording();
        while (recording) {
            int numberOfShort = audioRecord.read(audioData, 0, minBufferSize);
            for (int i = 0; i < numberOfShort; i++) {
                dataOutputStream.writeShort(audioData[i]);
            }
        }
        if (!recording.booleanValue()) {
            audioRecord.stop();
            dataOutputStream.close();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recording = false;
        if (audioTrack != null) {
            audioTrack.release();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}