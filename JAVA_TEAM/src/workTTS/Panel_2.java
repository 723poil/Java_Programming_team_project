package workTTS;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

public class Panel_2 extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private final JButton resetPath; // 경로 재설정해주는 버튼
	private final JButton button2;
	private final JButton button3;
	public final JToggleButton button5;
	private final Panel_1 panel1;
	private final Panel_3 panel3;
	
	public Panel_2(Panel_1 panel1, Panel_3 panel3) { // 패널1을 가져와서 리스트 설정할때 도움을 줌
		setLayout(null);
		
		resetPath = new JButton("경로 재설정");    // 경로 재설정 버튼
		button2 = new JButton("리스트 초기화");  // 버튼 클릭 시 재확인 창이 뜨면 좋겠음 
		button3 = new JButton("리스트 삭제");      // 1개씩 위에서 부터 삭제 되도록 (mp3파일도 같이 삭제 되어야함)
		button5 = new JToggleButton("음성 반복 재생", false);
		this.panel1 = panel1;                   // 패널1 선언
		this.panel3 = panel3;
		
		resetPath.setBounds(15,25,150,50);
		button2.setBounds(15,125,150,50);
		button3.setBounds(15,225,150,50);
		button5.setBounds(15,325,150,50);
		
		add(resetPath);
		add(button2);
		add(button3);
		add(button5);
		
		EventHandler handler = new EventHandler();
		resetPath.addActionListener(handler);
		button2.addActionListener(handler);
		button3.addActionListener(handler);
		button5.addActionListener(handler);
	}
	
	private class EventHandler implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			if(event.getSource() == resetPath) { // 경로 재설정 버튼을 클릭했을 경우
				Total_Frame.directoryPath = Total_Frame.getDirectoryPath();   // Total_Frame의 변수와 method를 가져옴
				try {
					if(!Total_Frame.directoryPath.toString().equals("")) {
						Panel_1.UpdateTextList(panel1, panel3);   // 리스트 업데이트 함수
					}                                     // 여기있는 panel1이 생성자로 가져온 변수
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (UnsupportedAudioFileException e) {
					e.printStackTrace();
				}
			}
			else if(event.getSource() == button2 ) {
				if(!Total_Frame.directoryPath.toString().equals("") && JOptionPane.showConfirmDialog(null, "정말로 초기화 하시겠습니까?") == 0) {
					try {
						FileManager.deleteAllFile(Total_Frame.directoryPath);
					} catch (IOException | UnsupportedAudioFileException e) {
						e.printStackTrace();
					}
				}
				else if(Total_Frame.directoryPath.toString().equals("")) {
					JOptionPane.showMessageDialog(null, "경로를 지정하세요.");
				}
			}
			else if(event.getSource() == button3) {
				try {
					FileManager.deleteOneFile();
				} catch (IOException | UnsupportedAudioFileException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
