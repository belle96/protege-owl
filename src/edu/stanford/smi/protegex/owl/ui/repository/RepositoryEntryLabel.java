package edu.stanford.smi.protegex.owl.ui.repository;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.repository.util.RepositoryUtil;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 19, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class RepositoryEntryLabel extends JLabel {

    private URI name;

    private Action copyToClipboardAction;

    private Action downloadToLocalFileAction;

    private OWLModel model;


    public RepositoryEntryLabel(OWLModel model, URI name, String locationDescription, boolean imported) {
        this.name = name;
        this.model = model;
        setIconTextGap(20);
        String colour = "";
        if (imported) {
            colour = "rgb(90, 50, 180)";
        }
        else {
            colour = "rgb(20, 20, 20)";
        }
        String html = "<html><body>&nbsp;&nbsp;<font color=\"" + colour + "\">" + name + "</font> <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=\"rgb(100, 100, 100)\" size=\"-2\">(" + locationDescription + ")</font></body></html>";
        setText(html);
        setBackground(Color.WHITE);
        setOpaque(true);
        copyToClipboardAction = new AbstractAction("Copy to clipboard") {
            public void actionPerformed(ActionEvent e) {
                StringSelection sel = new StringSelection(RepositoryEntryLabel.this.name.toString());
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel, null);
            }
        };
        downloadToLocalFileAction = new AbstractAction("Copy to local file...") {
            public void actionPerformed(ActionEvent e) {
                copyToLocalFile();
            }
        };
        addMouseListener(new MouseAdapter() {

            private void showPopupMenu(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    JPopupMenu menu = new JPopupMenu();
                    menu.add(copyToClipboardAction);
                    menu.add(downloadToLocalFileAction);
                    menu.show(RepositoryEntryLabel.this, e.getX(), e.getY());
                }
            }


            public void mouseReleased(MouseEvent e) {
                showPopupMenu(e);
            }


            public void mouseClicked(MouseEvent e) {
                showPopupMenu(e);
            }
        });
    }


    private void copyToLocalFile() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            try {
                RepositoryUtil.createImportLocalCopy(model, name, f);
            }
            catch (OntologyLoadException e) {
                ProtegeUI.getModalDialogFactory().showErrorMessageDialog(model, e.getMessage());
            }
        }
    }


}

