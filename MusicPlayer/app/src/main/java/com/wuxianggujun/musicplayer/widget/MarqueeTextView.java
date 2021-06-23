package com.wuxianggujun.musicplayer.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.regex.Pattern;
/**
 * @author 12453
 * @since  2021/03/23
 * 引用： https://my.oschina.net/yanjiuyangok/blog/341309
 * */
@SuppressLint("AppCompatCustomView")
public class MarqueeTextView extends TextView {
    /**
     * 文字长度
     */
    private float textLength = 0f;
    /**
     * 滚动条长度
     */
    private float viewWidth = 0f;
    /**
     * 文本x轴 的坐标
     */
    private float tx = 0f;
    /**
     * 文本Y轴的坐标
     */
    private float ty = 0f;
    /**
     * 文本当前长度
     */
    private float temp_tx1 = 0.0f;
    /**
     * 文本当前变换的长度
     */
    private float temp_tx2 = 0x0f;
    /**
     * 文本滚动开关
     */
    private boolean isStarting = false;
    /**
     * 画笔对象
     */
    private Paint paint = null;
    /**
     * 显示的文字
     */
    private String text = "";
    /**
     * 文本滚动速度
     **/
    private float speed;
    /**
     * 是否是第一次滚动，用于设置首次滚动的位置
     **/
    private boolean isFirstScroll = true;
    /**
     * 设置首次滚动的位置,第二次从最右边开始
     * FirstScroll = 3 首次滚动的位置从整个Text宽度的(从左到右) FirstScroll分之一处开始滚动
     **/
    private static final int FirstScroll = 3;

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //Log.d("滚动Text", "onDetachedFromWindow: ");
        if (paint != null) {
            paint.reset();
            paint = null;
        }
        text = null;
    }

    /**
     * 初始化自动滚动条,每次改变文字内容时，都需要重新初始化一次
     *
     * @param windowManager 获取屏幕
     * @param text          显示的内容
     * @param speed         文字滚动的速度
     */
    public void initScrollTextView(WindowManager windowManager, String text, float speed) {
        // 得到画笔,获取父类的textPaint
        paint = this.getPaint();
        // 得到文字
        this.text = text;
        this.speed = speed;
        textLength = paint.measureText(text);// 获得当前文本字符串长度
        viewWidth = this.getWidth();// 获取宽度return mRight - mLeft;
        if (viewWidth == 0) {
            // 获取当前屏幕的属性
            Display display = windowManager.getDefaultDisplay();
            viewWidth = display.getWidth() / 2;// 获取屏幕宽度  viewWidth 是滚动的开始位置，需要修改的
            // 可再此入手
        }
        tx = textLength;
        temp_tx1 = viewWidth / FirstScroll + textLength;
        temp_tx2 = viewWidth / FirstScroll+ textLength * 2;// 自己定义，文字变化多少
        // // 文字的大小+距顶部的距离
        ty = this.getTextSize() + this.getPaddingTop();
    }

    /**
     * 开始滚动
     */
    public void starScroll() {
        if (!isFirstScroll) isFirstScroll = true;
        // 开始滚动
        isStarting = true;
        this.invalidate();// 刷新屏幕
    }

    /**
     * 停止方法,停止滚动
     */
    public void stopScroll() {
        if (isFirstScroll) isFirstScroll = false;
        // 停止滚动
        isStarting = false;
        this.invalidate();// 刷新屏幕
    }
    /**
     * 初始化滚动或者文本内容，根据文字的长度而定
     *
     * @param text 显示的内容
     * @param manager 获取不到此Text控件宽度的情况下，需要此对象
     * 如果文字长度大于16，那么选择滚动，没有达到就不滚动，需要自定义*/
    public void setText(String text,WindowManager manager) {
        int scrollLength,count = 0;
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");//中文与中文字符
        Pattern r = Pattern.compile("[\u3040-\u309F\u30A0-\u30FF]");//日文字符
        if (!p.matcher(text).find() && !r.matcher(text).find()) {
            //Log.d("滚动", "全是字母，算出大写字母的个数");a-z：97-122，A-Z：65-90，0-9：48-57。
            char[] x = text.toCharArray();
            for (int i = 0;i < x.length; i++){
                //Log.d("自定义view", "setText: "+x[i]);
                if(x[i] >= 'A' && x[i] <= 'Z') count++;//大写字母
            }
            scrollLength = 32;
        }else {//中文与中文字符,可能不全是字母，故需要对每个字符进行判断
            for (int i = 0;i < text.length(); i++){
                String x = text.substring(i);
                if(p.matcher(x).find() || r.matcher(text).find())  count++;
            }
            scrollLength = 16;
        }
        int trueLength = count *2 +(text.length() - count);
        if(trueLength >= 32) scrollLength = text.length();

        if (text.length() >= scrollLength) {
            if (isStarting) stopScroll();
            setText("");
            initScrollTextView(manager,text,2);
            starScroll();
        }else {
            stopScroll();
            this.text = text;
            setText(text);
        }
    }
    /**
     * 重写父类方法，如果文本滚动，父控件中文本缓存为空或者为上次不滚动的内容，不利于做一些判断*/
    @Override
    public String getText() {
        return text;
    }

    /**
     * 重写onDraw方法
     */
    @Override
    protected void onDraw(Canvas canvas) {
        if (isStarting) {
            //Log.d("滚动Text", "onDraw: ");
            isFirstScroll = false;
            // A-Alpha透明度/R-Read红色/g-Green绿色/b-Blue蓝色
            //paint.setARGB(255, 200, 200, 200);
            canvas.drawText(text, temp_tx1 - tx, ty, paint);
            tx += speed;
            // 当文字滚动到屏幕的最左边
            if (tx > temp_tx2) {
                //Log.d("滚动Text", "onDraw: 滚动到最左边");
                // 把文字设置到最右边开始
                tx = textLength;
                if (!isFirstScroll) {
                    temp_tx1 = viewWidth + textLength;
                    temp_tx2 = viewWidth + textLength * 2;// 自己定义，文字变化多少
                }
            }
            this.invalidate();// 刷新屏幕
        }
        super.onDraw(canvas);
    }
}
