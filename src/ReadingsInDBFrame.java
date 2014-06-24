
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ReadingsInDBFrame.java
 *
 * Created on Aug 5, 2011, 6:52:00 PM
 */
/**
 *
 * @author ANNAIENGG
 */
public class ReadingsInDBFrame extends javax.swing.JFrame {
//finally loaded pump's persisted values ;

    Readings loadedPumpReadings = null;
    // for declTable1
    public static final int SNO_COL = 0;
    public static final int INPASS_NO_COL = 1;
    public static final int DATE_COL = 2;
    public static final int TYPE_COL = 3;
    public static final int KW_COL = 4;
    public static final int HP_COL = 5;
    public static final int DISCH_COL = 6;
    public static final int HEAD_COL = 7;
    public static final int EFF_COL = 8;
    // for declTable2
    public static final int CURR_COL = 0;
    public static final int VOLT_COL = 1;
    public static final int PHASE_COL = 2;
    public static final int FREQ_COL = 3;
    public static final int HEAD_LOWER_COL = 4;
    public static final int HEAD_UPPER_COL = 5;
    public static final int DEL_SIZE_COL = 6;
    public static final int GAUGE_DIST_COL = 7;
    public static final int ISREF_COL = 8;
    // for dutyPointTable
    public static final int DP_DISCH_COL = 0;
    public static final int DP_HEAD_COL = 1;
    public static final int DP_EFF_COL = 2;
    public static final int DP_CURR_COL = 3;
    // for pumpSelectionTable
    public static final int PUMP_SEL_CARDINAL_NO_COL = 0;
    public static final int PUMP_SEL_SNO_COL = 1;
    public static final int PUMP_SEL_DATE_COL = 2;
    public static final int PUMP_SEL_TYPE_COL = 3;
    public static final int PUMP_SEL_DISCH_COL = 4;
    public static final int PUMP_SEL_HEAD_COL = 5;
    public static final int PUMP_SEL_EFF_COL = 6;
    public static final int PUMP_SEL_CURR_COL = 7;
//for readings table
    public static final int READINGS_CARDINAL_NO_COL = 0;
    public static final int READINGS_FREQ_COL = 1;
    public static final int READINGS_DEL_GAUGE_COL = 2;
    public static final int READINGS_DISCH_COL = 3;
    public static final int READINGS_VOLT_COL = 4;
    public static final int READINGS_CURR_COL = 5;
    public static final int READINGS_WATTS_COL = 6;
    private ReadingEntryPanel entryPanel = null;

    /** Creates new form ReadingsInDBFrame */
    public ReadingsInDBFrame() {
        initComponents();
    }

    public ReadingsInDBFrame(ReadingEntryPanel entryPanel) {

        initComponents();
        this.entryPanel = entryPanel;
    }

