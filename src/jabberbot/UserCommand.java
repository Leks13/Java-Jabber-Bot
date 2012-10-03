package jabberbot;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.Date;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.muc.MultiUserChat;

public class UserCommand {

    public static String muc;

    public static Boolean doUserCommand(String command, String jid, String admin) throws XMPPException, IOException {
        Boolean ans = false;
        String msg = "";

        if (command.startsWith(".uptime") && ans == false) {
            command = new StringBuffer(command).delete(0, 1).toString();
            System.out.println(command);
            Log.addToLog(command);
            Process p = Runtime.getRuntime().exec(command);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                msg = msg + '\n' + line;
            }
            XmppNet.sendMessage(jid, msg);
            ans = true;
        }

        if (command.startsWith(".report") && !ans) {
            command = new StringBuffer(command).delete(0, 7).toString();
            msg = command + " - " + jid;
            XmppNet.sendMessage(admin, msg);

            ans = true;
        }
        try {
            if (command.startsWith(".last")) {
                String muc2 = new StringBuffer(command).delete(0, 7).toString();
                DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT);
                Date aDate = new Date();
                String formattedDate = dateFormatter.format(aDate);
                if (jid.contains("@conference.")) {
                    muc2 = muc;
                }
                System.out.println("tail -50 log/muc/" + formattedDate + "/" + muc2 + ".log");
                Process p = Runtime.getRuntime().exec("tail -50 log/muc/" + formattedDate + "/" + muc2 + ".log");
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    msg = msg + '\n' + line;
                }
                XmppNet.sendMessage(jid, msg);
                ans = true;

            }
        } catch (IOException ex2) {
            msg = "File or directory not found";
            XmppNet.sendMessage(jid, msg);
                ans = true;

        }
        try {
            if (command.startsWith(".list")) {
                DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT);
                Date aDate = new Date();
                String formattedDate = dateFormatter.format(aDate);
                System.out.println("ls");
                File file = new File("log/muc/" + formattedDate);
                Runtime runtime = Runtime.getRuntime();
                Process p = runtime.exec("ls", null, file);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    msg = msg + '\n' + line;
                }
                XmppNet.sendMessage(jid, msg);
                ans = true;
            }
        } catch (IOException ex1) {
            msg = "File or directory not found";
            XmppNet.sendMessage(jid, msg);
                ans = true;
        }



        if (command.startsWith(".off") && !ans && jid.contains(admin)) {
            XmppNet.disconnect();

            ans = true;
        }


        if (command.startsWith(".roster") && !ans && jid.contains(admin)) {
            msg = XmppNet.getXmppRoster();
            XmppNet.sendMessage(jid, msg);
            ans = true;

        }

        if (command.startsWith(".status") && !ans && jid.contains(admin)) {
            command = new StringBuffer(command).delete(0, 8).toString();
            String status = command;
            Presence presence = new Presence(Presence.Type.available);
            presence.setStatus(status);
            XmppNet.connection.sendPacket(presence);
            ans = true;
        }

        if (command.startsWith(".muc") && !ans && jid.contains(admin)) {
            command = new StringBuffer(command).delete(0, 5).toString();
            muc = command;
            MultiUserChat mucU = new MultiUserChat(XmppNet.connection, command);
            mucU.join("Logger(beta)");
            XmppNet.sendMessage(admin, "I`m going..");

            ans = true;
        }

        if (command.startsWith(".help") && !ans) {
            msg = "Commands: \n.uptime - аптайм сервера,"
                    + " \n.report <сообщение> - написать админу,"
                    + " \n.last <конференция> - получить 50 последних сообщений из конференции, "
                    + "\n.list - список записанных конференций.";
            XmppNet.sendMessage(jid, msg);
            ans = true;
        }

        return ans;
    }
}
