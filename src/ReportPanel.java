
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Robot;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellRenderer;
import jxl.CellFormat;
import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import net.miginfocom.swing.MigLayout;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author bala
 */
public final class ReportPanel extends JPanel {

    private ReadingEntryPanel entryPanel;
    int reportTableHeight;
    double[] multFactors;

    public ReportPanel(ReadingEntryPanel entryPanel) {
        this.entryPanel = entryPanel;
        go();
    }

    void go() {
        double[][] given = parseEntryTable();
        multFactors = calculateMultFactors();
        double[][] data = this.findReportValues(given);
        setLayout(new MigLayout("",
                "[grow][grow][grow][grow]", "[][]20[]20[]20[]20[]20[]20[]50[]"));
        this.addDeclaredValues();
        this.getAndAddTable(data);
        this.addSignature();
    }

    public ReportPanel(ReadingEntryPanel entryPanel, boolean everythingInAPage) {
        this.entryPanel = entryPanel;
        double[][] given = parseEntryTable();
        multFactors = calculateMultFactors();
        double[][] data = this.findReportValues(given);
        setLayout(new MigLayout("insets 0 0 0 0",
                "[grow][grow][grow][grow]", "[][][][][][][][grow][bottom]20[bottom]"));
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        this.addDeclaredValues();
        this.getAndAddTable(data);
        GraphPanel gp = new GraphPanel(this);
        gp.getGraph().getPlot().setGridLinesType(GridLinesType.SQUARE_GRID_SPACING);
        gp.getGraph().getPlot().domainAxis.setAxisLineColor(Color.black);
        List l = gp.getGraph().getPlot().getRangeAxesList();
        for (Object r : l) {
            RangeAxis axis = (RangeAxis) r;
            axis.setAxisLinePaint(Color.black);
        }
        this.add(gp, "grow,span,wrap");//,gapright 10px");//,height 400:420:440
        this.addSignature();
    }

