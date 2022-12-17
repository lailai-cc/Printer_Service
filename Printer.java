public class Printer {
    private PrintRequest File;
    private int rest; //剩余页数
    private boolean working=false; //是否正在工作

    public boolean printerIdle(){
        if(!working) return true;
        else return false;
    }

    boolean printFile(PrintRequest r){
        if(!working) { //如果空闲
            File=r;
            rest=r.getPageNum();
            working=true;
            return true;
        }
        else return false;
    }

    public void printOnepage(){
        rest--;  //每次打印一页
        if(rest==0){
            working=false;
         //   System.out.println("pagenumber: "+File.getPageNum()+" waiting-time: "+File.getwaitTime());
        }
    }
    PrintRequest processForOneUnit(){
        if(!working || rest>1) return null;
        else{
            return File;
        }
    }
}