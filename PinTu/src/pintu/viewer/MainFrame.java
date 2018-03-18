package pintu.viewer;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import pintu.dao.HandleDB;
import pintu.handle.HandleImage;

public class MainFrame extends JFrame implements ActionListener{

	private JPanel contentPane;
	JPanel imagePanel = new JPanel();
	JLabel lblShow = new JLabel("");
	private JButton btnShow = new JButton("��Ϸ˵��");
	private JButton btnOrder = new JButton("��ʾ���а�");
	private JButton btnMusic = new JButton("��������");
	private JButton btnStart = new JButton("��ʼ��Ϸ");
	JComboBox<String> cbImage = null;
	JComboBox<String> cbGrade = null;
	private int nums = 3;   //����Ѷȵȼ�
	HandleDB hdb = new HandleDB();
	private AudioClip sound = null; 
	
	
	// ctrl + shift + O ����
	private Random rand = new Random();
	private int []numArr = null;
	
	private boolean isRun = false;   //��Ϸ�Ƿ�ʼ��
	
	private String imageName = "1.jpg";
	private int leftTime;  //��ʼʣ��ʱ��
	private Timer timer;
	
	private int blankR = 2;   //����ͼƬ���� 
	private int blankC = 2;   //����ͼƬ����
	
	ImageButton [][]btnArr = null;   
	private final JLabel lblNewLabel = new JLabel("ʣ��ʱ��:");
	private JLabel lblLeftTime = new JLabel("1200");
	
	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					 new MainFrame();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	

	public class MyTask extends TimerTask{

		@Override
		public void run() {
			leftTime--;
			lblLeftTime.setText(leftTime+"");
			if(leftTime == 0) {
				lblLeftTime.setText(leftTime+"");
				JOptionPane.showMessageDialog(null, "��Ϸʱ�䵽�ˣ�");
				this.cancel();
				
			}
		}
		
	}
	
