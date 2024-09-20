import javax.swing.JFrame;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Post_View extends JFrame {

	private JTextField title;
	private JTextField writer;

	public Post_View(BoardVO vo) {

		final int num = vo.getNum();
		
		
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

		Dimension win_size = getContentPane().getSize();

		int x_pos = (int) (screen.getWidth() / 2 - win_size.getWidth() / 2);
		int y_pos = (int) (screen.getHeight() / 2 - win_size.getHeight() / 2);

		setBounds(new Rectangle(x_pos - 190, y_pos - 350, 380, 700));
		setTitle("게시글");
		getContentPane().setLayout(null);

		JLabel vnameLabel = new JLabel("백신종류");
		vnameLabel.setBounds(17, 25, 100, 20);
		getContentPane().add(vnameLabel);

		JLabel vnameLabel_2 = new JLabel(vo.getVname());
		vnameLabel_2.setBounds(85, 25, 100, 20);
		getContentPane().add(vnameLabel_2);

		JLabel titleLabel = new JLabel("제목");
		titleLabel.setBounds(27, 60, 100, 20);
		getContentPane().add(titleLabel);

		JLabel titleLabel_2 = new JLabel(vo.getTitle());
		titleLabel_2.setBounds(85, 60, 200, 20);
		getContentPane().add(titleLabel_2);
		
		JLabel writerLabel = new JLabel("작성자");
		writerLabel.setBounds(22, 95, 100, 20);
		getContentPane().add(writerLabel);
		
		BoardDAO dao = new BoardDAOImpl();
		JLabel writerLabel_2 = new JLabel(dao.get_userid(vo.getWriter()));
		writerLabel_2.setBounds(85, 95, 100, 20);
		getContentPane().add(writerLabel_2);
		
		JLabel contentLabel = new JLabel("내용");
		contentLabel.setBounds(27, 130, 100, 20);
		getContentPane().add(contentLabel);

		JTextArea textArea = new JTextArea(vo.getContent());
		textArea.setLineWrap(true);
		textArea.setRows(5);
		textArea.setBounds(85, 130, 250, 460);
		getContentPane().add(textArea);
		textArea.setEditable(false);


		JButton btnDel = new JButton("글삭제");
		btnDel.setBounds(135, 620, 100, 23);
		btnDel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
//				BoardDAO dao = new BoardDAOImpl();
//				BoardVO vo = new BoardVO();
//
//				vo.setNum(num);
//				dao.delete(vo);
//
//				JOptionPane.showMessageDialog(null, "게시글 삭제가 완료되었습니다.");
//
//				setVisible(false);
				new Input_Password(num, vo.getWriter());
			}
		});
		getContentPane().add(btnDel);

		JButton btnClose = new JButton("닫기");
		btnClose.setBounds(250, 620, 100, 23);
		btnClose.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				setVisible(false);

			}
		});
		getContentPane().add(btnClose);

		setVisible(true);

	}

}
