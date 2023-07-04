package appswing;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import modelo.Ingresso;
import modelo.IngressoGrupo;
import modelo.IngressoIndividual;
import modelo.Jogo;
import modelo.Time;
import regras_negocio.Fachada;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class TelaConsulta {
	private JFrame frame;
	private JTable table;
	private JScrollPane scrollPane;
	private JLabel labelMessage; 
	
	public TelaConsulta() {
		initialize();
		frame.setVisible(true);
	}
	
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setTitle("Tela Consultas");
		frame.setBounds(100, 100, 729, 385);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				Fachada.inicializar();
			}
			@Override
			public void windowClosing(WindowEvent e) {
				Fachada.finalizar();
			}
		});
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(21, 43, 674, 219);
		
		frame.getContentPane().add(scrollPane);
		
		table = new JTable();
		table.setGridColor(Color.BLACK);
		table.setRequestFocusEnabled(false);
		table.setFocusable(false);
		table.setBackground(Color.WHITE);
		table.setFillsViewportHeight(true);
		table.setRowSelectionAllowed(true);
		table.setFont(new Font("Tahoma", Font.PLAIN, 14));
		scrollPane.setViewportView(table);
		table.setBorder(new LineBorder(new Color(0, 0, 0)));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setShowGrid(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		
		labelMessage = new JLabel("Selecione");
		labelMessage.setBounds(21, 273, 674, 14);
		frame.getContentPane().add(labelMessage);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Verificar os Jogos de um Time", "Verificar quantidade de ingressos totais disponíveis de um time", "Verificar Jogos de um time específico ", "Verificar times que jogaram em um local", "QVerificar quantidade de ingressos individuais vendidos de um time "}));
		comboBox.setBounds(21, 11, 506, 22);
		frame.getContentPane().add(comboBox);
		
		JButton btnConsultar = new JButton("Consultar");
		btnConsultar.setBounds(539, 11, 156, 23);
		frame.getContentPane().add(btnConsultar);
		btnConsultar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = comboBox.getSelectedIndex();
					switch (index) {
						case 0: //Verificar times que jogaram em um local
							try {
								String nomeLocal = JOptionPane.showInputDialog("Digite o Nome do Local:");
								List<Time> times = Fachada.listarTimesQueJogaramNoLocal(nomeLocal);

								DefaultTableModel model = new DefaultTableModel();

								model.addColumn("Time");
								model.addColumn("Origem");

								for (Time time: times) {
									model.addRow(new Object[] {time.getNome(), time.getOrigem()});
								}
								table.setModel(model);

							}catch(Exception ex) {
								labelMessage.setText(ex.getMessage());
							}
							break;
					}
				}
				catch(Exception ex) {
					labelMessage.setText(ex.getMessage());
				}
			}
		});
	}
}