	public void startThead() {
		if(timer !=null)      //����������Ϸ��������ͬʱ����
			timer.cancel();
		
		leftTime = 1200;
		timer = new Timer();
		timer.schedule(new MyTask(), 1000,1000); //һ��������̣߳�ÿ��һ����Ӧ�߳�
	}
	
	
	public void musicSwitch(ActionEvent e) {
		JButton btn = (JButton)e.getSource(); //�¼�Դָ�����ְ�ť
		if("��������".equals(btn.getText().trim())) {
			sound.loop();
			btn.setText("�ر�����");
		}else {
			sound.stop();
			btn.setText("��������");
		}
	}
	
	
	//ctrl + shift +f ���Ŵ���
    @Override
  	public void actionPerformed(ActionEvent arg0) {
    	if(arg0.getSource() == btnMusic) {
    		musicSwitch(arg0);
    	}else if(arg0.getSource() == btnShow) {
    		JOptionPane.showMessageDialog(this,"�����Ϸ��Ĳ����棡 made by��ըС����");
    	}else if(arg0.getSource() == btnStart) {
    		//ȷ�϶Ի��� ����ֵ�� ��0 ��1 ȡ��2
    		if(isRun) {
    		    int n = JOptionPane.showConfirmDialog(this, "�����Ҫ���¿�ʼ��Ϸ��");
    		    if(n == 0)
        		    init();
    		        startThead();
    		}else {
    			init();
    			startThead();
    			isRun = true;
    			btnStart.setText("���¿�ʼ");
    		}
   	
    	
    	}else if(arg0.getSource() == btnOrder) {
    		new TimeOrderDialog(this,true);   //����dialogʱ�����岻�ɶ�
    	}else {
    		switchImage(arg0);
    	}
	}
    
    
    public void switchImage(ActionEvent e) {
    	ImageButton btn = (ImageButton) e.getSource();
    	int row = btn.getRow();
    	int col = btn.getCol();
    	/*�ж�������ͼƬ�Ƿ�����*/
    	if(Math.abs(row - blankR) + Math.abs(col - blankC) == 1) {   //abs�����ֵ
    		//����ͼƬ���ǰ�������ť�ϵ�ͼƬ��Ž������ٸ��°�ť�ϵ�ͼƬ����
    		int temp = btnArr[row][col].getNum();
    		//����ͼƬ���
    		btnArr[row][col].setNum(btnArr[blankR][blankC].getNum());
    		btnArr[blankR][blankC].setNum(temp);
    		//���°�ťͼƬ
    		btnArr[row][col].updateImage(false);     
    		btnArr[blankR][blankC].updateImage(true);
    		 //��������ͼƬ������
    		blankR = row;   
    		blankC = col;
    		
    		if(checkOrder()) {
    			//��һ������ָ��Ϣ�������ĸ����齨���ڶ�������ָ��Ϣ��������
    			JOptionPane.showMessageDialog(this, "WIN!");
    			int leftTime = Integer.parseInt(lblLeftTime.getText());
    			int time = 1200 - leftTime;   //������Ϸ����ʱ��
    			String name = JOptionPane.showInputDialog(this, "�������ǳ�");
    			if(name == null || "".equals(name.trim())) {   //�������������ַ���
    				name = "����";
    			}
    			hdb.insertInfo(name, time);
    		}
    	}
    }
    
    
    public void initNum() {
    	numArr = new int[nums*nums];
    	for(int i=0; i<nums*nums; i++) {
    		numArr[i] = i+1;
    	}
    	
        /*���ϴ��*/
    	for(int i=0; i<nums*nums-2; i++) {  //9��ͼƬʼ�����أ�ֻ�����1-8��ͼƬ
    		//���±�Ϊi���±�Ϊ7���������ѡ��һ��
    		int n = rand.nextInt(nums*nums-i-1)+i;     // ��Χ��[i,i+nums*nums-i-1-1]
    		int temp = numArr[i];
    		numArr[i] = numArr[n];
    		numArr[n] = temp;
    	}
    	/*//����
    	for(int i=0; i<nums*nums; i++) {
    		System.out.print(numArr[i] + "  ");
    	}
    	System.out.println();*/
    }
     
  
	public void init() {
		
		initImage();
		initGrade();
		initNum();
		
    	btnArr = new ImageButton[nums][nums];
    	imagePanel.removeAll();
    	imagePanel.setLayout(new GridLayout(nums,nums));
    	
    	HandleImage hi = new HandleImage();
    	hi.deleteAll();
    	
    	
    	// ǰ׺Ϊϵͳ��ǰʱ��
    	long l = System.currentTimeMillis();
    	String preName = String.valueOf(l);
    	//cuttingImage(int size, int rowSize, int colSize, String preName, String imageName)
    	hi.cuttingImage(495/nums, nums, nums, preName, imageName);
    	
    	
    	int k = 0;   //ͼƬ���
    	for(int i=0; i<nums; i++) {
    		for(int j=0; j<nums; j++) {
    			//public ImageButton(int row, int col, int num, String preName) 
    			ImageButton btn = new ImageButton(i, j, numArr[k], preName + "_");
    			k++; 
    			
    			imagePanel.add(btn);
    			btnArr[i][j] = btn;                  //nums��nums�а�ť
    			btnArr[i][j].addActionListener(this); //�߼�����ͼƬ���һ��
    		}
    	}
    	
    	
    	blankR = nums-1;   //Ĭ�����һ�����һ��
    	blankC = nums-1;    
    	btnArr[blankR][blankC].updateImage(false);
    }
	
	
	public boolean checkOrder() {
		boolean flag = true;
		int n = 1;
		for(int row=0; row<nums; row++) {
			for(int col=0; col<nums; col++) {
				if(btnArr[row][col].getNum() != n) {
					flag = false;
					break;      //ֻ����Сѭ��
				}
				n++;
			}
		}
		return flag;	    
	}
	
	
	public void initGrade() {
		int n = cbGrade.getSelectedIndex();
		if(n==0 || n==1)
			nums = 3;
		else if(n==2)
			nums = 4;
		else if(n==3)
			nums = 5;	
	}
	
	
	public void initImage() {
		int index = cbImage.getSelectedIndex();
		if(index == 0)
			index = 1;
		imageName = index + ".jpg";
		URL url = this.getClass().getResource("/images/" + imageName);  //ͳһ��Դ��λ��
		ImageIcon icon = new ImageIcon(url);
		icon.setImage(icon.getImage().getScaledInstance(220, 220, Image.SCALE_DEFAULT));
		//�ı�ͼƬ��С����ȣ��߶ȣ����ŷ�ʽ��
		lblShow.setIcon(icon);
		
	}
	/**
	 * Create the frame.
	 */

	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 770, 560);
        this.setLocationRelativeTo(this);
        this.setTitle("-��ըС���ʵ�ƴͼ��Ϸ-");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
				
		imagePanel.setBackground(Color.GRAY);
		imagePanel.setBounds(15, 10, 495, 495);
		contentPane.add(imagePanel);
		
		
		lblShow.setBackground(Color.RED);
		lblShow.setBounds(520, 10, 220,220);
		lblShow.setBorder(new TitledBorder(null,"��ͼ"));
		contentPane.add(lblShow);
		
		
		btnMusic.setBounds(576, 398, 113, 27);
		contentPane.add(btnMusic);
		btnMusic.addActionListener(this);
		
		
		btnShow.setBounds(576, 438, 113, 27);
		contentPane.add(btnShow);
		btnShow.addActionListener(this);
		
		
		btnStart.setBounds(576, 478, 113, 27);
		contentPane.add(btnStart);
		btnStart.addActionListener(this);
		
		
		lblNewLabel.setFont(new Font("����", Font.BOLD, 16));
		lblNewLabel.setBounds(530, 243, 87, 27);
		contentPane.add(lblNewLabel);
		
		
		lblLeftTime.setForeground(Color.ORANGE);
		lblLeftTime.setFont(new Font("����", Font.BOLD, 16));
		lblLeftTime.setBounds(645, 247, 72, 18);
		contentPane.add(lblLeftTime);
		
		String []strImage = {"ѡ��ͼƬ", "��","�۲�", "�ɶ�"};
		cbImage = new JComboBox<String>(strImage); //������
		cbImage.setBounds(576, 283, 113, 24);
		contentPane.add(cbImage);
		
		String []strGrade = {"ѡ���Ѷ�","3��3", "4��4", "5��5"};
		cbGrade = new JComboBox<String>(strGrade);
		cbGrade.setBounds(576, 320, 113, 24);
		contentPane.add(cbGrade);
		
		
		btnOrder.setBounds(576, 358, 113, 27);
		contentPane.add(btnOrder);
		btnOrder.addActionListener(this);
		
		URL urlSound = this.getClass().getResource("/music/Taylor Swift - Look What You Made Me Do.wav"); //javaһ�㲻֧��MP3�ļ�
		sound = Applet.newAudioClip(urlSound); //ͬ����Դ��λ��
		
		this.setVisible(true);
		
	}
}
