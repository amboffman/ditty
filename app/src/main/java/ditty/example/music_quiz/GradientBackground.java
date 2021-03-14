package ditty.example.music_quiz;

import android.graphics.drawable.AnimationDrawable;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.music.ditty.R;

public class GradientBackground {
    protected ConstraintLayout background;
    protected  GradientBackground(ConstraintLayout viewBg){
        background = viewBg;
    }
    protected void getGradientBackground(){
    background.setBackgroundResource(R.drawable.gradient_animation);
    AnimationDrawable animDrawable = (AnimationDrawable) background.getBackground();
        animDrawable.setEnterFadeDuration(10);
        animDrawable.setExitFadeDuration(5000);
        animDrawable.start();
    }


}
