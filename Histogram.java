import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Histogram {
    private final int histogramWidth = 45;// 柱形图的宽度
    private final int histogramPitch = 20;// 柱形图的间距
    private double scaling = 1f;// 缩放的比例
    private int maxStrWidth = 5; // 字符串需要的最大宽度


    public BufferedImage paintHistogram(String title, double[] v, double[]u, int n) {
        int width = n * histogramWidth+ n*histogramPitch+50; //图片宽度
        int height = 350; //图片高度
        scaling = calculateScale(v, u,height);//计算缩放比例

        BufferedImage bufferImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferImage.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        FontMetrics metrics = null;

        g.setFont(new Font(null, Font.BOLD, 18));
        g.setColor(Color.RED); //标题用红色字
        g.drawString(title, (bufferImage.getWidth() - g.getFontMetrics().stringWidth(title)) >> 1, 30);// 画标题

        g.setFont(new Font(null, Font.PLAIN, 12));
        metrics = g.getFontMetrics();
        g.setColor(Color.BLACK); //坐标系用黑色画
        g.drawLine(10, 0, 10, height - 15); // 画Y坐标
        g.drawLine(10, height - 15, width, height - 15);// 画X坐标

        int j = 0;
        Color c=Color.BLUE; //蓝色画柱状图
        for (int i = 0; i < n; ++i) {
            g.setColor(c);
            int x = 20 + i * (histogramPitch + histogramWidth + (maxStrWidth >> 1));// X坐标
            int y = height - 16- (int) (v[i]/u[i] * scaling); // Y坐标

            // 标出每个范围内的平均等待时间
            g.drawString(String.format("%.2f", v[i]/u[i]) , x+5 - ((metrics.stringWidth(v[i]/u[i] + "") - histogramWidth*2) >>1), y);

            // 画平面的柱状图
            g.drawRect(x, y, histogramWidth, (int) (v[i]/u[i] * scaling));
            g.fillRect(x, y, histogramWidth, (int) (v[i]/u[i] * scaling));

            // 标出x轴上的每个区域范围
            String[] str = new String[n];
            for(int k=0;k<n-1;++k){
                String s=new String(k*10+1+"~"+((k+1)*10));
                str[k]=s;
            }
            str[n-1]=new String(">"+10*(n-1));
            g.drawString(str[i], x - ((metrics.stringWidth(str[i]) - histogramWidth) >> 1),
                    height - 2);
        }
        return bufferImage;
    }

    public double calculateScale(double[] v,double[] u , int h){
        //计算缩放比例
        double scale = 1f;
        double max = Double.MIN_VALUE;
        for(int i=0 , len=v.length ; i < len ;++i){
            if(v[i]/u[i]>h && v[i]/u[i]>max){
                max=v[i]/u[i];
            }
        }
        if(max > h){
            scale=((int)(h*1.0/max*1000)-1.5)*1.0/1000;
        }
        return scale;
    }

}
