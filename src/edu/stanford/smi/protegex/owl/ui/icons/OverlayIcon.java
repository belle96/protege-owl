package edu.stanford.smi.protegex.owl.ui.icons;

import javax.swing.*;
import java.awt.*;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class OverlayIcon implements Icon {

    private Image baseImage;

    private int baseX;

    private int baseY;

    private int height;

    private Image topImage;

    private int topX;

    private int topY;

    private int width;


    public OverlayIcon(String baseIconName, int baseX, int baseY,
                       String topIconName, int topX, int topY) {
        this(baseIconName, baseX, baseY, topIconName, topX, topY, OWLIcons.class);
    }


    public OverlayIcon(String baseIconName, int baseX, int baseY,
                       String topIconName, int topX, int topY, Class clazz) {
        this(Toolkit.getDefaultToolkit().
                getImage(OWLIcons.getImageURL(clazz, baseIconName)),
                baseX, baseY,
                Toolkit.getDefaultToolkit().
                        getImage(OWLIcons.getImageURL(OWLIcons.class, topIconName)),
                topX, topY);
    }


    public OverlayIcon(Image baseImage, int baseX, int baseY,
                       Image topImage, int topX, int topY) {
        this(baseImage, baseX, baseY, topImage, topX, topY, 15, 15);
    }


    public OverlayIcon(Image baseImage, int baseX, int baseY,
                       Image topImage, int topX, int topY, int width, int height) {
        this.baseImage = baseImage;
        this.baseX = baseX;
        this.baseY = baseY;
        this.topImage = topImage;
        this.topX = topX;
        this.topY = topY;
        this.width = width;
        this.height = height;
    }


    public int getIconHeight() {
        return height;
    }


    public int getIconWidth() {
        return width;
    }


    public Icon getGrayedIcon() {
        Image grayBaseImage = GrayFilter.createDisabledImage(baseImage);
        Image grayTopImage = GrayFilter.createDisabledImage(topImage);
        return new OverlayIcon(grayBaseImage, baseX, baseY, grayTopImage, topX, topY);
    }


    public void paintIcon(Component c, Graphics g, int x, int y) {
        if (y > 0 && (baseX != 0 || baseY != 0 || topX != 0 || topY != 0)) {
            x = 0;
            y = 0;
        }
        new ImageIcon(baseImage).paintIcon(c, g, baseX + x, baseY + y);
        new ImageIcon(topImage).paintIcon(c, g, topX + x, topY + y);
    }
}
