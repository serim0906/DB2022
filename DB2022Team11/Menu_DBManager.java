package DB2022team11;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Menu_DBManager extends JFrame{
	String name[]={"신규 등록","정보 수정","정보 삭제"};
	JMenuItem mi_area[]=new JMenuItem[3], mi_agency[]=new JMenuItem[3], mi_owner[]=new JMenuItem[3],
			mi_building[]=new JMenuItem[3], mi_sale[]=new JMenuItem[3],	mi_view;
	
	public Menu_DBManager() {
		Color b=new Color(244,244,244);  
		Font f1 = new Font("아임크리수진", Font.PLAIN, 13);

		setTitle("[어쩔 DB] DB관리자 메뉴");
		setSize(700, 700);
		setBackground(b);
		getContentPane().setLayout(null);
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
		
		JMenuBar m = new JMenuBar();
        setJMenuBar( m );
        

        JMenu m_agency = new JMenu("부동산 정보 관리");     
        JMenu m_owner = new JMenu("집주인 정보 관리");
        JMenu m_building = new JMenu("건물 정보 관리");     
        JMenu m_sale = new JMenu("매물 정보 관리");     
        //JMenu m_seeAll = new JMenu("DB 데이터 보기"); 
        m_agency.setFont(f1);	m.add(m_agency);
        m_owner.setFont(f1);	m.add(m_owner);		m_building.setFont(f1);		m.add(m_building);
        m_sale.setFont(f1);		m.add(m_sale);		
        //m_seeAll.setFont(f1);	
      
        //모든 데이터 보기는 JMenuItem 없음
        //m_seeAll.addActionListener(new MyActionListener());
        //m.add(m_seeAll);
        
        for(int i=0; i< 3; i++) {
	        mi_agency[i]=new JMenuItem(name[i]);	mi_agency[i].addActionListener(new MyActionListener());    	m_agency.add(mi_agency[i]);
	        mi_owner[i]=new JMenuItem(name[i]);		mi_owner[i].addActionListener(new MyActionListener());    	m_owner.add(mi_owner[i]);
	        mi_building[i]=new JMenuItem(name[i]);	mi_building[i].addActionListener(new MyActionListener());   m_building.add(mi_building[i]);
	        mi_sale[i]=new JMenuItem(name[i]);		mi_sale[i].addActionListener(new MyActionListener());    	m_sale.add(mi_sale[i]);
        }
        //mi_view = new JMenuItem("실행");	mi_view.addActionListener(new MyActionListener());    	m_seeAll.add(mi_view);


	}
	class MyActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
        	String cmd = ae.getActionCommand();
            JMenuItem item = (JMenuItem)ae.getSource();
            
            if(item==mi_agency[0]){
            	new INSERT_AGENCY();
            }
            else if(item==mi_agency[1]){
            	new updateAgency();
            }
            else if(item==mi_agency[2]){
            	new deleteTable_agency();
            }
            
            else if(item==mi_owner[0]){
            	new INSERT_OWNER();
            }
            else if(item==mi_owner[1]){
            	new updateOwner();
            }
            else if(item==mi_owner[2]){
            	new deleteTable_owner();
            }
            
            else if(item==mi_building[0]){
            	new INSERT_BUILDING();
            }
            else if(item==mi_building[1]){
            	new updateBuilding();
            }
            else if(item==mi_building[2]){
            	new deleteTable_building();
            }
            
            else if(item==mi_sale[0]){
            	new INSERT_SALE();
            }
            else if(item==mi_sale[1]){
            	new updateSale();
            }
            else if(item==mi_sale[2]){
            	new deleteTable_sale();
            }
            
            else if(item==mi_view){
            	new showTable_user();
            }
        }
        
    }


}