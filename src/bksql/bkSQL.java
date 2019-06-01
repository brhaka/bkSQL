/**
 *
 * @author Brhaka - Eduardo J Martinez
 */

package bksql;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class bkSQL extends javax.swing.JFrame {
    private static final String APPDATA = System.getenv("APPDATA") + "\\bkSQL";
    private static final String LICENSE = GetLicense.License();
    private static ImageIcon icon = new ImageIcon("icon/bkSQL-logo.png");
    private Boolean isAutoBackup = true;

    public bkSQL() {
        initComponents();

        setIconImage(icon.getImage());
    }

    private void Generate() throws InterruptedException, SAXException, ParserConfigurationException {
        String host = jtHost.getText();
        String user = jtUser.getText();
        String password = jpPassword.getText();
        String db = jtDataBase.getText();
        if(jcAllDB.isSelected()) {
            db = "all";
        }
        String saveLocation = jtSaveLocation.getText();
        String computerPass = jpComputerPassword.getText();

        if(Useful.IsNullOrEmpty(host) || Useful.IsNullOrEmpty(user) || Useful.IsNullOrEmpty(password) || Useful.IsNullOrEmpty(db) || Useful.IsNullOrEmpty(saveLocation)) {
            JOptionPane.showMessageDialog(rootPane, "All values are required.", "ERROR", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
	Date date = new Date();
        String batName = dateFormat.format(date) + ".bat";
        String exeName = dateFormat.format(date) + ".exe";
	String path = APPDATA + "\\bat\\" + db + "_" + batName;
        String pathNB = APPDATA + "\\bat";
        String pathEXE = APPDATA + "\\exe\\" + db + "_" + exeName;
        String pathNE = APPDATA + "\\exe";

        String mysqldump = System.getProperty("user.dir") + "\\lib\\mysqldump.exe";

        try {
            FileWriter fw = new FileWriter(path);
            BufferedWriter bw = new BufferedWriter(fw);

            String content = "";
            if(jcAllDB.isSelected()) {
                content = "for /f \"tokens=2 delims==\" %%I in ('wmic os get localdatetime /format:list') do set datetime=%%I\n" +
                "set DATET=%datetime:~0,8%-%datetime:~8,6%\n" +
                "\n" +
                "\"" + mysqldump + "\" --single-transaction=TRUE --host=\"" + host + "\" --user=\"" + user + "\" --password=\"" + password + "\" –all-databases > \"" + saveLocation + "\"%DATET%.sql\n" +
                "exit";

                db = "all";
            } else {
                content = "for /f \"tokens=2 delims==\" %%I in ('wmic os get localdatetime /format:list') do set datetime=%%I\n" +
                "set DATET=%datetime:~0,8%-%datetime:~8,6%\n" +
                "\n" +
                "\"" + mysqldump + "\" --single-transaction=TRUE --host=\"" + host + "\" --user=\"" + user + "\" --password=\"" + password + "\" " + db + " > \"" + saveLocation + "\"%DATET%.sql\n" +
                "exit";
            }

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

                WinScheduler.CreateSchedule("bkSQL_" + db, path, pathNB, batName, jtHour.getText(), repeat, db, computerPass, "create", pathEXE, pathNE);
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

    private void AllDB() {
        jlDataBase.setEnabled(false);
        jtDataBase.setEnabled(false);
    }

    private void SpecificDB() {
        jlDataBase.setEnabled(true);
        jtDataBase.setEnabled(true);
    }

    private static void CheckAppData() {
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
        if(!new File(APPDATA+"\\exe").exists()) {
            new File(APPDATA+"\\exe").mkdir();
            System.out.println("bkSQL's exe folder succesfully created!");
        }
        if(!new File(APPDATA+"\\data").exists()) {
            new File(APPDATA+"\\data").mkdir();
            System.out.println("bkSQL's data folder succesfully created!");
        }
    }

    private static void Alerts() {
        JFrame jf = new JFrame();
        int result = 0;

        int resultGithub = JOptionPane.showConfirmDialog(jf, "This program is from a Open-Source GitHub repository, \nthat can be found on https://github.com/brhaka/bkSQL, \ndesigned and developed by Brhaka (https://brhaka.com).", "Please Read Carefully", JOptionPane.OK_CANCEL_OPTION);
        if(resultGithub != JOptionPane.OK_OPTION) {
            System.exit(0);
        } else {
            result += 1;
        }

        int resultProblems = JOptionPane.showConfirmDialog(jf, "Brhaka is not responsible for any data loss, \ncorruption, or any related problems.", "Please Read Carefully", JOptionPane.OK_CANCEL_OPTION);
        if(resultProblems != JOptionPane.OK_OPTION) {
            System.exit(0);
        } else {
            result += 1;
        }

        JTextArea textArea = new JTextArea(LICENSE);
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        scrollPane.setPreferredSize(new Dimension(400, 400));
        int resultLicense = JOptionPane.showConfirmDialog(null, scrollPane, "bkSQL - License, Please Read Carefully", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if(resultLicense != JOptionPane.OK_OPTION) {
            System.exit(0);
        } else {
            result += 1;
        }

        if(result == 3) {
            try {
                FileWriter fw = new FileWriter(APPDATA + "\\data\\alerts.brk");
                BufferedWriter bw = new BufferedWriter(fw);

                String content = "~´´´][]~~____-'''\"\"~";

                bw.write(content);
                bw.close();
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(bkSQL.class.getName()).log(Level.SEVERE, null, ex);
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
        jlDataBase = new javax.swing.JLabel();
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
        jcAllDB = new javax.swing.JCheckBox();
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

        jlDataBase.setText("DataBase");
        jpHome.add(jlDataBase, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 92, 117, -1));

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
        jpHome.add(jbCreate, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, -1, -1));

        jlHour.setText("Hour");
        jpHome.add(jlHour, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, 70, -1));

        jlRepeat.setText("Repeat");
        jpHome.add(jlRepeat, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, 70, -1));

        jcRepeat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Daily", "Weekly", "Monthly", "Once" }));
        jcRepeat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jpHome.add(jcRepeat, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 270, 296, -1));

        buttonGroup1.add(jrManual);
        jrManual.setText("Manual backup");
        jrManual.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jrManual.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jrManualMouseReleased(evt);
            }
        });
        jpHome.add(jrManual, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 190, -1, -1));

        buttonGroup1.add(jrAuto);
        jrAuto.setSelected(true);
        jrAuto.setText("Auto backup");
        jrAuto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jrAuto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jrAutoMouseReleased(evt);
            }
        });
        jpHome.add(jrAuto, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 210, -1, -1));

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
        jpHome.add(jtHour, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 240, 296, -1));

        jlByBrhaka.setForeground(new java.awt.Color(0, 51, 255));
        jlByBrhaka.setText("by Brhaka");
        jlByBrhaka.setToolTipText("https://brhaka.com");
        jlByBrhaka.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jlByBrhaka.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jlByBrhakaMouseReleased(evt);
            }
        });
        jpHome.add(jlByBrhaka, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 310, -1, -1));

        jlCurrentPassword.setText("Current User Password");
        jpHome.add(jlCurrentPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 144, 117, -1));
        jpHome.add(jpComputerPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(131, 141, 255, -1));

        jcAllDB.setText("Bakcup all DataBases on server");
        jcAllDB.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jcAllDBMouseReleased(evt);
            }
        });
        jpHome.add(jcAllDB, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 170, -1, -1));

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
            .addComponent(jlName, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane.getAccessibleContext().setAccessibleName("Home");

        getAccessibleContext().setAccessibleDescription("bkSQL");

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

    private void jcAllDBMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jcAllDBMouseReleased
        if(jcAllDB.isSelected()) {
            AllDB();
        } else {
            SpecificDB();
        }
    }//GEN-LAST:event_jcAllDBMouseReleased

    public static void main(String args[]) {
        CheckAppData();

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

        if(new File(APPDATA + "\\data\\alerts.brk").exists()) {
            try {
                FileReader fr = new FileReader(APPDATA + "\\data\\alerts.brk");
                BufferedReader br = new BufferedReader(fr);

                if(!"~´´´][]~~____-'''\"\"~".equals(br.readLine())) {
                    Alerts();
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(bkSQL.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(bkSQL.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            Alerts();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JTabbedPane jTabbedPane;
    private javax.swing.JButton jbCreate;
    private javax.swing.JButton jbDeleteAll;
    private javax.swing.JCheckBox jcAllDB;
    private javax.swing.JComboBox<String> jcRepeat;
    private javax.swing.JLabel jlByBrhaka;
    private javax.swing.JLabel jlCurrentPassword;
    private javax.swing.JLabel jlDataBase;
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
