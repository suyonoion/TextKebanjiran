package com.suyonoion.textkebanjiran;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Method;

@SuppressWarnings("ALL")
public class OmbakView extends TextView {

    public interface AnimationSetupCallback {
        public void onSetupAnimation(OmbakView OmbakView);
    }

    private AnimationSetupCallback animationSetupCallback;
    private float maskX, maskY;
    private boolean sinking;
    private boolean setUp;

    private BitmapShader shader;
    private Matrix shaderMatrix;
    private Drawable banjir;
    private float offsetY;

    public OmbakView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        if (!isInEditMode()){
            init();
        }
    }

    public OmbakView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        if (!isInEditMode()){
            init();
        }
    }

    public OmbakView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        if (!isInEditMode()){
            init();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        // TODO Auto-generated method stub
        super.onAttachedToWindow();
        new SettingsObserver(new Handler()).observe();
        init();
    }
    class SettingsObserver extends ContentObserver {

        public SettingsObserver(Handler handler) {
            super(handler);
            // TODO Auto-generated constructor stub
        }

        void observe(){
            ContentResolver resolver = OmbakView.this.getContext().getContentResolver();
            resolver.registerContentObserver(Settings.System.getUriFor("ganti_text_ombak"), false, this);
            resolver.registerContentObserver(Settings.System.getUriFor("ukuran_ombak"), false, this);
            init();
        }



        @Override
        public void onChange(boolean selfChange) {
            // TODO Auto-generated method stub
            //super.onChange(selfChange);
            init();
        }

    }

    private void init() {
        shaderMatrix = new Matrix();
        OmbakView ov = (OmbakView) findViewById(tujuanFolderRes("OmbakView_id","id"));
        String ganti_txt_banjir = Settings.System.getString(getContext().getContentResolver(),"ganti_text_ombak");
        String ganti_ukuran_banjir = Settings.System.getString(getContext().getContentResolver(),"ukuran_ombak");
        if(TextUtils.isEmpty(ganti_txt_banjir)){
            ganti_txt_banjir = "MaaadGroup.com";
        }
        if(TextUtils.isEmpty(ganti_ukuran_banjir)){
            ganti_ukuran_banjir = "17";
        }

        int myNum = 0;
        try {
            myNum = Integer.parseInt(ganti_ukuran_banjir);
        } catch (NumberFormatException nfe) {

        }
        setText(ganti_txt_banjir);
        setTextSize(myNum);
        new Ombak().start(ov);
        ov.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), GantiTextPref.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                v.getContext().startActivity(intent);
                try {
                    Object service = getContext().getSystemService("statusbar");
                    Class<?> statusbarManager = Class.forName("android.app.StatusBarManager");
                    int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                    if (currentapiVersion <= 16) {
                        Method collapse = statusbarManager.getMethod("collapse");
                        collapse.invoke(service);
                    } else {
                        Method collapse2 = statusbarManager.getMethod("collapsePanels");
                        collapse2.invoke(service);
                    }

                } catch (Exception ex) { }

            }
        });
    }

    public AnimationSetupCallback getAnimationSetupCallback() {
        return animationSetupCallback;
    }

    public void setAnimationSetupCallback(AnimationSetupCallback animationSetupCallback) {
        this.animationSetupCallback = animationSetupCallback;
    }

    public float getMaskX() {
        return maskX;
    }

    public void setMaskX(float maskX) {
        this.maskX = maskX;
        invalidate();
    }

    public float getMaskY() {
        return maskY;
    }

    public void setMaskY(float maskY) {
        this.maskY = maskY;
        invalidate();
    }

    public boolean isSinking() {
        return sinking;
    }

    public void setSinking(boolean sinking) {
        this.sinking = sinking;
    }

    public boolean isSetUp() {
        return setUp;
    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
        createShader();
    }

    @Override
    public void setTextColor(ColorStateList colors) {
        super.setTextColor(colors);
        createShader();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        createShader();

        if (!setUp) {
            setUp = true;
            if (animationSetupCallback != null) {
                animationSetupCallback.onSetupAnimation(OmbakView.this);
            }
        }
    }

    private void createShader() {

        if (banjir == null) {
            banjir = getResources().getDrawable(R.drawable.gelombang);
        }

        int banjirW = banjir.getIntrinsicWidth();
        int banjirH = banjir.getIntrinsicHeight();

        Bitmap b = Bitmap.createBitmap(banjirW, banjirH, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);

        c.drawColor(getCurrentTextColor());

        banjir.setBounds(0, 0, banjirW, banjirH);
        banjir.draw(c);

        shader = new BitmapShader(b, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
        getPaint().setShader(shader);

        offsetY = (getHeight() - banjirH) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (sinking && shader != null) {
            if (getPaint().getShader() == null) {
                getPaint().setShader(shader);
            }
            shaderMatrix.setTranslate(maskX, maskY + offsetY);
            shader.setLocalMatrix(shaderMatrix);
        } else {
            getPaint().setShader(null);
        }
        super.onDraw(canvas);
    }

    public int tujuanFolderRes(String NamaFile, String NamaFolder)
    {
        return getContext().getResources().getIdentifier(NamaFile, NamaFolder, getContext().getPackageName());
    }
}