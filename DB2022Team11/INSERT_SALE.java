package DB2022team11;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.w3c.dom.ls.LSOutput;

import java.awt.event.*;
import java.awt.*;

public class INSERT_SALE extends JFrame implements ActionListener{
	
	JTextField txt1, txt2, txt3, txt4, txt6, txt7,txt8,txt9;
	JRadioButton rd1, rd2, rd3;
	
	public Statement s;
	public ResultSet r;
	
	private Vector<String> Head = new Vector<String>();
	
	private JTable table;
	private DefaultTableModel model;
	private int ID_COLUMN = 0;
	private int NAME_COLUMN = 0;
	private int NUMBER_COLUMN = 0;

	private RoundedButton Show_Button = new RoundedButton("보기");
	private RoundedButton Insert_Button = new RoundedButton("등록");
	Container me = this;
	
	JPanel panel;
	JScrollPane ScPane;
	int count = 0;
	
	public INSERT_SALE ( ){
		setTitle("[어쩔 DB] 매물 삽입");
		Color b=new Color(244,244,244);
		setBackground(b);
		setSize(700, 700);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setLocationRelativeTo(null);
		Font f1 = new Font("아임크리수진",Font.PLAIN, 13);
		
		// 레이블 생성
		JLabel lbl1 = new JLabel("매물 id ");
		lbl1.setFont(f1);
		JLabel lbl2 = new JLabel("부동산 id  ");
		lbl2.setFont(f1);
		JLabel lbl3 = new JLabel("집주인 id ");
		lbl3.setFont(f1);
		JLabel lbl4 = new JLabel("지역 id ");
		lbl4.setFont(f1);
		JLabel lbl5 = new JLabel("매물 종류 ");
		lbl5.setFont(f1);
		JLabel lbl6 = new JLabel("매매가/전세가 ");
		lbl6.setFont(f1);
		JLabel lbl7 = new JLabel(" 보증금 ");
		lbl7.setFont(f1);
		JLabel lbl8 = new JLabel("건물 id ");
		lbl8.setFont(f1);
		
		JLabel lbl9 = new JLabel("상세 주소");
		lbl9.setFont(f1);
		
		JLabel won1 = new JLabel("만원");
		won1.setFont(f1);
		JLabel won2 = new JLabel("만원");
		won2.setFont(f1);
		
		
		//텍스트 필드 생성
		txt1 = new JTextField("Pid000",6); 
		txt2 = new JTextField("Aid000", 6); 
		txt3 = new JTextField("Oid000", 6); 
		txt4 = new JTextField("Lid000", 6); 
		txt6 = new JTextField(20); //매매가
		txt7 = new JTextField(20); //보증금	
		txt8 = new JTextField("Bid000", 20); //빌딩아이디	
		txt9 = new JTextField(20); //상세주소
	
		rd1 = new JRadioButton("매매"); 	rd1.setBounds(220, 270, 50, 20);
        rd2 = new JRadioButton("전세");	rd2.setBounds(300, 270, 50, 20);
        rd3 = new JRadioButton("월세");	rd3.setBounds(380, 270, 50, 20);
        // 라디오 버튼을 그룹화 하기위한 객체 생성
        ButtonGroup groupRd = new ButtonGroup();
        groupRd.add(rd1);
        groupRd.add(rd2);
        groupRd.add(rd3);
        

        JPanel ShowAllPanel = new JPanel();
		ShowAllPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		ShowAllPanel.add(Insert_Button);
		ShowAllPanel.add(Show_Button);

		
		JPanel UpdatePanel = new JPanel();
		UpdatePanel.setLayout(new GridLayout(10,4));
		UpdatePanel.add(lbl1);			UpdatePanel.add(txt1);
		UpdatePanel.add(lbl2);			UpdatePanel.add(txt2);
		UpdatePanel.add(lbl3);			UpdatePanel.add(txt3);
		UpdatePanel.add(lbl4);			UpdatePanel.add(txt4);
		UpdatePanel.add(lbl5);			UpdatePanel.add(rd1);	UpdatePanel.add(rd2);	UpdatePanel.add(rd3);
		UpdatePanel.add(lbl6);			UpdatePanel.add(txt6); 
		UpdatePanel.add(lbl7);			UpdatePanel.add(txt7);
		UpdatePanel.add(lbl8);			UpdatePanel.add(txt8);
		UpdatePanel.add(lbl9);			UpdatePanel.add(txt9);


		
		Container contentPane = getContentPane();
		contentPane.setLayout(new FlowLayout());
		contentPane.setBackground(new Color(244, 244, 244));
			
		contentPane.add(UpdatePanel);
		contentPane.add(ShowAllPanel);
		
		Show_Button.addActionListener(this);
		Insert_Button.addActionListener(this);

		setVisible(true);
	} 
	
