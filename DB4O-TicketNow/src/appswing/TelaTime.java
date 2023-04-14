package appswing;

import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import modelo.Time;
import regras_negocio.Fachada;
import java.awt.BorderLayout;

public class TelaTime {
	private JFrame frame;
	private JTable table;
	private JScrollPane scrollPane;
	private JButton button;
	private JTable table_1;
	
	public TelaTime() {
		initialize();
		frame.setVisible(true);
	}
	
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		table_1 = new JTable();
		frame.getContentPane().add(table_1, BorderLayout.CENTER);
		frame.addWindowListener(new WindowAdapter() {
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
	}
	
	/* --------------------------------------------------------------------- */
	
	public void listagem() {
		try {
			List<Time> lista = Fachada.listarTimes();
			
			//model contem todas as linhas e colunas da tabela
			DefaultTableModel model = new DefaultTableModel();
			
			// colunas 
			model.addColumn("nome");
			model.addColumn("origem");
			
			// linhas
			for(Time time : lista) {
				model.addRow(new Object[] {
						time.getNome() + "", time.getOrigem()
				});
			}
			
			table.setModel(model);
		}
		catch(Exception erro){
		}
	}
}
