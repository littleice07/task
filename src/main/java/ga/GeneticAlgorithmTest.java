package ga;

public class GeneticAlgorithmTest extends GeneticAlgorithm{  
    /***/
	public static final int GENE_LENGTH = 15;
	/**基因对应的数值上限，由基因 的位数决定*/
    public static final int NUM = 1 << GENE_LENGTH;  
    
    public GeneticAlgorithmTest() {  
        super(GENE_LENGTH);    
    }  
      
    @Override  
    public int changeX(Chromosome chro) {  
        // TODO Auto-generated method stub    
        return chro.getNum();  
    }  
  
    @Override  
    public double caculateY(int x1, int x2) {  
        // TODO Auto-generated method stub 
    	return x1*x1+x2*x2;  
    	
    }  
  
    @Override  
    public double caculateY(double x) {
    	return x*Math.sin(10*Math.PI*x)+2.0;
    }
    
    public static void main(String[] args) {  
    	System.out.println("NUM="+NUM);
        GeneticAlgorithmTest test = new GeneticAlgorithmTest(); 
        test.setDdWindow(new DynamicDataWindow("遗传算法最优化求解过程"));
        test.caculte();  
    }  
}  
