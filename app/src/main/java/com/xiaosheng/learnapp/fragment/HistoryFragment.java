package com.xiaosheng.learnapp.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.xiaosheng.learnapp.R;
import com.xiaosheng.learnapp.databinding.FragmentHistoryBinding;
import com.xiaosheng.learnapp.databinding.FragmentSearchBinding;


public class HistoryFragment extends Fragment {
    private FragmentHistoryBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         binding = FragmentHistoryBinding.inflate(inflater, container, false);

         init();

         return binding.getRoot();
//        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    private void init() {
        // 读取sharedPreferenced
        // 获取editor
        SharedPreferences sp = getActivity().getSharedPreferences("history", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();

        binding.writeSpBtn.setOnClickListener(v->{
            String string = binding.writeSp.getText().toString();
            edit.putString("data", string);
            Toast.makeText(getContext(), "sp写入成功", Toast.LENGTH_LONG).show();
            // 保存
            edit.apply();
        });
        binding.readSpBtn.setOnClickListener(v->{
            String string = sp.getString("data", "");
            binding.showSp.setText(string);
            Toast.makeText(getActivity(), "读取sp成功", Toast.LENGTH_LONG).show();
        });

        binding.animBtn.setOnClickListener(v->{
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.alpha_animation);
            binding.showSp.startAnimation(animation);
        });

        MyView myView = new MyView(getContext());
        binding.getRoot().addView(myView);
    }


    public class MyView extends View{
        private Paint paint;

        public MyView(Context context) {
            super(context);
            paint = new Paint();
            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2);
        }


        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
//            canvas.drawColor(Color.BLUE);
            canvas.drawRect(100, 0, 200,100,paint);
            canvas.drawRoundRect(100,150,200, 300, 200,200, paint);
            canvas.drawCircle(300,300, 30, paint);
        }
    }

}