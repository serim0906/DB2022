package DB2022team11;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class Menu_Customer extends JFrame{
	public Menu_Customer() {
		Color b=new Color(244,244,244); 
		Color lb=new Color(34,54,77);
		
		setTitle("[어쩔 DB] 세입자 메뉴");
		setSize(700, 700);
		setBackground(b);
		getContentPane().setLayout(null);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Font f1 = new Font("아임크리수진",Font.PLAIN, 30);
		Font f2 = new Font("아임크리수진",Font.PLAIN, 20);
		
		
		JLabel lbl1 = new JLabel("~어쩔 부동산DB에 오신 것을 환영합니다~");
		lbl1.setBounds(80, 150, 600, 50);
		lbl1.setFont(f1);	lbl1.setForeground(lb);
		getContentPane().add(lbl1);
		
		JLabel lbl2 = new JLabel("원하시는 메뉴를 선택해주세요");
		lbl2.setBounds(200, 200, 600, 50);
		lbl2.setFont(f2);	lbl2.setForeground(lb);
		getContentPane().add(lbl2);

		//검색 버튼
		RoundedButton2 btnSearch = new RoundedButton2("1.  원하는  매물  검색하기");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Search();
			}
		});
		btnSearch.setBounds(90, 350, 500, 50); //가로 세로 폭 높이
		getContentPane().add(btnSearch);

		
		//DB 정보 보기 버튼
		RoundedButton2 btnCustomer = new RoundedButton2("2.  매물  정보  보기       ");
		btnCustomer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new showTable_user();		
			}
		});
		btnCustomer.setBounds(90, 425, 500, 50);
		getContentPane().add(btnCustomer);

		
		setVisible(true);
	}
}