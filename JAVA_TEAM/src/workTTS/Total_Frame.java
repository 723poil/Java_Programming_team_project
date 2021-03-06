package workTTS;



import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;


public class Total_Frame extends JFrame { //
	
	static Panel_1 panel1 = new Panel_1(); // 리스트 패널 생성
	static Panel_3 panel3 = new Panel_3(); // 텍스트필드 패널 생성
	static Panel_2 panel2 = new Panel_2(panel1, panel3); // 버튼 패널 생성
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static Path directoryPath = Paths.get("");

	public static void main(String[] args) {
		
		JFrame totalFrame = new JFrame("Working TTS"); // Frame 생성
		totalFrame.setLayout(null);
			
		totalFrame.setBounds(0, 0, 500, 650);
		
		panel1.setBounds(5, 5, 300, 400);		
		panel2.setBounds(305, 0, 195, 400);		
		panel3.setBounds(5, 400, 475, 220);

		totalFrame.add(panel1);
		totalFrame.add(panel2);
		totalFrame.add(panel3);
		
		try{
	        //Windows 스타일의 Look & Feel로 설정
	        UIManager.setLookAndFeel (
	            "com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
	 
	        //설정에 반영한다.
	        SwingUtilities.updateComponentTreeUI(totalFrame) ;
	   
	    //에러 처리 블록
	    }catch(Exception e){
	        System.out.println(e + "오류 발생");
	    }
		
		totalFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // frame 창 실행
		totalFrame.setVisible(true);
		totalFrame.setLocationRelativeTo(null);
		totalFrame.setResizable(false);		
		
		totalFrame.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowOpened(WindowEvent e) { // 창이 실행 될 경우
				directoryPath = getDirectoryPath();   // 디렉토리 경로를 구하는 함수
				try {
					if(!directoryPath.toString().equals("")) {
						Panel_1.UpdateTextList(panel1, panel3);   // 리스트 업데이트 함수
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (UnsupportedAudioFileException e1) {
					e1.printStackTrace();
				}
			}
		});
	}
	
	public static Path getDirectoryPath() {                               // 디렉토리 경로 구하는 함수
		JFileChooser fileChooser = new JFileChooser();                    // FileChooser 선언
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);  // 디렉토리만 선택할 수 있도록 설정
		int result = fileChooser.showOpenDialog(fileChooser);             // 대화창 열기
		if(result == JFileChooser.CANCEL_OPTION) return Paths.get("");    // 취소 클릭 시 빈 경로 반환
		
		return fileChooser.getSelectedFile().toPath();                    // 경로값 리턴
	}
}
