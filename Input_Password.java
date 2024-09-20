import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class Input_Password extends JFrame {
	private JPasswordField password;

	public Input_Password(int num, int writer) {

		BoardDAO dao = new BoardDAOImpl();

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

		Dimension win_size = getContentPane().getSize();

		int x_pos = (int) (screen.getWidth() / 2 - win_size.getWidth() / 2);
		int y_pos = (int) (screen.getHeight() / 2 - win_size.getHeight() / 2);

		setTitle("비밀번호 확인");
		setBounds(new Rectangle(x_pos - 190, y_pos - 350, 380, 200));
		getContentPane().setLayout(null);

		JLabel pwLabel = new JLabel("비밀번호 입력");
		pwLabel.setBounds(27, 60, 120, 20);
		getContentPane().add(pwLabel);

		password = new JPasswordField();
		password.setBounds(130, 60, 200, 21);
		getContentPane().add(password);
		password.setColumns(10);

		JButton btnClose = new JButton("확인");
		btnClose.setBounds(245, 110, 97, 23);
		btnClose.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (password.getText().trim().length() == 0) {
					JOptionPane.showMessageDialog(null, "비밀번호를 입력해주세요.");
				} else {
					if (dao.password_check(password.getText(), writer) == true) {
						BoardDAO dao = new BoardDAOImpl();
						BoardVO vo = new BoardVO();

						vo.setNum(num);
						dao.delete(vo);

						JOptionPane.showMessageDialog(null, "게시글 삭제가 완료되었습니다.");

						setVisible(false);

					} else {
						JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다.");
					}

				}
			}
		});
		getContentPane().add(btnClose);

		setVisible(true);
	}

}
