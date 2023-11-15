import java.io.*;
import java.net.*;
import java.util.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class CalcServerEx {

    static class ClientHandler extends Thread {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            ) {
                while (true) {
                    String inputMessage = in.readLine();
                    if (inputMessage.equalsIgnoreCase("bye")) {
                        System.out.println("클라이언트에서 연결을 종료하였음");
                        break;
                    }
                    System.out.println(inputMessage);

                    String res = calc(inputMessage);
                    out.write(res + "\n");
                    out.flush();
                }
            } catch (IOException e) {
                System.out.println("클라이언트와 통신 중 오류가 발생했습니다.");
            } finally {
                try {
                    if (socket != null)
                        socket.close();
                } catch (IOException e) {
                    System.out.println("클라이언트 소켓 닫는 중 오류가 발생했습니다.");
                }
            }
        }
    }

    public static String calc(String exp) {
        StringTokenizer st = new StringTokenizer(exp, " ");
        if (st.countTokens() != 3)
            return "error : too many arguments";
        String res = "";
        int op1 = Integer.parseInt(st.nextToken());
        String opcode = st.nextToken();
        int op2 = Integer.parseInt(st.nextToken());
        switch (opcode) {
            case "+":
                res = Integer.toString(op1 + op2);
                break;
            case "-":
                res = Integer.toString(op1 - op2);
                break;
            case "*":
                res = Integer.toString(op1 * op2);
                break;
            case "/":
                if (op2 == 0){
                    res = "error : divided by zero";
                    break;
                }
                res = Integer.toString(op1 / op2);
                break;
            default:
                res = "error : wrong operator";
        }
        return res;
    }

    public static void main(String[] args) {
        ServerSocket listener = null;

        try {
            listener = new ServerSocket(9999);
            System.out.println("연결을 기다리고 있습니다.....");

            while (true) {
                Socket socket = listener.accept();
                System.out.println("연결되었습니다.");

                ClientHandler clientThread = new ClientHandler(socket);
                clientThread.start();
            }
        } catch (IOException e) {
            System.out.println("서버 실행 중 오류가 발생했습니다.");
        } finally {
            try {
                if (listener != null)
                    listener.close();
            } catch (IOException e) {
                System.out.println("서버 소켓 닫는 중 오류가 발생했습니다.");
            }
        }
    }
}