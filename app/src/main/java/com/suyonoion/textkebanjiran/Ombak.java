package com.suyonoion.textkebanjiran;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.animation.LinearInterpolator;

@SuppressWarnings("ALL")
public class Ombak {

    private AnimatorSet SetAnimasi;
    private Animator.AnimatorListener animatorListener;

    public Animator.AnimatorListener getAnimatorListener() {
        return animatorListener;
    }

    public void setAnimatorListener(Animator.AnimatorListener animatorListener) {
        this.animatorListener = animatorListener;
    }

    public void start(final OmbakView textView) {

        final Runnable bergelombang = new Runnable() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void run() {
                textView.setSinking(true);
                ObjectAnimator AnimasiXmendatar = ObjectAnimator.ofFloat(textView, "maskX", 0, 200);
                AnimasiXmendatar.setRepeatCount(ValueAnimator.INFINITE);
                AnimasiXmendatar.setDuration(2000);
                AnimasiXmendatar.setStartDelay(0);
                SetAnimasi = new AnimatorSet();
                SetAnimasi.playTogether(AnimasiXmendatar);
                SetAnimasi.setInterpolator(new LinearInterpolator());
                SetAnimasi.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        textView.setSinking(false);

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            textView.postInvalidate();
                        } else {
                            textView.postInvalidateOnAnimation();
                        }

                        SetAnimasi = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });


                if (animatorListener != null) {
                    SetAnimasi.addListener(animatorListener);
                }

                SetAnimasi.start();
            }
        };

        if (!textView.isSetUp()) {
            textView.setAnimationSetupCallback(new OmbakView.AnimationSetupCallback() {
                @Override
                public void onSetupAnimation(final OmbakView target) {
                    bergelombang.run();
                }
            });
        } else {
            bergelombang.run();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void batal() {
        if (SetAnimasi != null) {
            SetAnimasi.cancel();
        }
    }
}
