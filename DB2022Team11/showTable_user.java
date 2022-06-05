package DB2022team11;

import java.sql.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class showTable_user extends JFrame implements ActionListener {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/DB2022team11";
	
	static final String USER = "DB2022team11";
	static final String PASS = "DB2022team11";
	
	public Connection conn;
	public Statement s;
	public ResultSet r;
	
	Container me = this;
	JPanel panel;
	JScrollPane ScPane;
	int count = 0;
	
	Font font = new Font("아임크리수진", Font.PLAIN, 12);
	
	private JComboBox Category; //열람할 테이블 선택하는 콤보버튼
	private RoundedButton Show_Button = new RoundedButton("확인");
	
	//테이블
	private Vector<String> Head = new Vector<String>();
	private JTable table;
	private DefaultTableModel model;
	private static final int BOOLEAN_COLUMN = 0;
	
	//생성자
	public showTable_user() {
		//콤보박스
		JPanel ComboBoxPanel = new JPanel();
		String[] category = {"지역", "매물", "부동산", "건물", "집주인"}; //열람할 테이블 선택
		Category = new JComboBox(category);
		ComboBoxPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel msg = new JLabel("열람하고자 하는 테이블을 선택하세요: ");
		
		msg.setFont(font);
		Category.setFont(font);
		Show_Button.setFont(font);
		
		ComboBoxPanel.add(msg);
		ComboBoxPanel.add(Category);
		ComboBoxPanel.add(Show_Button);
		
		//전체 layout
		JPanel Top = new JPanel();
		Top.setLayout(new BoxLayout(Top, BoxLayout.Y_AXIS));
		Top.add(ComboBoxPanel);
		
		JPanel Halfway = new JPanel();
		Halfway.setLayout(new BoxLayout(Halfway, BoxLayout.X_AXIS));
		
		JPanel Bottom = new JPanel();
		Bottom.setLayout(new BoxLayout(Bottom, BoxLayout.X_AXIS));
		
		JPanel ShowVertical = new JPanel();
		ShowVertical.setLayout(new BoxLayout(ShowVertical, BoxLayout.Y_AXIS));
		ShowVertical.add(Halfway);
		ShowVertical.add(Bottom);
		
		add(Top, BorderLayout.NORTH);
		add(ShowVertical, BorderLayout.SOUTH);
		
		Show_Button.addActionListener(this);
		
		//기본 설정
		setTitle("[어쩔DB] 테이블 보기");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setSize(700, 700);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	//action 처리
	public void actionPerformed(ActionEvent e) {
		
		// ----------- DB 연결 ----------- //
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver"); // JDBC 드라이버 연결
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
		
			System.out.println("정상적으로 연결되었습니다.");
			
		} catch (SQLException e1) {
			System.err.println("연결할 수 없습니다.");
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			System.err.println("드라이버를 로드할 수 없습니다.");
			e1.printStackTrace();
		}
		
		// ----------- 구현 ----------- //
		
		if (count == 1) { //초기화 코드
			me.remove(panel);
			revalidate();
		}
		
		// ----------- 확인 ----------- //
		
		if (e.getSource() == Show_Button) { //확인 버튼 눌렀을 때
			Head.clear();
			Head.add("선택");
			String stmt = "";
			
			if(Category.getSelectedItem().toString() == "지역") { //지역 테이블 보여주기
				Head.add("area id");
				Head.add("구");
				Head.add("동");
				stmt = "SELECT * FROM DB2022_AREA";
			}
			else if(Category.getSelectedItem().toString() == "매물") { //매물 테이블 보여주기
				Head.add("Pid");
				Head.add("agency id");
				Head.add("owner id");
				Head.add("area id");
				Head.add("rent type");
				Head.add("price");
				Head.add("deposit");
				Head.add("sale date");
				Head.add("building id");
				Head.add("address");
				stmt = "SELECT * FROM DB2022_SALE";
			}
			else if(Category.getSelectedItem().toString() == "부동산") { //부동산 테이블 보여주기
				Head.add("agency id");
				Head.add("agency name");
				Head.add("agency address");
				Head.add("agency number");
				Head.add("area id");
				stmt = "SELECT * FROM DB2022_AGENCY";
			}
			else if(Category.getSelectedItem().toString() == "건물") { //건물 테이블 보여주기
				Head.add("building id");
				Head.add("building name");
				Head.add("building type");
				Head.add("area id");
				stmt = "SELECT * FROM DB2022_BUILDING";
			}
			else if(Category.getSelectedItem().toString() == "집주인") { //집주인 테이블 보여주기
				Head.add("owner id");
				Head.add("owner name");
				Head.add("owner number");
				stmt = "SELECT * FROM DB2022_OWNER";
			}
			
			model = new DefaultTableModel(Head, 0);
			table = new JTable(model) {
				@Override
				public Class getColumnClass(int column) {
				if (column == 0) {
					return Boolean.class;
				} else
					return String.class;
				}
			};
			
			try {
				count = 1;
				s = conn.createStatement();
				r = s.executeQuery(stmt);
				ResultSetMetaData rsmd = r.getMetaData();
				int columnCnt = rsmd.getColumnCount();
				
				while (r.next()) {
				Vector<Object> tuple = new Vector<Object>();
				tuple.add(false);
				
					for (int i = 1; i < columnCnt + 1; i++) {
						tuple.add(r.getString(rsmd.getColumnName(i)));
					}
					model.addRow(tuple);
				}
				
			} catch (SQLException ee) {
				System.out.println("actionPerformed err : " + ee);
				ee.printStackTrace();
			}
			
			panel = new JPanel();
			ScPane = new JScrollPane(table);
			table.getModel().addTableModelListener(new CheckBoxModelListener());
			ScPane.setPreferredSize(new Dimension(650, 200));
			panel.add(ScPane);
			add(panel, BorderLayout.CENTER);
			revalidate();
		}
	}
	
	public class CheckBoxModelListener implements TableModelListener {
		
		public void tableChanged(TableModelEvent e) {
			int row = e.getFirstRow();
			int column = e.getColumn();
			if (column == BOOLEAN_COLUMN) {
				TableModel model = (TableModel) e.getSource();
				String columnName = model.getColumnName(1);
				Boolean checked = (Boolean) model.getValueAt(row, column);
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new showTable_user();
	}
}