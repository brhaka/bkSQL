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

        if(Useful.IsNullOrEmpty(host) || Useful.IsNullOrEmpty(user) || Useful.IsNullOrEmpty(password) || Useful.IsNullOrEmpty(db) || Useful.IsNullOrEmpty(saveLocation)) {
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

                WinScheduler.CreateSchedule("bkSQL_" + db, path, pathNB, jtHour.getText(), repeat, db, computerPass, "create");
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

    private void DeleteAll() {
        int resp = JOptionPane.showConfirmDialog(rootPane, "Are you sure that you want to delete ALL BACKUP CONFIGURATIONS? This action can't be undone.", "Are you sure?", JOptionPane.YES_NO_OPTION);

        if(resp == JOptionPane.YES_OPTION) {
            if(new File(APPDATA).exists()) {
                Useful.DeleteEntireDirectory(APPDATA);
                System.out.println("Backup configurations successfuly deleted.");
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jlNameClick = new javax.swing.JLabel();
        jlName = new javax.swing.JLabel();
        jTabbedPane = new javax.swing.JTabbedPane();
        jpHome = new javax.swing.JPanel();
        jlHost = new javax.swing.JLabel();
        jlPassword = new javax.swing.JLabel();
        jlUser = new javax.swing.JLabel();
        jlDatabase = new javax.swing.JLabel();
        jlSaveLocation = new javax.swing.JLabel();
        jtSaveLocation = new javax.swing.JTextField();
        jtDataBase = new javax.swing.JTextField();
        jtUser = new javax.swing.JTextField();
        jtHost = new javax.swing.JTextField();
        jpPassword = new javax.swing.JPasswordField();
        jbCreate = new javax.swing.JButton();
        jlHour = new javax.swing.JLabel();
        jlRepeat = new javax.swing.JLabel();
        jcRepeat = new javax.swing.JComboBox<>();
        jrManual = new javax.swing.JRadioButton();
        jrAuto = new javax.swing.JRadioButton();
        jtHour = new javax.swing.JFormattedTextField();
        jlByBrhaka = new javax.swing.JLabel();
        jlCurrentPassword = new javax.swing.JLabel();
        jpComputerPassword = new javax.swing.JPasswordField();
        jpOthers = new javax.swing.JPanel();
        jbDeleteAll = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("bkSQL");
        setLocationByPlatform(true);
        setResizable(false);

        jlNameClick.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jlNameClick.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jlNameClickMouseReleased(evt);
            }
        });

        jlName.setFont(new java.awt.Font("Unispace", 0, 24)); // NOI18N
        jlName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlName.setText("bkSQL");
        jlName.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jpHome.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jlHost.setText("Host");
        jpHome.add(jlHost, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 14, 117, -1));

        jlPassword.setText("Password");
        jpHome.add(jlPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 66, 117, -1));

        jlUser.setText("User");
        jpHome.add(jlUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 117, -1));

        jlDatabase.setText("DataBase");
        jpHome.add(jlDatabase, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 92, 117, -1));

        jlSaveLocation.setText("Save Location");
        jpHome.add(jlSaveLocation, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 118, 117, -1));
        jpHome.add(jtSaveLocation, new org.netbeans.lib.awtextra.AbsoluteConstraints(131, 115, 255, -1));
        jpHome.add(jtDataBase, new org.netbeans.lib.awtextra.AbsoluteConstraints(131, 89, 255, -1));
        jpHome.add(jtUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(131, 37, 255, -1));
        jpHome.add(jtHost, new org.netbeans.lib.awtextra.AbsoluteConstraints(131, 11, 255, -1));
        jpHome.add(jpPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(131, 63, 255, -1));

        jbCreate.setText("Create");
        jbCreate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbCreate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jbCreateMouseReleased(evt);
            }
        });
        jpHome.add(jbCreate, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, -1, -1));

        jlHour.setText("Hour");
        jpHome.add(jlHour, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 70, -1));

        jlRepeat.setText("Repeat");
        jpHome.add(jlRepeat, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, 70, -1));

        jcRepeat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Daily", "Weekly", "Monthly", "Once" }));
        jcRepeat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jpHome.add(jcRepeat, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 260, 296, -1));

        buttonGroup1.add(jrManual);
        jrManual.setText("Manual backup");
        jrManual.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jrManual.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jrManualMouseReleased(evt);
            }
        });
        jpHome.add(jrManual, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 180, -1, -1));

        buttonGroup1.add(jrAuto);
        jrAuto.setSelected(true);
        jrAuto.setText("Auto backup");
        jrAuto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jrAuto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jrAutoMouseReleased(evt);
            }
        });
        jpHome.add(jrAuto, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 200, -1, -1));

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
        jpHome.add(jtHour, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 230, 296, -1));

        jlByBrhaka.setForeground(new java.awt.Color(0, 51, 255));
        jlByBrhaka.setText("by Brhaka");
        jlByBrhaka.setToolTipText("https://brhaka.com");
        jlByBrhaka.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jlByBrhaka.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jlByBrhakaMouseReleased(evt);
            }
        });
        jpHome.add(jlByBrhaka, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 300, -1, -1));

        jlCurrentPassword.setText("Current User Password");
        jpHome.add(jlCurrentPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 144, 117, -1));
        jpHome.add(jpComputerPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(131, 141, 255, -1));

        jTabbedPane.addTab("Home", jpHome);

        jpOthers.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jbDeleteAll.setText("Delete all Backup Configurations");
        jbDeleteAll.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jbDeleteAllMouseReleased(evt);
            }
        });
        jpOthers.add(jbDeleteAll, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 280, -1, 30));

        jTabbedPane.addTab("Others", jpOthers);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(156, 156, 156)
                .addComponent(jlNameClick, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jlName, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jlNameClick, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jlName, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jTabbedPane.getAccessibleContext().setAccessibleName("Home");

        getAccessibleContext().setAccessibleDescription("bkSQL by Brhaka");

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jlNameClickMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlNameClickMouseReleased
        try {
            java.awt.Desktop.getDesktop().browse(java.net.URI.create("https://github.com/brhaka/bkSQL"));
        } catch (IOException ex) {
            Logger.getLogger(bkSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jlNameClickMouseReleased

    private void jlByBrhakaMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlByBrhakaMouseReleased
        try {
            java.awt.Desktop.getDesktop().browse(java.net.URI.create("https://brhaka.com"));
        } catch (IOException ex) {
            Logger.getLogger(bkSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jlByBrhakaMouseReleased

    private void jtHourFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtHourFocusLost
        if("  :  :  ".equals(jtHour.getText())) {
            jtHour.setText("00:00:00");
        }
    }//GEN-LAST:event_jtHourFocusLost

    private void jtHourFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtHourFocusGained
        if("00:00:00".equals(jtHour.getText())) {
            jtHour.setText("");
        }
    }//GEN-LAST:event_jtHourFocusGained

    private void jrAutoMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jrAutoMouseReleased
        Auto();
    }//GEN-LAST:event_jrAutoMouseReleased

    private void jrManualMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jrManualMouseReleased
        Manual();
    }//GEN-LAST:event_jrManualMouseReleased

    private void jbCreateMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbCreateMouseReleased
        try {
            Generate();
        } catch (InterruptedException ex) {
            Logger.getLogger(bkSQL.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(bkSQL.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(bkSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jbCreateMouseReleased

    private void jbDeleteAllMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbDeleteAllMouseReleased
        DeleteAll();
    }//GEN-LAST:event_jbDeleteAllMouseReleased

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
    private javax.swing.JTabbedPane jTabbedPane;
    private javax.swing.JButton jbCreate;
    private javax.swing.JButton jbDeleteAll;
    private javax.swing.JComboBox<String> jcRepeat;
    private javax.swing.JLabel jlByBrhaka;
    private javax.swing.JLabel jlCurrentPassword;
    private javax.swing.JLabel jlDatabase;
    private javax.swing.JLabel jlHost;
    private javax.swing.JLabel jlHour;
    private javax.swing.JLabel jlName;
    private javax.swing.JLabel jlNameClick;
    private javax.swing.JLabel jlPassword;
    private javax.swing.JLabel jlRepeat;
    private javax.swing.JLabel jlSaveLocation;
    private javax.swing.JLabel jlUser;
    private javax.swing.JPasswordField jpComputerPassword;
    private javax.swing.JPanel jpHome;
    private javax.swing.JPanel jpOthers;
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
