import java.util.LinkedList;

public class PrintDispatcher {
    private LinkedList<PrintQueue> dispatcher;
    private int queueNum; //队列中PrintQueue的数量
    private String type; //打印策略
    private int k; //如果一个队列已经被访问过k次
    public PrintDispatcher(int n,String t,int k){
        dispatcher= new LinkedList<>();
        for(int i=0;i<n;++i) {
            dispatcher.add(new PrintQueue());
        }
        queueNum=n;
        type=t;
        this.k=k;
    }
    public boolean enqueue(PrintRequest pr) {
        if(pr==null) return false;
        int n;
        switch(type){
            case "A":
                dispatcher.element().add(pr);
                break;
            case "B":
                n=(pr.getPageNum()-1)/10;
                if(n<queueNum){
                    PrintQueue q=dispatcher.get(n);
                    q.add(pr);
                }
                else{//如果页数超过10n，则直接加在最后一个队列中
                    PrintQueue q=dispatcher.get(queueNum-1);
                    q.add(pr);
                }
                break;
            case "C":
                n=pr.getPageNum()%queueNum;
                PrintQueue q=dispatcher.get(n);
                q.add(pr);
                break;

        }
        return true;
    }
    public boolean isEmpty(){ //判断是否所有队列为空
        for(int i=0;i<queueNum;++i){
            if(!dispatcher.get(i).isEmpty()) return false;
        }
        return true;
    }
    public PrintRequest dequeue(){
        PrintQueue q = null;
        for(PrintQueue p:dispatcher) {
            if (!(p.isEmpty())) {
                q=p;
                break;
            }
        }
        if(q!=null)  {
            PrintRequest t=q.remove();
            hungry(); //动态调整优先级
            return t;
        }
        else return null;
    }

    public void hungry(){  //使用动态优先级缓解饥饿效应
        for(int i=0;i<queueNum;++i){
            if(dispatcher.get(i).getQtimes()==k){ //如果一个队列已经被访问过k次
                int j=i+1;
                while(j<queueNum&&dispatcher.get(j).isEmpty()){
                    ++j;
                }
                if(j!=queueNum){ //从j队列中取出一个放到i队列中
                    PrintRequest t=dispatcher.get(j).remove();
                    dispatcher.get(i).add(t);
                }
                dispatcher.get(i).clear(); //清空访问次数
            }
        }

    }
    public String toString() {
        StringBuilder s = new StringBuilder();
        //显示逐个队列状态
        for (int i = 0; i < queueNum; ++i) {
            s.append(i).append(": visit-times:").append(dispatcher.get(i).getQtimes()).append(" size:").append(dispatcher.get(i).size()).append("\n");
        }
        return s.toString();
    }
}