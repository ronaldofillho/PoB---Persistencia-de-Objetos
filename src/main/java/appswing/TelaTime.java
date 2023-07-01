/**********************************
 * IFPB - Curso Superior de Tec. em Sist. para Internet
 * Persistencia de objetos
 * Prof. Fausto Maranh�o Ayres
 **********************************/
package appswing;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import modelo.Jogo;
import modelo.Time;
import regras_negocio.Fachada;

public class TelaTime {
	private JFrame frmTimes;
	private JTable table;
	private JScrollPane scrollPane;
	private JButton button;
	private JButton btnListarTimes;
	private JTextField textField_1;
	private JLabel label;
	private JLabel lblOrigem;
	private JLabel label_8;
	private JLabel lblNome;
	private JTextField textField;
	private JButton btnSelecionarTime;



	/**
	 * Launch the application.
	 */
	//	public static void main(String[] args) {
	//		EventQueue.invokeLater(new Runnable() {
	//			public void run() {
	//				try {
	//					TelaJogo window = new TelaJogo();
	//					window.frame.setVisible(true);
	//				} catch (Exception e) {
	//					e.printStackTrace();
	//				}
	//			}
	//		});
	//	}

	/**
	 * Create the application.
	 */
	public TelaTime() {
		initialize();
		frmTimes.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTimes = new JFrame();
		frmTimes.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 12));
		frmTimes.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				Fachada.inicializar();
				listagem();
			}
			@Override
			public void windowClosing(WindowEvent e) {
				Fachada.finalizar();
			}
		});

		frmTimes.setTitle("Times");
		frmTimes.setBounds(100, 100, 912, 325);
		frmTimes.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmTimes.getContentPane().setLayout(null);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(26, 42, 844, 120);
		frmTimes.getContentPane().add(scrollPane);

		table = new JTable();
		table.setGridColor(Color.BLACK);
		table.setRequestFocusEnabled(false);
		table.setFocusable(false);
		table.setBackground(Color.WHITE);
		table.setFillsViewportHeight(true);
		table.setRowSelectionAllowed(true);
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		scrollPane.setViewportView(table);
		table.setBorder(new LineBorder(new Color(0, 0, 0)));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setShowGrid(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);


		button = new JButton("Criar time");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(textField_1.getText().isEmpty() || textField.getText().isEmpty() ){
						label.setText("campo vazio");
						return;
					}

					String origem = textField_1.getText();
					String nome = textField.getText();
					Time time = Fachada.criarTime(nome, origem);
					label.setText("time criado: " + time.getNome());
					textField_1.setText("");
					textField.setText("");
					listagem();
				}
				catch(Exception ex) {
					label.setText(ex.getMessage());
				}
			}
		});
		button.setFont(new Font("Tahoma", Font.PLAIN, 12));
		button.setBounds(517, 191, 95, 23);
		frmTimes.getContentPane().add(button);
		
		btnSelecionarTime = new JButton("Apagar time");
		btnSelecionarTime.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (table.getSelectedRow() >= 0) {
						String nome = (String) table.getValueAt(table.getSelectedRow(), 0);
						Fachada.apagarTime(nome);
						label.setText("Time apagado: " + nome);
						listagem();
					}
				}
				catch(Exception ex) {
					label.setText(ex.getMessage());
				}
			}
		});
		btnSelecionarTime.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnSelecionarTime.setBounds(723, 174, 147, 23);
		frmTimes.getContentPane().add(btnSelecionarTime);

		label = new JLabel("");
		label.setForeground(Color.BLUE);
		label.setBackground(Color.RED);
		label.setBounds(26, 226, 830, 14);
		frmTimes.getContentPane().add(label);

		lblOrigem = new JLabel("Origem:");
		lblOrigem.setHorizontalAlignment(SwingConstants.LEFT);
		lblOrigem.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblOrigem.setBounds(264, 193, 50, 18);
		frmTimes.getContentPane().add(lblOrigem);

		textField_1 = new JTextField();
		textField_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		textField_1.setColumns(10);
		textField_1.setBounds(320, 191, 169, 20);
		frmTimes.getContentPane().add(textField_1);

		label_8 = new JLabel("selecione");
		label_8.setBounds(26, 163, 561, 14);
		frmTimes.getContentPane().add(label_8);

		btnListarTimes = new JButton("Listar times");
		btnListarTimes.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnListarTimes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listagem();
			}
		});
		btnListarTimes.setBounds(26, 8, 110, 23);
		frmTimes.getContentPane().add(btnListarTimes);

		lblNome = new JLabel("Nome:");
		lblNome.setHorizontalAlignment(SwingConstants.LEFT);
		lblNome.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNome.setBounds(26, 189, 50, 23);
		frmTimes.getContentPane().add(lblNome);

		textField = new JTextField();
		textField.setFont(new Font("Dialog", Font.PLAIN, 12));
		textField.setColumns(10);
		textField.setBounds(77, 191, 169, 20);
		frmTimes.getContentPane().add(textField);
	}

	//*****************************
	public void listagem () {
		try{
			List<Time> lista = Fachada.listarTimes();

			//model contem todas as linhas e colunas da tabela
			DefaultTableModel model = new DefaultTableModel();
			//colunas
			model.addColumn("nome");
			model.addColumn("origem");
			model.addColumn("jogos");
			
			//linhas
			for(Time time: lista) {
				model.addRow(new Object[] {
						time.getNome()+"", time.getOrigem(), time.getJogos()
				});
			}
			table.setModel(model);
			label_8.setText("resultados: "+lista.size()+ " times - selecione uma linha");
		}
		catch(Exception erro){
			label.setText(erro.getMessage());
		}
	}
}
