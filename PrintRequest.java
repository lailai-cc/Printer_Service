public class PrintRequest {
    private String fileName; //文件名
    private int pageNum; //页数
    private int time; //发出该请求的时间

    public PrintRequest(String name,int num,int t){
        fileName=name;
        pageNum=num;
        time=t;
    }

    public int getTime(){
        return time;
    }

    public int getPageNum() {
        return pageNum;
    }

    public String toString(){
        StringBuilder s=new StringBuilder();
        s.append("FileName:").append(fileName);
        s.append(" PageNumber:").append(pageNum);
        s.append(" RequestTime:").append(time);
        return s.toString();
    }
}
