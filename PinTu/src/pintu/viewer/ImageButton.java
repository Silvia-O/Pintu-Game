package pintu.viewer;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class ImageButton extends JButton {
    private int row;          //������
    private int col;          //������
    private int num;          //ͼƬ���
    private String preName;   //ͼƬǰ׺
    
	public ImageButton(int row, int col, int num, String preName) {
		super();
		this.row = row;    
		this.col = col;    
		this.num = num;    
		this.preName = preName; 
		updateImage(true);
	}
	
	
	/*���СͼƬ*/
	public void updateImage(boolean isShow) {
		if(isShow) {
			String fileName = this.getClass().getResource
					("/images/subImages/" + preName + num + ".jpg").getFile(); //�õ�СͼƬ��ַ·��
			ImageIcon icon = new ImageIcon(fileName);
			this.setIcon(icon);
			//��÷�����ϵͳ��Ŀ¼ bin
		}else {
			this.setIcon(null);
		}
	}
	
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getPreName() {
		return preName;
	}
	public void setPreName(String preName) {
		this.preName = preName;
	}
    
}
