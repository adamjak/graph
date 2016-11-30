package net.adamjak.thomas.graph.application.gui;

import javax.swing.KeyStroke;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Created by Tomas Adamjak on 23.11.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class GuiAccelerators
{
	public final static KeyStroke ALT_A = KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK);
	public final static KeyStroke ALT_C = KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK);
	public final static KeyStroke ALT_D = KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.ALT_MASK);
	public final static KeyStroke ALT_S = KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK);

	public final static KeyStroke CTRL_A = KeyStroke.getKeyStroke(KeyEvent.VK_A, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
	public final static KeyStroke CTRL_I = KeyStroke.getKeyStroke(KeyEvent.VK_I, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
	public final static KeyStroke CTRL_O = KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
	public final static KeyStroke CTRL_Q = KeyStroke.getKeyStroke(KeyEvent.VK_Q, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
	public final static KeyStroke CTRL_S = KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());

	public final static KeyStroke CTRL_SHIFT_A = KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK + ActionEvent.SHIFT_MASK);
	public final static KeyStroke CTRL_SHIFT_S = KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK + ActionEvent.SHIFT_MASK);


}
