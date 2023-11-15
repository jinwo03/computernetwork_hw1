import java.io.*;
import java.net.*;
import java.util.*;

public class CalcClientEx {
    public static void main(String[] args) {
        try {
            // 파일 입력 스트림 객체 생성
            FileInputStream inner = new FileInputStream("server_info.dat");

            // 파일 데이터를 저장할 배열 생성 (0으로 자동 초기화 됨)
            String OutputString = "";
            byte arr[] = new byte[16];
            while (true) {				
                int num = inner.read(arr);
                if (num < 0)					
                    break;				
                for (int cnt = 0; cnt < num; cnt++){
                    int value = arr[cnt] & 0xff; // 바이트를 int로 변환
                    char charact = (char) value;
                    OutputString += Character.toString(charact);
                }									
                
            }
            
            String[] array = OutputString.split(" ");
            array[0]=array[0].substring(1);

        BufferedReader in = null;
        BufferedWriter out = null;
        Socket socket = null;
        Scanner scanner = new Scanner(System.in);
        try {
            socket = new Socket(array[0], Integer.parseInt(array[2]));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            while (true) {
                System.out.print("계산식(빈칸으로 띄어 입력,예:24 + 42)>>"); // 프롬프트
                String outputMessage = scanner.nextLine(); // 키보드에서 수식 읽기
                if (outputMessage.equalsIgnoreCase("bye")) {
                    out.write(outputMessage + "\n"); // "bye" 문자열 전송
                    out.flush();
                    break; // 사용자가 "bye"를 입력한 경우 서버로 전송 후 연결 종료
                }
                out.write(outputMessage + "\n"); // 키보드에서 읽은 수식 문자열 전송
                out.flush();
                String inputMessage = in.readLine(); // 서버로부터 계산 결과 수신
                System.out.println("계산 결과: " + inputMessage);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                scanner.close();
                if (socket != null)
                    socket.close(); // 클라이언트 소켓 닫기
            } catch (IOException e) {
                System.out.println("서버와 채팅 중 오류가 발생했습니다.");
            }
        }
            // 파일 입력 스트림 닫기
            // 파일 스트림 버퍼 해제, 프로세스와 OS 연결 해제
        inner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
