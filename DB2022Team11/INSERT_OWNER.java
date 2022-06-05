package DB2022team11;

import java.sql.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.awt.*;
import javax.swing.event.TableModelEvent;


public class INSERT_OWNER extends JFrame implements ActionListener{
	JTextField txt1, txt2, txt3;
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
	
	public INSERT_OWNER ( ){
		setTitle("[어쩔 DB] 집주인 삽입");
		Color b=new Color(244,244,244);
		setBackground(b);
		setSize(700, 700);
		getContentPane().setLayout(null);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setLocationRelativeTo(null);
		Font f1 = new Font("아임크리수진",Font.PLAIN, 13);
		
		
		// 레이블 생성
		JLabel lbl1 = new JLabel("집주인 id ");
		lbl1.setFont(f1);
		JLabel lbl2 = new JLabel("집주인 이름  ");
		lbl2.setFont(f1);
		JLabel lbl3 = new JLabel("집주인 전화번호  ");
		lbl3.setFont(f1);
		
		//텍스트 필드 생성
		txt1 = new JTextField("Oid000", 6); 
		txt2 = new JTextField(20); 
		txt3 = new JTextField(20); 

		
		JPanel ShowAllPanel = new JPanel();
		ShowAllPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		ShowAllPanel.add(Insert_Button);
		ShowAllPanel.add(Show_Button);
		
		
		JPanel UpdatePanel = new JPanel();
		UpdatePanel.setLayout(new GridLayout(3,3));
		UpdatePanel.add(lbl1);				UpdatePanel.add(txt1);
		UpdatePanel.add(lbl2);		UpdatePanel.add(txt2);
		UpdatePanel.add(lbl3);

		
		UpdatePanel.add(txt3);
		
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
	        	String  user = "DB2022Team11";
	        	String password ="DB2022Team11";
	        	
	            conn = DriverManager.getConnection(url, user, password);
	            System.out.println("Insert_OWNER Successfully Connection!");	//연결 확인 메세지
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
			
			
			 // ----------- OWNER TABLE 보기 ----------- //
			if(e.getSource() == Show_Button) {
				Head.clear();
				Head.add("owner_id");
				Head.add("owner_name");
				Head.add("owner_number");
				
				String stmt = "SELECT * FROM DB2022_OWNER";
				
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
						input[0] = r.getString("owner_id");
						input[1] = r.getString("owner_name");
						input[2] = r.getString("owner_number");
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
	        
			
	        
			// ------------ INSERT 집주인 ---------------//
	        if(e.getSource() == Insert_Button) {
	        	try {
	        		String id_num = txt1.getText();
		            String name = txt2.getText();
		            String number = txt3.getText();
				 
		            PreparedStatement pStmt = conn.prepareStatement(
		            		"insert into DB2022_OWNER values(?,?,?)");
		            
		            //ID 네자리 이상 불가능
		            if(id_num.length() > 6) {JOptionPane.showMessageDialog(null, "id는 세자리까지 입력가능합니다.", "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);}	            
		           	//ID null값 불가능
		           	if(id_num.equals("Oid000") || id_num.isEmpty()) {                  
		            	JOptionPane.showMessageDialog(null, "집주인 id번호를 입력해주세요. \n", "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE); 
		            }   
		           	else {pStmt.setString(1, id_num);}
		          	pStmt.setString(2, name);
		           	pStmt.setString(3, number);
		           	pStmt.executeUpdate(); 
		           	conn.commit();
		        	JOptionPane.showMessageDialog(null, "입력하신 집주인을 등록하였습니다.");
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
		        	JOptionPane.showMessageDialog(null, "새로운 집주인 등록에 실패했습니다. \n", "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);  
		        }
            	
            }
            
        

        } 
	 	public void tableChanged(TableModelEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	 	public static void main(String args[]){
			new INSERT_OWNER();
	    }
		
}