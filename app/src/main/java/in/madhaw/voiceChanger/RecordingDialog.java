package in.madhaw.voiceChanger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class RecordingDialog extends AppCompatActivity implements Animation.AnimationListener {
    ImageView imageView;
    TextView localButton;
    Animation animRotate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording_dialog);
        imageView = findViewById(R.id.imgLogo);
        localButton = findViewById(R.id.btnCancel);
        animRotate = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_out);
        animRotate.setAnimationListener(this);
        imageView.setVisibility(View.VISIBLE);
        imageView.startAnimation(this.animRotate);
        localButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.recording = false;
                RecordingDialog.this.finish();
            }
        });


    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        imageView.startAnimation(this.animRotate);

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.recording = Boolean.valueOf(false);
        finish();
    }
}