    public int findRowCount() {

        JTable entryTable = getEntryPanel().getTable();
        int i = 0;
        for (i = 0; i < 8; i++) {
            String str = null;
            try {
                str = (entryTable.getValueAt(i, 1)).toString();
                if (str.trim().length() == 0) {
                    break;
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                break;
            }

        }  //System.out.println("Row count is " + i);
        return i;

    }

    public double[][] parseEntryTable() {
        int rows = findRowCount();
        int cols = 7;
        JTable entryTable = entryPanel.getTable();
        double[][] given = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                given[i][j] = Double.parseDouble((entryTable.getValueAt(i, j)).toString());
            }
        }
        return given;
    }

    public double[] calculateMultFactors() {
        double[] multFactors = new double[7];
        JTable entryTable = entryPanel.getTable();
        multFactors[0] = 0.0;
        for (int i = 1; i < 7; i++) {
            multFactors[i] = Double.parseDouble((entryTable.getValueAt(8, i)).toString());
        }
        return multFactors;
    }

    public double[][] findReportValues(double[][] given) {
        // try {
        int rows = findRowCount();

        int cols = 15;
        double diaDel = Double.parseDouble(entryPanel.getDelSizeField().getText());
        double pipeConstant = 4000.0 * 4000.0 / (Math.PI * Math.PI * diaDel * diaDel * diaDel * diaDel * 2.0 * 9.81);
        double data[][] = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            double rater = 50.0 / given[i][EntryTableConstants.FREQ_COL_INDEX];
            data[i][ReportTableConstants.SLNO_COL_INDEX] = i + 1;
            data[i][ReportTableConstants.FREQ_COL_INDEX] = given[i][EntryTableConstants.FREQ_COL_INDEX]
                    * multFactors[EntryTableConstants.FREQ_COL_INDEX];
            data[i][ReportTableConstants.DGR_COL_INDEX] = given[i][EntryTableConstants.DGR_COL_INDEX]
                    * multFactors[EntryTableConstants.DGR_COL_INDEX];
            double disch = given[i][EntryTableConstants.DISCH_COL_INDEX] * multFactors[EntryTableConstants.DISCH_COL_INDEX];
            data[i][ReportTableConstants.VHC_COL_INDEX] = disch * disch * pipeConstant;
            data[i][ReportTableConstants.TH_COL_INDEX] = given[i][ReportTableConstants.DGR_COL_INDEX]
                    + Double.parseDouble((getEntryPanel().getGaugDistField()).getText())
                    + data[i][ReportTableConstants.VHC_COL_INDEX];
            data[i][ReportTableConstants.DISCH_COL_INDEX] = given[i][EntryTableConstants.DISCH_COL_INDEX]
                    * multFactors[EntryTableConstants.DISCH_COL_INDEX];
            data[i][ReportTableConstants.VOL_COL_INDEX] = given[i][EntryTableConstants.VOL_COL_INDEX]
                    * multFactors[EntryTableConstants.VOL_COL_INDEX];

            data[i][ReportTableConstants.CURR_COL_INDEX] = given[i][EntryTableConstants.CURR_COL_INDEX]
                    * multFactors[EntryTableConstants.CURR_COL_INDEX];
            data[i][ReportTableConstants.MINPUT_COL_INDEX] = given[i][EntryTableConstants.POWER_COL_INDEX]
                    * multFactors[EntryTableConstants.POWER_COL_INDEX];
            data[i][ReportTableConstants.RDISCH_COL_INDEX] = rater * data[i][ReportTableConstants.DISCH_COL_INDEX];
            data[i][ReportTableConstants.RHEAD_COL_INDEX] = rater * rater * data[i][ReportTableConstants.TH_COL_INDEX];
            data[i][ReportTableConstants.RINPUT_COL_INDEX] = rater * rater * rater * data[i][ReportTableConstants.MINPUT_COL_INDEX];
            data[i][ReportTableConstants.POP_COL_INDEX] = data[i][ReportTableConstants.RDISCH_COL_INDEX]
                    * data[i][ReportTableConstants.RHEAD_COL_INDEX] / 102.00;
            data[i][ReportTableConstants.EFF_COL_INDEX] = data[i][ReportTableConstants.POP_COL_INDEX] / data[i][ReportTableConstants.RINPUT_COL_INDEX] * 100.00;

        }
        return data;
    }

    public void addTitle() {
        JPanel titlePanel = new JPanel() {

            public void paintComponent(Graphics g) {
                Graphics2D g2D = (Graphics2D) g;
                g2D.setFont(new Font("SansSerif", Font.PLAIN, 18));
                g2D.drawString("Annai Engineering Company,Coimbatore", 2, 25);
                g2D.setFont(new Font("SansSerif", Font.BOLD, 12));
                g2D.drawString("Test Report of Openwell Submersible Pumpset, IS 14220", 2, 45);
            }
        };
        add(titlePanel, "grow,wrap,span");
    }

    public void getAndAddTable(double[][] data) {

        Object[] columnNames = {"Sl.No", "Freq", "<HTML>Del.Gauge<BR>Reading", "VHC", "<HTML>Total<BR>Head</HTML>",
            "Disch", "Voltage", "Current", "<HTML>Motor<BR>Input",
            "<HTML>Rated<BR>Disch<HTML>", "<HTML>Rated<BR>Head</HTML>", "<HTML>Rated<BR>Input</HTML>", "<HTML>Rated<BR>Output</HTML>",
            "<HTML>Overall<BR>Eff.</HTML>"};
        Object[][] dataObj = new Object[this.findRowCount() + 1][15];
        dataObj[0] = new Object[]{"", "Hz", "m", "m", "m", "lps", "V", "A", "kW", "lps", "m", "kW", "kW", "%"};
        for (int i = 1; i < findRowCount() + 1; i++) {
            for (int j = 0; j < 14; j++) {

                dataObj[i][j] = String.format("%,.2f", (float) (data[i - 1][j]));
                if (j == 0) {
                    dataObj[i][j] = String.format("%,.0f", (float) (data[i - 1][j]));
                }
            }
        }
        JTable reportTable = new JTable(dataObj, columnNames);
        TableCellRenderer rendererFromHeader = reportTable.getTableHeader().getDefaultRenderer();

        JLabel headerLabel = (JLabel) rendererFromHeader;
        headerLabel.setHorizontalAlignment(JLabel.LEFT); // Here you can set the alignment you want
        for (int col = 0; col < reportTable.getColumnCount(); col++) {
            reportTable.getColumnModel().getColumn(col)
                    .setHeaderRenderer(new MyHeaderRenderer(Color.black));
        }
        reportTable.setBorder(BorderFactory.createLineBorder(Color.black));

        reportTable.setBackground(Color.white);
        reportTable.setRowHeight(17);
        reportTable.setRowMargin(3);
        reportTable.setGridColor(Color.black);

        //reportTable.getTableHeader().setPreferredSize(new Dimension(reportTable.getColumnModel().getTotalColumnWidth(), 40));
        reportTable.getTableHeader().setPreferredSize(new Dimension(getWidth(), 40));
        reportTable.getTableHeader().setFont(new Font("SansSerif", Font.PLAIN, 8));
        reportTable.getTableHeader().setBackground(Color.white);
        FontMetrics fm = reportTable.getFontMetrics(new Font("SansSerif", Font.PLAIN, 8));
        reportTable.setFont(new Font("SansSerif", Font.PLAIN, 8));
        reportTable.getColumnModel().getColumn(ReportTableConstants.DGR_COL_INDEX).setMinWidth(fm.stringWidth(("Del. Gauge")));
        reportTable.setGridColor(Color.black);
        JScrollPane tScroller = new JScrollPane(reportTable);
        reportTable.setFillsViewportHeight(true);
        reportTable.setPreferredScrollableViewportSize(new Dimension(getWidth(),//reportTable.getHeight()+
                reportTable.getRowCount() * (reportTable.getRowHeight())));
        add(tScroller, "grow,span,wrap");//,gapright 10px");
    }

    public void generateExcelReport(GraphPanel gp) {
        try {
            WritableFont times = new WritableFont(WritableFont.TIMES, 10, WritableFont.NO_BOLD);
            WritableCellFormat wcf = new WritableCellFormat(times);

            File in = new File("resources\\xlReport.xls");
            File out = new File("resources\\xlReportFilled.xls");
            Workbook wb = Workbook.getWorkbook(in);
            WritableWorkbook copy = Workbook.createWorkbook(out, wb);
            WritableSheet sheet = copy.getSheet(0);
            Label lbl = new Label(4, 3, ": " + entryPanel.getPumpTypeField().getText());
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);
            lbl = new Label(4, 4, ": " + entryPanel.getSlNoField().getText());
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);
            lbl = new Label(4, 5, ": " + entryPanel.getDelSizeField().getText());
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);
            lbl = new Label(4, 6, ": " + entryPanel.getHeadField().getText());
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);
            lbl = new Label(4, 7, ": " + entryPanel.getDischField().getText());
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);

            lbl = new Label(9, 3, ": " + entryPanel.gethRangeLwrField().getText()
                    + "/" + entryPanel.gethRangeUprField().getText());
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);

            lbl = new Label(9, 4, ": " + entryPanel.getEffField().getText());
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);
            lbl = new Label(9, 5, ": " + entryPanel.getRatingField().getText());
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);
            lbl = new Label(9, 6, ": " + entryPanel.getVoltField().getText());
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);
            lbl = new Label(9, 7, ": " + entryPanel.getCurrField().getText());
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);
            lbl = new Label(14, 6, ": " + entryPanel.getGaugDistField().getText());
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);

            double[][] given = parseEntryTable();
            double[][] data = this.findReportValues(given);

            for (int i = 0; i < findRowCount(); i++) {
                int colCount = 14;
                for (int j = 0; j < colCount; j++) {
                    int iNew = i + 16;
                    int jNew = j + 1;
                    //Label lbl;

                    if (j == 0) {
                        lbl = new Label(jNew, iNew, String.format("%,.0f", (float) (data[i][j])));

                        wcf = new WritableCellFormat(times);//lbl.getCellFormat());
                        wcf.setBorder(Border.LEFT, BorderLineStyle.THIN);
                        wcf.setBorder(Border.RIGHT, BorderLineStyle.THIN);

                    } else if (j == 9) {
                        lbl = new Label(jNew, iNew, String.format("%,.2f", (float) (data[i][11])));

                        wcf = new WritableCellFormat(times);
                        wcf.setBorder(Border.RIGHT, BorderLineStyle.THIN);
                    } else if (j == 11) {
                        lbl = new Label(jNew, iNew, String.format("%,.2f", (float) (data[i][9])));
                        wcf = new WritableCellFormat(times);//wcf = new WritableCellFormat(lbl.getCellFormat());
                        wcf.setBorder(Border.RIGHT, BorderLineStyle.THIN);
                    } else {
                        lbl = new Label(jNew, iNew, String.format("%,.2f", (float) (data[i][j])));
                        wcf = new WritableCellFormat(times);//wcf = new WritableCellFormat(lbl.getCellFormat());
                        wcf.setBorder(Border.RIGHT, BorderLineStyle.THIN);
                    }
                    //top horizontal line of table
                    if (i == 0) {
                        wcf.setBorder(Border.TOP, BorderLineStyle.THIN);
                    }

                    //bottom horizontal line of table
                    if (i == (this.findRowCount() - 1)) {
                        wcf.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
                    }
                    lbl.setCellFormat(wcf);
                    sheet.addCell(lbl);
                }
            }
            //final GraphPanel
            gp = new GraphPanel(new ReportPanel(entryPanel));

            ArrayList<Renderer> rendererList = gp.getGraph().getPlot().getRendererList();
            for (Renderer renderer : rendererList) {
                renderer.setCurvePaint(Color.BLACK);
                renderer.setStroke(new BasicStroke(1.5f));
            }
            ArrayList<RangeAxis> rangeAxisList = gp.getGraph().getPlot().getRangeAxesList();
            Font old = null;
            for (RangeAxis rAxis : rangeAxisList) {
                rAxis.setAxisLinePaint(Color.BLACK);
                old = rAxis.getFont();
                rAxis.setFont(new Font(old.getFontName(), Font.BOLD, 12));
            }

            Plot p = gp.getGraph().getPlot();
            p.sethExrColor(Color.BLACK);
            Stroke s = new BasicStroke(1.0f);
            p.sethExrStroke(s);
            p.sethExrColor(Color.BLACK);
            p.seteExrStroke(s);
            p.seteExrColor(Color.BLACK);
            p.setcExrStroke(s);
            p.setcExrColor(Color.BLACK);

            gp.getGraph().getPlot().getDomainAxis().setAxisLineColor(Color.black);
            // Font old = gp.getGraph().getPlot().getDomainAxis().getFont();

            gp.getGraph().getPlot().getDomainAxis().setFont(new Font(old.getFontName(), Font.BOLD, 12));
            //gp.getGraph().getPlot().

            BufferedImage image = new BufferedImage(
                    1024 - 8, 768 - 120, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2D = (Graphics2D) image.getGraphics();
            g2D.scale(1.0, 1);

            gp.paint(g2D);
            File file = null;
            try {
                file = new File("GraphOut.png");
                ImageIO.write(image, "png", file);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            WritableImage wImage = new WritableImage(1, 25, 14, 19, file);
            sheet.addImage(wImage);
            PumpValues obs = gp.getGraph().getPlot().getObsValues();
            PumpValues decl = gp.declaredValues;
            lbl = new Label(1, 46, "Type : " + entryPanel.getPumpTypeField().getText());
            wcf = new WritableCellFormat(times);// new WritableCellFormat(lbl.getCellFormat());
            wcf.setBorder(Border.LEFT, BorderLineStyle.THIN);
            wcf.setBorder(Border.RIGHT, BorderLineStyle.THIN);
            // wcf.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);
            lbl = new Label(7, 46, entryPanel.getSlNoField().getText());
            wcf = new WritableCellFormat(times);//wcf = new WritableCellFormat(lbl.getCellFormat());
            wcf.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
            wcf.setBorder(Border.RIGHT, BorderLineStyle.THIN);
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);
            lbl = new Label(1, 47, "Head Range , mWC : " + entryPanel.gethRangeLwrField().getText() + " / " + entryPanel.gethRangeUprField().getText());
            wcf = new WritableCellFormat(times);//wcf = new WritableCellFormat(lbl.getCellFormat());
            wcf.setBorder(Border.LEFT, BorderLineStyle.THIN);
            wcf.setBorder(Border.RIGHT, BorderLineStyle.THIN);
            // wcf.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            String dateString = formatter.format(entryPanel.getDateChooser().getDate());
            lbl = new Label(1, 48, "Date : " + dateString);
            wcf = new WritableCellFormat(times);//wcf = new WritableCellFormat(lbl.getCellFormat());
            wcf.setBorder(Border.LEFT, BorderLineStyle.THIN);
            wcf.setBorder(Border.RIGHT, BorderLineStyle.THIN);
            wcf.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);

            lbl = new Label(10, 46, String.format("%.2f", decl.getDischarge()));
            wcf = new WritableCellFormat(times);//wcf = new WritableCellFormat(lbl.getCellFormat());
            wcf.setBorder(Border.LEFT, BorderLineStyle.THIN);
            wcf.setBorder(Border.RIGHT, BorderLineStyle.THIN);
            lbl.setCellFormat(wcf);

            sheet.addCell(lbl);
            lbl = new Label(11, 46, String.format("%.2f", decl.getHead()));
            wcf = new WritableCellFormat(times);//wcf = new WritableCellFormat(lbl.getCellFormat());
            wcf.setBorder(Border.LEFT, BorderLineStyle.THIN);
            wcf.setBorder(Border.RIGHT, BorderLineStyle.THIN);
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);
            lbl = new Label(13, 46, String.format("%.2f", decl.getEfficiency()));
            wcf = new WritableCellFormat(times);//wcf = new WritableCellFormat(lbl.getCellFormat());
            wcf.setBorder(Border.LEFT, BorderLineStyle.THIN);
            wcf.setBorder(Border.RIGHT, BorderLineStyle.THIN);
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);
            lbl = new Label(14, 46, String.format("%.2f", decl.getMaxCurrent()));
            wcf = new WritableCellFormat(times);//wcf = new WritableCellFormat(lbl.getCellFormat());
            wcf.setBorder(Border.LEFT, BorderLineStyle.THIN);
            wcf.setBorder(Border.RIGHT, BorderLineStyle.THIN);
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);

            lbl = new Label(10, 47, String.format("%.2f", obs.getDischarge()));
            wcf = new WritableCellFormat(times);//wcf = new WritableCellFormat(lbl.getCellFormat());
            wcf.setBorder(Border.LEFT, BorderLineStyle.THIN);
            wcf.setBorder(Border.RIGHT, BorderLineStyle.THIN);
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);
            lbl = new Label(11, 47, String.format("%.2f", obs.getHead()));
            wcf = new WritableCellFormat(times);//wcf = new WritableCellFormat(lbl.getCellFormat());
            wcf.setBorder(Border.LEFT, BorderLineStyle.THIN);
            wcf.setBorder(Border.RIGHT, BorderLineStyle.THIN);
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);
            lbl = new Label(13, 47, String.format("%.2f", obs.getEfficiency()));
            wcf = new WritableCellFormat(times);//wcf = new WritableCellFormat(lbl.getCellFormat());
            wcf.setBorder(Border.LEFT, BorderLineStyle.THIN);
            wcf.setBorder(Border.RIGHT, BorderLineStyle.THIN);
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);
            lbl = new Label(14, 47, String.format("%.2f", obs.getMaxCurrent()));
            wcf = new WritableCellFormat(times);//wcf = new WritableCellFormat(lbl.getCellFormat());
            wcf.setBorder(Border.LEFT, BorderLineStyle.THIN);
            wcf.setBorder(Border.RIGHT, BorderLineStyle.THIN);
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);

            lbl = new Label(10, 48, obs.getDischResult().toString());
            wcf = new WritableCellFormat(times);
            wcf.setBorder(Border.LEFT, BorderLineStyle.THIN);
            wcf.setBorder(Border.RIGHT, BorderLineStyle.THIN);
            wcf.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);
            lbl = new Label(11, 48, obs.getHeadResult().toString());
            wcf = new WritableCellFormat(times);
            wcf.setBorder(Border.LEFT, BorderLineStyle.THIN);
            wcf.setBorder(Border.RIGHT, BorderLineStyle.THIN);
            wcf.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);
            lbl = new Label(13, 48, obs.getEffResult().toString());
            wcf = new WritableCellFormat(times);
            wcf.setBorder(Border.LEFT, BorderLineStyle.THIN);
            wcf.setBorder(Border.RIGHT, BorderLineStyle.THIN);
            wcf.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);
            lbl = new Label(14, 48, obs.getCurrResult().toString());
            wcf = new WritableCellFormat(times);
            wcf.setBorder(Border.LEFT, BorderLineStyle.THIN);
            wcf.setBorder(Border.RIGHT, BorderLineStyle.THIN);
            wcf.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
            lbl.setCellFormat(wcf);
            sheet.addCell(lbl);
            copy.write();
            copy.close();
            String fileName = "resources\\xlReportFilled.xls";
            String[] commands = {"cmd", "/c", "start", "\"DummyTitle\"", fileName};//
            Runtime.getRuntime().exec(commands);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public PumpValues getDeclaredValues() {
        PumpValues values = new PumpValues();
        values.setsNo(this.entryPanel.getSlNoField().getText().trim());
        values.setType(this.entryPanel.getPumpTypeField().getText());
        values.setDate(this.entryPanel.getDateChooser().getDate());
        values.setDischarge(Double.parseDouble(getEntryPanel().getDischField().getText()));
        values.setHead(Double.parseDouble(getEntryPanel().getHeadField().getText()));
        values.setEfficiency(Double.parseDouble(getEntryPanel().getEffField().getText()));
        values.setMaxCurrent(Double.parseDouble(getEntryPanel().getCurrField().getText()));
        values.setHeadRangeMax(Double.parseDouble(getEntryPanel().gethRangeUprField().getText()));
        values.setHeadRangeMin(Double.parseDouble(getEntryPanel().gethRangeLwrField().getText()));
        return values;
    }

    public MaxValuesForScale getValuesForScale() {

        MaxValuesForScale values = new MaxValuesForScale();
        values.setDischMax(Double.parseDouble(getEntryPanel().getDischMaxForScaleField().getText()));
        values.setHeadMax(Double.parseDouble(getEntryPanel().getHeadMaxForScaleField().getText()));
        values.setCurrMax(Double.parseDouble(getEntryPanel().getCurrMaxForScaleField().getText()));
        values.setEffMax(Double.parseDouble(getEntryPanel().getEffMaxForScaleField().getText()));
        return values;
    }

    public Dataset getDataset(DatasetAndCurveType type) {

        int rows = findRowCount();
        Dataset dataset = null;
        switch (type) {

            case DISCHARGE_VS_CURRENT: {

                double[] currents = new double[rows];
                double[] discharges = new double[rows];
                double[][] data = this.findReportValues(this.parseEntryTable());
                for (int i = 0; i < rows; i++) {
                    currents[i] = data[i][ReportTableConstants.CURR_COL_INDEX];
                    discharges[i] = data[i][ReportTableConstants.RDISCH_COL_INDEX];
                }
                dataset = new Dataset(discharges, currents, DatasetAndCurveType.DISCHARGE_VS_CURRENT);
                break;
            }

            case DISCHARGE_VS_EFFICIENCY: {

                double[] efficiencies = new double[rows];
                double[] discharges = new double[rows];
                double[][] data = this.findReportValues(this.parseEntryTable());
                for (int i = 0; i < rows; i++) {
                    efficiencies[i] = data[i][ReportTableConstants.EFF_COL_INDEX];
                    discharges[i] = data[i][ReportTableConstants.RDISCH_COL_INDEX];
                }
                dataset = new Dataset(discharges, efficiencies, DatasetAndCurveType.DISCHARGE_VS_EFFICIENCY);
                break;
            }

            case DISCHARGE_VS_HEAD: {

                double[] heads = new double[rows];
                double[] discharges = new double[rows];
                double[][] data = this.findReportValues(this.parseEntryTable());
                for (int i = 0; i < rows; i++) {
                    heads[i] = data[i][ReportTableConstants.RHEAD_COL_INDEX];
                    discharges[i] = data[i][ReportTableConstants.RDISCH_COL_INDEX];
                }
                dataset = new Dataset(discharges, heads, DatasetAndCurveType.DISCHARGE_VS_HEAD);
                break;
            }
        }
        return dataset;
    }

    /**
     * @return the entryPanel
     */
    public ReadingEntryPanel getEntryPanel() {
        return entryPanel;
    }

    private void addDeclaredValues() {
        this.setBackground(Color.white);
        Font lFont = new Font("SansSerif", Font.PLAIN, 18);
        JLabel compLabel = new JLabel("Annai Engineering Company,Coimbatore");
        compLabel.setFont(lFont);
        this.add(compLabel, "grow,wrap,span");
        lFont = new Font("SansSerif", Font.BOLD, 12);
        compLabel = new JLabel("Test Report of Openwell Submersible Pumpset, IS 14220");// 2, 45); 
        compLabel.setFont(lFont);
        this.add(compLabel, "grow,wrap,span");
        Font labelFont = new Font("SansSerif", Font.PLAIN, 8);
        JLabel slNoLabel = new JLabel("Sl. No : ");
        slNoLabel.setFont(labelFont);
        this.add(slNoLabel, "grow");
        JLabel enteredSlNoLabel = new JLabel(entryPanel.getSlNoField().getText());
        enteredSlNoLabel.setFont(labelFont);
        this.add(enteredSlNoLabel, "grow");

        JLabel ipNoLabel = new JLabel("InPass No. : ");
        this.add(ipNoLabel, "grow");
        ipNoLabel.setFont(labelFont);
        JLabel enteredIpNoLabel = new JLabel(entryPanel.getIpNoField().getText());
        this.add(enteredIpNoLabel, "grow");
        enteredIpNoLabel.setFont(labelFont);
        JLabel dateLabel = new JLabel("Date : ");
        this.add(dateLabel, "grow");
        dateLabel.setFont(labelFont);
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = formatter.format(entryPanel.getDateChooser().getDate());
        JLabel enteredDateLabel = new JLabel(dateString);

        this.add(enteredDateLabel, "grow");
        enteredDateLabel.setFont(labelFont);

        JLabel typeLabel = new JLabel("Pump Type : ");
        this.add(typeLabel, "grow");
        typeLabel.setFont(labelFont);
        JLabel enteredTypeLabel = new JLabel(entryPanel.getPumpTypeField().getText());
        this.add(enteredTypeLabel, "grow,wrap");
        enteredTypeLabel.setFont(labelFont);
        JLabel ratingLabel = new JLabel("Rating (kW/HP)     :");
        this.add(ratingLabel, "grow");
        ratingLabel.setFont(labelFont);

        JLabel enteredRatingLabel = new JLabel(entryPanel.getRatingField().getText());
        this.add(enteredRatingLabel, "grow");
        enteredRatingLabel.setFont(labelFont);

        JLabel headLabel = new JLabel("Total Head(m) : ");
        this.add(headLabel, "grow");
        headLabel.setFont(labelFont);
        JLabel enteredHeadLabel = new JLabel(entryPanel.getHeadField().getText());
        this.add(enteredHeadLabel, "grow");
        enteredHeadLabel.setFont(labelFont);
        JLabel dischLabel = new JLabel("Discharge (lps) : ");
        this.add(dischLabel, "grow");
        dischLabel.setFont(labelFont);
        JLabel enteredDischLabel = new JLabel(entryPanel.getDischField().getText());
        this.add(enteredDischLabel, "grow");
        enteredDischLabel.setFont(labelFont);

        JLabel effLabel = new JLabel("Overall Eff.(%) : ");
        this.add(effLabel, "grow");
        effLabel.setFont(labelFont);
        JLabel enteredEffLabel = new JLabel(entryPanel.getEffField().getText());
        this.add(enteredEffLabel, "grow,wrap");
        enteredEffLabel.setFont(labelFont);
        JLabel currLabel = new JLabel("Max.Current (A) : ");
        this.add(currLabel, "grow");
        currLabel.setFont(labelFont);
        JLabel enteredCurrLabel = new JLabel(entryPanel.getCurrField().getText());
        this.add(enteredCurrLabel, "grow");
        enteredCurrLabel.setFont(labelFont);
        JLabel headRangeLabel = new JLabel("Head Range (m) : ");
        this.add(headRangeLabel, "grow");
        headRangeLabel.setFont(labelFont);
        JLabel enteredHeadRangeLabel = new JLabel(entryPanel.gethRangeLwrField().getText() + " / " + entryPanel.gethRangeUprField().getText());
        this.add(enteredHeadRangeLabel, "grow");
        enteredHeadRangeLabel.setFont(labelFont);
        JLabel voltLabel = new JLabel("Voltage (V) : ");
        this.add(voltLabel, "grow");
        voltLabel.setFont(labelFont);
        JLabel enteredVoltLabel = new JLabel(entryPanel.getVoltField().getText());
        this.add(enteredVoltLabel, "grow");
        enteredVoltLabel.setFont(labelFont);
        JLabel phaseLabel = new JLabel("Phase  : ");
        this.add(phaseLabel, "grow");
        phaseLabel.setFont(labelFont);
        JLabel enteredPhaseLabel = new JLabel(entryPanel.getPhaseField().getText());
        this.add(enteredPhaseLabel, "grow,wrap");
        enteredPhaseLabel.setFont(labelFont);
        JLabel freqLabel = new JLabel("Frequency (Hz) : ");
        this.add(freqLabel, "grow");
        freqLabel.setFont(labelFont);
        JLabel enteredFreqLabel = new JLabel(entryPanel.getFreqField().getText());
        this.add(enteredFreqLabel, "grow");
        enteredFreqLabel.setFont(labelFont);
        JLabel delSizeLabel = new JLabel("Del. Size(mm)  : ");
        this.add(delSizeLabel, "grow");
        delSizeLabel.setFont(labelFont);
        JLabel enteredDelSizeLabel = new JLabel(entryPanel.getDelSizeField().getText());
        this.add(enteredDelSizeLabel, "grow");
        enteredDelSizeLabel.setFont(labelFont);
        JLabel gDistLabel = new JLabel("Gauge Distance (m) : ");
        this.add(gDistLabel, "grow");
        gDistLabel.setFont(labelFont);
        JLabel enteredGDistLabel = new JLabel(entryPanel.getGaugDistField().getText());
        this.add(enteredGDistLabel, "grow");
        enteredGDistLabel.setFont(labelFont);
        JLabel remarksLabel = new JLabel("Remarks : ");
        this.add(remarksLabel, "grow");
        remarksLabel.setFont(labelFont);
        JLabel enteredRemarksLabel = new JLabel(entryPanel.getRemarksField().getText());
        this.add(enteredRemarksLabel, "grow,wrap");
        enteredRemarksLabel.setFont(labelFont);
    }

    public void addSignature() {
        Font lFont = new Font("SansSerif", Font.PLAIN, 8);
        JLabel compLabel = new JLabel("Casing Pressure Test : Casing withstood 1.5 times the max. discharge pressure for 2 mins.");
        compLabel.setFont(lFont);
        add(compLabel, "grow,wrap,span");
        lFont = new Font("SansSerif", Font.BOLD, 8);
        compLabel = new JLabel("Signature");// 2, 45); 
        compLabel.setFont(lFont);
        add(compLabel, "grow,wrap,span,gapbottom 10px");
    }

    public static void showErrorMessage() {
        JFrame f = new JFrame();
        f.setBounds(400, 200, 600, 400);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setVisible(true);

        JOptionPane.showMessageDialog(f,
                "Enter only  numbers as values and ensure that no two discharge values are equal.");
        return;
    }
}

class MyHeaderRenderer extends JLabel implements
        TableCellRenderer, Serializable {

    public MyHeaderRenderer(Color gridColorIn) {
        this.setBorder(BorderFactory.createLineBorder(gridColorIn));
        this.setFont(new Font("SansSerif", Font.PLAIN, 8));
    }

    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row,
            int column) {
        this.setText(value.toString());
        return this;
    }
}
