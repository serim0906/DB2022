package DB2022team11;

import java.sql.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;


public class updateOwner extends JFrame implements ActionListener{
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
	private JLabel Setlabel_2 = new JLabel("새로운 전화번호: ");
	private JTextField setName = new JTextField(10);
	private JTextField setNumber = new JTextField(20);
	int count = 0;
	
	public updateOwner() {
		Font f1 = new Font("아임크리수진",Font.PLAIN, 13);
		Setlabel_1.setFont(f1);
		Setlabel_2.setFont(f1);
		
		// 버튼 패널
		JPanel ShowAllPanel = new JPanel();
		ShowAllPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		ShowAllPanel.add(Update_Button);
		ShowAllPanel.add(Show_Button);
		
		// 집주인 update 패널
		JPanel UpdatePanel = new JPanel();
		UpdatePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		UpdatePanel.add(Setlabel_1);
		UpdatePanel.add(setName);
		UpdatePanel.add(Setlabel_2);
		UpdatePanel.add(setNumber);

		
		Container contentPane = getContentPane();
		contentPane.setLayout(new FlowLayout());
		contentPane.setBackground(new Color(244, 244, 244));
			
		contentPane.add(UpdatePanel);
		contentPane.add(ShowAllPanel);
		
		Show_Button.addActionListener(this);
		Update_Button.addActionListener(this);
		
		setTitle("[어쩔 DB] 집주인 수정");
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
			Head.add("owner_id");
			Head.add("owner_name");
			Head.add("owner_number");
			
			// select문으로 DB2022_OWNER 테이블 전체 보기
			String stmt = "SELECT * FROM DB2022_OWNER";
			
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
				if(Head.get(i) == "owner_id") {
					ID_COLUMN = i;
				}
				else if(Head.get(i) == "owner_name") {
					NAME_COLUMN = i;
				} else if(Head.get(i) == "owner_number") {
					NUMBER_COLUMN = i;
				}
			}
			// JTable은 행의 1열의 Boolean 값을 '선택'열에서 체크박스로 나타내기 위해 Boolean.class 반환
			table = new JTable(model) {
				@SuppressWarnings("unchecked")
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
					String[] input = new String[4];
					input[1] = r.getString("owner_id");
					input[2] = r.getString("owner_name");
					input[3] = r.getString("owner_number");
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
		// for, if문을 통해 '선택' 체크박스가 체크된 행의 owner_id를 vector에 더함
		if(e.getSource() == Update_Button) {
			Vector<String> update_owner_id = new Vector<String>();
			try {
				String columnName_1 = model.getColumnName(2);  // 집주인 이름
				String columnName_2 = model.getColumnName(3);  // 전화번호
				if(columnName_1 == "owner_name" && columnName_2 == "owner_number") {
					for(int i=0; i<table.getRowCount();i++){
						// '선택' 체크박스가 체크되었을 경우 컬럼 값 가져옴
						if(table.getValueAt(i, 0) == Boolean.TRUE) {
							update_owner_id.add((String) table.getValueAt(i, 1));
							String updateName = setName.getText();
							String updateNumber = setNumber.getText();
							table.setValueAt(updateName,  i,  NAME_COLUMN);
							table.setValueAt(updateNumber,  i,  NUMBER_COLUMN);
						}
					}
					for(int i=0; i<update_owner_id.size(); i++) {
						// update문
						conn.setAutoCommit(false);  // 트랜잭션 
						String updateStmt = "UPDATE DB2022_OWNER SET owner_name = ?, owner_number = ? WHERE owner_id = ?";
						PreparedStatement p = conn.prepareStatement(updateStmt);
						p.clearParameters();
						String updateName = setName.getText();
						String updateNumber = setNumber.getText();
						p.setString(1, updateName);
						p.setString(2, updateNumber);
						p.setString(3, String.valueOf(update_owner_id.get(i)));
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
		new updateOwner();
	}
}