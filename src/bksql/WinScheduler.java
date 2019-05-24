/**
 *
 * @author Brhaka - Eduardo J Martinez
 */

package bksql;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WinScheduler {
    protected static void CreateSchedule(String name, String path, String hour, String repeat) throws IOException, InterruptedException {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	Date date = new Date();
	String today = dateFormat.format(date);

        String command = "";

        command += " schtasks.exe";
        command += " /CREATE";
        command += " /TN";
        command += " \"" + name + "\"";
        command += " /TR";
        command += " \"" + path + "\"";
        command += " /SC";
        command += " " + repeat;
        command += " /ST";
        command += " " + hour;
        command += " /SD";
        command += " " + today;

        System.out.println(command);

        Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"" + command + "\"");
    }
}
