package workTTS;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

public class Panel_3 extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	// panel3 컴포넌트
	private final JTextField textField;       // 텍스트를 입력할 공간
	private final JButton buttonText;         // 텍스트를 입력하고 텍스트 파일에 저장할 때 사용
	public JLabel listCount; 
	public final JButton stopButton;
	public final JToggleButton pauseButton;
	public final JToggleButton playButton;
	public final JProgressBar voicePlayBar;
	private final JLabel mainImage;
	private ButtonGroup bg = new ButtonGroup();
	public final Icon clickedplay;
	public final Icon playImage;
	public final Icon clickedpause;
	public final Icon pauseImage;
	public static boolean dispose = false;
	public JLabel curtime;
	public JLabel totaltime;
	
	public Panel_3() {
		setLayout(null); //
		
		listCount = new JLabel("리스트 수");          // 리스트에 있는 텍스트의 수 표시
		stopButton = new JButton();        // 음성 정지 버튼
		pauseButton = new JToggleButton("", false);       // 음성 일시정지 버튼
		playButton = new JToggleButton("", false);        // 음성 재생 버튼
		voicePlayBar = new JProgressBar(); // 음성 진행바
		mainImage = new JLabel();
		curtime = new JLabel("00:00");
		totaltime = new JLabel("00:00");
		
		totaltime.setHorizontalAlignment(SwingConstants.RIGHT);
		
		bg.add(playButton);
		bg.add(pauseButton);
		
		playImage = makeIcon("playImage.png");    // 재생 아이콘 생성
		pauseImage = makeIcon("pauseImage.png");  // 일시정지 아이콘 생성
		Icon stopImage = makeIcon("stopImage.png");    // 정지 아이콘 생성
		Icon main = makeIcon("mainImage.png");
		clickedplay  = new ImageIcon(getClass().getResource("clickedplay.png"));
		clickedpause  = new ImageIcon(getClass().getResource("clickedpause.png"));
		
		playButton.setIcon(playImage);             // 버튼에 set
		pauseButton.setIcon(pauseImage);
		stopButton.setIcon(stopImage);
		mainImage.setIcon(main);
		
		voicePlayBar.setValue(0);
		
		curtime.setBounds(0, 60,150,20);
		totaltime.setBounds(150, 60, 150, 20);
		listCount.setBounds(0, 0, 480, 30);
		voicePlayBar.setBounds(0, 30, 300, 30);
		stopButton.setBounds(40, 85, 50, 50);
		playButton.setBounds(120, 85, 50, 50);
		pauseButton.setBounds(200, 85, 50, 50);
		mainImage.setBounds(320, 0, 140, 140);
		
		add(listCount);
		add(voicePlayBar);
		add(stopButton);
		add(playButton);
		add(pauseButton);
		add(mainImage);
		add(curtime);
		add(totaltime);
		
		textField = new JTextField(70);
		textField.setBounds(3, 155, 400, 50);
		add(textField);
		
		buttonText = new JButton("저장");
		buttonText.setBounds(413, 155, 60, 50);
		add(buttonText);
		
		Handler handler = new Handler();
		textField.addActionListener(handler);
		buttonText.addActionListener(handler);
		stopButton.addActionListener(handler);
	}
	
	private class Handler implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			String string = "";		// text 저장 공간
			
			if(event.getSource() == stopButton) {
				dispose = true;
			}
			
			if(event.getSource() == textField || event.getSource() == buttonText) {
				if(Panel_1.count == 29) {
					JOptionPane.showMessageDialog(null, "리스트가 가득 차 있습니다.");
					return;
				}
				string = textField.getText();
				StringBuilder builder = new StringBuilder();
				
				try {
					builder.append(new String(string.getBytes(StandardCharsets.UTF_8), "UTF-8"));
					Panel_1.storedTTS[++Panel_1.count] = builder.toString();	// storedTTS 객체에 저장
					if(Panel_1.count < 10) {
						File file = new File(Total_Frame.directoryPath.toString()+
								"\\"+"0_"+Panel_1.count+".txt");
						setTextFile(file, builder);	// 텍스트 파일 생성 및 파일에 텍스트 추가
					}
					else if(Panel_1.count < 20) {
						File file = new File(Total_Frame.directoryPath.toString()+
								"\\"+"1_"+(Panel_1.count-10)+".txt");
						setTextFile(file, builder);	// 텍스트 파일 생성 및 파일에 텍스트 추가
					}
					else {
						File file = new File(Total_Frame.directoryPath.toString()+
								"\\"+"2_"+(Panel_1.count-20)+".txt");
						setTextFile(file, builder);	// 텍스트 파일 생성 및 파일에 텍스트 추가
					}
					
					SetFileTTS.create(string, Panel_1.count);	// TTS 파일 생성
					Panel_1.valueCount = true;
//					AudioPlayer.playAudio(new File(Total_Frame.directoryPath.toString()
//							+"\\TTS\\"+Panel_1.count+".mp3"));
					Panel_1.executorService.execute(new Audiothread(new File(Total_Frame.directoryPath.toString()
							+"\\TTS\\"+Panel_1.count+".mp3")));
					
					Total_Frame.panel1.updateUI();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				
				textField.setText("");		// textField 초기화
			}
		}
	}
	
	public Icon makeIcon(String imageName) {  // 아이콘 생성 method
		return new ImageIcon(getClass().getResource(imageName));
	}
	
	public void setTextFile(File file, StringBuilder string) {              	// 텍스트파일에 입력한 텍스트 추가
			try {
				file.createNewFile();
				
				BufferedWriter output = new BufferedWriter(
								new OutputStreamWriter(new FileOutputStream(file.getPath()), "UTF-8"));
				
				output.write(string.toString());
				output.close();
				int listcount = Panel_1.count+1;
				Total_Frame.panel3.listCount.setText("리스트 수 "+listcount+" / 30"); // 리스트 수 나타내는 라벨 업데이트
				Total_Frame.panel3.listCount.updateUI();	
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}
