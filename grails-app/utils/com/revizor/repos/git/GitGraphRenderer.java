package com.revizor.repos.git;


import com.revizor.repos.BranchColor;
import static com.revizor.utils.Constants.ROW_HEIGHT;
import static com.revizor.utils.Constants.SPACE;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revplot.AbstractPlotRenderer;
import org.eclipse.jgit.revplot.PlotLane;

import java.io.Serializable;

/**
 *
 * {@link http://download.eclipse.org/jgit/docs/latest/apidocs/index.html?org/eclipse/jgit/revplot/PlotCommit.html}
 */
public class GitGraphRenderer extends AbstractPlotRenderer<PlotLane, BranchColor> implements Serializable {

    private StringBuffer sb = new StringBuffer();
    private String message;

    public String getSVG() {
        return sb.toString();
    }

    public String getMessage(){
        return this.message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int drawLabel(int i, int i2, Ref ref) {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BranchColor laneColor(PlotLane plotLane) {
        return BranchColor.BLUE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void drawLine(BranchColor branchColor, int x1,
                            int y1,
                            int x2,
                            int y2,
                            int width) {
        if (x1 == x2) {
            // this is a vertical line
            sb.append("<line x1='").append(x1).append("' y1='").append(y1).append("' x2='").append(x2).append("' y2='").append(y2).append("' class='svg-path' />");
        }
        else {
            // this is a curve line
            sb.append("<path d='M").append(x1).append(SPACE).append(y1).append(" C").append(x1).append(SPACE).append(y1 + ROW_HEIGHT).append(SPACE)
                    .append(x2).append(SPACE).append(y2 - ROW_HEIGHT).append(SPACE).append(x2).append(SPACE).append(y2).append("' class='svg-path' />");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void drawCommitDot(int x,
                                 int y,
                                 int w,
                                 int h) {
        sb.append("<circle cx='").append(x).append("' cy='").append(y).append("' r='4' class='svg-circle' />");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void drawBoundaryDot(int x,
                                   int y,
                                   int w,
                                   int h) {
        sb.append("<circle cx='").append(x).append("' cy='").append(y).append("' r='2' class='svg-circle' />");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void drawText(String msg,
                            int x,
                            int y) {
        this.message = msg;
    }
}
