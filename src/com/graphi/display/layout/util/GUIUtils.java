package com.graphi.display.layout.util;

import java.awt.Color;
import javax.swing.JComponent;
import net.java.balloontip.BalloonTip;
import net.java.balloontip.styles.RoundedBalloonStyle;
import net.java.balloontip.utils.ToolTipUtils;

public class GUIUtils
{
    public static void showTooltip(JComponent component, String text)
    {
        ToolTipUtils.balloonToToolTip(new BalloonTip(component, text, new RoundedBalloonStyle(3, 3, Color.WHITE, Color.DARK_GRAY), false), 500, 5000);
    }
}
