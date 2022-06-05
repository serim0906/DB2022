package DB2022team11;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class Start_screen extends JFrame {
	private Image background=new ImageIcon("C:\\first_image.png").getImage();  //배경이미지

	public Start_screen() {
		setTitle("어쩔매물 DB Service");
		setSize(700, 700);
		getContentPane().setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		//DB 관리자 유저
		RoundedButton btnManager = new RoundedButton("관리자 로그인");
		btnManager.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//DB 관리자용 프레임 객체 선언
				new Menu_DBManager();
				//첫 화면은 꺼짐
				setVisible(false);
			}
		});
		btnManager.setBounds(250, 400, 150, 50); //가로 세로 폭 높이
		getContentPane().add(btnManager);

		
		//세입자 유저 
		RoundedButton btnCustomer = new RoundedButton("세입자 로그인");
		btnCustomer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//세입자용 프레임 객체 선언
				new Menu_Customer();
				//첫 화면은 꺼짐
				setVisible(false);
				
			}
		});
		btnCustomer.setBounds(250, 475, 150, 50);
		getContentPane().add(btnCustomer);

		setVisible(true);
	}
	
	public void paint(Graphics g) {//그리는 함수
		g.drawImage(background, 0, 0, null);//background를 그려줌
	}
	 
	public static void main(String[] args) {
		new Start_screen();
	}


}