/**
 *
 * @author Brhaka - Eduardo J Martinez
 */

package bksql;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class WinScheduler {
    private static final String APPDATA = System.getenv("APPDATA") + "\\bkSQL";

    protected static void CreateSchedule(String name, String path, String pathNB, String hour, String repeat, String db, String password, String action) throws IOException, InterruptedException, SAXException, ParserConfigurationException {
        JFrame rootPane = new JFrame();

        if(Useful.IsNullOrEmpty(name) || Useful.IsNullOrEmpty(path) || Useful.IsNullOrEmpty(hour) || Useful.IsNullOrEmpty(repeat) || Useful.IsNullOrEmpty(db) || Useful.IsNullOrEmpty(pathNB) || Useful.IsNullOrEmpty(password) || Useful.IsNullOrEmpty(action)) {
            JOptionPane.showMessageDialog(rootPane, "An critical error ocurred (0x912836).", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd_hh-mm-ss");
        DateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
	Date date = new Date();
        String formaDay = dayFormat.format(date);
	String dateToSchedule = formaDay + "T" + hour;
        String today = dateFormat.format(date);

        InetAddress addr = InetAddress.getLocalHost();
        String computerName = addr.getHostName();
        String userName = System.getProperty("user.name");
        String xmlName = APPDATA + "\\xml\\task_" + db + "_" + today + ".xml";

        FileWriter fw = new FileWriter(xmlName);
        BufferedWriter bw = new BufferedWriter(fw);

        String xml = XML.GetXML();

        dateToSchedule = dateToSchedule.replace("_", "T");

        xml = xml.replace("[USER]", userName);
        xml = xml.replace("[NAME]", name);
        xml = xml.replace("[START_DATE]", dateToSchedule);
        xml = xml.replace("[PATH_W_BAT]", path);
        xml = xml.replace("[PATH_N_BAT]", pathNB);

        bw.write(xml);

        bw.close();
        fw.close();

        String command = "@echo off\n" +
            "\n" +
            ":: BatchGotAdmin\n" +
            ":-------------------------------------\n" +
            "REM  --> Check for permissions\n" +
            "    IF \"%PROCESSOR_ARCHITECTURE%\" EQU \"amd64\" (\n" +
            ">nul 2>&1 \"%SYSTEMROOT%\\SysWOW64\\cacls.exe\" \"%SYSTEMROOT%\\SysWOW64\\config\\system\"\n" +
            ") ELSE (\n" +
            ">nul 2>&1 \"%SYSTEMROOT%\\system32\\cacls.exe\" \"%SYSTEMROOT%\\system32\\config\\system\"\n" +
            ")\n" +
            "\n" +
            "REM --> If error flag set, we do not have admin.\n" +
            "if '%errorlevel%' NEQ '0' (\n" +
            "    echo Requesting administrative privileges...\n" +
            "    goto UACPrompt\n" +
            ") else ( goto gotAdmin )\n" +
            "\n" +
            ":UACPrompt\n" +
            "    echo Set UAC = CreateObject^(\"Shell.Application\"^) > \"%temp%\\getadmin.vbs\"\n" +
            "    set params= %*\n" +
            "    echo UAC.ShellExecute \"cmd.exe\", \"/c \"\"%~s0\"\" %params:\"=\"\"%\", \"\", \"runas\", 1 >> \"%temp%\\getadmin.vbs\"\n" +
            "\n" +
            "    \"%temp%\\getadmin.vbs\"\n" +
            "    del \"%temp%\\getadmin.vbs\"\n" +
            "    exit /B\n" +
            "\n" +
            ":gotAdmin\n" +
            "    pushd \"%CD%\"\n" +
            "    CD /D \"%~dp0\"" +
            "\n\nschtasks /create /xml \"" + xmlName + "\" /tn \"" + name + "\" /ru \"" + computerName + "\\\\" + userName + "\" /rp " + password +
            "\npause";
        String pathToBat = APPDATA + "\\bat\\cmd.bat";

        FileWriter fwb = new FileWriter(pathToBat);
        BufferedWriter bwb = new BufferedWriter(fwb);

        bwb.write(command);

        bwb.close();
        fwb.close();

        command = "cmd /K start cmd.exe /K \"C:\\WINDOWS\\system32\\cmd.exe /K \"" + pathToBat + "\"\"";

        System.out.println(command);

        Runtime.getRuntime().exec(command);

        JOptionPane.showMessageDialog(rootPane, "Successfuly created backup files!", "Success!", JOptionPane.PLAIN_MESSAGE);
    }
}
