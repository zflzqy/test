package zfl.zidingyiview.imageHelp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;


public class imageHelper {
    private float hue;
    public static Bitmap imageHandler(Bitmap image, float hue, float saturation, float lum){
       //创建一个与传进来的图片大小一致的bmp,创建画布和画笔
        Bitmap bmp = Bitmap.createBitmap(image.getWidth(),image.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        //设置抗锯齿
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //色相，何种颜色
        ColorMatrix hueMatrix = new ColorMatrix();
        hueMatrix.setRotate(0,hue);
        hueMatrix.setRotate(1,hue);
        hueMatrix.setRotate(2,hue);
        //饱和度
        ColorMatrix saturationMatrix = new ColorMatrix();
        saturationMatrix.setSaturation(saturation);
        //亮度
        ColorMatrix lunMatrix = new ColorMatrix();
        lunMatrix.setScale(lum,lum,lum,1);
        //将色相，饱和度，亮度糅合到image里边
        ColorMatrix imageMatrix = new ColorMatrix();
        imageMatrix.postConcat(hueMatrix);
        imageMatrix.postConcat(saturationMatrix);
        imageMatrix.postConcat(lunMatrix);
        //创建过滤画笔，原理是使用Canvas canvas = new Canvas(bmp);
        //使用bmp构造的canvas(画布)使用image和paint画笔效果画bmp图片
        paint.setColorFilter(new ColorMatrixColorFilter(imageMatrix));
        canvas.drawBitmap(image,0,0,paint);

        return bmp;
    }
    public static Bitmap imagedipian(Bitmap image){
        int width = image.getWidth();
        int height = image.getHeight();
        int color;//存储取出来的像素点
        int a,r,g,b;//像素点颜色的四个分量
        Bitmap bitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        int[] oldpx = new int[width*height];
        int[] newpx = new int[width*height];
        //应该用传进来的图片得到像素
        image.getPixels(oldpx,0,width,0,0,width,height);
        for (int i =0;i < width*height;i++)
        {
            color = oldpx[i];
            //取像素点颜色的四个分量
            r = Color.red(color);
            g = Color.green(color);
            b = Color.blue(color);
            a = Color.alpha(color);
            //进行底片公式计算
            r=gongshi(r);
            g=gongshi(g);
            b=gongshi(b);
            newpx[i] = Color.argb(a,r,g,b);
        }
        bitmap.setPixels(newpx,0,width,0,0,width,height);
        return bitmap;
    }

    public static Bitmap iamgeold(Bitmap image){
        int width = image.getWidth();
        int height = image.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        int[] oldpx = new int[width*height];
        int[] newpx = new int[width*height];
        int color,r,g,b,a;
        image.getPixels(oldpx,0,width,0,0,width,height);
        for (int i = 0 ;i<width*height;i++){
            color = oldpx[i];
            r = Color.red(color);
            g = Color.green(color);
            b = Color.blue(color);
            a = Color.alpha(color);
            r = (int) (0.393 * r + 0.769 * g + 0.189 * b);
            g = (int) (0.349 * r + 0.686 * g + 0.168 * b);
            b = (int) (0.272 * r + 0.534 * g + 0.131 * b);
            r=gongshi1(r);
            g=gongshi1(g);
            b=gongshi1(b);
            newpx[i]=Color.argb(a,r,g,b);
        }
        bitmap.setPixels(newpx,0,width,0,0,width,height);
        return bitmap;
    }
    public static Bitmap fudiao(Bitmap bm) {
        Bitmap bmp = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(),
                Bitmap.Config.ARGB_8888);
        int width = bm.getWidth();
        int height = bm.getHeight();
        int color = 0, colorBefore = 0;
        int a, r, g, b;
        int r1, g1, b1;

        int[] oldPx = new int[width * height];
        int[] newPx = new int[width * height];

        bm.getPixels(oldPx, 0, bm.getWidth(), 0, 0, width, height);
        for (int i = 1; i < width * height; i++) {
            colorBefore = oldPx[i - 1];
            a = Color.alpha(colorBefore);
            r = Color.red(colorBefore);
            g = Color.green(colorBefore);
            b = Color.blue(colorBefore);

            color = oldPx[i];
            r1 = Color.red(color);
            g1 = Color.green(color);
            b1 = Color.blue(color);


            r=gongshi2(r,r1);
            g=gongshi2(g,g1);
            b=gongshi2(b,b1);
            newPx[i] = Color.argb(a, r, g, b);
        }
        bmp.setPixels(newPx, 0, width, 0, 0, width, height);
        return bmp;
    }

    private static int  gongshi1(int i) {
        if (i>255){
            i = 255;
        }
        return i;
    }
    private static int  gongshi2(int i,int b) {
        i = (i-b+127);
        if (i>255){
            i = 255;
        }
        return i;
    }


    private static int gongshi(int i) {
        i = 255-i;
        if (i>255){
            i = 255;
        }
        else if (i<0){
            i = 0;
        }
        return i;
    }
}
