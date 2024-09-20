import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class Post_Search extends JFrame {
	private JTable table;
	private JTextField searchString;

	public Post_Search(List<BoardVO> vos) {

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

		Dimension win_size = getContentPane().getSize();

		int x_pos = (int) (screen.getWidth() / 2 - win_size.getWidth() / 2);
		int y_pos = (int) (screen.getHeight() / 2 - win_size.getHeight() / 2);

		setTitle("백신 접종 후기 게시판");
		setBounds(new Rectangle(x_pos - 190, y_pos - 350, 380, 700));
		getContentPane().setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(8, 49, 350, 570);
		getContentPane().add(scrollPane);

		BoardDAO dao = new BoardDAOImpl();
		List<BoardVO> list = vos;

		String[] colNames = new String[] { "백신종류", "제목", "작성자" };
		Object[][] rowDatas = new Object[list.size()][colNames.length];

		for (int i = 0; i < list.size(); i++) {
			rowDatas[i] = new Object[] { list.get(i).getVname(), list.get(i).getTitle(),
					dao.get_userid(list.get(i).getWriter()) };
		}
		table = new JTable();
		table.setModel(new DefaultTableModel(rowDatas, colNames) {
			boolean[] columnEditables = new boolean[] { false, false, false };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(0).setPreferredWidth(70);
		table.getColumnModel().getColumn(1).setResizable(false);
		table.getColumnModel().getColumn(1).setPreferredWidth(200);
		table.getColumnModel().getColumn(2).setResizable(false);
		table.getColumnModel().getColumn(2).setPreferredWidth(50);

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				int rowNum = table.getSelectedRow();
				BoardVO vos = new BoardVO();
				vos = list.get(rowNum);

				new Post_View(vos);
			}
		});
		scrollPane.setViewportView(table);

		JLabel lblNewLabel = new JLabel("검색");
		lblNewLabel.setBounds(10, 635, 56, 15);
		getContentPane().add(lblNewLabel);

		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] { "제목", "내용", "백신종류" }));
		comboBox.setBounds(45, 632, 70, 21);
		getContentPane().add(comboBox);

		searchString = new JTextField();
		searchString.setBounds(122, 632, 145, 21);
		getContentPane().add(searchString);
		searchString.setColumns(10);

		JButton btnSearch = new JButton("search");
		btnSearch.setBounds(275, 632, 80, 23);
		btnSearch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (searchString.getText().trim().length() == 0) {
					JOptionPane.showMessageDialog(null, "검색어를 입력해주세요.");
				} else {
					BoardDAO dao = new BoardDAOImpl();
					if (String.valueOf(comboBox.getSelectedItem()) == "제목") {
						dao.search("ptitle", searchString.getText());
					} else if (String.valueOf(comboBox.getSelectedItem()) == "내용") {
						dao.search("pcontent", searchString.getText());
					} else if (String.valueOf(comboBox.getSelectedItem()) == "백신종류") {
						dao.search("vname", searchString.getText());
					}

					setVisible(false);
				}

			}
		});
		getContentPane().add(btnSearch);

		JButton btnWrite = new JButton("글작성");
		btnWrite.setBounds(12, 16, 110, 23);
		btnWrite.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new Post_Add();

			}
		});

		getContentPane().add(btnWrite);

		JButton btnrefresh = new JButton("새로고침");
		btnrefresh.setBounds(128, 16, 110, 23);
		btnrefresh.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				new BoardList();
			}
		});
		getContentPane().add(btnrefresh);

		JButton btnclose = new JButton("닫기");
		btnclose.setBounds(244, 16, 110, 23);
		btnclose.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		getContentPane().add(btnclose);

		setVisible(true);
	}
}