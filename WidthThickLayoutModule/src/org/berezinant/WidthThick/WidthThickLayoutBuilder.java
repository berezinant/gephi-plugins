/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.berezinant.WidthThick;
import javax.swing.Icon;
import javax.swing.JPanel;
import org.openide.util.lookup.ServiceProvider;
import org.gephi.layout.spi.*;
import org.openide.util.NbBundle;

/**
 *
 * @author berezinant
 */
@ServiceProvider(service = LayoutBuilder.class)
public class WidthThickLayoutBuilder implements LayoutBuilder {  

    private WidthThickLayoutUI ui = new WidthThickLayoutUI();
    
    @Override
    public String getName() {
        return NbBundle.getMessage(WidthThickLayoutBuilder.class, "WidthThickLayoutBuilder.name");
    }

    @Override
    public LayoutUI getUI() {
        return ui;
    }

    @Override
    public Layout buildLayout() {
        return new WidthThickLayout(this, 2000, 2000);
    }
    
    private static class WidthThickLayoutUI implements LayoutUI {

        public String getDescription() {
            return NbBundle.getMessage(WidthThickLayoutBuilder.class, "WidthThickLayout.description");
        }

        public Icon getIcon() {
            return null;
        }

        public JPanel getSimplePanel(Layout layout) {
            return null;
        }

        public int getQualityRank() {
            return -1;
        }

        public int getSpeedRank() {
            return -1;
        }
    }
    
}
