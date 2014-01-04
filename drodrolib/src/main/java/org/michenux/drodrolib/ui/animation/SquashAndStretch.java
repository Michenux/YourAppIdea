package org.michenux.drodrolib.ui.animation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SquashAndStretch {

    private final AccelerateInterpolator sAccelerator = new AccelerateInterpolator();
    private final DecelerateInterpolator sDecelerator = new DecelerateInterpolator();

    @Inject
    SquashAndStretch() {
    }

    public void animate(View view, View container, long baseDuration, long animatorScale) {
        if (android.os.Build.VERSION.SDK_INT>=14) {
            this.animateApi14(view, container, baseDuration, animatorScale);
        }
    }

    @TargetApi(14)
    private void animateApi14(View view, View container, long baseDuration, long animatorScale) {
        long animationDuration = (long) (baseDuration * animatorScale);

        // Scale around bottom/middle to simplify squash against the window bottom
        view.setPivotX(view.getWidth() / 2);
        view.setPivotY(view.getHeight());

        // Animate the button down, accelerating, while also stretching in Y and squashing in X
        PropertyValuesHolder pvhTY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y,
                container.getHeight() - (view.getHeight() + view.getTop() + container.getPaddingTop()));
        PropertyValuesHolder pvhSX = PropertyValuesHolder.ofFloat(View.SCALE_X, .7f);
        PropertyValuesHolder pvhSY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.2f);
        ObjectAnimator downAnim = ObjectAnimator.ofPropertyValuesHolder(
                view, pvhTY, pvhSX, pvhSY);
        downAnim.setInterpolator(sAccelerator);
        downAnim.setDuration((long) (animationDuration * 2));

        // Stretch in X, squash in Y, then reverse
        pvhSX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.5f);
        pvhSY = PropertyValuesHolder.ofFloat(View.SCALE_Y, .5f);
        ObjectAnimator stretchAnim =
                ObjectAnimator.ofPropertyValuesHolder(view, pvhSX, pvhSY);
        stretchAnim.setRepeatCount(1);
        stretchAnim.setRepeatMode(ValueAnimator.REVERSE);
        stretchAnim.setInterpolator(sDecelerator);
        stretchAnim.setDuration(animationDuration);

        // Animate back to the start
        pvhTY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0);
        pvhSX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1);
        pvhSY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1);
        ObjectAnimator upAnim =
                ObjectAnimator.ofPropertyValuesHolder(view, pvhTY, pvhSX, pvhSY);
        upAnim.setDuration((long) (animationDuration * 2));
        upAnim.setInterpolator(sDecelerator);

        AnimatorSet set = new AnimatorSet();
        set.playSequentially(downAnim, stretchAnim, upAnim);
        set.start();
    }
}
