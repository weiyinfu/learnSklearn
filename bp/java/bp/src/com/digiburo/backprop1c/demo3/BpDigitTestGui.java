package com.digiburo.backprop1c.demo3;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;

import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import com.digiburo.backprop1c.network.Pattern;
import com.digiburo.backprop1c.network.PatternList;

/**
 * GUI for BdDemo3.  Divided into four panels.
 * Left to right: selection, edit, result.
 * Bottom: apply button.
 *
 * <P>
 * Selection panel as ten buttons, one for each digit.  This permits
 * selection of any training digit.  When the button is pressed,
 * the original pixels as presented in the edit panel.
 *
 * <P>
 * Edit panel allows a pixel to be toggled as set/cleared.  This
 * allows one to distort the original pattern.
 *
 * <P>
 * Result panel shows the answer from the neural network.  
 *
 * <P>
 * Apply button submits the pattern contained in the edit panel
 * to the neural network and updates the result panel w/the answer.
 *
 * @author G.S. Cole (gsc@digiburo.com)
 * @version $Id: BpDigitTestGui.java,v 1.3 2002/02/03 20:31:41 gsc Exp $
 */

/*
 * Development Environment:
 *   Linux 2.2.12-20 (Red Hat 6.1)
 *   Java Developers Kit 1.2.2-RC2-K
 *
 * Legalise:  
 *   Copyright (C) 2002 Digital Burro, INC.
 *
 * Maintenance History:
 *   $Log: BpDigitTestGui.java,v $
 *   Revision 1.3  2002/02/03 20:31:41  gsc
 *   Format tweaks
 *
 *   Revision 1.2  2002/02/03 18:51:08  gsc
 *   Format tweaks
 *
 *   Revision 1.1  2002/02/03 04:24:05  gsc
 *   Initial Check In
 */

public class BpDigitTestGui extends JFrame {
 
	public static final String TRAIN_FILENAME = "tmp/demo3.trn";    
  public static final String NETWORK_FILENAME = "demo3.serial";

  public static final double ONE = 0.9999999999;
  public static final double ZERO = 0.0000000001;

  private final NnResultPanel nrp;
  private final PatternEditPanel pep;
  private final GridBagConstraints gbc;

	/**
   *
   */
  public BpDigitTestGui(String frame_title, final BpDemo3 bd3, final PatternList training) {
    super(frame_title);

    //
    // Specify layout hints
    //
    gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.BOTH;
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.weightx = 100;
    gbc.weighty = 100;

    getContentPane().setLayout(new GridBagLayout());

    pep = new PatternEditPanel(bd3, training);
    PatternSelectPanel psp = new PatternSelectPanel(pep);

    nrp = new NnResultPanel();
    JButton apply_button = new JButton("Apply Pattern");

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    getContentPane().add(psp, gbc);

    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    getContentPane().add(pep, gbc);

    gbc.gridx = 2;
    gbc.gridy = 0;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    getContentPane().add(nrp, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 3;
    gbc.gridheight = 1;
    gbc.weighty = 20;
    getContentPane().add(apply_button, gbc);

    apply_button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        nrp.setAnswer(bd3.runNetwork(pep.getPattern()));
      }
    });
  }

  /**
   * driver
   */
  public static void main(String args[]) throws Exception {
    System.out.println("begin:" + args.length);

    String trainFileName = TRAIN_FILENAME;
    String networkFileName = NETWORK_FILENAME;

    if (args.length == 2) {
      trainFileName = args[0];
      networkFileName = args[1];
    }

    PatternList training = new PatternList();
    training.reader(new File(trainFileName));

    BpDemo3 bd3 = new BpDemo3(new File(networkFileName));

    try {
      //must include or looks bad on osx
      UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
    } catch (Exception exception) {
      exception.printStackTrace();
    }

    BpDigitTestGui bdtg = new BpDigitTestGui("BP Demo 3", bd3, training);
    bdtg.setBackground(java.awt.Color.white);
    bdtg.setSize(500, 350);
    bdtg.setVisible(true);

    System.out.println("end");
  }

  /**
   * eclipse generated
   */
  private static final long serialVersionUID = -7071344260848058959L;
}

/**
 * Select a digit to display.
 */
class PatternSelectPanel extends JPanel {

