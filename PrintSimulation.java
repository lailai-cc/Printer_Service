import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PrintSimulation {
    private double pro; //当前这一秒有打印请求到来的概率
    private int qn;//有多少个队列
    private int k;//当某个队列被访问了k次后，将提升优先级
    private String type; //将选择哪种优先级策略
    private int maxWait=0; //最大等待时间
    private int sumWait=0; //总的等待时间
    private int Num;//总的打印数量
    double[] draw_wait;//记录对应范围内的请求的等待时间，用于绘制直方图
    double[] draw_num;//记录对应范围内的请求的数量，用于绘制直方图
    int clock=0;
    PrintDispatcher pd;
    Printer print;
    public PrintSimulation(double p,int n,int k,String a){
        pro=p;
        qn=n;
        this.k=k;
        type=a;
        Num=0;
        pd=new PrintDispatcher(n,type,k);
        print=new Printer();
        draw_wait=new double[n];
        draw_num=new double[n];
    }
    public PrintRequest request(){//产生打印请求
        double rd= Math.random(); //产生一个在0到1之间的随机数
        if(rd>pro) return null;
        else{
            String s=new String("File"+Num++);
            return new PrintRequest(s, (int) (1+Math.random() * 100),clock);
        }
    }
    public void simulation(){  //模拟打印机行为
        if(!print.printerIdle()){//如果打印机正在忙
            print.printOnepage();
        }
        else{
            if(!pd.isEmpty()){//如果打印机空闲且有请求正在等待
                PrintRequest temp=pd.dequeue();
                print.printFile(temp);
                int waitTime=clock-temp.getTime(); //计算任务等待时间
               /* temp.setWait(waitTime);*/
                int x=(temp.getPageNum()-1)/10;
                if(x<qn-1) {
                    draw_wait[x]+=waitTime;
                    draw_num[x]++;
                }
                else{
                    draw_wait[qn-1]+=waitTime;
                    draw_num[qn-1]++;
                }
                if(waitTime>maxWait) maxWait=waitTime;
                sumWait+=waitTime;
            }
        }
    }
    public void process(){ //打印过程
        for(clock=0;clock<1000;++clock){ //在1000秒内
            PrintRequest r=request();
            if(r!=null) pd.enqueue(r);
            simulation();
        }
        while(!pd.isEmpty()||!print.printerIdle()){
            simulation();
            ++clock;
        }
    }

    public int LongestWait(){
        return maxWait;
    }
    public double AveWait(){
        return sumWait*1.0/Num;
    }
    public void drawHistogram(){
        System.out.print("     y\n     ^");//画y轴
        System.out.println();
        double[] ave=new double[qn];
        int  max=0;  //最大平均等待时间，用于确定直方图y轴的最大值
        for(int i=0;i<qn;++i){
            ave[i]=draw_wait[i]/draw_num[i];
            if(ave[i]>max)max=(int)ave[i]+1;
        }
        while(max%10!=0)max++; //取大等于max的最小的10的倍数，用于给直方图分区
        int per=max/10;
        int len=Integer.toString(max).length();
        for(int j=max;j>=0;j=j-per){
            int l=Integer.toString(j).length();
            while(l<len) {
                System.out.print(' ');
                l++;
            }
            System.out.print(j+"|\t");
            for(int i=0;i<qn;i++){
                double k=draw_wait[i]/draw_num[i];
                if(k>=j)System.out.print("*\t\t");
                else System.out.print(" \t\t");;
            }
            System.out.println();
        }
        System.out.print("\t |--");
        for(int i=0;i<qn-1;i++){
            System.out.print(i+1+"~"+(i+1)*10+"\t");
        }
        System.out.println(">10("+qn+"-1) --->x");
    }
    public void draw() {
        Histogram planeHistogram = new Histogram();
        BufferedImage image = planeHistogram.paintHistogram("Average waiting time",draw_wait,draw_num,qn);
        File output = new File("gram.jpg");
        try {
            ImageIO.write(image, "jpg", output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        double p=Double.parseDouble(args[0]);
        int n=Integer.parseInt(args[1]);
        int k=Integer.parseInt(args[2]);
        String a=args[3];
        PrintSimulation ps=new PrintSimulation(p,n,k,a);
        ps.process();
        System.out.println(ps.LongestWait());
        System.out.println(ps.AveWait());
        ps.drawHistogram();
        ps.draw();
    }
}