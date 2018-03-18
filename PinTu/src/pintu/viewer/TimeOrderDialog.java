package pintu.viewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import pintu.dao.HandleDB;
import pintu.entity.TimeOrder;

public class TimeOrderDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	
	
	public TimeOrderDialog(MainFrame parent,boolean isShow) {
		super(parent,isShow);    //�������Ƿ�ɲ���
		
	
		
		this.setTitle("��ʾ���а�");
		setBounds(100, 100, 450, 300);
		this.setResizable(false);
		this.setLocationRelativeTo(this);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel label = new JLabel("\u6392\u884C\u699C");
		label.setFont(new Font("����", Font.PLAIN, 18));
		label.setBounds(178, 13, 82, 30);
		contentPanel.add(label);
		
		JTextArea taResult = new JTextArea();
		taResult.setBackground(Color.LIGHT_GRAY);
		taResult.setEditable(false);
		taResult.setBounds(40, 40, 352, 163);
		contentPanel.add(taResult);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		HandleDB hdb = new HandleDB();
		ArrayList<TimeOrder> al =hdb.selectInfo();
		if(al.size()==0) {
			taResult.setText("��ʱû����Ϸ��¼");
		}else {
			taResult.append("����\t����\t����ʱ��\r\n\r\n");
			for(int i=0; i<al.size(); i++) {
				TimeOrder to = al.get(i);
				taResult.append("��"+(i+1)+"��\t" + to.getName() +"\t" + to.getTime() +"\r\n");
			}
		}
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
}
