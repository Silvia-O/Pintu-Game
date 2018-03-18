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
	private JButton btnShow = new JButton("游戏说明");
	private JButton btnOrder = new JButton("显示排行榜");
	private JButton btnMusic = new JButton("开启音乐");
	private JButton btnStart = new JButton("开始游戏");
	JComboBox<String> cbImage = null;
	JComboBox<String> cbGrade = null;
	private int nums = 3;   //标记难度等级
	HandleDB hdb = new HandleDB();
	private AudioClip sound = null; 
	
	
	// ctrl + shift + O 导包
	private Random rand = new Random();
	private int []numArr = null;
	
	private boolean isRun = false;   //游戏是否开始了
	
	private String imageName = "1.jpg";
	private int leftTime;  //初始剩余时间
	private Timer timer;
	
	private int blankR = 2;   //隐藏图片的行 
	private int blankC = 2;   //隐藏图片的列
	
	ImageButton [][]btnArr = null;   
	private final JLabel lblNewLabel = new JLabel("剩余时间:");
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
				JOptionPane.showMessageDialog(null, "游戏时间到了！");
				this.cancel();
				
			}
		}
		
	}
	
	public void startThead() {
		if(timer !=null)      //避免重新游戏后多个任务同时进行
			timer.cancel();
		
		leftTime = 1200;
		timer = new Timer();
		timer.schedule(new MyTask(), 1000,1000); //一秒后启动线程，每隔一秒响应线程
	}
	
	
	public void musicSwitch(ActionEvent e) {
		JButton btn = (JButton)e.getSource(); //事件源指向音乐按钮
		if("开启音乐".equals(btn.getText().trim())) {
			sound.loop();
			btn.setText("关闭音乐");
		}else {
			sound.stop();
			btn.setText("开启音乐");
		}
	}
	
	
	//ctrl + shift +f 重排代码
    @Override
  	public void actionPerformed(ActionEvent arg0) {
    	if(arg0.getSource() == btnMusic) {
    		musicSwitch(arg0);
    	}else if(arg0.getSource() == btnShow) {
    		JOptionPane.showMessageDialog(this,"这个游戏真的不会玩！ made by油炸小汪肥");
    	}else if(arg0.getSource() == btnStart) {
    		//确认对话框 返回值： 是0 否1 取消2
    		if(isRun) {
    		    int n = JOptionPane.showConfirmDialog(this, "您真的要重新开始游戏吗？");
    		    if(n == 0)
        		    init();
    		        startThead();
    		}else {
    			init();
    			startThead();
    			isRun = true;
    			btnStart.setText("重新开始");
    		}
   	
    	
    	}else if(arg0.getSource() == btnOrder) {
    		new TimeOrderDialog(this,true);   //弹出dialog时主窗体不可动
    	}else {
    		switchImage(arg0);
    	}
	}
    
    
    public void switchImage(ActionEvent e) {
    	ImageButton btn = (ImageButton) e.getSource();
    	int row = btn.getRow();
    	int col = btn.getCol();
    	/*判断与隐藏图片是否相邻*/
    	if(Math.abs(row - blankR) + Math.abs(col - blankC) == 1) {   //abs求绝对值
    		//交换图片就是把两个按钮上的图片序号交换，再更新按钮上的图片即可
    		int temp = btnArr[row][col].getNum();
    		//交换图片序号
    		btnArr[row][col].setNum(btnArr[blankR][blankC].getNum());
    		btnArr[blankR][blankC].setNum(temp);
    		//更新按钮图片
    		btnArr[row][col].updateImage(false);     
    		btnArr[blankR][blankC].updateImage(true);
    		 //更新隐藏图片的行列
    		blankR = row;   
    		blankC = col;
    		
    		if(checkOrder()) {
    			//第一个参数指消息框依赖的父亲组建，第二个参数指消息框中内容
    			JOptionPane.showMessageDialog(this, "WIN!");
    			int leftTime = Integer.parseInt(lblLeftTime.getText());
    			int time = 1200 - leftTime;   //本次游戏所用时间
    			String name = JOptionPane.showInputDialog(this, "请输入昵称");
    			if(name == null || "".equals(name.trim())) {   //不输入或输入空字符串
    				name = "匿名";
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
    	
        /*随机洗牌*/
    	for(int i=0; i<nums*nums-2; i++) {  //9号图片始终隐藏，只需打乱1-8号图片
    		//从下标为i到下标为7的数中随机选择一个
    		int n = rand.nextInt(nums*nums-i-1)+i;     // 范围是[i,i+nums*nums-i-1-1]
    		int temp = numArr[i];
    		numArr[i] = numArr[n];
    		numArr[n] = temp;
    	}
    	/*//测试
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
    	
    	
    	// 前缀为系统当前时间
    	long l = System.currentTimeMillis();
    	String preName = String.valueOf(l);
    	//cuttingImage(int size, int rowSize, int colSize, String preName, String imageName)
    	hi.cuttingImage(495/nums, nums, nums, preName, imageName);
    	
    	
    	int k = 0;   //图片编号
    	for(int i=0; i<nums; i++) {
    		for(int j=0; j<nums; j++) {
    			//public ImageButton(int row, int col, int num, String preName) 
    			ImageButton btn = new ImageButton(i, j, numArr[k], preName + "_");
    			k++; 
    			
    			imagePanel.add(btn);
    			btnArr[i][j] = btn;                  //nums行nums列按钮
    			btnArr[i][j].addActionListener(this); //逻辑上与图片面板一致
    		}
    	}
    	
    	
    	blankR = nums-1;   //默认最后一行最后一列
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
					break;      //只跳出小循环
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
		URL url = this.getClass().getResource("/images/" + imageName);  //统一资源定位符
		ImageIcon icon = new ImageIcon(url);
		icon.setImage(icon.getImage().getScaledInstance(220, 220, Image.SCALE_DEFAULT));
		//改变图片大小（宽度，高度，缩放方式）
		lblShow.setIcon(icon);
		
	}
	/**
	 * Create the frame.
	 */

	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 770, 560);
        this.setLocationRelativeTo(this);
        this.setTitle("-油炸小汪肥的拼图游戏-");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
				
		imagePanel.setBackground(Color.GRAY);
		imagePanel.setBounds(15, 10, 495, 495);
		contentPane.add(imagePanel);
		
		
		lblShow.setBackground(Color.RED);
		lblShow.setBounds(520, 10, 220,220);
		lblShow.setBorder(new TitledBorder(null,"样图"));
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
		
		
		lblNewLabel.setFont(new Font("宋体", Font.BOLD, 16));
		lblNewLabel.setBounds(530, 243, 87, 27);
		contentPane.add(lblNewLabel);
		
		
		lblLeftTime.setForeground(Color.ORANGE);
		lblLeftTime.setFont(new Font("宋体", Font.BOLD, 16));
		lblLeftTime.setBounds(645, 247, 72, 18);
		contentPane.add(lblLeftTime);
		
		String []strImage = {"选择图片", "我","聚餐", "派对"};
		cbImage = new JComboBox<String>(strImage); //处理泛型
		cbImage.setBounds(576, 283, 113, 24);
		contentPane.add(cbImage);
		
		String []strGrade = {"选择难度","3×3", "4×4", "5×5"};
		cbGrade = new JComboBox<String>(strGrade);
		cbGrade.setBounds(576, 320, 113, 24);
		contentPane.add(cbGrade);
		
		
		btnOrder.setBounds(576, 358, 113, 27);
		contentPane.add(btnOrder);
		btnOrder.addActionListener(this);
		
		URL urlSound = this.getClass().getResource("/music/Taylor Swift - Look What You Made Me Do.wav"); //java一般不支持MP3文件
		sound = Applet.newAudioClip(urlSound); //同意资源定位符
		
		this.setVisible(true);
		
	}
}
