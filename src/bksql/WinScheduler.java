/**
 *
 * @author Brhaka - Eduardo J Martinez
 */

package bksql;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class WinScheduler {
    private static final String APPDATA = System.getenv("APPDATA") + "\\bkSQL";

    private static String name = "", path = "", pathNB = "", batName = "", hour = "", repeat = "", db = "", password = "", action = "", pathEXE = "", pathNE = "";

    protected static void CreateSchedule(String nameV, String pathV, String pathNBV, String batNameV, String hourV, String repeatV, String dbV, String passwordV, String actionV, String pathEXEV, String pathNEV) throws IOException, InterruptedException, SAXException, ParserConfigurationException {
        JFrame rootPane = new JFrame();

        if(Useful.IsNullOrEmpty(nameV) || Useful.IsNullOrEmpty(pathV) || Useful.IsNullOrEmpty(hourV) || Useful.IsNullOrEmpty(repeatV) || Useful.IsNullOrEmpty(dbV) || Useful.IsNullOrEmpty(pathNBV) || Useful.IsNullOrEmpty(batNameV) || Useful.IsNullOrEmpty(passwordV) || Useful.IsNullOrEmpty(actionV) || Useful.IsNullOrEmpty(pathNEV) || Useful.IsNullOrEmpty(pathEXEV)) {
            JOptionPane.showMessageDialog(rootPane, "An critical error ocurred (0x912836).", "ERROR", JOptionPane.ERROR_MESSAGE);
            bkSQL.EnableAll();
        } else {
            name = nameV;
            path = pathV;
            pathNB = pathNBV;
            batName = batNameV;
            hour = hourV;
            repeat = repeatV;
            db = dbV;
            password = passwordV;
            action = actionV;
            pathEXE = pathEXEV;
            pathNE = pathNEV;

            Thread c = new Thread(Create);
            c.start();
        }
    }

    private static Runnable Create = new Runnable() {
        public void run() {
            FileWriter fw = null;
            try {
                JFrame rootPane = new JFrame();

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss");
                DateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                String formaDay = dayFormat.format(date);
                String dateToSchedule = formaDay + "T" + hour;
                String today = dateFormat.format(date);
                InetAddress addr = InetAddress.getLocalHost();
                String computerName = addr.getHostName();
                String userName = System.getProperty("user.name");
                String xmlName = APPDATA + "\\xml\\task_" + db + "_" + today + ".xml";
                fw = new FileWriter(xmlName);
                BufferedWriter bw = new BufferedWriter(fw);
                String xml = XML.GetXML();
                dateToSchedule = dateToSchedule.replace("_", "T");
                xml = xml.replace("[USER]", userName);
                xml = xml.replace("[NAME]", name);
                xml = xml.replace("[START_DATE]", dateToSchedule);
                xml = xml.replace("[PATH_W_BAT]", "\"" + pathEXE + "\"");
                xml = xml.replace("[PATH_N_BAT]", "\"" + pathNE + "\"");
                bw.write(xml);
                bw.close();
                fw.close();
                bkSQL.jProgressBar.setValue(bkSQL.jProgressBar.getValue() + 10);
                String command = "@echo off\n" +
                        "\n" +
                        ":: BatchGotAdmin\n" +
                        ":-------------------------------------\n" +
                        "REM  --> Check for permissions\n" +
                        ">nul 2>&1 \"%SYSTEMROOT%\\system32\\cacls.exe\" \"%SYSTEMROOT%\\system32\\config\\system\"\n" +
                        "\n" +
                        "REM --> If error flag set, we do not have admin.\n" +
                        "if '%errorlevel%' NEQ '0' (\n" +
                        "    echo Requesting administrative privileges...\n" +
                        "    goto UACPrompt\n" +
                        ") else ( goto gotAdmin )\n" +
                        "\n" +
                        ":UACPrompt\n" +
                        "    echo Set UAC = CreateObject^(\"Shell.Application\"^) > \"%temp%\\getadmin.vbs\"\n" +
                        "    echo UAC.ShellExecute \"%~s0\", \"\", \"\", \"runas\", 1 >> \"%temp%\\getadmin.vbs\"\n" +
                        "\n" +
                        "    \"%temp%\\getadmin.vbs\"\n" +
                        "    exit /B\n" +
                        "\n" +
                        ":gotAdmin\n" +
                        "    if exist \"%temp%\\getadmin.vbs\" ( del \"%temp%\\getadmin.vbs\" )\n" +
                        "    pushd \"%CD%\"\n" +
                        "    CD /D \"%~dp0\"\n" +
                        ":--------------------------------------" +
                        "\n\nschtasks /create /xml \"" + xmlName + "\" /tn \"" + name + "\" /ru \"" + computerName + "\\\\" + userName + "\" /rp " + password +
                        "\nexit";
                String pathToBat = APPDATA + "\\bat\\cmd.bat";
                FileWriter fwb = new FileWriter(pathToBat);
                BufferedWriter bwb = new BufferedWriter(fwb);
                bwb.write(command);
                bwb.close();
                fwb.close();
                bkSQL.jProgressBar.setValue(bkSQL.jProgressBar.getValue() + 10);
                command = "cmd /c start /wait \"C:\\WINDOWS\\system32\\cmd.exe\" \"" + pathToBat + "\"";
                Process proc = Runtime.getRuntime().exec(command);
                proc.waitFor();
                command = "cmd /c start /wait " + System.getProperty("user.dir") + "\\lib\\bat2exeIEXP.bat \"" + path + "\"";
                proc = Runtime.getRuntime().exec(command);
                proc.waitFor();
                String pathToExe = System.getProperty("user.dir") + "\\" + db + "_" + batName.replaceFirst(".bat", ".exe");
                File exe = new File(pathToExe);
                bkSQL.jProgressBar.setValue(bkSQL.jProgressBar.getValue() + 10);
                Boolean wasDeleted = false;
                if(exe.exists()) {
                    Files.copy(Paths.get(pathToExe), Paths.get(APPDATA + "\\exe\\" + db + "_" + batName.replaceFirst(".bat", ".exe")));
                    wasDeleted = exe.delete();

                    if(wasDeleted == true) {
                        wasDeleted = new File(APPDATA + "\\bat\\cmd.bat").delete();
                    }
                } else {
                    JOptionPane.showMessageDialog(rootPane, "An critical error ocurred (0x715482).", "ERROR", JOptionPane.ERROR_MESSAGE);
                    return;
                }   bkSQL.jProgressBar.setValue(bkSQL.jProgressBar.getValue() + 10);
                if(wasDeleted == true) {
                    JOptionPane.showMessageDialog(rootPane, "Successfuly created backup files!", "Success!", JOptionPane.PLAIN_MESSAGE);
                    bkSQL.jProgressBar.setValue(100);
                    bkSQL.EnableAll();
                } else {
                    JOptionPane.showMessageDialog(rootPane, "An critical error ocurred (0x103628).", "ERROR", JOptionPane.ERROR_MESSAGE);
                    bkSQL.jProgressBar.setValue(0);
                    bkSQL.EnableAll();
                }
            } catch (IOException ex) {
                Logger.getLogger(WinScheduler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(WinScheduler.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    fw.close();
                } catch (IOException ex) {
                    Logger.getLogger(WinScheduler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    };
}
