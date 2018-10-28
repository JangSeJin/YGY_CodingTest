package com.hour24.ygy.utils;

import android.app.Activity;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.hour24.ygy.view.CircleTransform;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class Utils {

    /**
     * @description 이미지 로드
     */
    @BindingAdapter({"loadImage"})
    public static void loadImage(final ImageView view, String url) {
        try {

            Picasso.get()
                    .load(url)
                    .transform(new CircleTransform())
                    .into(view, new Callback() {
                        @Override
                        public void onSuccess() {
                            view.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(Exception e) {
                            view.setVisibility(View.GONE);
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @description 키보드 보임/숨김
     */
    public static void setKeyboardShowHide(final Activity activity, final View view, final boolean isShow) {
        try {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (isShow) {
                        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
                    } else {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @description 소수점 자르고 보여줌
     */
    @BindingAdapter({"number", "point"})
    public static void setNumaberPointClipping(TextView view, double number, String count) {
        try {
            view.setText(String.format("%." + count + "f", number));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @description 이미지 컬러 변경
     */
    @BindingAdapter({"color"})
    public static void setColorFilter(ImageView view, String color) {
        try {
            view.setColorFilter(Color.parseColor(color));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
