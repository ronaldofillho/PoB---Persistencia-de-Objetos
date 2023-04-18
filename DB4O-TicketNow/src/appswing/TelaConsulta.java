package appswing;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import regras_negocio.Fachada;

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
		labelMessage.setBounds(21, 273, 431, 14);
		frame.getContentPane().add(labelMessage);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Quais as datas que um time X joga", "Quantos ingressos INDIVIDUAIS um time X vendeu", "Quantos ingressos em GRUPO um time X vende", "Qual o estoque de ingressos disponíveis para os jogos de um time X", "Quais os códigos de ingressos de todos os jogos de um time X"}));
		comboBox.setBounds(21, 11, 539, 22);
		frame.getContentPane().add(comboBox);
	}
}
