/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.util.Map;
import settings.SettingsManager;
import javax.swing.JOptionPane;
/**
 *
 * @author luca6
 */
public class DroneControllerMonitor extends javax.swing.JFrame {
   
    private SettingsManager manager;
    /**
     * Creates new form DroneControllerMonitor
     */
    public DroneControllerMonitor() {
        this.manager = new SettingsManager();
        initComponents();
        updateSettingsValues(manager);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelHeader = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanelBody = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanePageLog = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanelSettings = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        sensibilityValueTextBox = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        heightPointsValueTextBox = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        DegreesSensibilityValueTextBox = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        MovementDelayValueTextBox = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        DeltaAverageMultiplierValueTextBox = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanelHeader.setBackground(new java.awt.Color(0, 78, 112));

        jLabel1.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Drone Controller");
        jPanelHeader.add(jLabel1);

        getContentPane().add(jPanelHeader, java.awt.BorderLayout.PAGE_START);

        jPanelBody.setBackground(new java.awt.Color(29, 182, 209));
        jPanelBody.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jPanelBody.setLayout(new java.awt.BorderLayout());

        jPanePageLog.setLayout(new java.awt.BorderLayout());

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setText("Developed by DCS Team.\nDesigned by Luca Di Bello.\n---------------------------------------\n\n");
        jScrollPane1.setViewportView(jTextArea1);

        jPanePageLog.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Log", jPanePageLog);

        jPanelSettings.setLayout(new java.awt.GridLayout(6, 2));

        jLabel2.setText("Sensibility");
        jPanelSettings.add(jLabel2);

        sensibilityValueTextBox.setText("VALUE");
        jPanelSettings.add(sensibilityValueTextBox);

        jLabel3.setText("Height Points");
        jPanelSettings.add(jLabel3);

        heightPointsValueTextBox.setText("VALUE");
        jPanelSettings.add(heightPointsValueTextBox);

        jLabel4.setText("Degrees Sensibility");
        jPanelSettings.add(jLabel4);

        DegreesSensibilityValueTextBox.setText("VALUE");
        jPanelSettings.add(DegreesSensibilityValueTextBox);

        jLabel5.setText("Moviment Delay");
        jPanelSettings.add(jLabel5);

        MovementDelayValueTextBox.setText("VALUE");
        jPanelSettings.add(MovementDelayValueTextBox);

        jLabel6.setText("Delta Average Multiplier");
        jPanelSettings.add(jLabel6);

        DeltaAverageMultiplierValueTextBox.setText("VALUE");
        jPanelSettings.add(DeltaAverageMultiplierValueTextBox);

        jButton1.setText("Apply settings");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanelSettings.add(jButton1);

        jButton2.setText("Refresh settings");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanelSettings.add(jButton2);

        jTabbedPane1.addTab("Settings", jPanelSettings);

        jPanelBody.add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanelBody, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
        int cake =  JOptionPane.showConfirmDialog(null,
             "Do you wanna apply this settings?", "DCS Controller - Settings", JOptionPane.YES_NO_OPTION);

        if (cake == JOptionPane.YES_OPTION) {
            manager.setSetting("degrees_sensibility", this.DegreesSensibilityValueTextBox.getText());
            manager.setSetting("deltaAverageMultiplier", this.DeltaAverageMultiplierValueTextBox.getText());
            manager.setSetting("movementDelay", this.MovementDelayValueTextBox.getText());
            manager.setSetting("sensibility", this.sensibilityValueTextBox.getText());
            manager.setSetting("height_points_number", this.heightPointsValueTextBox.getText());
            
            System.out.println("[Success] Settings applied successfully");
        } else if (cake == JOptionPane.NO_OPTION) {
            this.updateSettingsValues(manager);
           System.err.println("Apply settings operation aborted.");
        }
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.updateSettingsValues(manager);
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DroneControllerMonitor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DroneControllerMonitor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DroneControllerMonitor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DroneControllerMonitor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DroneControllerMonitor().setVisible(true);
            }
        });
    }
    
    private void updateSettingsValues(SettingsManager manager){
        Map<String, String> options = manager.getSettings();
        
        this.DegreesSensibilityValueTextBox.setText(options.get("degrees_sensibility"));
        this.DeltaAverageMultiplierValueTextBox.setText(options.get("deltaAverageMultiplier"));
        this.MovementDelayValueTextBox.setText(options.get("movementDelay"));
        this.sensibilityValueTextBox.setText(options.get("sensibility"));
        this.heightPointsValueTextBox.setText(options.get("height_points_number"));
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField DegreesSensibilityValueTextBox;
    private javax.swing.JTextField DeltaAverageMultiplierValueTextBox;
    private javax.swing.JTextField MovementDelayValueTextBox;
    private javax.swing.JTextField heightPointsValueTextBox;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanePageLog;
    private javax.swing.JPanel jPanelBody;
    private javax.swing.JPanel jPanelHeader;
    private javax.swing.JPanel jPanelSettings;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField sensibilityValueTextBox;
    // End of variables declaration//GEN-END:variables
}