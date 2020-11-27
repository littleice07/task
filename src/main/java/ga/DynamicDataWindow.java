package ga;

/* DataWindow.java */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JFrame;

import java.text.DateFormat;
import java.util.Date;
import java.util.Random;

/**
 * ��̬������������
 *http://bookshadow.com/weblog/2014/05/16/java-swing-line-chart/
 * @author tyhj_sf@163.com
 */
public class DynamicDataWindow extends JFrame {

	private static final long serialVersionUID = 3158195564992240333L;
	private static final int MAX_SAMPLES = 100000;
	private double yMax = 0.0;
	private double yMin = Double.MAX_VALUE;
    private int index = 0;
    private long[] time = new long[MAX_SAMPLES];
    private double[] val = new double[MAX_SAMPLES];
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel dataPanel;
    private javax.swing.JPanel coordinatePanel;
    private javax.swing.JTextArea dataTextArea;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
   
    DateFormat fmt = DateFormat.getDateTimeInstance();
    
    private int left;
    private int top;
    private int right;
    private int bottom;

    private int y0;   //y�����λ��
    private int yn;//y��ĩ��λ��
    private int x0;//x�����λ��
    private int xn;//x��ĩ��λ��
    
    private double eqdiviY;//����Y�ȷ���
    private double vscale; 
    
    private int tickX;//��ȼ������
    private int timescale;//����Ϊ��׼��ʱ�䱶��
    private double timeMargin;//��ȼ��
    private double tscale;// ÿһ�����ش���ĺ�����
    private int temp;//

    /** Creates new form DataWindow */
    public DynamicDataWindow() {
        initComponents();
        initDisplay();
    }

    public DynamicDataWindow(String ieee) {
        initComponents();
        setTitle(ieee);
        initDisplay();
    }
    
    private void initDisplay() {
		left = dataPanel.getX() + 10; // get size of pane
		top = dataPanel.getY() + 30;
		right = left + dataPanel.getWidth() - 20;
		bottom = top + dataPanel.getHeight() - 20;

		y0 = bottom - 40; // y�����λ��
		yn = top + 40;// y��ĩ��λ��
		x0 = left + 50;// x�����λ��
		xn = right; // x��ĩ��λ��

		eqdiviY = 125.0;// ����Y�ȷ���
		vscale = (yn - y0) / eqdiviY;

		tickX = 25;// ��ȼ������
		timescale = 1;
		timeMargin = timescale*1000.0;// ��ȼ��Ϊ1��
		tscale = 1.0 / (timeMargin / tickX); // ÿһ�����ش���ĺ�����
	}
    //����y���ȷ�Χ
    public void setyMaxMin(double y) {
    	if (y>this.yMax) {
    		this.yMax = y*1.1;
		}
    	if (y<this.yMin) {
    		this.yMin = y*0.9;
		}
	}

    //������ϵ������ݲ�ˢ������
    public void addData(long t, double v) {
    	setyMaxMin(v);
        time[index] = t;
        val[index++] = v;
        dataTextArea.append(fmt.format(new Date(t)) + "    value = " + v + "\n");
        dataTextArea.setCaretPosition(dataTextArea.getText().length());
        repaint();
    }

    
    // Graph the sensor values in the dataPanel JPanel
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        left = dataPanel.getX() + 10; // get size of pane
		top = dataPanel.getY() + 30;
		right = left + dataPanel.getWidth() - 20;
		bottom = top + dataPanel.getHeight() - 20;

		y0 = bottom - 40; // y�����λ��
		yn = top + 40;// y��ĩ��λ��
		x0 = left + 50;// x�����λ��
		xn = right; // x��ĩ��λ��

		eqdiviY = 125.0;// ����Y�ȷ���
		vscale = (yn - y0) / eqdiviY;

//		tickX = 25;// ��ȼ������
		timeMargin = timescale*1000;// ��ȼ��Ϊ1��
		tscale = 1.0 / (timeMargin / tickX); // ÿһ�����ش���ĺ�����
		
        // ����x�ᣬΪʱ����
        g.setColor(Color.WHITE);
        g.drawLine(x0, yn, x0, y0);//�����ޱ�ȵ�Y��
        g.drawLine(x0, y0, xn, y0);//�����ޱ�ȵ�X��
        for (int xt = x0 + tickX; xt < xn; xt += tickX) {  
            g.drawLine(xt, y0 + 5, xt, y0 - 5);//���Ʊ��
            int time = (xt - x0) / tickX;//����ÿ����ȵ�ʱ��
            g.drawString(Integer.toString(timescale*time), xt - (time < 10 ? 3 : 7) , y0 + 20);//����ÿ����ȶ�Ӧ��ֵ
            if (xt >= xn-tickX) {
            	g.drawString("ʱ�䣨s��", xt - 25 , y0 + 40);//��������
			}
        }

        // ����y�ᣬΪ������
        g.setColor(Color.WHITE);
        double tickY=20;//��ȼ��
        for (double vt = 0; vt <= eqdiviY; vt += tickY) {    
            int v = y0 + (int)(vt * vscale);//
            g.drawLine(x0 - 5, v, x0 + xn, v);//���Ʊ��
            g.drawString(Float.toString((float)(yMin+(vt/eqdiviY)*(yMax-yMin))), x0 - 38 , v + 5);//����ÿ����ȶ�Ӧ��ֵ
            if (vt >= 120) {
            	g.drawString("����", x0 - 30 , v-15);//��������
			}
        }

        // ������������
        g.setColor(Color.RED);
        int xp = -1;//ǰһ������ֵ��x;
        int vp = -1;//ǰһ������ֵ��y;
        for (int i = 0; i < index; i++) {
            int x = x0 + (int)((time[i] - time[0]) * tscale);
            int v = y0 + (int)(((val[i]-yMin)/(yMax-yMin)) *(yn-y0));
            if (xp > 0) {//ʱ�䲻Ϊ��
                g.drawLine(xp, vp, x, v);//���Ƶ�ǰ����
            }
            xp = x;
            vp = v;
        }
        g.drawString(Double.toString(val[(index-1)<0? 0 : (index-1)]),xp-5 , vp-5);//������һ������ֵ
        //����x���ȼ��
        if (xp>=xn) {
        	timescale*=2;
        	temp=timescale/100;
        	while(temp>0){
        		tickX+=9;
        		temp/=10;
        	}
		}
    }

    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // //GEN-BEGIN:initComponents
    private void initComponents() {

        dataPanel = new javax.swing.JPanel();
//        coordinatePanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        dataTextArea = new javax.swing.JTextArea();

        dataPanel.setBackground(new Color(0, 0, 0, 220));
        dataPanel.setMinimumSize(new java.awt.Dimension(400, 250));
        dataPanel.setPreferredSize(new java.awt.Dimension(800, 500));
        getContentPane().add(dataPanel, java.awt.BorderLayout.CENTER);      
        
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setMinimumSize(new java.awt.Dimension(400, 100));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(400, 100));

        dataTextArea.setColumns(20);
        dataTextArea.setEditable(false);
        dataTextArea.setRows(4);
        jScrollPane1.setViewportView(dataTextArea);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.SOUTH);

        pack();
    }// //GEN-END:initComponents


}