/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pdf;



import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

/**
 *
 * @author Daniel
 */
public class ImagePanel extends JPanel{
    private Image img;
    
    public ImagePanel(Image img){
        this.img = img;
        Dimension dimension = new Dimension(img.getWidth(null),img.getHeight(null));
        this.setPreferredSize(dimension);
        this.setMaximumSize(dimension);
        this.setMaximumSize(dimension);
        this.setSize(dimension);
        this.setLayout(null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(img, 0, 0, null);
    }
    
}
