package com.revizor.repos.git;


import com.revizor.repos.BranchColor;
import com.revizor.repos.Commit;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revplot.AbstractPlotRenderer;
import org.eclipse.jgit.revplot.PlotLane;

import java.io.Serializable;

import static com.revizor.utils.Constants.ROW_HEIGHT;
import static com.revizor.utils.Constants.SPACE;

/**
 *
 * For more information please refer to the javaDocs:
 * <a href="http://download.eclipse.org/jgit/docs/latest/apidocs/org/eclipse/jgit/revplot/AbstractPlotRenderer.html">Link</a>
 *
 */
public class GitGraphRenderer extends AbstractPlotRenderer<PlotLane, BranchColor> implements Serializable {

    private StringBuffer sb = new StringBuffer();
    private int topPadding = 0;
    private Commit commit = new Commit();
    public static final int LANE_WIDTH = 14; // TODO: re-think this: AbstractPlotRenderer.LANE_WIDTH is private

    public Commit getRenderedCommit(){
        this.commit.setSvg(sb.toString());
        this.sb = new StringBuffer();
        return this.commit;
    }

    public void reset(int rowNumber) {
        this.topPadding = rowNumber * ROW_HEIGHT;
        this.commit = new Commit();
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

        if (Math.abs(x1 - x2) < LANE_WIDTH && y1 != y2 && x1 != x2) {
            // it is a corner
            sb.append("<path d='M ").append(x1).append(SPACE).append(y1 + topPadding)
                    .append(" C ").append(x2).append(SPACE).append(y1 + topPadding).append(SPACE)
                    .append(x2).append(SPACE).append(y1 + topPadding).append(SPACE)
                    .append(x2).append(SPACE).append(y2 + topPadding).append("' class='svg-path' />");
        }
        else if (x1 == x2 || y1 == y2) {
            // this is a straight line
            sb.append("<line x1='")
                    .append(x1).append("' y1='").append(y1 + topPadding).append("' x2='").append(x2).append("' y2='").append(y2 + topPadding)
                    .append("' class='svg-path' />");
        }
        else {

            // this is a curve line (like merging)
            sb.append("<path d='M ").append(x1).append(SPACE).append(y1 + topPadding)
                    .append(" C ").append(x1).append(SPACE).append(y2 + topPadding).append(SPACE)
                    .append(x2).append(SPACE).append(y1 + topPadding).append(SPACE)
                    .append(x2).append(SPACE).append(y2 + topPadding).append("' class='svg-path' />");
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
        sb.append("<circle cx='").append(x + 5).append("' cy='").append(y + topPadding + 2).append("' r='3' class='svg-circle' />");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void drawBoundaryDot(int x,
                                   int y,
                                   int w,
                                   int h) {
        sb.append("<circle cx='").append(x).append("' cy='").append(y + topPadding).append("' r='2' class='svg-circle' />");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void drawText(String msg,
                            int x,
                            int y) {
        this.commit.setMessage(msg);
        this.commit.setPadding(x);
    }
}
