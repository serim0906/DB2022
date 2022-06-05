package DB2022team11;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

//지역, 매물 종류(매매, 전세, 월세), 비용 조건에 따른 매물 검색 기능 구현 클래스
public class Search extends JFrame{
	String[] areaName = {"서대문구 대현동","서대문구 신촌동", "은평구 신사동", "마포구 연남동"};
	JComboBox<String> area_gu;
	
	//매물 종류 선택 박스
	String[] rentType= {"매매","전세","월세"};
	JComboBox<String> rent_type;
	//최소 최대 비용
	JTextField minPrice = new JTextField(10);
	JTextField maxPrice = new JTextField(10);
	RoundedButton enter = new RoundedButton("Enter");
	//결과 출력 테이블 생성
	String header[] = {"종류", "비용", "보증금", "건물명", "주거 유형", "날짜", "매물 위치"};
	DefaultTableModel model = new DefaultTableModel(header, 0);
	JTable table = new JTable(model);
	
	JScrollPane sc = new JScrollPane(table);

	public Search() { 
        
		setTitle("[어쩔 DB] 매물 검색");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		Container contentPane = getContentPane();
		contentPane.setLayout(new FlowLayout());
		contentPane.setBackground(new Color(244,244,244));
		
		area_gu = new JComboBox(areaName);
		contentPane.add(area_gu);
		
		//매매,전세,월세 선택
		rent_type = new JComboBox(rentType);
		contentPane.add(rent_type);
		
		contentPane.add(minPrice);
		contentPane.add(new JLabel("만원 이상"));
		minPrice.addKeyListener(new MyKeyListener());
		contentPane.add(maxPrice);
		contentPane.add(new JLabel("만원 이하"));
		maxPrice.addKeyListener(new MyKeyListener());
		contentPane.add(enter);
		enter.addActionListener(new MyActionListener());
		enter.addKeyListener(new MyKeyListener());
		
		//결과 출력 테이블 
		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(40);
		columnModel.getColumn(1).setPreferredWidth(50);
		columnModel.getColumn(2).setPreferredWidth(40);
		columnModel.getColumn(3).setPreferredWidth(50);
		columnModel.getColumn(4).setPreferredWidth(50);
		columnModel.getColumn(5).setPreferredWidth(60);
		columnModel.getColumn(6).setPreferredWidth(250);
		contentPane.add(sc);
		sc.setPreferredSize(new Dimension(600,250));
		
		setSize(700, 700); 
		setVisible(true);
		setLocationRelativeTo(null);
	}
	
	private class MyActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			RoundedButton b = (RoundedButton)e.getSource();
			model.setRowCount(0);
			searchResult(minPrice.getText(), maxPrice.getText(), (String)area_gu.getSelectedItem(), (String)rent_type.getSelectedItem());
		}
	}
	private class MyKeyListener implements KeyListener{
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode(); 
			if(key == KeyEvent.VK_ENTER) {
				model.setRowCount(0);
				searchResult(minPrice.getText(), maxPrice.getText(), (String)area_gu.getSelectedItem(), (String)rent_type.getSelectedItem());
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
	
	//입력 조건에 따른 결과 출력 함수
	//지역, 매물 종류, 비용 입력받고 조건 만족하는 매물 정보를 결과 테이블에 추가
	public void searchResult(String minPrice, String maxPrice, String area, String rentType) {
		String area_view = null;
		String min_price = minPrice;
		String max_price = maxPrice;
		String rent_type = rentType;
	    
		switch(area) {
		case "서대문구 대현동":
			area_view = "DB2022_daehyun_dong";
			break;
		case "서대문구 신촌동":
			area_view = "DB2022_sinchon_dong";
			break;
		case "은평구 신사동":
			area_view = "DB2022_sinsa_dong";
			break;
		case "마포구 연남동":
			area_view = "DB2022_yeonnam_dong";
			break;
			
		}
		
		try {   Connection conn = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/DB2022Team11", "DB2022Team11", "DB2022Team11");
				
		PreparedStatement pstmt = conn.prepareStatement(
				"select rent_type, price, deposit, building_name, building_type, sale_date, address "
				+ "from DB2022_SALE join "+area_view+ " on DB2022_SALE.building_id = "+area_view+".building_id "
				+ "where rent_type = ? and price >= ? and price <= ? "); 
		
		pstmt.setString(1, rent_type);
		if(min_price.isEmpty()) {
			pstmt.setInt(2, 0);
		}else {
			pstmt.setInt(2, Integer.parseInt(min_price));
		}
		if(max_price.isEmpty()) {
			pstmt.setInt(3,  10000000);
		}else {
			pstmt.setInt(3, Integer.parseInt(max_price));
		}
	    ResultSet rset = pstmt.executeQuery();
	    
		//조건 만족하는 매물 정보를 배열에 저장, 테이블 row에 추가
		while(rset.next()) {
			String[] input = new String[7];
			input[0] = rset.getString("rent_type");
			input[1] = Integer.toString(rset.getInt("price"));
			input[2] = Integer.toString(rset.getInt("deposit"));
			input[3] = rset.getString("building_name");
			input[4] = rset.getString("building_type");
			input[5] = rset.getString("sale_date");
			input[6] = rset.getString("address");
			
			model.addRow(input);
		}
	}catch(SQLException se) {
		se.printStackTrace();
	}}
	
	public static void main(String[] args) {
		Search search = new Search();
	}

}