	@Override
	public void actionPerformed(ActionEvent e) {
		 Connection conn = null;
	        
	     //JDBC 연결 부분
	     try{
	    	 String url = "jdbc:mysql://localhost:3306/DB2022Team11";
	       	 
	        //Database user, password
	        String  user = "db2022team11";
	        String password ="db2022team11";
	        	
	        conn = DriverManager.getConnection(url, user, password);
	        System.out.println("Insert_AGENCY Successfully Connection!");	//연결 확인 메세지
	        conn.setAutoCommit(false);
	     }
	     catch(SQLException e1) {
	    	System.out.println("연결할 수 없습니다.");
			e1.printStackTrace();
	     }
	     
			// --------- // 
			if(count==1) {
				me.remove(panel);
				revalidate();
			}
			
			
			 // ----------- SALE TABLE 보기 ----------- //
			if(e.getSource() == Show_Button) {
				Head.clear();
				Head.add("sale id");
				Head.add("agency id");
				Head.add("owner id");
				Head.add("area id");
				Head.add("rent type");
				Head.add("price");
				Head.add("deposit");
				Head.add("sale date");
				Head.add("building id");
				Head.add("address");
				
				String stmt = "SELECT * FROM DB2022_SALE";
				
				model = new DefaultTableModel(Head, 0) {
					@Override
					public boolean isCellEditable(int row, int column) {
						if(column > 0) {
							return false;
						} else {
							return true;
						}
					}
				};

				
				table = new JTable(model);
				
				try {
					count = 1;
					s = conn.createStatement();
					r = s.executeQuery(stmt);

					while(r.next()) {
						String[] input = new String[10];
						input[0] = r.getString("Pid");
						input[1] = r.getString("agency_id");
						input[2] = r.getString("owner_id");
						input[3] = r.getString("area_id");
						input[4] = r.getString("rent_type");
						input[5] = r.getString("price");
						input[6] = r.getString("deposit");
						input[7] = r.getString("sale_date");
						input[8] = r.getString("building_id");
						input[9] = r.getString("address");
						
						model.addRow(input);
					}
					
				} catch(SQLException ee) {
					System.out.println("actionPerformed err: " + ee);
					ee.printStackTrace();
				}
				
				panel = new JPanel();
				ScPane = new JScrollPane(table);
				ScPane.setPreferredSize(new Dimension(650, 350));
				panel.add(ScPane);
				add(panel,BorderLayout.CENTER);
				revalidate();
			}
			
			
			
			// ------------ INSERT 매물 ---------------//
			if(e.getSource() == Insert_Button) {
				try{
					String id_num, agency_id, owner_id, area_id, rent_type, date, building_id , address, price, deposit;
					id_num = txt1.getText();					
		            agency_id = txt2.getText(); 				owner_id = txt3.getText();   		   
		            area_id = txt4.getText();					rent_type = null;	
		            price = txt6.getText();	
		            deposit = txt7.getText();				
		            building_id = txt8.getText();			address = txt9.getText();
		            
		            // 현재 날짜 구하기
		            LocalDate now = LocalDate.now();
		            date = now.toString();
	
		            if(rd1.isSelected())
		            	rent_type = rd1.getText();
		            else if(rd2.isSelected())
		            	rent_type = rd2.getText();
		            else if(rd3.isSelected())
		            	rent_type = rd3.getText();
		            else 
		            	JOptionPane.showMessageDialog(null, "건물 종류를 선택해주세요 \n", "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
		            
		            PreparedStatement pStmt = conn.prepareStatement(
		            		"insert into DB2022_SALE values(?,?,?,?,?,?,?,?,?,?)");
		            
		            //ID 네자리 이상 불가능
		            if(id_num.length() > 6) {JOptionPane.showMessageDialog(null, "id는 세자리까지 입력가능합니다.", "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);}	            
		           	//ID null값 불가능
		           	if(id_num.equals("Pid000") || id_num.isEmpty()) {//id는 not null                  
		            	JOptionPane.showMessageDialog(null, "매뭏 id번호를 입력해주세요. \n", "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE); 
		            }   
		           	else { pStmt.setString(1, id_num);}
		          	pStmt.setString(2, agency_id);
		           	pStmt.setString(3, owner_id);
		          	pStmt.setString(4, area_id);
		           	pStmt.setString(5, rent_type);
		           	if(!price.isBlank())   {pStmt.setInt(6, Integer.parseInt(price));}
		           	else {pStmt.setString(6,null);}
		           	if(!deposit.isBlank()) {pStmt.setInt(7, Integer.parseInt(deposit));}
		           	else {pStmt.setString(7,null);}
		           	pStmt.setString(8, date);
		           	pStmt.setString(9, building_id);
		           	pStmt.setString(10, address);
		           	pStmt.executeUpdate();
		           	conn.commit();
		        	JOptionPane.showMessageDialog(null, "입력하신 매물을 등록하였습니다.");
		        }catch (SQLException sqle) {
		        	sqle.printStackTrace();
					try {
						if(conn!=null)
							conn.rollback();
					}catch (SQLException e1) {
						e1.printStackTrace();
					}
		        	System.out.println("SQLException : " + sqle);
		        	JOptionPane.showMessageDialog(null, "새로운 매물 등록에 실패했습니다. \n", "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE); 
		        } 
			}
		}
	
	
	
	public static void main(String args[]){
		new INSERT_SALE();
   }
	

}