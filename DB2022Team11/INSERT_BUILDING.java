package DB2022team11;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.event.*;
import java.awt.*;

public class INSERT_BUILDING extends JFrame implements ActionListener{
	JTextField txt1, txt2, txt4;
	JRadioButton rd1, rd2, rd3, rd4;
	
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
	
	public INSERT_BUILDING ( ){
		setTitle("[어쩔 DB] 건물 삽입");
		Color b=new Color(244,244,244);
		setBackground(b);
		setSize(700, 700);
		getContentPane().setLayout(null);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setLocationRelativeTo(null);
		Font f1 = new Font("아임크리수진",Font.PLAIN, 13);
		
		// 레이블 생성
		JLabel lbl1 = new JLabel("건물 id ");
		lbl1.setFont(f1);
		
		JLabel lbl2 = new JLabel("건물 이름  ");
		lbl2.setFont(f1);
		
		JLabel lbl3 = new JLabel("건물 종류 ");
		lbl3.setFont(f1);
		
		JLabel lbl4 = new JLabel("지역 id ");
		lbl4.setFont(f1);
		
		// 20자리 텍스트 필드 생성
		txt1 = new JTextField("Bid000",6); 
		txt2 = new JTextField(20); 
		txt4 = new JTextField("Lid000",20); 

		
		rd1 = new JRadioButton("아파트"); 	
        rd2 = new JRadioButton("단독주택");	
        rd3 = new JRadioButton("빌라");		
        rd4 = new JRadioButton("오피스텔");	
        // 라디오 버튼을 그룹화 하기위한 객체 생성
        ButtonGroup groupRd = new ButtonGroup();
        groupRd.add(rd1);
        groupRd.add(rd2);
        groupRd.add(rd3);
        groupRd.add(rd4);

        
        JPanel ShowAllPanel = new JPanel();
		ShowAllPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		ShowAllPanel.add(Insert_Button);
		ShowAllPanel.add(Show_Button);
		
		JPanel UpdatePanel = new JPanel();
		UpdatePanel.setLayout(new GridLayout(6, 4));
		UpdatePanel.add(lbl1);			UpdatePanel.add(txt1);
		UpdatePanel.add(lbl2);			UpdatePanel.add(txt2);
		UpdatePanel.add(lbl3);			UpdatePanel.add(new JLabel(" ")); 
		UpdatePanel.add(rd1);	UpdatePanel.add(rd2);	UpdatePanel.add(rd3);	UpdatePanel.add(rd4);
		UpdatePanel.add(lbl4);			UpdatePanel.add(txt4);
		

		
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
			
			
			 // -----------  TABLE 보기 ----------- //
			if(e.getSource() == Show_Button) {
				Head.clear();
				Head.add("building_id");
				Head.add("building_name");
				Head.add("building_type");
				Head.add("area_id");
				
				String stmt = "SELECT * FROM DB2022_BUILDING";
				
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
						String[] input = new String[4];
						input[0] = r.getString("building_id");
						input[1] = r.getString("building_name");
						input[2] = r.getString("building_type");
						input[3] = r.getString("area_id");
						model.addRow(input);
					}
					
				} catch(SQLException ee) {
					System.out.println("actionPerformed err: " + ee);
					ee.printStackTrace();
				}
				
				panel = new JPanel();
				ScPane = new JScrollPane(table);
				ScPane.setPreferredSize(new Dimension(600, 400));
				panel.add(ScPane);
				add(panel,BorderLayout.CENTER);
				revalidate();
			}
			
			
			
			// ------------ INSERT 건물 ---------------//
	        if(e.getSource() == Insert_Button) {
	        	try{
		            String id_num = txt1.getText();
		            String name = txt2.getText();
		            String type= " ";
		            String area_id = txt4.getText();
		        
		            if(rd1.isSelected())
		            	 type = rd1.getText();
		            else if(rd2.isSelected())
		            	 type = rd2.getText();
		            else if(rd3.isSelected())
		            	 type = rd3.getText();
		            else if(rd4.isSelected())
		            	 type = rd4.getText();
		            else 
		            	JOptionPane.showMessageDialog(null, "건물 종류를 선택해주세요", "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);

		           PreparedStatement pStmt = conn.prepareStatement(
		            		"insert into DB2022_BUILDING values(?,?,?,?)");
		           	
		           //ID 네자리 이상 불가능
		            if(id_num.length() > 6) {JOptionPane.showMessageDialog(null, "id는 세자리까지 입력가능합니다.", "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);}	            
		           	//ID null값 불가능
		            if(id_num.equals("Bid000") || id_num.isEmpty()) { 
		            	JOptionPane.showMessageDialog(null, "건물 id번호를 입력해주세요. \n", "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE); 
		            }   
		           	else {pStmt.setString(1, id_num);}
		          	pStmt.setString(2, name);
		           	pStmt.setString(3, type);
		          	pStmt.setString(4, area_id);
		           	pStmt.executeUpdate();
		           	conn.commit();
		           	
		        	JOptionPane.showMessageDialog(null, "입력하신 건물정보를 등록하였습니다.");
		        }
		        catch (SQLException sqle) {
		        	sqle.printStackTrace();
					try {
						if(conn!=null)
							conn.rollback();
					}catch (SQLException e1) {
						e1.printStackTrace();
					}
		        	System.out.println("SQLException : " + sqle);
		        	JOptionPane.showMessageDialog(null, "새로운 건물 등록에 실패했습니다. \n", "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE); 
		        }
	        }
	        
	}
	
	public static void main(String args[]){
		new INSERT_BUILDING();
    }
	

}