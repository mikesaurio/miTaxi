package gob.df.mx.mitaxi.utils;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;

public class AnimatorFactory {
	
	public static Animation startAnimationFadeIn(float fromAlpha, float toAlpha, int duration) {
		Animation anim = new AlphaAnimation(fromAlpha, toAlpha);
		anim.setInterpolator(new DecelerateInterpolator());
		anim.setDuration(duration);
		
		return anim;
	}
	
	public static Animation startAnimationFadeOut(float fromAlpha, float toAlpha, int startOffset, int duration) {
		Animation anim = new AlphaAnimation(fromAlpha, toAlpha);
		anim.setInterpolator(new AccelerateInterpolator()); //and this
		anim.setStartOffset(startOffset);
		anim.setDuration(duration);
		
		return anim;
	}
	
	public static Animation startAnimationTranslate(int duration, float fromXDelta, float toXDelta, float fromYDelta, float toYDelta) {
		TranslateAnimation anim = new TranslateAnimation(-fromXDelta, toXDelta, -fromYDelta, toYDelta);
		anim.setFillAfter(true);
		anim.setDuration(duration);
		
		return anim;
	}
}