  /**
   * @param pep
   */
  public PatternSelectPanel(final PatternEditPanel pep) {
    setLayout(new GridLayout(5, 2));

    JButton selections[] = new JButton[10];
    for (int ii = 0; ii < 10; ii++) {
      selections[ii] = new JButton(Integer.toString(ii));
      add(selections[ii]);

      selections[ii].addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent event) {
          pep.setPattern(Integer.parseInt(event.getActionCommand()));
        }
      });
    }

    BevelBorder bevel_border = new BevelBorder(BevelBorder.RAISED);
    this.setBorder(new TitledBorder(bevel_border, "Select Pattern"));
  }

  /**
   * eclipse generated
   */
  private static final long serialVersionUID = -5698181693665181035L;
}

/**
 * Show the responses from BackProp
 */
class NnResultPanel extends JPanel {

  private JButton[] results;

  /**
   *
   */
  public NnResultPanel() {
    setLayout(new GridLayout(5, 2));

    results = new JButton[10];
    for (int ii = 0; ii < 10; ii++) {
      results[ii] = new JButton(Integer.toString(ii));
      add(results[ii]);
    }

    BevelBorder bevel_border = new BevelBorder(BevelBorder.RAISED);
    this.setBorder(new TitledBorder(bevel_border, "Answer"));

    double temp[] = new double[10];
    setAnswer(temp);
  }

  /**
   *
   */
  public void setAnswer(double[] answers) {
    for (int ii = 0; ii < answers.length; ii++) {
      if (answers[ii] > 0.75) {
        results[ii].setOpaque(true);
//        results[ii].setBorderPainted(false);
        results[ii].setBackground(Color.red);
        results[ii].setForeground(Color.black);
      } else {
        results[ii].setOpaque(true);
//        results[ii].setBorderPainted(false);
        results[ii].setBackground(Color.black);
        results[ii].setForeground(Color.red);
      }
    }
  }

  /**
   * eclipse generated
   */
  private static final long serialVersionUID = -4356291377159597583L;
}

/**
 * Interactive 5x5 edit panel
 */
class PatternEditPanel extends JPanel {
  private PatternList training;

  private JButton[] pixels;
  private double[] pattern;

  /**
   *
   */
  public PatternEditPanel(BpDemo3 bd3, PatternList training) {
    this.training = training;

    pattern = new double[25];

    setLayout(new GridLayout(5, 5));

    pixels = new JButton[25];
    for (int ii = 0; ii < 25; ii++) {
      pattern[ii] = 0.0;
      pixels[ii] = new JButton(Integer.toString(ii));
      pixels[ii].setBackground(java.awt.Color.black);
      pixels[ii].setOpaque(true);
      add(pixels[ii]);

      pixels[ii].addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent event) {
          int ndx = Integer.parseInt(event.getActionCommand());

          if (pattern[ndx] < 0.5) {
            pattern[ndx] = BpDigitTestGui.ONE;
            pixels[ndx].setBackground(java.awt.Color.red);
            pixels[ndx].setOpaque(true);
          } else {
            pattern[ndx] = BpDigitTestGui.ZERO;
            pixels[ndx].setBackground(java.awt.Color.black);
            pixels[ndx].setOpaque(true);
          }
        }
      });
    }

    BevelBorder bevelBorder = new BevelBorder(BevelBorder.RAISED);
    this.setBorder(new TitledBorder(bevelBorder, "Pattern Edit"));
  }

  /**
   * Show the pattern used for training
   *
   * @param ndx index into PatternList
   */
  public void setPattern(int ndx) {
    // System.out.println("set pattern " + ndx);

    Pattern pp = training.get(ndx);

    double[] original = pp.getInput();

    for (int ii = 0; ii < original.length; ii++) {
      pattern[ii] = original[ii];

      // System.out.println(ii + " " + original[ii]);

      if (original[ii] < 0.5) {
        pixels[ii].setBackground(java.awt.Color.black);
        pixels[ii].setOpaque(true);
      } else {
        pixels[ii].setBackground(java.awt.Color.red);
        pixels[ii].setOpaque(true);
      }
    }
  }

  /**
   *
   */
  public double[] getPattern() {
    return (pattern);
  }

  /**
   * eclipse generated
   */
  private static final long serialVersionUID = 7143340791046661355L;
}
