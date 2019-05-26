/**
 *
 * @author Brhaka - Eduardo J Martinez
 */

package bksql;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class bkSQL extends javax.swing.JFrame {
    private static final String APPDATA = System.getenv("APPDATA") + "\\bkSQL";
    private Boolean isAutoBackup = true;

    public bkSQL() {
        initComponents();
    }

    private void Generate() throws InterruptedException, SAXException, ParserConfigurationException {
        System.out.println("AppData: "+APPDATA);
        if(!new File(APPDATA).exists()) {
            new File(APPDATA).mkdir();
            System.out.println("bkSQL's APPDATA folder succesfully created!");
        }
        if(!new File(APPDATA+"\\bat").exists()) {
            new File(APPDATA+"\\bat").mkdir();
            System.out.println("bkSQL's bat folder succesfully created!");
        }
        if(!new File(APPDATA+"\\xml").exists()) {
            new File(APPDATA+"\\xml").mkdir();
            System.out.println("bkSQL's xml folder succesfully created!");
        }

        String host = jtHost.getText();
        String user = jtUser.getText();
        String password = jpPassword.getText();
        String db = jtDataBase.getText();
        String saveLocation = jtSaveLocation.getText();
        String computerPass = jpComputerPassword.getText();

        if(Useful.IsNotNullOrEmpty(host) || Useful.IsNotNullOrEmpty(user) || Useful.IsNotNullOrEmpty(password) || Useful.IsNotNullOrEmpty(db) || Useful.IsNotNullOrEmpty(saveLocation)) {
            JOptionPane.showMessageDialog(rootPane, "All values are required.", "ERROR", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
	Date date = new Date();
	String path = APPDATA + "\\bat\\" + db + "_" + dateFormat.format(date) + ".bat";
        String pathNB = APPDATA + "\\bat";

        String mysqldump = System.getProperty("user.dir") + "\\lib\\mysqldump.exe";

        try {
            FileWriter fw = new FileWriter(path);
            BufferedWriter bw = new BufferedWriter(fw);

            String content = "for /f \"tokens=2 delims==\" %%I in ('wmic os get localdatetime /format:list') do set datetime=%%I\n" +
            "set DATET=%datetime:~0,8%-%datetime:~8,6%\n" +
            "\n" +
            "\"" + mysqldump + "\" --host=\"" + host + "\" --user=\"" + user + "\" --password=\"" + password + "\" " + db + " > \"" + saveLocation + "\"%DATET%.sql\n" +
            "exit";

            bw.write(content);
            bw.close();
            fw.close();

            if(isAutoBackup) {
                String repeat = "";

                if(jcRepeat.getSelectedItem() == "Daily") {
                    repeat = "DAILY";
                }
                if(jcRepeat.getSelectedItem() == "Weekly") {
                    repeat = "WEEKLY";
                }
                if(jcRepeat.getSelectedItem() == "Monthly") {
                    repeat = "MONTHLY";
                }
                if(jcRepeat.getSelectedItem() == "Once") {
                    repeat = "DOONCE";
                }

                WinScheduler.CreateSchedule("bkSQL_" + db, path, pathNB, jtHour.getText(), repeat, db, computerPass);
            } else {
                JOptionPane.showMessageDialog(rootPane, "Successfuly created backup files!", "Success!", JOptionPane.PLAIN_MESSAGE);
            }
        } catch (IOException ex) {
            Logger.getLogger(bkSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void Manual() {
        jlHour.setEnabled(false);
        jtHour.setEnabled(false);
        jlRepeat.setEnabled(false);
        jcRepeat.setEnabled(false);

        isAutoBackup = false;
    }

    private void Auto() {
        jlHour.setEnabled(true);
        jtHour.setEnabled(true);
        jlRepeat.setEnabled(true);
        jcRepeat.setEnabled(true);

        isAutoBackup = true;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jlHost = new javax.swing.JLabel();
        jlUser = new javax.swing.JLabel();
        jlPassword = new javax.swing.JLabel();
        jlDatabase = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jtSaveLocation = new javax.swing.JTextField();
        jtDataBase = new javax.swing.JTextField();
        jtUser = new javax.swing.JTextField();
        jtHost = new javax.swing.JTextField();
        jpPassword = new javax.swing.JPasswordField();
        jButton1 = new javax.swing.JButton();
        jlHour = new javax.swing.JLabel();
        jlRepeat = new javax.swing.JLabel();
        jcRepeat = new javax.swing.JComboBox<>();
        jrManual = new javax.swing.JRadioButton();
        jrAuto = new javax.swing.JRadioButton();
        jtHour = new javax.swing.JFormattedTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jpComputerPassword = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("bkSQL");
        setLocationByPlatform(true);

        jlHost.setText("Host");

        jlUser.setText("Password");

        jlPassword.setText("User");

        jlDatabase.setText("DataBase");

        jLabel5.setText("Save Location");

        jButton1.setText("Create");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton1MouseReleased(evt);
            }
        });

        jlHour.setText("Hour");

        jlRepeat.setText("Repeat");

        jcRepeat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Daily", "Weekly", "Monthly", "Once" }));
        jcRepeat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        buttonGroup1.add(jrManual);
        jrManual.setText("Manual backup");
        jrManual.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jrManual.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jrManualMouseReleased(evt);
            }
        });

        buttonGroup1.add(jrAuto);
        jrAuto.setSelected(true);
        jrAuto.setText("Auto backup");
        jrAuto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jrAuto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jrAutoMouseReleased(evt);
            }
        });

        try {
            jtHour.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:##:##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jtHour.setText("00:00:00");
        jtHour.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtHourFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtHourFocusLost(evt);
            }
        });

        jLabel1.setForeground(new java.awt.Color(0, 51, 255));
        jLabel1.setText("by Brhaka");
        jLabel1.setToolTipText("https://brhaka.com");
        jLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel1MouseReleased(evt);
            }
        });

        jLabel2.setText("Current User Password");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jlPassword, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jlUser, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jlDatabase, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                            .addComponent(jlHost, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(4, 4, 4)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jrAuto)
                                    .addComponent(jrManual))
                                .addGap(0, 186, Short.MAX_VALUE))
                            .addComponent(jtUser)
                            .addComponent(jtHost, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jtSaveLocation)
                            .addComponent(jtDataBase, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jpPassword, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jpComputerPassword)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jlRepeat, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                            .addComponent(jlHour, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jtHour)
                            .addComponent(jcRepeat, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlHost)
                    .addComponent(jtHost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlPassword)
                    .addComponent(jtUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlUser)
                    .addComponent(jpPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlDatabase)
                    .addComponent(jtDataBase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jtSaveLocation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jpComputerPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addComponent(jrManual)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jrAuto)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlHour)
                    .addComponent(jtHour, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jcRepeat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlRepeat))
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jLabel1))
                .addContainerGap())
        );

        getAccessibleContext().setAccessibleDescription("bkSQL by Brhaka");

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseReleased
        try {
            Generate();
        } catch (InterruptedException ex) {
            Logger.getLogger(bkSQL.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(bkSQL.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(bkSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1MouseReleased

    private void jrManualMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jrManualMouseReleased
        Manual();
    }//GEN-LAST:event_jrManualMouseReleased

    private void jrAutoMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jrAutoMouseReleased
        Auto();
    }//GEN-LAST:event_jrAutoMouseReleased

    private void jtHourFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtHourFocusGained
        if("00:00:00".equals(jtHour.getText())) {
            jtHour.setText("");
        }
    }//GEN-LAST:event_jtHourFocusGained

    private void jtHourFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtHourFocusLost
        if("  :  :  ".equals(jtHour.getText())) {
            jtHour.setText("00:00:00");
        }
    }//GEN-LAST:event_jtHourFocusLost

    private void jLabel1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseReleased
        try {
            java.awt.Desktop.getDesktop().browse(java.net.URI.create("https://brhaka.com"));
        } catch (IOException ex) {
            Logger.getLogger(bkSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jLabel1MouseReleased

    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(bkSQL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(bkSQL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(bkSQL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(bkSQL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        //</editor-fold>
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new bkSQL().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JComboBox<String> jcRepeat;
    private javax.swing.JLabel jlDatabase;
    private javax.swing.JLabel jlHost;
    private javax.swing.JLabel jlHour;
    private javax.swing.JLabel jlPassword;
    private javax.swing.JLabel jlRepeat;
    private javax.swing.JLabel jlUser;
    private javax.swing.JPasswordField jpComputerPassword;
    private javax.swing.JPasswordField jpPassword;
    private javax.swing.JRadioButton jrAuto;
    private javax.swing.JRadioButton jrManual;
    private javax.swing.JTextField jtDataBase;
    private javax.swing.JTextField jtHost;
    private javax.swing.JFormattedTextField jtHour;
    private javax.swing.JTextField jtSaveLocation;
    private javax.swing.JTextField jtUser;
    // End of variables declaration//GEN-END:variables
}
