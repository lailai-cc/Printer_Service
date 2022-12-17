import java.util.LinkedList;

public class PrintQueue {
    private LinkedList<PrintRequest> queue;
    private int qtimes; //当前已经被访问了多少次
    public PrintQueue(){
        queue= new LinkedList<>();
        qtimes=0;
    }
    public int getQtimes(){
        return qtimes;
    }
    public void clear(){ //清空访问次数
        qtimes=0;
    }
    public boolean add(PrintRequest r) {
        return queue.add(r);
    }
    public boolean offer(PrintRequest r){
        return queue.offer(r);
    }
    //弹出位于队首的对象
    public PrintRequest remove() {
        qtimes++;
        return queue.remove();
    }
    public PrintRequest poll(){
        qtimes++;
        return queue.poll();
    }
    public int size(){ //返回队列大小
        return queue.size();
    }
    //返回位于队首的对象，并不删除
    public PrintRequest peek(){
        return queue.peek();
        //返回位于队首的对象
    }
    public PrintRequest element(){
        return queue.element();
    }
    public boolean isEmpty(){
        return queue.isEmpty();
    }

}