    public void saveReadingsToDB(ReadingEntryPanel panel, GraphPanel gp) {
        EntryValues values = new EntryValues(panel);
        Session session = null;
 Transaction tx=null;
        Readings rdg = null;
        int lsize = 0;
        try {

            SessionFactory sessions = HibernateUtil.getSessionFactory();
            session = sessions.openSession();
            tx = session.beginTransaction();
            Query query = session.createQuery("from Readings rdg where rdg.sNo = :typeString");
            query.setParameter("typeString", values.getSlNoFieldString().replace(" ", ""));
            List list = query.list();
            lsize = list.size();
            System.out.println("Size is " + list.size());
            session.flush();
            tx.commit();
        } catch (Exception ex) {
            if(tx !=null)
                tx.rollback();
        } finally {
            session.close();
        }

        if (lsize != 0) {
            String str = values.getSlNoFieldString().replace(" ", "");
            this.deleteBySNo(str);
            System.out.println("deleted  " + str);

        }

        try {
            SessionFactory sessions = HibernateUtil.getSessionFactory();
            session = sessions.openSession();
            tx = session.beginTransaction();
            rdg = new Readings();


            rdg.setsNo(values.getSlNoFieldString().replace(" ", ""));
            rdg.setInPassNo(values.getIpNoFieldString());
            rdg.setDate(values.getDateFieldDate());
            rdg.setGaugeDistance(Double.parseDouble(values.getGaugDistFieldString()));
            rdg.setIsRef(values.getIsRefFieldString());
            rdg.setRemarks(values.getRemarksFieldString());
            ObsValuesOfReadings obsValues = new ObsValuesOfReadings();
            PumpValues obValues = gp.getGraph().getPlot().getObsValues();
            obsValues.setDischarge(obValues.getDischarge());
            obsValues.setHead(obValues.getHead());
            obsValues.setCurrent(obValues.getMaxCurrent());
            obsValues.setEff(obValues.getEfficiency());
            rdg.setObsValues(obsValues);
            DeclValuesOfReadings declValues = new DeclValuesOfReadings();
            declValues.setType(values.getPumpTypeFieldString().replace(" ", ""));
            String[] parts = values.getRatingFieldString().split("/");
            declValues.setkW(Double.parseDouble(parts[0]));
            declValues.setHp(Double.parseDouble(parts[1]));
            declValues.setDelSize(Double.parseDouble(values.getDelSizeFieldString()));
            declValues.setDischarge(Double.parseDouble(values.getDischFieldString()));
            declValues.setHead(Double.parseDouble(values.getHeadFieldString()));
            declValues.setEff(Double.parseDouble(values.getEffFieldString()));
            declValues.setCurrent(Double.parseDouble(values.getCurrFieldString()));
            declValues.setHead(Double.parseDouble(values.getHeadFieldString()));
            declValues.setLowerHead(Double.parseDouble(values.gethRangeLwrFieldString()));
            declValues.setUpperHead(Double.parseDouble(values.gethRangeUprFieldString()));
            declValues.setPhases(Integer.parseInt(values.getPhaseFieldString().replace(" ", "")));
            declValues.setVoltage(Integer.parseInt(values.getVoltFieldString()));
            rdg.setdeclValues(declValues);
            rdg.setDischMax(Double.parseDouble(panel.getDischMaxForScaleField().getText()));
            rdg.setHeadMax(Double.parseDouble(panel.getHeadMaxForScaleField().getText()));
            rdg.setEffMax(Double.parseDouble(panel.getEffMaxForScaleField().getText()));
            rdg.setCurrMax(Double.parseDouble(panel.getCurrMaxForScaleField().getText()));
            Object[][] dataRead = values.getTableValueStrings();

            // int cols = dataRead[0].length;
            JTable entryTable = panel.getTable();
            int rows = findFilledRows(entryTable);
            ReadingRow[] readingRows = new ReadingRow[rows + 1];
            ReadingRow one;
            for (int i = 0; i < rows; i++) {
                one = new ReadingRow();
                one.setCardinalNo(i + 1);
                one.setFreq(Double.parseDouble(dataRead[i][READINGS_FREQ_COL].toString()));
                one.setDelGaugeReading(Double.parseDouble(dataRead[i][READINGS_DEL_GAUGE_COL].toString()));
                one.setDisch(Double.parseDouble(dataRead[i][ReadingsInDBFrame.READINGS_DISCH_COL].toString()));
                one.setVolt(Double.parseDouble(dataRead[i][READINGS_VOLT_COL].toString()));
                one.setCurr(Double.parseDouble(dataRead[i][ReadingsInDBFrame.READINGS_CURR_COL].toString()));
                one.setWatts(Double.parseDouble(dataRead[i][READINGS_WATTS_COL].toString()));
                readingRows[i] = one;
            }
            one = new ReadingRow();
            one.setCardinalNo(111);
            one.setFreq(Double.parseDouble(dataRead[8][READINGS_FREQ_COL].toString()));
            one.setDelGaugeReading(Double.parseDouble(dataRead[8][READINGS_DEL_GAUGE_COL].toString()));
            one.setDisch(Double.parseDouble(dataRead[8][ReadingsInDBFrame.READINGS_DISCH_COL].toString()));
            one.setVolt(Double.parseDouble(dataRead[8][READINGS_VOLT_COL].toString()));
            one.setCurr(Double.parseDouble(dataRead[8][ReadingsInDBFrame.READINGS_CURR_COL].toString()));
            one.setWatts(Double.parseDouble(dataRead[8][READINGS_WATTS_COL].toString()));
            readingRows[rows] = one;


            rdg.setReadingRows(readingRows);


            session.save(rdg);


            session.flush();
            tx.commit();
        } catch (Exception ex) {
           // ex.printStackTrace();
            if(tx !=null)
                tx.rollback();
        } finally {
            session.close();
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        readingsTable = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        declTable1 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        declTable2 = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        pumpSelectionTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        dutyPointTable = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        loadReadingsButton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        deleteButton = new javax.swing.JButton();
        readingEntryPanel1 = new ReadingEntryPanel();
        jLabel5 = new javax.swing.JLabel();
        remarksField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        readingsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", null, null, null, null, null, null},
                {"2", null, null, null, null, null, null},
                {"3", null, null, null, null, null, null},
                {"4", null, null, null, null, null, null},
                {"5", null, null, null, null, null, null},
                {"6", null, null, null, null, null, null},
                {"7", null, null, null, null, null, null},
                {"8", null, null, null, null, null, null},
                {"Multiplication Factor", null, null, null, null, null, null}
            },
            new String [] {
                "No", "Freq", "Del.Gauge Reading", "Discharge", "Voltage", "Current", "Watts"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(readingsTable);

        declTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "SNo", "InPassNo", "Date", "Type", "kW", "HP", "Discharge", "Head", "OAEff"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(declTable1);

        declTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Max.Current", "Voltage", "Phase", "Freq", "HeadRangeLower", "HeadRangeUpper", "Del. Size", "Gauge Distance", "IS Ref"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(declTable2);

        pumpSelectionTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Sl.No", "Date", "Type", "Discharge", "Head", "OAEff", "Max. Current"
            }
        ));
        pumpSelectionTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pumpSelectionTableMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(pumpSelectionTable);

        jLabel1.setText("Declared Values :");

        jLabel2.setText("Observed Readings  :");

        dutyPointTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null}
            },
            new String [] {
                "Discharge", "Head", "OA Eff.", "Max. Current"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(dutyPointTable);

        jLabel3.setText("Observed Duty Point Values    :");

        loadReadingsButton.setText("Load the selected readings");
        loadReadingsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadReadingsButtonActionPerformed(evt);
            }
        });

        jLabel4.setText("Select a pump clicking on its row");

        deleteButton.setText("Delete Record");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        jLabel5.setText("Remarks    :");

        remarksField.setEditable(false);
        remarksField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                remarksFieldActionPerformed(evt);
            }
        });

        jLabel6.setText("ID and Observed Vlaues of PumpReading Saved : ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(184, 184, 184)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel3))
                .addGap(51, 51, 51)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(remarksField)
                    .addComponent(jScrollPane5))
                .addGap(47, 47, 47))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addContainerGap(634, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 854, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 864, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(781, 781, 781))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(readingEntryPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(873, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 864, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 864, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addContainerGap(770, Short.MAX_VALUE))))
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel4)
                .addGap(58, 58, 58)
                .addComponent(deleteButton)
                .addGap(89, 89, 89)
                .addComponent(loadReadingsButton)
                .addContainerGap(290, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(remarksField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(deleteButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(loadReadingsButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(readingEntryPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void loadReadingsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadReadingsButtonActionPerformed
        try{
            JTable entryTable = this.entryPanel.getTable();
        //String[][] tableStrings = new String[9][7];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 7; j++) {
//                tableStrings[i][j] = this.readingsTable.getValueAt(i, j).toString();
                entryTable.setValueAt(" ", i, j);
                if (this.readingsTable.getValueAt(i, j) != null) {
                    entryTable.setValueAt(this.readingsTable.getValueAt(i, j), i, j);

                }
            }
            //   EntryValues values = new EntryValues(this.entryPanel);
            entryPanel.getSlNoField().setText(this.declTable1.getValueAt(0, this.SNO_COL).toString());
            entryPanel.getIpNoField().setText(this.declTable1.getValueAt(0, this.INPASS_NO_COL).toString());
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            entryPanel.getDateChooser().setDate(this.loadedPumpReadings.getDate());

            entryPanel.getPumpTypeField().setText(this.declTable1.getValueAt(0, this.TYPE_COL).toString());

            entryPanel.getDelSizeField().setText(this.declTable2.getValueAt(0, this.DEL_SIZE_COL).toString());
            entryPanel.getRatingField().setText(this.declTable1.getValueAt(0, this.KW_COL).toString() + "/"
                    + this.declTable1.getValueAt(0, this.HP_COL).toString());
            entryPanel.getDischField().setText(this.declTable1.getValueAt(0, this.DISCH_COL).toString());

            entryPanel.getHeadField().setText(this.declTable1.getValueAt(0, this.HEAD_COL).toString());
            entryPanel.getEffField().setText(this.declTable1.getValueAt(0, this.EFF_COL).toString());
            entryPanel.getVoltField().setText(this.declTable2.getValueAt(0, this.VOLT_COL).toString());
            entryPanel.getCurrField().setText(this.declTable2.getValueAt(0, this.CURR_COL).toString());
            entryPanel.gethRangeLwrField().setText(this.declTable2.getValueAt(0, this.HEAD_LOWER_COL).toString());

            entryPanel.gethRangeUprField().setText(this.declTable2.getValueAt(0, this.HEAD_UPPER_COL).toString());

            entryPanel.getGaugDistField().setText(this.declTable2.getValueAt(0, this.GAUGE_DIST_COL).toString());
            entryPanel.getIsRefField().setText(this.declTable2.getValueAt(0, this.ISREF_COL).toString());
            entryPanel.getDischMaxForScaleField().setText(String.format("%.2f", loadedPumpReadings.getDischMax()));
            entryPanel.getHeadMaxForScaleField().setText(String.format("%.2f", loadedPumpReadings.getHeadMax()));
            entryPanel.getEffMaxForScaleField().setText(String.format("%.2f", loadedPumpReadings.getEffMax()));
            entryPanel.getCurrMaxForScaleField().setText(String.format("%.2f", loadedPumpReadings.getCurrMax()));
        }
        }catch(Exception ex)
        {
            
        }

    }//GEN-LAST:event_loadReadingsButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        String str = (String) this.pumpSelectionTable.getValueAt(this.pumpSelectionTable.getSelectedRow(), this.PUMP_SEL_SNO_COL);
        this.deleteBySNo(str);
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void pumpSelectionTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pumpSelectionTableMouseClicked
         Session session=null;
        try{
        int row = this.pumpSelectionTable.getSelectedRow();
        String slNo = (String) this.pumpSelectionTable.getValueAt(row, this.PUMP_SEL_SNO_COL);
        SessionFactory sessions = HibernateUtil.getSessionFactory();
        session = sessions.openSession();
        Readings rdg = (Readings) session.get(Readings.class, slNo);

        this.loadedPumpReadings = rdg;
        Object[][] data = new Object[1][this.declTable1.getColumnCount()];
        data[0][SNO_COL] = rdg.getsNo();
        data[0][INPASS_NO_COL] = rdg.getInPassNo();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        if (rdg.getDate() != null) {
            data[0][DATE_COL] = formatter.format(rdg.getDate());
        }
        data[0][TYPE_COL] = rdg.getdeclValues().getType();
        data[0][KW_COL] = rdg.getdeclValues().getkW();
        data[0][HP_COL] = rdg.getdeclValues().getHp();
        data[0][DISCH_COL] = rdg.getdeclValues().getDischarge();
        data[0][HEAD_COL] = rdg.getdeclValues().getHead();
        data[0][EFF_COL] = rdg.getdeclValues().getEff();
        this.displayTable(data, declTable1);

        data = new Object[1][this.declTable2.getColumnCount()];
        data[0][this.CURR_COL] = rdg.getdeclValues().getCurrent();
        data[0][this.VOLT_COL] = rdg.getdeclValues().getVoltage();
        data[0][this.PHASE_COL] = rdg.getdeclValues().getPhases();
        data[0][this.FREQ_COL] = 50.0;
        data[0][this.HEAD_LOWER_COL] = rdg.getdeclValues().getLowerHead();
        data[0][this.HEAD_UPPER_COL] = rdg.getdeclValues().getUpperHead();
        data[0][this.DEL_SIZE_COL] = rdg.getdeclValues().getDelSize();
        data[0][GAUGE_DIST_COL] = String.format("%,.2f", rdg.getGaugeDistance());
        data[0][this.ISREF_COL] = rdg.getIsRef().toString();
        this.displayTable(data, declTable2);

        data = new Object[1][this.dutyPointTable.getColumnCount()];
        data[0][this.DP_DISCH_COL] = String.format("%,.2f", rdg.getObsValues().getDischarge());
        data[0][this.DP_HEAD_COL] = String.format("%,.2f", rdg.getObsValues().getHead());
        data[0][this.DP_EFF_COL] = String.format("%,.2f", rdg.getObsValues().getEff());
        data[0][this.DP_CURR_COL] = String.format("%,.2f", rdg.getObsValues().getCurrent());
        this.displayTable(data, this.dutyPointTable);

        data = new Object[this.readingsTable.getRowCount()][7];
        ReadingRow one;
        for (int i = 0; i < rdg.getReadingRows().length - 1; i++) {

            one = rdg.getReadingRows()[i];
            data[i][this.READINGS_CARDINAL_NO_COL] = one.getCardinalNo();
            data[i][this.READINGS_FREQ_COL] = one.getFreq();
            data[i][this.READINGS_DEL_GAUGE_COL] = one.getDelGaugeReading();
            data[i][this.READINGS_DISCH_COL] = one.getDisch();
            data[i][this.READINGS_VOLT_COL] = one.getVolt();
            data[i][this.READINGS_CURR_COL] = one.getCurr();
            data[i][this.READINGS_WATTS_COL] = one.getWatts();
        }
        one = rdg.getReadingRows()[rdg.getReadingRows().length - 1];
        data[this.readingsTable.getRowCount() - 1][this.READINGS_CARDINAL_NO_COL] = "Multiplication Factor";
        data[this.readingsTable.getRowCount() - 1][this.READINGS_FREQ_COL] = one.getFreq();
        data[this.readingsTable.getRowCount() - 1][this.READINGS_DEL_GAUGE_COL] = one.getDelGaugeReading();
        data[this.readingsTable.getRowCount() - 1][this.READINGS_DISCH_COL] = one.getDisch();
        data[this.readingsTable.getRowCount() - 1][this.READINGS_VOLT_COL] = one.getVolt();
        data[this.readingsTable.getRowCount() - 1][this.READINGS_CURR_COL] = one.getCurr();
        data[this.readingsTable.getRowCount() - 1][this.READINGS_WATTS_COL] = one.getWatts();

        this.displayTable(data, this.readingsTable);
        this.remarksField.setText(rdg.getRemarks());
        session.flush();
        }catch(Exception ex){
         ex.printStackTrace();
        // if(session.getTransaction()!=null)
          //   try{session.getTransaction().rollback();}catch(Exception exc){exc.printStackTrace();}
        }finally{
           session.close();
        }
    }//GEN-LAST:event_pumpSelectionTableMouseClicked

    private void remarksFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_remarksFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_remarksFieldActionPerformed

    public void deleteBySNo(String str) {
        SessionFactory sessions = HibernateUtil.getSessionFactory();
        Session session = null;
        try {
            session = sessions.openSession();
            Transaction tx = session.beginTransaction();

            Readings rdg = (Readings) session.get(Readings.class, str);
            session.delete(rdg);
            tx.commit();
            session.flush();
           
        } catch (Exception ex) {
            if(session.getTransaction()!=null)
             session.getTransaction().rollback();
        } finally {
            session.close();
        }
       // try{Thread.sleep(500);} catch(Exception ex){}
        this.displayPumpSelectionTable();

    }

    public static int findFilledRows(JTable entryTable) {

        int i = 0;
        for (i = 0; i < 8; i++) {
            String str = (entryTable.getValueAt(i, 1)).toString();
            if (str.trim().length() == 0) {
                break;
            }
            if (i == 7) {
                return 8;//if the 8th row(i=7) and the row is not empty there are eight rows.
            }
        }

        //System.out.println("Row count is " + i);
        return i;
    }

    public void displayPumpSelectionTable() {
        //Every time initialize with zero rows.
        pumpSelectionTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                
            },
            new String [] {
                "No", "Sl.No", "Date", "Type", "Discharge", "Head", "OAEff", "Max. Current"
            }
        ));
         SessionFactory sessions = HibernateUtil.getSessionFactory();
        Session session = sessions.openSession();
        // Transaction tx = session.beginTransaction();
        Query query = session.createQuery("from Readings rdg");
        List list = query.list();
        if(list.size()==0) return;
        Object[][] data = new Object[list.size()][this.pumpSelectionTable.getColumnCount()];
        ArrayList alist = new ArrayList<Readings>();
        for (Object obj : list) {
            Readings rdg = (Readings) obj;
            alist.add(rdg);
        }
        Collections.sort(alist, new SorterBySNo());
        int i = 0;
        for (Object obj : alist) {
            Readings rdg = (Readings) obj;
            data[i][PUMP_SEL_CARDINAL_NO_COL] = i + 1;
            data[i][PUMP_SEL_SNO_COL] = rdg.getsNo();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            data[i][PUMP_SEL_DATE_COL] = formatter.format(rdg.getDate());
            data[i][PUMP_SEL_TYPE_COL] = rdg.getdeclValues().getType();
            data[i][PUMP_SEL_DISCH_COL] = String.format("%,.2f", rdg.getObsValues().getDischarge());
            data[i][PUMP_SEL_HEAD_COL] = String.format("%,.2f", rdg.getObsValues().getHead());
            data[i][PUMP_SEL_EFF_COL] = String.format("%,.2f", rdg.getObsValues().getEff());
            data[i][PUMP_SEL_CURR_COL] = String.format("%,.2f", rdg.getObsValues().getCurrent());
            i++;
        }

        this.displayTable(data, this.pumpSelectionTable);
        session.flush();
        session.close();


    }

    private void displayTable(Object[][] data, JTable theTable) {

        for (int i = 0; i < theTable.getRowCount(); i++) {
            for (int j = 0; j < theTable.getColumnModel().getColumnCount(); j++) {
                theTable.setValueAt(" ", i, j);
            }
        }
        int rows = data.length;
        //you will get exception in  data[0].length if data.length(rows) is zero.
        //data[0]-array out of bounds exception.
        if (rows==0) return;
        int cols = data[0].length;

        for (int i = 0; i < rows; i++) {
            if (i + 1 > theTable.getRowCount()) {
                ((DefaultTableModel) (theTable.getModel())).addRow(data[i]);
            }
            for (int j = 0; j < cols; j++) {
                theTable.setValueAt(data[i][j], i, j);
            }
        }
        theTable.validate();
        this.repaint();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new ReadingsInDBFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable declTable1;
    private javax.swing.JTable declTable2;
    private javax.swing.JButton deleteButton;
    private javax.swing.JTable dutyPointTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JButton loadReadingsButton;
    private javax.swing.JTable pumpSelectionTable;
    private ReadingEntryPanel readingEntryPanel1;
    private javax.swing.JTable readingsTable;
    private javax.swing.JTextField remarksField;
    // End of variables declaration//GEN-END:variables
}

class SorterBySNo implements Comparator<Readings> {

    public int compare(Readings o1, Readings o2) {
        return o1.getsNo().compareToIgnoreCase(o2.getsNo());
    }

    class SorterByType implements Comparator<Readings> {

        public int compare(Readings o1, Readings o2) {
            return o1.getdeclValues().getType().compareToIgnoreCase(o2.getdeclValues().getType());
        }
    }
}
