package br.pucrio.poo.views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import br.pucrio.poo.controllers.DicesController;
import br.pucrio.poo.models.domain.Player;

public class DicesPanel extends JPanel {
	private JButton button;
	
	private DicesImagesPanel imagesPanel;
	private JLabel diceOne;
	private JLabel diceTwo;
	private ActionListener buttonListener;

	public DicesPanel() {
		this.imagesPanel = new DicesImagesPanel();
		
		this.button = new JButton("Lan�ar Dado");
		this.diceOne = new JLabel("-");
		this.diceTwo = new JLabel("-");

		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		

		// panel do bot�o
		JPanel buttonPanel = new JPanel();
		BoxLayout layout = new BoxLayout(buttonPanel, BoxLayout.Y_AXIS);
		buttonPanel.setLayout(layout);
		buttonPanel.add(new JLabel("� JOGAR"));
		buttonPanel.add(button);

		// panel dos resultados
//		JPanel resultPanel = new JPanel();
//		layout = new BoxLayout(resultPanel, BoxLayout.Y_AXIS);
//		resultPanel.setLayout(layout);
//		resultPanel.add(new JLabel("Dices:"));
//		resultPanel.add(diceOne);
//		resultPanel.add(diceTwo);
//		this.add(resultPanel);
		
		this.add(buttonPanel,BorderLayout.NORTH);
		this.add(this.imagesPanel,BorderLayout.SOUTH);		

		//this.disablePanel();
	}
	
	public void repaint(Image dice1Image, Image dice2Image) {
		this.imagesPanel.setDice1Image(dice1Image);
		this.imagesPanel.setDice2Image(dice2Image);
		this.imagesPanel.setBounds(0, 0, dice1Image.getWidth(null), dice1Image.getHeight(null));
		this.repaint();
	}

	public void setDiceOneNumber(String diceNumber) {
		diceOne.setText(diceNumber);
	}

	public void setDiceTwoNumber(String diceNumber) {
		diceTwo.setText(diceNumber);
	}

	public void enableTo(final Player player, final DicesController dicesController) {
		if (this.buttonListener != null) {
			this.button.removeActionListener(buttonListener);
		}

		this.buttonListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				dicesController.roll(player);
			}
		};
		this.button.addActionListener(buttonListener);
		this.enablePanel();
	}

	public void disablePanel() {
		this.button.setEnabled(false);
	}

	private void enablePanel() {
		this.button.setEnabled(true);
	}

}