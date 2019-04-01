import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MainClass {
	private ServerSocket server;

	//사용자 객체들을 관리하는 ArrayList
	ArrayList<UserClass> user_list;
	ArrayList<String> user_nickname;
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		new MainClass();
	}	
    //메인메소드가 static으로 되어있기 때문에 다른것들을 다 static 으로 하기 귀찮기 때문에
	// 따로 생성자를 만들어서 진행 - > 메인에서는 호출정도의 기능만 구현하는게 좋다.
	public MainClass() {
		try {
			user_list=new ArrayList<UserClass>();
			user_nickname = new ArrayList<String>();
			// 서버 가동
			server=new ServerSocket();
			// 사용자 접속 대기 스레드 가동
			ConnectionThread thread= new ConnectionThread();
			thread.start();
		}catch(Exception e) {e.printStackTrace();}
	}
	
	class ConnectionThread extends Thread{
		
		@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					while(true) {
						System.out.println("사용자 접속 대기");
						
						Socket socket=server.accept();
				
						System.out.println("사용자가 접속하였습니다.");
						// 사용자 닉네임을 처리하는 스레드 가동
						NickNameThread thread=new NickNameThread(socket);
						thread.start();
						
					}
				}catch(Exception e) {e.printStackTrace();}
			}

		}
	class NickNameThread extends Thread{
		private Socket socket;
		
		public NickNameThread(Socket socket) {
			this.socket=socket;
		}
		
			public void run() {
				try {
					// 스트림 추출
					InputStream is = socket.getInputStream();
					OutputStream os= socket.getOutputStream();
					DataInputStream dis=new DataInputStream(is);
					DataOutputStream dos=new DataOutputStream(os);
					
					//닉네임 수신
					String nickName=dis.readUTF();
					// 환영 메세지를 전달한다.
					dos.writeUTF(nickName+" 님 환영합니다.");
					// 기 접속된 사용자들에게 접속 메세지를 전달한다.
					// 사용자 정보를 관리하는 객체를 생성한다.
					UserClass user= new UserClass(nickName,socket);
					
					user.start();
					user_list.add(user);
					//user_nickname.add("냥냥");
					
				}catch(Exception e) {e.printStackTrace();}
			}
		

	}
	class UserClass extends Thread {
		String nickName;
		Socket socket;
		DataInputStream dis;
		DataOutputStream dos;
		
		public UserClass(String nickName,Socket socket) {
			try {
			this.nickName=nickName;
			this.socket=socket;
			InputStream is=socket.getInputStream();
			OutputStream os=socket.getOutputStream();
			dis = new DataInputStream(is);
			dos=new DataOutputStream(os);
			
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		// 사용자로부터 메세지를 수신받는 스레드
		public void run() {
			try {
				while(true) {
					//클라이언트에게 메세지를 수신받는다.
					String msg=dis.readUTF();
					// 사용자들에게 메세지를 전달한다
					sendToClient(nickName+ " : "+ msg); 
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		
	}
	public synchronized void sendToClient(String msg) {
		try {
			// 사용자의 수만큼 반복
			for (UserClass user : user_list) {
				// 메세지를 클라이언트들에게 전달한다.
				user.dos.writeUTF(msg);
			
			
				
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	

	
}
