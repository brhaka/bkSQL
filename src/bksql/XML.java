/**
 *
 * @author Brhaka - Eduardo J Martinez
 */

package bksql;

public class XML {
    protected static String GetXML() {
        return "<?xml version=\"1.0\" encoding=\"UTF-16\"?>\n" +
            "<Task version=\"1.4\" xmlns=\"http://schemas.microsoft.com/windows/2004/02/mit/task\">\n" +
            "  <RegistrationInfo>\n" +
            "    <Author>[USER]</Author>\n" +
            "    <URI>\\[NAME]</URI>\n" +
            "  </RegistrationInfo>\n" +
            "  <Triggers>\n" +
            "    <CalendarTrigger>\n" +
            "      <StartBoundary>[START_DATE]</StartBoundary>\n" +
            "      <ExecutionTimeLimit>P1D</ExecutionTimeLimit>\n" +
            "      <Enabled>true</Enabled>\n" +
            "      <ScheduleByDay>\n" +
            "        <DaysInterval>1</DaysInterval>\n" +
            "      </ScheduleByDay>\n" +
            "    </CalendarTrigger>\n" +
            "  </Triggers>\n" +
            "  <Principals>\n" +
            "    <Principal id=\"Author\">\n" +
            "      <UserId>S-1-5-21-2326166032-1762985320-541803443-1001</UserId>\n" +
            "      <LogonType>Password</LogonType>\n" +
            "      <RunLevel>HighestAvailable</RunLevel>\n" +
            "    </Principal>\n" +
            "  </Principals>\n" +
            "  <Settings>\n" +
            "    <MultipleInstancesPolicy>StopExisting</MultipleInstancesPolicy>\n" +
            "    <DisallowStartIfOnBatteries>false</DisallowStartIfOnBatteries>\n" +
            "    <StopIfGoingOnBatteries>false</StopIfGoingOnBatteries>\n" +
            "    <AllowHardTerminate>true</AllowHardTerminate>\n" +
            "    <StartWhenAvailable>true</StartWhenAvailable>\n" +
            "    <RunOnlyIfNetworkAvailable>true</RunOnlyIfNetworkAvailable>\n" +
            "    <IdleSettings>\n" +
            "      <StopOnIdleEnd>false</StopOnIdleEnd>\n" +
            "      <RestartOnIdle>false</RestartOnIdle>\n" +
            "    </IdleSettings>\n" +
            "    <AllowStartOnDemand>true</AllowStartOnDemand>\n" +
            "    <Enabled>true</Enabled>\n" +
            "    <Hidden>true</Hidden>\n" +
            "    <RunOnlyIfIdle>false</RunOnlyIfIdle>\n" +
            "    <DisallowStartOnRemoteAppSession>false</DisallowStartOnRemoteAppSession>\n" +
            "    <UseUnifiedSchedulingEngine>true</UseUnifiedSchedulingEngine>\n" +
            "    <WakeToRun>false</WakeToRun>\n" +
            "    <ExecutionTimeLimit>PT0S</ExecutionTimeLimit>\n" +
            "    <Priority>5</Priority>\n" +
            "  </Settings>\n" +
            "  <Actions Context=\"Author\">\n" +
            "    <Exec>\n" +
            "      <Command>[PATH_W_BAT]</Command>\n" +
            "      <WorkingDirectory>[PATH_N_BAT]</WorkingDirectory>\n" +
            "    </Exec>\n" +
            "  </Actions>\n" +
            "</Task>";
    }
}
