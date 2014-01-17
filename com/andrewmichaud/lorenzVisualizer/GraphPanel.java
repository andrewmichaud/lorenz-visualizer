/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andrewmichaud.lorenzVisualizer;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andrew
 */
public class GraphPanel extends javax.swing.JPanel {

    // Different axis arrangements for graphs
    public static final int X_Y_AXES = 1;
    public static final int Y_Z_AXES = 2;
    public static final int Z_X_AXES = 3;
    
    public static final int POINT_SIZE = 2;
    
    private List<LorenzSystem> systems_;
    private Double[] initialPoint_;
    private boolean animate_;
    private int graphType_;
    
    /**
     * Creates new form NewJPanel
     */
    public GraphPanel() {
        initComponents();
    }
    
    public void setSystems(List<LorenzSystem> systems, boolean animate,
                           int graphType) {
        systems_ = systems;
        animate_ = animate;
        graphType_ = graphType;
    }
    
    public void setInitialPoint(Double[] initialPoint) {
        initialPoint_ = initialPoint;
    }
    
    private void drawGraph(Graphics2D g) {
        
        double height = this.getHeight();
        double width = this.getWidth();
        double oldX = 1;
        double oldY = 1;
        double oldZ = 1;
        
        switch (graphType_) {
                    case X_Y_AXES:
                        oldX = (initialPoint_[0] - 1) + width / 2.0;
                        oldY = (initialPoint_[1] - 1) + height / 2.0;
                        break;
                    case Y_Z_AXES:
                        oldY = (initialPoint_[1] - 1) + width / 2.0;
                        oldZ = (initialPoint_[2] - 1) + height / 2.0;
                        break;
                    case Z_X_AXES:
                        oldX = (initialPoint_[0] - 1) + width / 2.0;
                        oldZ = (initialPoint_[2] - 1) + height / 2.0;
                        break;
                    default:
                        oldX = (initialPoint_[0] - 1) + width / 2.0;
                        oldY = (initialPoint_[1] - 1) + height / 2.0;
                        break;
                }
        
        double x = 1;
        double y = 1;
        double z = 1;
        
        Ellipse2D e;
        Line2D l;

        for (LorenzSystem system : systems_) {
            
            for (BigDecimal[] point : system.getPoints()) {
                
                switch (graphType_) {
                    case X_Y_AXES:
                        x = (point[0].doubleValue() - 1) + width / 2.0;
                        y = (point[1].doubleValue() - 1) + height / 2.0;
                        l = new Line2D.Double(oldX, x, oldY, y);
                        //g.draw(l);
                        e = new Ellipse2D.Double(x, y, POINT_SIZE, POINT_SIZE);
                        g.fill(e);
                        break;
                    case Y_Z_AXES:
                        y = (point[1].doubleValue() - 1) + width / 2.0;
                        z = (point[2].doubleValue() - 1) + height / 2.0;
                        l = new Line2D.Double(oldY, y, oldZ, z);
                        //g.draw(l);
                        e = new Ellipse2D.Double(y, z, POINT_SIZE, POINT_SIZE);
                        g.fill(e);
                        break;
                    case Z_X_AXES:
                        x = (point[0].doubleValue() - 1) + width / 2.0;
                        z = (point[2].doubleValue() - 1) + height / 2.0;
                        l = new Line2D.Double(oldZ, z, oldX, x);
                        //g.draw(l);
                        e = new Ellipse2D.Double(z, x, POINT_SIZE, POINT_SIZE);
                        g.fill(e);
                        break;
                    default:
                        x = (point[0].doubleValue() - 1) + width / 2.0;
                        y = (point[1].doubleValue() - 1) + height / 2.0;
                        l = new Line2D.Double(oldX, x, oldY, y);
                        //g.draw(l);
                        e = new Ellipse2D.Double(x, y, POINT_SIZE, POINT_SIZE);
                        g.fill(e);
                        break;
                }
                             
                oldX = x;
                oldY = y;
                oldZ = z;
                
                if (animate_) {
                    try {
                        Thread.sleep(100);
                        repaint();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(GraphPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        
       
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGraph((Graphics2D) g);  
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables


}
