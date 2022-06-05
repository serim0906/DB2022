package DB2022team11;

import java.sql.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;


public class updateBuilding extends JFrame implements ActionListener{
	public Connection conn;
	public Statement s;
	public ResultSet r;
	
	private Vector<String> Head = new Vector<String>();
	
	private JTable table;
	private DefaultTableModel model;
	private static final int BOOLEAN_COLUMN = 0;
	private int ID_COLUMN = 0;
	private int NAME_COLUMN = 0;
	private int NUMBER_COLUMN = 0;
	
	// 보기 & 수정 버튼
	private RoundedButton Show_Button = new RoundedButton("보기");
	private RoundedButton Update_Button = new RoundedButton("수정");
	Container me = this;
	
	JPanel panel;
	JScrollPane ScPane;
	private JLabel Setlabel_1 = new JLabel("새로운 이름: ");
	private JLabel Setlabel_2 = new JLabel("새로운 건물 유형: ");
	private JTextField setName = new JTextField(10);
	private JRadioButton rd1, rd2, rd3, rd4;
	int count = 0;
	
	public updateBuilding() {
		Font f1 = new Font("아임크리수진",Font.PLAIN, 13);
		Setlabel_1.setFont(f1);
		Setlabel_2.setFont(f1);
		
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
        
		// 버튼 패널
		JPanel ShowAllPanel = new JPanel();
		ShowAllPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		ShowAllPanel.add(Update_Button);
		ShowAllPanel.add(Show_Button);
		
		// 빌딩 update 패널
		JPanel UpdatePanel = new JPanel();
		UpdatePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		UpdatePanel.add(Setlabel_1);
		UpdatePanel.add(setName);
		UpdatePanel.add(new JLabel("  "));
		UpdatePanel.add(Setlabel_2);
		UpdatePanel.add(rd1);	UpdatePanel.add(rd2);	UpdatePanel.add(rd3);	UpdatePanel.add(rd4);

		Container contentPane = getContentPane();
		contentPane.setLayout(new FlowLayout());
		contentPane.setBackground(new Color(244, 244, 244));
			
		contentPane.add(UpdatePanel);
		contentPane.add(ShowAllPanel);
	
		
		Show_Button.addActionListener(this);
		Update_Button.addActionListener(this);
		
		setTitle("[어쩔 DB] 건물 수정");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setSize(700, 700);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		// DB 연결
		try {
			Class.forName("com.mysql.jdbc.Driver");  // JDBC 드라이버 연결
			
			String user = "db2022team11";
			String pwd = "db2022team11";
			String url = "jdbc:mysql://localhost:3306/DB2022team11";
					
			conn = DriverManager.getConnection(url, user, pwd);
			System.out.println("정상적으로 연결되었습니다.");
		} catch(SQLException e1) {
			System.out.println("연결할 수 없습니다.");
			e1.printStackTrace();
		} catch(ClassNotFoundException e1) {
			System.out.println("드라이버를 로드할 수 없습니다.");
			e1.printStackTrace();
		}
		
		// --------- // 
		if(count==1) {
			me.remove(panel);
			revalidate();
		}
		
		// table 보기
		if(e.getSource() == Show_Button) {
			Head.clear();
			Head.add("선택");
			Head.add("building_id");
			Head.add("building_name");
			Head.add("building_type");
		 
			// select문으로 DB2022_BUILDING 테이블 전체 보기
			String stmt = "SELECT * FROM DB2022_BUILDING";
			
			// DefaultTablemodel 및 JTable 생성(update 위해 해당 열 저장)
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
			for(int i=0; i<Head.size(); i++) {
				if(Head.get(i) == "building_id") {
					ID_COLUMN = i;
				}
				else if(Head.get(i) == "building_name") {
					NAME_COLUMN = i;
				} else if(Head.get(i) == "building_type") {
					NUMBER_COLUMN = i;
				}
			}
			// JTable은 행의 1열의 Boolean 값을 '선택'열에서 체크박스로 나타내기 위해 Boolean.class 반환
			table = new JTable(model) {
				@Override
				public Class getColumnClass(int column) {
					if(column == 0) {
						return Boolean.class;  // 선택 체크박스
					} else
						return String.class;
				}
			};
			
			try {
				count = 1;
				s = conn.createStatement();
				r = s.executeQuery(stmt);

				while(r.next()) {
					String[] input = new String[5];
					input[1] = r.getString("building_id");
					input[2] = r.getString("building_name");
					input[3] = r.getString("building_type");
					model.addRow(input);
				}
				
			} catch(SQLException ee) {
				System.out.println("actionPerformed err: " + ee);
				ee.printStackTrace();
			}
			
			panel = new JPanel();
			ScPane = new JScrollPane(table);
		
			ScPane.setPreferredSize(new Dimension(600, 500));
			panel.add(ScPane);
			add(panel,BorderLayout.CENTER);
			revalidate();
		}
		
		
		// UPDATE
		// for, if문을 통해 '선택' 체크박스가 체크된 행의 building_id를 vector에 더함
		if(e.getSource() == Update_Button) {
			Vector<String> update_building_id = new Vector<String>();
			try {
				String columnName_1 = model.getColumnName(2);  // 건물 이름
				String columnName_2 = model.getColumnName(3);  // 건물종류
				if(columnName_1 == "building_name" && columnName_2 == "building_type") {
					for(int i=0; i<table.getRowCount();i++){
						// '선택' 체크박스가 체크되었을 경우 컬럼 값 가져옴
						if(table.getValueAt(i, 0) == Boolean.TRUE) {
							update_building_id.add((String) table.getValueAt(i, 1));
							String updateName = setName.getText();
							String updateType = ""; 
							if(rd1.isSelected())
								updateType = rd1.getText();
				            else if(rd2.isSelected())
				            	updateType = rd2.getText();
				            else if(rd3.isSelected())
				            	updateType = rd3.getText();
				            else if(rd4.isSelected())
				            	updateType = rd4.getText();

							table.setValueAt(updateName,  i,  NAME_COLUMN);
							table.setValueAt(updateType,  i,  NUMBER_COLUMN);
						}
					}
					for(int i=0; i<update_building_id.size(); i++) {
						// update문
						String updateStmt = "UPDATE DB2022_BUILDING SET building_name = ?, building_type = ? WHERE building_id = ?";
						PreparedStatement p = conn.prepareStatement(updateStmt);
						p.clearParameters();
						conn.setAutoCommit(false);  // 트랜잭션
						String updateName = setName.getText();
						String updateType = " "; 
						if(rd1.isSelected())
							updateType = rd1.getText();
			            else if(rd2.isSelected())
			            	updateType = rd2.getText();
			            else if(rd3.isSelected())
			            	updateType = rd3.getText();
			            else if(rd4.isSelected())
			            	updateType = rd4.getText();
						p.setString(1, updateName);
						p.setString(2, updateType);
						p.setString(3, String.valueOf(update_building_id.get(i)));
						p.executeUpdate();
						conn.commit();
					}
				} 
			} catch(SQLException sqle) {
				sqle.printStackTrace();
					try {
						if(conn!=null)
							conn.rollback();
					}catch (SQLException e1) {
						e1.printStackTrace();
					}
				System.out.println("actionPerformed err: " + sqle);	
		}
			panel = new JPanel();
			ScPane = new JScrollPane(table);
			ScPane.setPreferredSize(new Dimension(600, 500));
			panel.add(ScPane);
			add(panel,BorderLayout.CENTER);
			revalidate();
		}  // UPDATE 끝
	}	
	
	public static void main(String[] args) {
		new updateBuilding();
	}
}