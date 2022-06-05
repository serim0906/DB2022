package DB2022team11;

import javax.swing.*;   
import java.awt.*;

public class RoundedButton2 extends JButton {
      public RoundedButton2() { super(); decorate(); } 
      public RoundedButton2(String text) { super(text); decorate(); } 
      public RoundedButton2(Action action) { super(action); decorate(); } 
      public RoundedButton2(Icon icon) { super(icon); decorate(); } 
      public RoundedButton2(String text, Icon icon) { super(text, icon); decorate(); } 
      protected void decorate() { setBorderPainted(false); setOpaque(false); }
      @Override 
      protected void paintComponent(Graphics g) {
    	 Color c=new Color(64,90,121); //배경색 결정
         Color o=new Color(255,255,255); //글자색 결정
         Font f1 = new Font("아임크리수진", Font.PLAIN, 15); //글씨체
         
         int width = getWidth(); 
         int height = getHeight(); 
         Graphics2D graphics = (Graphics2D) g; 
         graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); 
         if (getModel().isArmed()) { graphics.setColor(c.darker()); } 
         else if (getModel().isRollover()) { graphics.setColor(c.brighter()); } 
         else { graphics.setColor(c); } 
         graphics.fillRoundRect(0, 0, width, height, 10, 10); 
         FontMetrics fontMetrics = graphics.getFontMetrics(); 
         Rectangle stringBounds = fontMetrics.getStringBounds(this.getText(), graphics).getBounds(); 
         int textX = (width - stringBounds.width) / 2; 
         int textY = (height - stringBounds.height) / 2 + fontMetrics.getAscent(); 
         graphics.setColor(o); 
         graphics.setFont(f1); 
         graphics.drawString(getText(), textX, textY); 
         graphics.dispose(); 
         super.paintComponent(g); 
         }
      }