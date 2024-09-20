
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Post_Add extends JFrame {
	private JTextField vname;
	private JTextField title;
	private JTextField writer;

	public Post_Add() {

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

		Dimension win_size = getContentPane().getSize();

		int x_pos = (int) (screen.getWidth() / 2 - win_size.getWidth() / 2);
		int y_pos = (int) (screen.getHeight() / 2 - win_size.getHeight() / 2);

		setBounds(new Rectangle(x_pos - 190, y_pos - 350, 380, 700));
		setTitle("�Խñ� ���");
		getContentPane().setLayout(null);

		JLabel vnameLabel = new JLabel("�������");
		vnameLabel.setBounds(17, 25, 100, 20);
		getContentPane().add(vnameLabel);

		JComboBox combobox = new JComboBox();
		combobox.setModel(new DefaultComboBoxModel(new String[] { "ȭ����", "�����", "�ƽ�Ʈ������ī", "�Ἶ" }));
		combobox.setBounds(85, 25, 140, 21);
		getContentPane().add(combobox);

		JLabel titleLabel = new JLabel("����");
		titleLabel.setBounds(27, 60, 100, 20);
		getContentPane().add(titleLabel);

		title = new JTextField();
		title.setBounds(85, 60, 250, 21);
		getContentPane().add(title);
		title.setColumns(10);

		JLabel writerLabel = new JLabel("���̵�");
		writerLabel.setBounds(22, 95, 100, 20);
		getContentPane().add(writerLabel);

		writer = new JTextField();
		writer.setBounds(85, 95, 250, 21);
		getContentPane().add(writer);
		writer.setColumns(10);

		JLabel contentLabel = new JLabel("����");
		contentLabel.setBounds(27, 130, 100, 20);
		getContentPane().add(contentLabel);

		JTextArea textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setRows(5);
		textArea.setBounds(85, 130, 250, 460);
		getContentPane().add(textArea);

		JButton btnWrite = new JButton("�ۼ��Ϸ�");
		btnWrite.setBounds(140, 620, 97, 23);
		btnWrite.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				BoardDAO dao = new BoardDAOImpl();
				BoardVO vo = new BoardVO();

				String vnames = String.valueOf(combobox.getSelectedItem());
				String titles = title.getText();
				String txtarea = textArea.getText();
				String userid = writer.getText();
				if (title.getText().trim().length() == 0) {
					JOptionPane.showMessageDialog(null, "������ �Է����ּ���.");
				} else if (writer.getText().trim().length() == 0) {
					JOptionPane.showMessageDialog(null, "�ۼ��� ���̵� �Է����ּ���.");
				} else if (textArea.getText().trim().length() == 0) {
					JOptionPane.showMessageDialog(null, "������ �Է����ּ���.");
				} else {
					if (dao.get_userno(userid) == -1) {
						JOptionPane.showMessageDialog(null, "�Է��� ���̵� �������� �ʽ��ϴ�. �ٽ� �Է����ּ���.");
					} else {
						vo.setVname(vnames);
						vo.setTitle(titles);
						vo.setContent(txtarea);
						vo.setWriter(dao.get_userno(userid));

						dao.insert(vo);
						JOptionPane.showMessageDialog(null, "�Խñ��� ��ϵǾ����ϴ�.");

						setVisible(false);
					}
				}

			}
		});
		getContentPane().add(btnWrite);

		JButton btnClose = new JButton("���");
		btnClose.setBounds(245, 620, 97, 23);
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