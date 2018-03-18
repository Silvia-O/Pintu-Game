package pintu.viewer;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class ImageButton extends JButton {
    private int row;          //所在行
    private int col;          //所在列
    private int num;          //图片编号
    private String preName;   //图片前缀
    
	public ImageButton(int row, int col, int num, String preName) {
		super();
		this.row = row;    
		this.col = col;    
		this.num = num;    
		this.preName = preName; 
		updateImage(true);
	}
	
	
	/*获得小图片*/
	public void updateImage(boolean isShow) {
		if(isShow) {
			String fileName = this.getClass().getResource
					("/images/subImages/" + preName + num + ".jpg").getFile(); //得到小图片网址路径
			ImageIcon icon = new ImageIcon(fileName);
			this.setIcon(icon);
			//获得发布后系统根目录 bin
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
