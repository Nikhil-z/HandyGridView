package com.huxq17.moveongridview;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private MoveOnGridView mGridView;
    private List<String> strList;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private TextPaint mTextPaint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public void initData() {
        strList = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            strList.add("ITEM " + i);
//        }
        strList.add("头条");
        strList.add("轻松一刻");
        strList.add("视频");
        strList.add("娱乐");
        strList.add("段子");
        strList.add("跟帖");
        strList.add("活力东奥学院");
        strList.add("北京");
        strList.add("社会");
        strList.add("军事");
        strList.add("热点");
        strList.add("直播");
        strList.add("网易号");
        strList.add("房产");
    }

    private void initView() {
        mGridView = findViewById(R.id.id_gridview);
        mGridView.setSelectorEnabled(false);
        final GridViewAdapter adapter = new GridViewAdapter(this, strList);
        mGridView.setAdapter(adapter);
        mGridView.setMode(MoveOnGridView.MODE.LONG_PRESS);
        mGridView.setAutoOptimize(true);
        adapter.notifyDataSetChanged();
        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (mGridView.getMode() != MoveOnGridView.MODE.TOUCH && !adapter.isFixed(position)) {//long press enter edit mode.
                    mGridView.setMode(MoveOnGridView.MODE.TOUCH);
                    return true;
                }
                return false;
            }
        });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "click item at " + position, Toast.LENGTH_SHORT).show();
            }
        });
        mGridView.setOnItemCapturedListener(new OnItemCapturedListener() {
            @Override
            public void onItemCaptured(View v, int position) {
                v.setScaleX(1.2f);
                v.setScaleY(1.2f);
//                v.setAlpha(0.6f);
            }

            @Override
            public void onItemReleased(View v, int position) {
                v.setScaleX(1f);
                v.setScaleY(1f);
//                v.setAlpha(1f);
            }

        });
        mGridView.setDrawer(new IDrawer() {
            @Override
            public void onDraw(Canvas canvas, int width, int height) {
                int offsetX = -DensityUtil.dip2px(MainActivity.this, 10);
                int offsetY = -DensityUtil.dip2px(MainActivity.this, 10);
                //文字绘制于gridview的右下角，并向左，向上偏移10dp。
                drawTips(canvas, width + offsetX, height + offsetY);
            }
        });
    }

    String paintText = "长按排序或删除";
    int textWidth;
    int textHeight;
    StaticLayout tipsLayout;

    private void drawTips(Canvas canvas, int width, int height) {
        if (mTextPaint == null) {
            mTextPaint = new TextPaint();
            mTextPaint.setColor(Color.parseColor("#CFCFCF"));
            mTextPaint.setTextSize(DensityUtil.dip2px(MainActivity.this, 12));
            Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
            textHeight = (int) (fontMetrics.bottom - fontMetrics.top) + 1;
            textWidth = (int) mTextPaint.measureText(paintText) + 1;
        }
        width = width - textWidth;
        height = height - textHeight;
        if (tipsLayout == null) {
            tipsLayout = new StaticLayout(paintText, mTextPaint, width, Layout.Alignment.ALIGN_NORMAL, 1.5f, 0f, false);
        }
        canvas.translate(width, height);
        tipsLayout.draw(canvas);
    }

    public void change(View v) {
        mGridView.setSelectorEnabled(!mGridView.isSelectorEnabled());
    }

    private void log(String msg) {
        Log.e(getClass().getCanonicalName(), msg);
    }
}
