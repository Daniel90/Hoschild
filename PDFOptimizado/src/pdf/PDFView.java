/*
 * PDFView.java
 * author: Daniel
 */

package pdf;

import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import consulta.sql;
import consulta.sqlKilos;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.Window;
import java.io.File;
import java.io.FileOutputStream;
import pdf.calendarioPDF;
import java.sql.*;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import pdf.formatoPDF;
import java.text.*;
import java.text.DecimalFormat;
import java.util.Calendar;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import pdf.envioCorreo;
/**
 * The application's main frame.
 */
public class PDFView extends FrameView {

    public PDFView(SingleFrameApplication app) {
        super(app);

        initComponents();
        //----------------------------------------
        //envioCorreo autonomo = new envioCorreo();
        //autonomo.envio();
        //System.out.println("HOLA");
        
        //---------------------------------------
        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
        
        
    }

    
    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = PDFApp.getApplication().getMainFrame();
            aboutBox = new PDFAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        PDFApp.getApplication().show(aboutBox);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox();
        jComboBox2 = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(pdf.PDFApp.class).getContext().getResourceMap(PDFView.class);
        mainPanel.setBackground(resourceMap.getColor("mainPanel.background")); // NOI18N
        mainPanel.setName("mainPanel"); // NOI18N

        jButton1.setIcon(resourceMap.getIcon("jButton1.icon")); // NOI18N
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setActionCommand(resourceMap.getString("jButton1.actionCommand")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N
        jButton1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jTextField2.setText(resourceMap.getString("txtAno.text")); // NOI18N
        jTextField2.setName("txtAno"); // NOI18N

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre" }));
        jComboBox1.setToolTipText(resourceMap.getString("jComboBox1.toolTipText")); // NOI18N
        jComboBox1.setName("jComboBox1"); // NOI18N

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "01 - Santiago", "02 - Temuco", "03 - Puerto Montt", "04 - Viña", "05 - Antofagasta", "06 - Hualpén", "07 - Copiapó", "08 - San Pedro", "10 - Rancagua" }));
        jComboBox2.setName("jComboBox2"); // NOI18N

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jLabel4.setIcon(resourceMap.getIcon("jLabel4.icon")); // NOI18N
        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox1, 0, 116, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGap(78, 78, 78)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3))
                    .addComponent(jButton1))
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        menuBar.setBackground(resourceMap.getColor("menuBar.background")); // NOI18N
        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(pdf.PDFApp.class).getContext().getActionMap(PDFView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setBackground(resourceMap.getColor("statusPanel.background")); // NOI18N
        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 188, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents
   
    public static PdfPTable createMiniTable(int dia, int mes, int ano) throws DocumentException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(9);
        cuerpo.setSize(8);
        fuente.setStyle(Font.BOLD);
        PdfPTable tabla = new PdfPTable(2);
        tabla.setWidths(new float[]{0.1f,0.1f});
        
        
        PdfPCell cellTITULO = new PdfPCell(new Paragraph("MAURICIO HOCHSCHILD",FontFactory.getFont(FontFactory.TIMES_BOLD,12)));
        cellTITULO.setHorizontalAlignment(Element.ALIGN_LEFT);
        cellTITULO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTITULO);
        
        PdfPCell cellFecha = new PdfPCell(new Paragraph("DOCUMENTO GENERADO A FECHA DE: "+dia+"/"+mes+"/"+ano+"",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellFecha.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellFecha.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFecha);
        return tabla;
   }
    
   public static PdfPTable createTableEneroD(int mes, ResultSet documentosPower7, ResultSet documentosKilos7, String sucursal, int ano) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(9);
        cuerpo.setSize(8);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(4);
        tabla.setWidths(new float[]{0.01f,0.1f,0.1f,0.1f});
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("PRECIOS POR KILOS AÑO "+ano+" - "+sucursal+"",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(4);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Precio Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);
        
        PdfPCell cellTot = new PdfPCell(new Paragraph("Promedio Kilos",fuente));
        cellTot.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTot.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTot);
        
        
        int j=1;
        float sumaEnero = 0,sumaT = 0;
        float sumaHorizontal = 0;
        while(documentosPower7.next() && documentosKilos7.next()){
                PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosPower7.getString("FAM187"),cuerpo));
                celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
                tabla.addCell(celdaFAM);
                           
                PdfPCell celdaDES = new PdfPCell(new Phrase(documentosPower7.getString("DES187"),cuerpo));
                celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
                tabla.addCell(celdaDES);


                DecimalFormat df = new DecimalFormat("###.00");
                String cadena = "";
                sumaEnero = sumaEnero + Float.parseFloat(documentosPower7.getString("V01187"));             
                float dividendo = Float.parseFloat(documentosPower7.getString("V01187"));
                float divisor = Float.parseFloat(documentosKilos7.getString("K01187"));
                float resultado = dividendo/divisor;
                cadena = String.valueOf(df.format(resultado));
                PdfPCell celda = new PdfPCell(new Phrase(cadena,cuerpo));
                celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                tabla.addCell(celda);
                
                DecimalFormat dfTot = new DecimalFormat("###.00");
                sumaHorizontal = resultado;
                sumaT = sumaT + sumaHorizontal;
                PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                tabla.addCell(cell);
                sumaHorizontal = 0;
                
        }
       
        
        return tabla;
   }
   public static PdfPTable createTableFD(int mes, ResultSet documentosPower7, ResultSet documentosKilos7, String sucursal,int ano) throws DocumentException, SQLException{
       Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(9);
        cuerpo.setSize(8);
        fuente.setStyle(Font.BOLD);
        
        
        
        PdfPTable tabla = new PdfPTable(5);
        tabla.setWidths(new float[]{0.015f,0.1f,0.1f,0.1f,0.1f});
        
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("PRECIOS POR KILOS AÑO "+ano+" - "+sucursal+"",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(5);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Precio Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Precio Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);
        
        PdfPCell cellTot = new PdfPCell(new Paragraph("Promedio Kilos",fuente));
        cellTot.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTot.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTot);
        
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaT = 0;
        while(documentosPower7.next() && documentosKilos7.next()){
                PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosPower7.getString("FAM187"),cuerpo));
                celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
                tabla.addCell(celdaFAM);
                
                PdfPCell celdaDES = new PdfPCell(new Phrase(documentosPower7.getString("DES187"),cuerpo));
                celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
                tabla.addCell(celdaDES);


                DecimalFormat dfEne = new DecimalFormat("###.00");
                String cadenaEne = "";
                sumaEnero = sumaEnero + Float.parseFloat(documentosPower7.getString("V01187"));
                float dividendoE = Float.parseFloat(documentosPower7.getString("V01187"));
                float divisorE = Float.parseFloat(documentosKilos7.getString("K01187"));
                float resE = dividendoE/divisorE;
                cadenaEne = String.valueOf(dfEne.format(resE));
                PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
                celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
                tabla.addCell(celdaEne);

                DecimalFormat dfFeb = new DecimalFormat("###.00");
                String cadenaFeb = "";
                sumaFeb = sumaFeb + Float.parseFloat(documentosPower7.getString("V02187"));
                float dividendoF = Float.parseFloat(documentosPower7.getString("V02187"));
                float divisorF = Float.parseFloat(documentosKilos7.getString("K02187"));
                float resF = dividendoF/divisorF;
                cadenaFeb = String.valueOf(dfFeb.format(resF));
                PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
                celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
                tabla.addCell(celdaFeb);
                
                DecimalFormat dfTot = new DecimalFormat("###.00");
                sumaHorizontal = (resE + resF)/2;
                                                  
                
                sumaT = (sumaT + sumaHorizontal)/2;
                PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                tabla.addCell(cell);
                sumaHorizontal = 0;
        }    
        
        return tabla; 
   }
   public static PdfPTable createTableMD(int mes, ResultSet documentosPower7, ResultSet documentosKilos7, String sucursal,int ano) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(9);
        cuerpo.setSize(8);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(6);
        tabla.setWidths(new float[]{0.017f,0.1f,0.1f,0.1f,0.1f,0.1f});
        
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("PRECIOS POR KILOS AÑO "+ano+" - "+sucursal+"",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(6);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Precio Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Precio Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Precio Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);
        
        PdfPCell cellTot = new PdfPCell(new Paragraph("Promedio Kilos",fuente));
        cellTot.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTot.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTot);
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumT = 0;
         while(documentosPower7.next() && documentosKilos7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosPower7.getString("FAM187"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosPower7.getString("DES187"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);


            DecimalFormat dfEne = new DecimalFormat("###.00");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosPower7.getString("V01187"));
            float dividendoE = Float.parseFloat(documentosPower7.getString("V01187"));
            float divisorE = Float.parseFloat(documentosKilos7.getString("K01187"));
            float resE = dividendoE/divisorE;
            cadenaEne = String.valueOf(dfEne.format(resE));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###.00");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosPower7.getString("V02187"));
            float dividendoF = Float.parseFloat(documentosPower7.getString("V02187"));
            float divisorF = Float.parseFloat(documentosKilos7.getString("K02187"));
            float resF = dividendoF/divisorF;
            cadenaFeb = String.valueOf(dfFeb.format(resF));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb); 

            DecimalFormat dfMar = new DecimalFormat("###.00");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosPower7.getString("V03187"));
            float dividendoM = Float.parseFloat(documentosPower7.getString("V03187"));
            float divisorM = Float.parseFloat(documentosKilos7.getString("K03187"));
            float resM = dividendoM/divisorM;
            cadenaMar = String.valueOf(dfMar.format(resM));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);
            
            DecimalFormat dfTot = new DecimalFormat("###.00");
            sumaHorizontal = (resE+resF+resM)/3;
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            sumT = sumT + sumaHorizontal;
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
        }
        
        return tabla;
   }
   public static PdfPTable createTableAD(int mes, ResultSet documentosPower7, ResultSet documentosKilos7, String sucursal,int ano) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(9);
        cuerpo.setSize(8);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(7);
        tabla.setWidths(new float[]{0.02f,0.1f,0.1f,0.1f,0.1f,0.1f,0.1f});
        
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("PRECIOS POR KILOS AÑO "+ano+" - "+sucursal+"",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(7);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Precio Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Precio Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Precio Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);

        PdfPCell cellAbr = new PdfPCell(new Paragraph("Precio Abril",fuente));
        cellAbr.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAbr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbr);
        
        PdfPCell cellTot = new PdfPCell(new Paragraph("Promedio kilos",fuente));
        cellTot.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTot.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTot);
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumaAbr = 0,sumT = 0;
        
        while(documentosPower7.next() && documentosKilos7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosPower7.getString("FAM187"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosPower7.getString("DES187"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);


            DecimalFormat dfEne = new DecimalFormat("###.00");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosPower7.getString("V01187"));
            float dividendoE = Float.parseFloat(documentosPower7.getString("V01187"));
            float divisorE = Float.parseFloat(documentosKilos7.getString("K01187"));
            float resE = dividendoE/divisorE;
            cadenaEne = String.valueOf(dfEne.format(resE));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###.00");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosPower7.getString("V02187"));
            float dividendoF = Float.parseFloat(documentosPower7.getString("V02187"));
            float divisorF = Float.parseFloat(documentosKilos7.getString("K02187"));
            float resF = dividendoF/divisorF;
            cadenaFeb = String.valueOf(dfFeb.format(resF));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb); 

            DecimalFormat dfMar = new DecimalFormat("###.00");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosPower7.getString("V03187"));
            float dividendoM = Float.parseFloat(documentosPower7.getString("V03187"));
            float divisorM = Float.parseFloat(documentosKilos7.getString("K03187"));
            float resM = dividendoM/divisorM;
            cadenaMar = String.valueOf(dfMar.format(resM));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);

            DecimalFormat dfAbr = new DecimalFormat("###.00");
            String cadenaAbr = "";
            sumaAbr = sumaAbr + Float.parseFloat(documentosPower7.getString("V04187"));
            float dividendoA = Float.parseFloat(documentosPower7.getString("V04187"));
            float divisorA = Float.parseFloat(documentosKilos7.getString("K04187"));
            float resA = dividendoA/divisorA;
            cadenaAbr = String.valueOf(dfAbr.format(resA));
            PdfPCell celdaAbr = new PdfPCell(new Phrase(cadenaAbr,cuerpo));
            celdaAbr.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAbr);
            
            DecimalFormat dfTot = new DecimalFormat("###.00");
            sumaHorizontal = (resE+resF+resM+resA)/4;
            sumT = sumT = sumaHorizontal;
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
        }
      
        return tabla;
    
   }
   public static PdfPTable createTableMayD(int mes, ResultSet documentosPower7, ResultSet documentosKilos7, String sucursal, int ano) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(9);
        cuerpo.setSize(8);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(8);
        tabla.setWidths(new float[]{0.023f,0.1f,0.1f,0.1f,0.1f,0.1f,0.1f,0.1f});
        
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("PRECIOS POR KILOS AÑO "+ano+" - "+sucursal+"",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(8);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Precio Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Precio Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Precio Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);

        PdfPCell cellAbr = new PdfPCell(new Paragraph("Precio Abril",fuente));
        cellAbr.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAbr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbr);

        PdfPCell cellMayo = new PdfPCell(new Paragraph("Precio Mayo",fuente));
        cellMayo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMayo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMayo);
        
        PdfPCell cellTot = new PdfPCell(new Paragraph("Promedio kilos",fuente));
        cellTot.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTot.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTot);
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumaAbr = 0,sumaMay = 0,sumT = 0;
        
        while(documentosPower7.next() && documentosKilos7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosPower7.getString("FAM187"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosPower7.getString("DES187"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);


            DecimalFormat dfEne = new DecimalFormat("###.00");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosPower7.getString("V01187"));
            float dividendoE = Float.parseFloat(documentosPower7.getString("V01187"));
            float divisorE = Float.parseFloat(documentosKilos7.getString("K01187"));
            float resE = dividendoE/divisorE;
            cadenaEne = String.valueOf(dfEne.format(resE));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###.00");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosPower7.getString("V02187"));
            float dividendoF = Float.parseFloat(documentosPower7.getString("V02187"));
            float divisorF = Float.parseFloat(documentosKilos7.getString("K02187"));
            float resF = dividendoF/divisorF;
            cadenaFeb = String.valueOf(dfFeb.format(resF));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb); 

            DecimalFormat dfMar = new DecimalFormat("###.00");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosPower7.getString("V03187"));
            float dividendoM = Float.parseFloat(documentosPower7.getString("V03187"));
            float divisorM = Float.parseFloat(documentosKilos7.getString("K03187"));
            float resM = dividendoM/divisorM;
            cadenaMar = String.valueOf(dfMar.format(resM));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);

            DecimalFormat dfAbr = new DecimalFormat("###.00");
            String cadenaAbr = "";
            sumaAbr = sumaAbr + Float.parseFloat(documentosPower7.getString("V04187"));
            float dividendoA = Float.parseFloat(documentosPower7.getString("V04187"));
            float divisorA = Float.parseFloat(documentosKilos7.getString("K04187"));
            float resA = dividendoA/divisorA;
            cadenaAbr = String.valueOf(dfAbr.format(resA));
            PdfPCell celdaAbr = new PdfPCell(new Phrase(cadenaAbr,cuerpo));
            celdaAbr.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAbr);

            DecimalFormat dfMayo = new DecimalFormat("###.00");
            String cadenaMayo = "";
            sumaMay = sumaMay + Float.parseFloat(documentosPower7.getString("V05187"));
            float dividendoMAY = Float.parseFloat(documentosPower7.getString("V05187"));
            float divisorMAY = Float.parseFloat(documentosKilos7.getString("K05187"));
            float resMay = dividendoMAY/divisorMAY;
            cadenaMayo = String.valueOf(dfMayo.format(resMay));
            PdfPCell celdaMayo = new PdfPCell(new Phrase(cadenaMayo,cuerpo));
            celdaMayo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMayo);
            
            DecimalFormat dfTot = new DecimalFormat("###.00");
            sumaHorizontal = (resE+resF+resM+resA+resMay)/5;
            sumT = sumT + sumaHorizontal;
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
        }
        
        return tabla;
   }
   public static PdfPTable createTableJD(int mes, ResultSet documentosPower7, ResultSet documentosKilos7, String sucursal,int ano) throws DocumentException, SQLException{
       Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(9);
        cuerpo.setSize(8);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(9);
        tabla.setWidths(new float[]{0.028f,0.11f,0.1f,0.1f,0.1f,0.1f,0.1f,0.1f,0.1f});
        
        
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("PRECIOS POR KILOS AÑO "+ano+" - "+sucursal+"",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(9);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Precio Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Precio Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Precio Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);

        PdfPCell cellAbr = new PdfPCell(new Paragraph("Precio Abril",fuente));
        cellAbr.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAbr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbr);

        PdfPCell cellMayo = new PdfPCell(new Paragraph("Precio Mayo",fuente));
        cellMayo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMayo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMayo);

        PdfPCell cellJun = new PdfPCell(new Paragraph("Precio Junio",fuente));
        cellJun.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJun.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJun);
        
        PdfPCell cellTot = new PdfPCell(new Paragraph("Promedio kilos",fuente));
        cellTot.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTot.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTot);
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumaAbr = 0,sumaMay = 0,sumaJun = 0,sumT = 0;
        while(documentosPower7.next() && documentosKilos7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosPower7.getString("FAM187"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosPower7.getString("DES187"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);


            DecimalFormat dfEne = new DecimalFormat("###.00");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosPower7.getString("V01187"));
            float dividendoE = Float.parseFloat(documentosPower7.getString("V01187"));
            float divisorE = Float.parseFloat(documentosKilos7.getString("K01187"));
            float resE = dividendoE/divisorE;
            cadenaEne = String.valueOf(dfEne.format(resE));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###.00");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosPower7.getString("V02187"));
            float dividendoF = Float.parseFloat(documentosPower7.getString("V02187"));
            float divisorF = Float.parseFloat(documentosKilos7.getString("K02187"));
            float resF = dividendoF/divisorF;
            cadenaFeb = String.valueOf(dfFeb.format(resF));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb);

            DecimalFormat dfMar = new DecimalFormat("###.00");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosPower7.getString("V03187"));
            float dividendoM = Float.parseFloat(documentosPower7.getString("V03187"));
            float divisorM = Float.parseFloat(documentosKilos7.getString("K03187"));
            float resM = dividendoM/divisorM;
            cadenaMar = String.valueOf(dfMar.format(resM));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);

            DecimalFormat dfAbr = new DecimalFormat("###.00");
            String cadenaAbr = "";
            sumaAbr = sumaAbr + Float.parseFloat(documentosPower7.getString("V04187"));
            float dividendoA = Float.parseFloat(documentosPower7.getString("V04187"));
            float divisorA = Float.parseFloat(documentosKilos7.getString("K04187"));
            float resA = dividendoA/divisorA;
            cadenaAbr = String.valueOf(dfAbr.format(resA));
            PdfPCell celdaAbr = new PdfPCell(new Phrase(cadenaAbr,cuerpo));
            celdaAbr.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAbr);

            DecimalFormat dfMayo = new DecimalFormat("###.00");
            String cadenaMayo = "";
            sumaMay = sumaMay + Float.parseFloat(documentosPower7.getString("V05187"));
            float dividendoMAY = Float.parseFloat(documentosPower7.getString("V05187"));
            float divisorMAY = Float.parseFloat(documentosKilos7.getString("K05187"));
            float resMay = dividendoMAY/divisorMAY;
            cadenaMayo = String.valueOf(dfMayo.format(resMay));
            PdfPCell celdaMayo = new PdfPCell(new Phrase(cadenaMayo,cuerpo));
            celdaMayo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMayo);

            DecimalFormat dfJun = new DecimalFormat("###.00");
            String cadenaJun = "";
            sumaJun = sumaJun + Float.parseFloat(documentosPower7.getString("V06187"));
            float dividendoJ = Float.parseFloat(documentosPower7.getString("V06187"));
            float divisorJ = Float.parseFloat(documentosKilos7.getString("K06187"));
            float resJ = dividendoJ/divisorJ;
            cadenaJun = String.valueOf(dfJun.format(resJ));
            PdfPCell celdaJun = new PdfPCell(new Phrase(cadenaJun,cuerpo));
            celdaJun.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJun);
            
            DecimalFormat dfTot = new DecimalFormat("###,###,###.00");
            sumaHorizontal = (resE+resF+resM+resA+resMay+resJ)/6;
            sumT = sumT + sumaHorizontal;
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
        }
        
        return tabla;
   }
   public static PdfPTable createTableJulD(int mes, ResultSet documentosPower7, ResultSet documentosKilos7, String sucursal,int ano) throws DocumentException, SQLException{
       Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(9);
        cuerpo.setSize(8);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(10);
        tabla.setWidths(new float[]{0.033f,0.13f,0.1f,0.11f,0.1f,0.1f,0.1f,0.1f,0.1f,0.12f});
        
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("PRECIOS POR KILOS AÑO "+ano+" - "+sucursal+"",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(10);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Precio Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Precio Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Precio Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);

        PdfPCell cellAbr = new PdfPCell(new Paragraph("Precio Abril",fuente));
        cellAbr.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAbr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbr);

        PdfPCell cellMayo = new PdfPCell(new Paragraph("Precio Mayo",fuente));
        cellMayo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMayo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMayo);

        PdfPCell cellJun = new PdfPCell(new Paragraph("Precio Junio",fuente));
        cellJun.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJun.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJun);

        PdfPCell cellJul = new PdfPCell(new Paragraph("Precio Julio",fuente));
        cellJul.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJul.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJul);
        
        PdfPCell cellTot = new PdfPCell(new Paragraph("Promedio kilos",fuente));
        cellTot.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTot.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTot);
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumaAbr = 0,sumaMay = 0,sumaJun = 0,sumaJul = 0,sumT = 0;
        
        while(documentosPower7.next() && documentosKilos7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosPower7.getString("FAM187"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosPower7.getString("DES187"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);

            DecimalFormat dfEne = new DecimalFormat("###.00");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosPower7.getString("V01187"));
            float dividendoE = Float.parseFloat(documentosPower7.getString("V01187"));
            float divisorE = Float.parseFloat(documentosKilos7.getString("K01187"));
            float resE = dividendoE/divisorE;
            cadenaEne = String.valueOf(dfEne.format(resE));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###.00");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosPower7.getString("V02187"));
            float dividendoF = Float.parseFloat(documentosPower7.getString("V02187"));
            float divisorF = Float.parseFloat(documentosKilos7.getString("K02187"));
            float resF = dividendoF/divisorF;
            cadenaFeb = String.valueOf(dfFeb.format(resF));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb);

            DecimalFormat dfMar = new DecimalFormat("###.00");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosPower7.getString("V03187"));
            float dividendoM = Float.parseFloat(documentosPower7.getString("V03187"));
            float divisorM = Float.parseFloat(documentosKilos7.getString("K03187"));
            float resM = dividendoM/divisorM;
            cadenaMar = String.valueOf(dfMar.format(resM));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);

            DecimalFormat dfAbr = new DecimalFormat("###.00");
            String cadenaAbr = "";
            sumaAbr = sumaAbr + Float.parseFloat(documentosPower7.getString("V04187"));
            float dividendoA = Float.parseFloat(documentosPower7.getString("V04187"));
            float divisorA = Float.parseFloat(documentosKilos7.getString("K04187"));
            float resA = dividendoA/divisorA;
            cadenaAbr = String.valueOf(dfAbr.format(resA));
            PdfPCell celdaAbr = new PdfPCell(new Phrase(cadenaAbr,cuerpo));
            celdaAbr.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAbr);

            DecimalFormat dfMayo = new DecimalFormat("###.00");
            String cadenaMayo = "";
            sumaMay = sumaMay + Float.parseFloat(documentosPower7.getString("V05187"));
            float dividendoMAY = Float.parseFloat(documentosPower7.getString("V05187"));
            float divisorMAY = Float.parseFloat(documentosKilos7.getString("K05187"));
            float resMay = dividendoMAY/divisorMAY;
            cadenaMayo = String.valueOf(dfMayo.format(resMay));
            PdfPCell celdaMayo = new PdfPCell(new Phrase(cadenaMayo,cuerpo));
            celdaMayo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMayo);

            DecimalFormat dfJun = new DecimalFormat("###.00");
            String cadenaJun = "";
            sumaJun = sumaJun + Float.parseFloat(documentosPower7.getString("V06187"));
            float dividendoJ = Float.parseFloat(documentosPower7.getString("V06187"));
            float divisorJ = Float.parseFloat(documentosKilos7.getString("K06187"));
            float resJ = dividendoJ/divisorJ;
            cadenaJun = String.valueOf(dfJun.format(resJ));
            PdfPCell celdaJun = new PdfPCell(new Phrase(cadenaJun,cuerpo));
            celdaJun.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJun);
            

            DecimalFormat dfJul = new DecimalFormat("###.00");
            String cadenaJul = "";
            sumaJul = sumaJul + Float.parseFloat(documentosPower7.getString("V07187"));
            float dividendoJul = Float.parseFloat(documentosPower7.getString("V07187"));
            float divisorJul = Float.parseFloat(documentosKilos7.getString("K07187"));
            float resJul = dividendoJul/divisorJul;
            cadenaJul = String.valueOf(dfJul.format(resJul));
            PdfPCell celdaJul = new PdfPCell(new Phrase(cadenaJul,cuerpo));
            celdaJul.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJul);
            
            DecimalFormat dfTot = new DecimalFormat("###.00");
            sumaHorizontal = (resE+resF+resM+resA+resMay+resJ+resJul)/7;
            sumT = sumT +sumaHorizontal;
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
        }
        
        return tabla;
   }  
   public static PdfPTable createTableAgoD(int mes, ResultSet documentosPower7, ResultSet documentosKilos7, String sucursal,int ano) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(8);
        cuerpo.setSize(7);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(11);
        tabla.setWidths(new float[]{0.31f,1.2f,1,1,1,1,1,1,1,1,1});
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("PRECIOS POR KILOS AÑO "+ano+" - "+sucursal+"",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(11);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Precio Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Precio Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Precio Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);

        PdfPCell cellAbr = new PdfPCell(new Paragraph("Precio Abril",fuente));
        cellAbr.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAbr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbr);

        PdfPCell cellMayo = new PdfPCell(new Paragraph("Precio Mayo",fuente));
        cellMayo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMayo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMayo);

        PdfPCell cellJun = new PdfPCell(new Paragraph("Precio Junio",fuente));
        cellJun.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJun.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJun);

        PdfPCell cellJul = new PdfPCell(new Paragraph("Precio Julio",fuente));
        cellJul.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJul.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJul);

        PdfPCell cellAgo = new PdfPCell(new Paragraph("Precio Agosto",fuente));
        cellAgo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAgo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAgo);
        
        PdfPCell cellT = new PdfPCell(new Paragraph("Promedio kilos",fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumaAbr = 0,sumaMay = 0,sumaJun = 0,sumaJul = 0,sumaAgos = 0,sumT = 0;
        while(documentosPower7.next() && documentosKilos7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosPower7.getString("FAM187"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosPower7.getString("DES187"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);

            DecimalFormat dfEne = new DecimalFormat("###.00");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosPower7.getString("V01187"));
            float dividendoE = Float.parseFloat(documentosPower7.getString("V01187"));
            float divisorE = Float.parseFloat(documentosKilos7.getString("K01187"));
            float resE = dividendoE/divisorE;
            cadenaEne = String.valueOf(dfEne.format(resE));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###.00");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosPower7.getString("V02187"));
            float dividendoF = Float.parseFloat(documentosPower7.getString("V02187"));
            float divisorF = Float.parseFloat(documentosKilos7.getString("K02187"));
            float resF = dividendoF/divisorF;
            cadenaFeb = String.valueOf(dfFeb.format(resF));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb);

            DecimalFormat dfMar = new DecimalFormat("###.00");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosPower7.getString("V03187"));
            float dividendoM = Float.parseFloat(documentosPower7.getString("V03187"));
            float divisorM = Float.parseFloat(documentosKilos7.getString("K03187"));
            float resM = dividendoM/divisorM;
            cadenaMar = String.valueOf(dfMar.format(resM));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);

            DecimalFormat dfAbr = new DecimalFormat("###.00");
            String cadenaAbr = "";
            sumaAbr = sumaAbr + Float.parseFloat(documentosPower7.getString("V04187"));
            float dividendoA = Float.parseFloat(documentosPower7.getString("V04187"));
            float divisorA = Float.parseFloat(documentosKilos7.getString("K04187"));
            float resA = dividendoA/divisorA;
            cadenaAbr = String.valueOf(dfAbr.format(resA));
            PdfPCell celdaAbr = new PdfPCell(new Phrase(cadenaAbr,cuerpo));
            celdaAbr.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAbr);

            DecimalFormat dfMayo = new DecimalFormat("###.00");
            String cadenaMayo = "";
            sumaMay = sumaMay + Float.parseFloat(documentosPower7.getString("V05187"));
            float dividendoMAY = Float.parseFloat(documentosPower7.getString("V05187"));
            float divisorMAY = Float.parseFloat(documentosKilos7.getString("K05187"));
            float resMay = dividendoMAY/divisorMAY;
            cadenaMayo = String.valueOf(dfMayo.format(resMay));
            PdfPCell celdaMayo = new PdfPCell(new Phrase(cadenaMayo,cuerpo));
            celdaMayo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMayo);

            DecimalFormat dfJun = new DecimalFormat("###.00");
            String cadenaJun = "";
            sumaJun = sumaJun + Float.parseFloat(documentosPower7.getString("V06187"));
            float dividendoJ = Float.parseFloat(documentosPower7.getString("V06187"));
            float divisorJ = Float.parseFloat(documentosKilos7.getString("K06187"));
            float resJ = dividendoJ/divisorJ;
            cadenaJun = String.valueOf(dfJun.format(resJ));
            PdfPCell celdaJun = new PdfPCell(new Phrase(cadenaJun,cuerpo));
            celdaJun.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJun);
            

            DecimalFormat dfJul = new DecimalFormat("###.00");
            String cadenaJul = "";
            sumaJul = sumaJul + Float.parseFloat(documentosPower7.getString("V07187"));
            float dividendoJul = Float.parseFloat(documentosPower7.getString("V07187"));
            float divisorJul = Float.parseFloat(documentosKilos7.getString("K07187"));
            float resJul = dividendoJul/divisorJul;
            cadenaJul = String.valueOf(dfJul.format(resJul));
            PdfPCell celdaJul = new PdfPCell(new Phrase(cadenaJul,cuerpo));
            celdaJul.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJul);

            DecimalFormat dfAgo = new DecimalFormat("###.00");
            String cadenaAgo = "";
            sumaAgos = sumaAgos + Float.parseFloat(documentosPower7.getString("V08187"));
            float dividendoAgos = Float.parseFloat(documentosPower7.getString("V08187"));
            float divisorAgos = Float.parseFloat(documentosKilos7.getString("K08187"));
            float resAgos = dividendoAgos/divisorAgos;
            cadenaAgo = String.valueOf(dfAgo.format(resAgos));
            PdfPCell celdaAgo = new PdfPCell(new Phrase(cadenaAgo,cuerpo));
            celdaAgo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAgo);
            
            DecimalFormat dfTot = new DecimalFormat("###.00");
            sumaHorizontal = (resE+resF+resM+resA+resMay+resJ+resJul+resAgos)/8;
            sumT = sumT +sumaHorizontal;
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
            
        }
        
        return tabla;
   }  
   public static PdfPTable createTableSD(int mes, ResultSet documentosPower7, ResultSet documentosKilos7, String sucursal,int ano) throws DocumentException, SQLException{
      Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(7);
        cuerpo.setSize(6);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(12);
        tabla.setWidths(new float[]{0.5f,2,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.8f,1.5f});
        
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("PRECIOS POR KILOS AÑO "+ano+" - "+sucursal+"",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(12);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Precio Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Precio Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Precio Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);

        PdfPCell cellAbr = new PdfPCell(new Paragraph("Precio Abril",fuente));
        cellAbr.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAbr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbr);

        PdfPCell cellMayo = new PdfPCell(new Paragraph("Precio Mayo",fuente));
        cellMayo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMayo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMayo);

        PdfPCell cellJun = new PdfPCell(new Paragraph("Precio Junio",fuente));
        cellJun.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJun.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJun);

        PdfPCell cellJul = new PdfPCell(new Paragraph("Precio Julio",fuente));
        cellJul.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJul.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJul);

        PdfPCell cellAgo = new PdfPCell(new Paragraph("Precio Agosto",fuente));
        cellAgo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAgo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAgo);

        PdfPCell cellSep = new PdfPCell(new Paragraph("Precio Septiembre",fuente));
        cellSep.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellSep.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellSep);
        
        PdfPCell cellT = new PdfPCell(new Paragraph("Promedio kilos",fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumaAbr = 0,sumaMay = 0,sumaJun = 0,sumaJul = 0,sumaAgos = 0,sumaSep = 0,sumT = 0;

        while(documentosPower7.next() && documentosKilos7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosPower7.getString("FAM187"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosPower7.getString("DES187"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);

            DecimalFormat dfEne = new DecimalFormat("###.00");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosPower7.getString("V01187"));
            float dividendoE = Float.parseFloat(documentosPower7.getString("V01187"));
            float divisorE = Float.parseFloat(documentosKilos7.getString("K01187"));
            float resE = dividendoE/divisorE;
            cadenaEne = String.valueOf(dfEne.format(resE));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###.00");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosPower7.getString("V02187"));
            float dividendoF = Float.parseFloat(documentosPower7.getString("V02187"));
            float divisorF = Float.parseFloat(documentosKilos7.getString("K02187"));
            float resF = dividendoF/divisorF;
            cadenaFeb = String.valueOf(dfFeb.format(resF));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb);

            DecimalFormat dfMar = new DecimalFormat("###.00");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosPower7.getString("V03187"));
            float dividendoM = Float.parseFloat(documentosPower7.getString("V03187"));
            float divisorM = Float.parseFloat(documentosKilos7.getString("K03187"));
            float resM = dividendoM/divisorM;
            cadenaMar = String.valueOf(dfMar.format(resM));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);

            DecimalFormat dfAbr = new DecimalFormat("###.00");
            String cadenaAbr = "";
            sumaAbr = sumaAbr + Float.parseFloat(documentosPower7.getString("V04187"));
            float dividendoA = Float.parseFloat(documentosPower7.getString("V04187"));
            float divisorA = Float.parseFloat(documentosKilos7.getString("K04187"));
            float resA = dividendoA/divisorA;
            cadenaAbr = String.valueOf(dfAbr.format(resA));
            PdfPCell celdaAbr = new PdfPCell(new Phrase(cadenaAbr,cuerpo));
            celdaAbr.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAbr);

            DecimalFormat dfMayo = new DecimalFormat("###.00");
            String cadenaMayo = "";
            sumaMay = sumaMay + Float.parseFloat(documentosPower7.getString("V05187"));
            float dividendoMAY = Float.parseFloat(documentosPower7.getString("V05187"));
            float divisorMAY = Float.parseFloat(documentosKilos7.getString("K05187"));
            float resMay = dividendoMAY/divisorMAY;
            cadenaMayo = String.valueOf(dfMayo.format(resMay));
            PdfPCell celdaMayo = new PdfPCell(new Phrase(cadenaMayo,cuerpo));
            celdaMayo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMayo);

            DecimalFormat dfJun = new DecimalFormat("###.00");
            String cadenaJun = "";
            sumaJun = sumaJun + Float.parseFloat(documentosPower7.getString("V06187"));
            float dividendoJ = Float.parseFloat(documentosPower7.getString("V06187"));
            float divisorJ = Float.parseFloat(documentosKilos7.getString("K06187"));
            float resJ = dividendoJ/divisorJ;
            cadenaJun = String.valueOf(dfJun.format(resJ));
            PdfPCell celdaJun = new PdfPCell(new Phrase(cadenaJun,cuerpo));
            celdaJun.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJun);
            

            DecimalFormat dfJul = new DecimalFormat("###.00");
            String cadenaJul = "";
            sumaJul = sumaJul + Float.parseFloat(documentosPower7.getString("V07187"));
            float dividendoJul = Float.parseFloat(documentosPower7.getString("V07187"));
            float divisorJul = Float.parseFloat(documentosKilos7.getString("K07187"));
            float resJul = dividendoJul/divisorJul;
            cadenaJul = String.valueOf(dfJul.format(resJul));
            PdfPCell celdaJul = new PdfPCell(new Phrase(cadenaJul,cuerpo));
            celdaJul.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJul);

            DecimalFormat dfAgo = new DecimalFormat("###.00");
            String cadenaAgo = "";
            sumaAgos = sumaAgos + Float.parseFloat(documentosPower7.getString("V08187"));
            float dividendoAgos = Float.parseFloat(documentosPower7.getString("V08187"));
            float divisorAgos = Float.parseFloat(documentosKilos7.getString("K08187"));
            float resAgos = dividendoAgos/divisorAgos;
            cadenaAgo = String.valueOf(dfAgo.format(resAgos));
            PdfPCell celdaAgo = new PdfPCell(new Phrase(cadenaAgo,cuerpo));
            celdaAgo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAgo);

            DecimalFormat dfSep = new DecimalFormat("###.00");
            String cadenaSep = "";
            sumaSep = sumaSep + Float.parseFloat(documentosPower7.getString("V09187"));
            float dividendoS = Float.parseFloat(documentosPower7.getString("V09187"));
            float divisorS = Float.parseFloat(documentosKilos7.getString("K09187"));
            float resS = dividendoS/divisorS;
            cadenaSep = String.valueOf(dfSep.format(resS));
            PdfPCell celdaSep = new PdfPCell(new Phrase(cadenaSep,cuerpo));
            celdaSep.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaSep);
            
            DecimalFormat dfTot = new DecimalFormat("###.00");
            sumaHorizontal = (resE+resF+resM+resA+resMay+resJ+resJul+resAgos+resS)/9;
            sumT = sumT + sumaHorizontal;
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
        }
        
        return tabla; 
   }   
   public static PdfPTable createTableOD(int mes, ResultSet documentosPower7, ResultSet documentosKilos7, String sucursal,int ano) throws DocumentException, SQLException{
       Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(7);
        cuerpo.setSize(6);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(13);
        tabla.setWidths(new float[]{0.5f,1.8f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.8f,1.5f,1.5f});
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("PRECIOS POR KILOS AÑO "+ano+" -"+sucursal+"",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(13);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Precio Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Precio Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Precio Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);

        PdfPCell cellAbr = new PdfPCell(new Paragraph("Precio Abril",fuente));
        cellAbr.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAbr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbr);

        PdfPCell cellMayo = new PdfPCell(new Paragraph("Precio Mayo",fuente));
        cellMayo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMayo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMayo);

        PdfPCell cellJun = new PdfPCell(new Paragraph("Precio Junio",fuente));
        cellJun.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJun.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJun);

        PdfPCell cellJul = new PdfPCell(new Paragraph("Precio Julio",fuente));
        cellJul.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJul.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJul);

        PdfPCell cellAgo = new PdfPCell(new Paragraph("Precio Agosto",fuente));
        cellAgo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAgo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAgo);

        PdfPCell cellSep = new PdfPCell(new Paragraph("Precio Septiembre",fuente));
        cellSep.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellSep.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellSep);

        PdfPCell cellOct = new PdfPCell(new Paragraph("Precio Octubre",fuente));
        cellOct.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellOct.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellOct);
        
        PdfPCell cellT = new PdfPCell(new Paragraph("Promedio kilos",fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumaAbr = 0,sumaMay = 0,sumaJun = 0,sumaJul = 0,sumaAgos = 0,sumaSep = 0,sumaOct = 0,sumT = 0;
        
        while(documentosPower7.next() && documentosKilos7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosPower7.getString("FAM187"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosPower7.getString("DES187"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);

            DecimalFormat dfEne = new DecimalFormat("###.00");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosPower7.getString("V01187"));
            float dividendoE = Float.parseFloat(documentosPower7.getString("V01187"));
            float divisorE = Float.parseFloat(documentosKilos7.getString("K01187"));
            float resE = dividendoE/divisorE;
            cadenaEne = String.valueOf(dfEne.format(resE));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###.00");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosPower7.getString("V02187"));
            float dividendoF = Float.parseFloat(documentosPower7.getString("V02187"));
            float divisorF = Float.parseFloat(documentosKilos7.getString("K02187"));
            float resF = dividendoF/divisorF;
            cadenaFeb = String.valueOf(dfFeb.format(resF));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb);

            DecimalFormat dfMar = new DecimalFormat("###.00");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosPower7.getString("V03187"));
            float dividendoM = Float.parseFloat(documentosPower7.getString("V03187"));
            float divisorM = Float.parseFloat(documentosKilos7.getString("K03187"));
            float resM = dividendoM/divisorM;
            cadenaMar = String.valueOf(dfMar.format(resM));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);

            DecimalFormat dfAbr = new DecimalFormat("###.00");
            String cadenaAbr = "";
            sumaAbr = sumaAbr + Float.parseFloat(documentosPower7.getString("V04187"));
            float dividendoA = Float.parseFloat(documentosPower7.getString("V04187"));
            float divisorA = Float.parseFloat(documentosKilos7.getString("K04187"));
            float resA = dividendoA/divisorA;
            cadenaAbr = String.valueOf(dfAbr.format(resA));
            PdfPCell celdaAbr = new PdfPCell(new Phrase(cadenaAbr,cuerpo));
            celdaAbr.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAbr);

            DecimalFormat dfMayo = new DecimalFormat("###.00");
            String cadenaMayo = "";
            sumaMay = sumaMay + Float.parseFloat(documentosPower7.getString("V05187"));
            float dividendoMAY = Float.parseFloat(documentosPower7.getString("V05187"));
            float divisorMAY = Float.parseFloat(documentosKilos7.getString("K05187"));
            float resMay = dividendoMAY/divisorMAY;
            cadenaMayo = String.valueOf(dfMayo.format(resMay));
            PdfPCell celdaMayo = new PdfPCell(new Phrase(cadenaMayo,cuerpo));
            celdaMayo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMayo);

            DecimalFormat dfJun = new DecimalFormat("###.00");
            String cadenaJun = "";
            sumaJun = sumaJun + Float.parseFloat(documentosPower7.getString("V06187"));
            float dividendoJ = Float.parseFloat(documentosPower7.getString("V06187"));
            float divisorJ = Float.parseFloat(documentosKilos7.getString("K06187"));
            float resJ = dividendoJ/divisorJ;
            cadenaJun = String.valueOf(dfJun.format(resJ));
            PdfPCell celdaJun = new PdfPCell(new Phrase(cadenaJun,cuerpo));
            celdaJun.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJun);
            

            DecimalFormat dfJul = new DecimalFormat("###.00");
            String cadenaJul = "";
            sumaJul = sumaJul + Float.parseFloat(documentosPower7.getString("V07187"));
            float dividendoJul = Float.parseFloat(documentosPower7.getString("V07187"));
            float divisorJul = Float.parseFloat(documentosKilos7.getString("K07187"));
            float resJul = dividendoJul/divisorJul;
            cadenaJul = String.valueOf(dfJul.format(resJul));
            PdfPCell celdaJul = new PdfPCell(new Phrase(cadenaJul,cuerpo));
            celdaJul.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJul);

            DecimalFormat dfAgo = new DecimalFormat("###.00");
            String cadenaAgo = "";
            sumaAgos = sumaAgos + Float.parseFloat(documentosPower7.getString("V08187"));
            float dividendoAgos = Float.parseFloat(documentosPower7.getString("V08187"));
            float divisorAgos = Float.parseFloat(documentosKilos7.getString("K08187"));
            float resAgos = dividendoAgos/divisorAgos;
            cadenaAgo = String.valueOf(dfAgo.format(resAgos));
            PdfPCell celdaAgo = new PdfPCell(new Phrase(cadenaAgo,cuerpo));
            celdaAgo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAgo);

            DecimalFormat dfSep = new DecimalFormat("###.00");
            String cadenaSep = "";
            sumaSep = sumaSep + Float.parseFloat(documentosPower7.getString("V09187"));
            float dividendoS = Float.parseFloat(documentosPower7.getString("V09187"));
            float divisorS = Float.parseFloat(documentosKilos7.getString("K09187"));
            float resS = dividendoS/divisorS;
            cadenaSep = String.valueOf(dfSep.format(resS));
            PdfPCell celdaSep = new PdfPCell(new Phrase(cadenaSep,cuerpo));
            celdaSep.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaSep);

            DecimalFormat dfOct = new DecimalFormat("###.00");
            String cadenaOct = "";
            sumaOct = sumaOct + Float.parseFloat(documentosPower7.getString("V10187"));
            float dividendoO = Float.parseFloat(documentosPower7.getString("V10187"));
            float divisorO = Float.parseFloat(documentosKilos7.getString("K10187"));
            float resO = dividendoO/divisorO;
            cadenaOct = String.valueOf(dfOct.format(resO));
            PdfPCell celdaOct = new PdfPCell(new Phrase(cadenaOct,cuerpo));
            celdaOct.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaOct);
            
            DecimalFormat dfTot = new DecimalFormat("###.00");
            sumaHorizontal = (resE+resF+resM+resA+resMay+resJ+resJul+resAgos+resS+resO)/10;
            sumT = sumT + sumaHorizontal;
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
        } 
        
        return tabla;
   }   
   public static PdfPTable createTableND(int mes, ResultSet documentosPower7, ResultSet documentosKilos7, String sucursal,int ano) throws DocumentException, SQLException{
      Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(6);
        cuerpo.setSize(5);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(14);
        tabla.setWidths(new float[]{0.45f,1.5f,1.2f,1.3f,1.2f,1.2f,1.1f,1.2f,1.2f,1.3f,1.2f,1.2f,1.5f,1.5f});
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("PRECIOS POR KILOS AÑO "+ano+" - "+sucursal+"",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(14);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Precio Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Precio Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Precio Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);

        PdfPCell cellAbr = new PdfPCell(new Paragraph("Precio Abril",fuente));
        cellAbr.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAbr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbr);

        PdfPCell cellMayo = new PdfPCell(new Paragraph("Precio Mayo",fuente));
        cellMayo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMayo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMayo);

        PdfPCell cellJun = new PdfPCell(new Paragraph("Precio Junio",fuente));
        cellJun.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJun.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJun);

        PdfPCell cellJul = new PdfPCell(new Paragraph("Precio Julio",fuente));
        cellJul.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJul.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJul);

        PdfPCell cellAgo = new PdfPCell(new Paragraph("Precio Agosto",fuente));
        cellAgo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAgo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAgo);

        PdfPCell cellSep = new PdfPCell(new Paragraph("Precio Septiembre",fuente));
        cellSep.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellSep.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellSep);

        PdfPCell cellOct = new PdfPCell(new Paragraph("Precio Octubre",fuente));
        cellOct.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellOct.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellOct);

        PdfPCell cellNov = new PdfPCell(new Paragraph("Precio Noviembre",fuente));
        cellNov.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellNov.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellNov);
        
        PdfPCell cellT = new PdfPCell(new Paragraph("Promedio kilos",fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumaAbr = 0,sumaMay = 0,sumaJun = 0,sumaJul = 0,sumaAgos = 0,sumaSep = 0,sumaOct = 0,sumaNov = 0,sumT = 0;
        
        while(documentosPower7.next() && documentosKilos7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosPower7.getString("FAM187"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosPower7.getString("DES187"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);

            DecimalFormat dfEne = new DecimalFormat("###.00");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosPower7.getString("V01187"));
            float dividendoE = Float.parseFloat(documentosPower7.getString("V01187"));
            float divisorE = Float.parseFloat(documentosKilos7.getString("K01187"));
            float resE = dividendoE/divisorE;
            cadenaEne = String.valueOf(dfEne.format(resE));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###.00");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosPower7.getString("V02187"));
            float dividendoF = Float.parseFloat(documentosPower7.getString("V02187"));
            float divisorF = Float.parseFloat(documentosKilos7.getString("K02187"));
            float resF = dividendoF/divisorF;
            cadenaFeb = String.valueOf(dfFeb.format(resF));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb);

            DecimalFormat dfMar = new DecimalFormat("###.00");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosPower7.getString("V03187"));
            float dividendoM = Float.parseFloat(documentosPower7.getString("V03187"));
            float divisorM = Float.parseFloat(documentosKilos7.getString("K03187"));
            float resM = dividendoM/divisorM;
            cadenaMar = String.valueOf(dfMar.format(resM));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);

            DecimalFormat dfAbr = new DecimalFormat("###.00");
            String cadenaAbr = "";
            sumaAbr = sumaAbr + Float.parseFloat(documentosPower7.getString("V04187"));
            float dividendoA = Float.parseFloat(documentosPower7.getString("V04187"));
            float divisorA = Float.parseFloat(documentosKilos7.getString("K04187"));
            float resA = dividendoA/divisorA;
            cadenaAbr = String.valueOf(dfAbr.format(resA));
            PdfPCell celdaAbr = new PdfPCell(new Phrase(cadenaAbr,cuerpo));
            celdaAbr.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAbr);

            DecimalFormat dfMayo = new DecimalFormat("###.00");
            String cadenaMayo = "";
            sumaMay = sumaMay + Float.parseFloat(documentosPower7.getString("V05187"));
            float dividendoMAY = Float.parseFloat(documentosPower7.getString("V05187"));
            float divisorMAY = Float.parseFloat(documentosKilos7.getString("K05187"));
            float resMay = dividendoMAY/divisorMAY;
            cadenaMayo = String.valueOf(dfMayo.format(resMay));
            PdfPCell celdaMayo = new PdfPCell(new Phrase(cadenaMayo,cuerpo));
            celdaMayo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMayo);

            DecimalFormat dfJun = new DecimalFormat("###.00");
            String cadenaJun = "";
            sumaJun = sumaJun + Float.parseFloat(documentosPower7.getString("V06187"));
            float dividendoJ = Float.parseFloat(documentosPower7.getString("V06187"));
            float divisorJ = Float.parseFloat(documentosKilos7.getString("K06187"));
            float resJ = dividendoJ/divisorJ;
            cadenaJun = String.valueOf(dfJun.format(resJ));
            PdfPCell celdaJun = new PdfPCell(new Phrase(cadenaJun,cuerpo));
            celdaJun.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJun);
            

            DecimalFormat dfJul = new DecimalFormat("###.00");
            String cadenaJul = "";
            sumaJul = sumaJul + Float.parseFloat(documentosPower7.getString("V07187"));
            float dividendoJul = Float.parseFloat(documentosPower7.getString("V07187"));
            float divisorJul = Float.parseFloat(documentosKilos7.getString("K07187"));
            float resJul = dividendoJul/divisorJul;
            cadenaJul = String.valueOf(dfJul.format(resJul));
            PdfPCell celdaJul = new PdfPCell(new Phrase(cadenaJul,cuerpo));
            celdaJul.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJul);

            DecimalFormat dfAgo = new DecimalFormat("###.00");
            String cadenaAgo = "";
            sumaAgos = sumaAgos + Float.parseFloat(documentosPower7.getString("V08187"));
            float dividendoAgos = Float.parseFloat(documentosPower7.getString("V08187"));
            float divisorAgos = Float.parseFloat(documentosKilos7.getString("K08187"));
            float resAgos = dividendoAgos/divisorAgos;
            cadenaAgo = String.valueOf(dfAgo.format(resAgos));
            PdfPCell celdaAgo = new PdfPCell(new Phrase(cadenaAgo,cuerpo));
            celdaAgo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAgo);

            DecimalFormat dfSep = new DecimalFormat("###.00");
            String cadenaSep = "";
            sumaSep = sumaSep + Float.parseFloat(documentosPower7.getString("V09187"));
            float dividendoS = Float.parseFloat(documentosPower7.getString("V09187"));
            float divisorS = Float.parseFloat(documentosKilos7.getString("K09187"));
            float resS = dividendoS/divisorS;
            cadenaSep = String.valueOf(dfSep.format(resS));
            PdfPCell celdaSep = new PdfPCell(new Phrase(cadenaSep,cuerpo));
            celdaSep.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaSep);

            DecimalFormat dfOct = new DecimalFormat("###.00");
            String cadenaOct = "";
            sumaOct = sumaOct + Float.parseFloat(documentosPower7.getString("V10187"));
            float dividendoO = Float.parseFloat(documentosPower7.getString("V10187"));
            float divisorO = Float.parseFloat(documentosKilos7.getString("K10187"));
            float resO = dividendoO/divisorO;
            cadenaOct = String.valueOf(dfOct.format(resO));
            PdfPCell celdaOct = new PdfPCell(new Phrase(cadenaOct,cuerpo));
            celdaOct.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaOct);

            DecimalFormat dfNov = new DecimalFormat("###.00");
            String cadenaNov = "";
            sumaNov = sumaNov + Float.parseFloat(documentosPower7.getString("V11187"));
            float dividendoN = Float.parseFloat(documentosPower7.getString("V11187"));
            float divisorN = Float.parseFloat(documentosKilos7.getString("K11187"));
            float resN = dividendoN/divisorN;
            cadenaNov = String.valueOf(dfNov.format(resN));
            PdfPCell celdaNov = new PdfPCell(new Phrase(cadenaNov,cuerpo));
            celdaNov.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaNov);
            
            DecimalFormat dfTot = new DecimalFormat("###.00");
            sumaHorizontal = (resE+resF+resM+resA+resMay+resJ+resJul+resAgos+resS+resO+resN)/11;
            sumT = sumT + sumaHorizontal;
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
            
        }
        
        return tabla; 
   }  
   public static PdfPTable createTableDD(int mes, ResultSet documentosPower7, ResultSet documentosKilos7, String sucursal,int ano) throws DocumentException, SQLException{
       Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(6);
        cuerpo.setSize(5);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(15);
        tabla.setWidths(new float[]{0.5f,1.8f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.8f,1.5f,1.5f,1.5f,1.8f});
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("PRECIOS POR KILOS AÑO "+ano+" - "+sucursal+"",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(15);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Precio Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Precio Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Precio Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);

        PdfPCell cellAbr = new PdfPCell(new Paragraph("Precio Abril",fuente));
        cellAbr.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAbr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbr);

        PdfPCell cellMayo = new PdfPCell(new Paragraph("Precio Mayo",fuente));
        cellMayo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMayo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMayo);

        PdfPCell cellJun = new PdfPCell(new Paragraph("Precio Junio",fuente));
        cellJun.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJun.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJun);

        PdfPCell cellJul = new PdfPCell(new Paragraph("Precio Julio",fuente));
        cellJul.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJul.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJul);

        PdfPCell cellAgo = new PdfPCell(new Paragraph("Precio Agosto",fuente));
        cellAgo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAgo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAgo);

        PdfPCell cellSep = new PdfPCell(new Paragraph("Precio Septiembre",fuente));
        cellSep.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellSep.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellSep);

        PdfPCell cellOct = new PdfPCell(new Paragraph("Precio Octubre",fuente));
        cellOct.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellOct.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellOct);

        PdfPCell cellNov = new PdfPCell(new Paragraph("Precio Noviembre",fuente));
        cellNov.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellNov.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellNov);

        PdfPCell cellDic = new PdfPCell(new Paragraph("Precio Diciembre",fuente));
        cellDic.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDic.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDic);
        
        PdfPCell cellT = new PdfPCell(new Paragraph("Promedio kilos",fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumaAbr = 0,sumaMay = 0,sumaJun = 0,sumaJul = 0,sumaAgos = 0,sumaSep = 0,sumaOct = 0,sumaNov = 0,sumaDic = 0,sumT = 0;

         while(documentosPower7.next() && documentosKilos7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosPower7.getString("FAM187"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosPower7.getString("DES187"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);

            DecimalFormat dfEne = new DecimalFormat("###.00");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosPower7.getString("V01187"));
            float dividendoE = Float.parseFloat(documentosPower7.getString("V01187"));
            float divisorE = Float.parseFloat(documentosKilos7.getString("K01187"));
            float resE = dividendoE/divisorE;
            cadenaEne = String.valueOf(dfEne.format(resE));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###.00");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosPower7.getString("V02187"));
            float dividendoF = Float.parseFloat(documentosPower7.getString("V02187"));
            float divisorF = Float.parseFloat(documentosKilos7.getString("K02187"));
            float resF = dividendoF/divisorF;
            cadenaFeb = String.valueOf(dfFeb.format(resF));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb);

            DecimalFormat dfMar = new DecimalFormat("###.00");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosPower7.getString("V03187"));
            float dividendoM = Float.parseFloat(documentosPower7.getString("V03187"));
            float divisorM = Float.parseFloat(documentosKilos7.getString("K03187"));
            float resM = dividendoM/divisorM;
            cadenaMar = String.valueOf(dfMar.format(resM));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);

            DecimalFormat dfAbr = new DecimalFormat("###.00");
            String cadenaAbr = "";
            sumaAbr = sumaAbr + Float.parseFloat(documentosPower7.getString("V04187"));
            float dividendoA = Float.parseFloat(documentosPower7.getString("V04187"));
            float divisorA = Float.parseFloat(documentosKilos7.getString("K04187"));
            float resA = dividendoA/divisorA;
            cadenaAbr = String.valueOf(dfAbr.format(resA));
            PdfPCell celdaAbr = new PdfPCell(new Phrase(cadenaAbr,cuerpo));
            celdaAbr.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAbr);

            DecimalFormat dfMayo = new DecimalFormat("###.00");
            String cadenaMayo = "";
            sumaMay = sumaMay + Float.parseFloat(documentosPower7.getString("V05187"));
            float dividendoMAY = Float.parseFloat(documentosPower7.getString("V05187"));
            float divisorMAY = Float.parseFloat(documentosKilos7.getString("K05187"));
            float resMay = dividendoMAY/divisorMAY;
            cadenaMayo = String.valueOf(dfMayo.format(resMay));
            PdfPCell celdaMayo = new PdfPCell(new Phrase(cadenaMayo,cuerpo));
            celdaMayo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMayo);

            DecimalFormat dfJun = new DecimalFormat("###.00");
            String cadenaJun = "";
            sumaJun = sumaJun + Float.parseFloat(documentosPower7.getString("V06187"));
            float dividendoJ = Float.parseFloat(documentosPower7.getString("V06187"));
            float divisorJ = Float.parseFloat(documentosKilos7.getString("K06187"));
            float resJ = dividendoJ/divisorJ;
            cadenaJun = String.valueOf(dfJun.format(resJ));
            PdfPCell celdaJun = new PdfPCell(new Phrase(cadenaJun,cuerpo));
            celdaJun.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJun);
            

            DecimalFormat dfJul = new DecimalFormat("###.00");
            String cadenaJul = "";
            sumaJul = sumaJul + Float.parseFloat(documentosPower7.getString("V07187"));
            float dividendoJul = Float.parseFloat(documentosPower7.getString("V07187"));
            float divisorJul = Float.parseFloat(documentosKilos7.getString("K07187"));
            float resJul = dividendoJul/divisorJul;
            cadenaJul = String.valueOf(dfJul.format(resJul));
            PdfPCell celdaJul = new PdfPCell(new Phrase(cadenaJul,cuerpo));
            celdaJul.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJul);

            DecimalFormat dfAgo = new DecimalFormat("###.00");
            String cadenaAgo = "";
            sumaAgos = sumaAgos + Float.parseFloat(documentosPower7.getString("V08187"));
            float dividendoAgos = Float.parseFloat(documentosPower7.getString("V08187"));
            float divisorAgos = Float.parseFloat(documentosKilos7.getString("K08187"));
            float resAgos = dividendoAgos/divisorAgos;
            cadenaAgo = String.valueOf(dfAgo.format(resAgos));
            PdfPCell celdaAgo = new PdfPCell(new Phrase(cadenaAgo,cuerpo));
            celdaAgo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAgo);

            DecimalFormat dfSep = new DecimalFormat("###.00");
            String cadenaSep = "";
            sumaSep = sumaSep + Float.parseFloat(documentosPower7.getString("V09187"));
            float dividendoS = Float.parseFloat(documentosPower7.getString("V09187"));
            float divisorS = Float.parseFloat(documentosKilos7.getString("K09187"));
            float resS = dividendoS/divisorS;
            cadenaSep = String.valueOf(dfSep.format(resS));
            PdfPCell celdaSep = new PdfPCell(new Phrase(cadenaSep,cuerpo));
            celdaSep.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaSep);

            DecimalFormat dfOct = new DecimalFormat("###.00");
            String cadenaOct = "";
            sumaOct = sumaOct + Float.parseFloat(documentosPower7.getString("V10187"));
            float dividendoO = Float.parseFloat(documentosPower7.getString("V10187"));
            float divisorO = Float.parseFloat(documentosKilos7.getString("K10187"));
            float resO = dividendoO/divisorO;
            cadenaOct = String.valueOf(dfOct.format(resO));
            PdfPCell celdaOct = new PdfPCell(new Phrase(cadenaOct,cuerpo));
            celdaOct.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaOct);

            DecimalFormat dfNov = new DecimalFormat("###.00");
            String cadenaNov = "";
            sumaNov = sumaNov + Float.parseFloat(documentosPower7.getString("V11187"));
            float dividendoN = Float.parseFloat(documentosPower7.getString("V11187"));
            float divisorN = Float.parseFloat(documentosKilos7.getString("K11187"));
            float resN = dividendoN/divisorN;
            cadenaNov = String.valueOf(dfNov.format(resN));
            PdfPCell celdaNov = new PdfPCell(new Phrase(cadenaNov,cuerpo));
            celdaNov.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaNov);

            DecimalFormat dfDic = new DecimalFormat("###.00");
            String cadenaDic = "";
            sumaDic = sumaDic + Float.parseFloat(documentosPower7.getString("V12187"));
            float dividendoD = Float.parseFloat(documentosPower7.getString("V12187"));
            float divisorD = Float.parseFloat(documentosKilos7.getString("K12187"));
            float resD = dividendoD/divisorD;
            cadenaDic = String.valueOf(dfDic.format(resD));
            PdfPCell celdaDic = new PdfPCell(new Phrase(cadenaDic,cuerpo));
            celdaDic.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaDic);
            
            DecimalFormat dfTot = new DecimalFormat("###.00");
            sumaHorizontal = (resE+resF+resM+resA+resMay+resJ+resJul+resAgos+resS+resO+resN+resD)/12;
            sumT = sumT + sumaHorizontal;
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
        }
         
        return tabla;
   }
    
   public static PdfPTable createTableEneroV(int mes,ResultSet documentosPower7,String sucursal,int ano) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(9);
        cuerpo.setSize(8);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(4);
        tabla.setWidths(new float[]{0.01f,0.1f,0.1f,0.1f});
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("VENTA EN PESOS AÑO "+ano+" - "+sucursal+"",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(4);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Ventas $ Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);
        
        PdfPCell cellTot = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellTot.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTot.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTot);
        
        
        int j=1;
        float sumaEnero = 0,sumaT = 0;
        float sumaHorizontal = 0;
        while(documentosPower7.next()){
                PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosPower7.getString("FAM187"),cuerpo));
                celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
                tabla.addCell(celdaFAM);

                PdfPCell celdaDES = new PdfPCell(new Phrase(documentosPower7.getString("DES187"),cuerpo));
                celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
                tabla.addCell(celdaDES);


                DecimalFormat df = new DecimalFormat("###,###,###.00");
                String cadena = "";
                sumaEnero = sumaEnero + Float.parseFloat(documentosPower7.getString("V01187"));
                
                cadena = String.valueOf(df.format(Float.parseFloat(documentosPower7.getString("V01187"))));
                PdfPCell celda = new PdfPCell(new Phrase(cadena,cuerpo));
                celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                tabla.addCell(celda);
                
                DecimalFormat dfTot = new DecimalFormat("###,###,###.00");
                sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosPower7.getString("V01187"));
                sumaT = sumaT + sumaHorizontal;
                PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                tabla.addCell(cell);
                sumaHorizontal = 0;
                
        }
        PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.00");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.00");
        PdfPCell cellT = new PdfPCell(new Phrase(df.format(sumaT),fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        
        return tabla;
   }  
   public static PdfPTable createTableEFV(int mes,ResultSet documentosPower7,String sucursal,int ano) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(9);
        cuerpo.setSize(8);
        fuente.setStyle(Font.BOLD);
        
        
        
        PdfPTable tabla = new PdfPTable(5);
        tabla.setWidths(new float[]{0.015f,0.1f,0.1f,0.1f,0.1f});
        
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("VENTA EN PESOS AÑO "+ano+" - "+sucursal+"",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(5);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Ventas $ Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Ventas $ Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);
        
        PdfPCell cellTot = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellTot.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTot.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTot);
        
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaT = 0;
        while(documentosPower7.next()){
                PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosPower7.getString("FAM187"),cuerpo));
                celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
                tabla.addCell(celdaFAM);
                
                PdfPCell celdaDES = new PdfPCell(new Phrase(documentosPower7.getString("DES187"),cuerpo));
                celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
                tabla.addCell(celdaDES);


                DecimalFormat dfEne = new DecimalFormat("###,###,###.00");
                String cadenaEne = "";
                sumaEnero = sumaEnero + Float.parseFloat(documentosPower7.getString("V01187"));
                cadenaEne = String.valueOf(dfEne.format(Float.parseFloat(documentosPower7.getString("V01187"))));
                PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
                celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
                tabla.addCell(celdaEne);

                DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
                String cadenaFeb = "";
                sumaFeb = sumaFeb + Float.parseFloat(documentosPower7.getString("V02187"));
                cadenaFeb = String.valueOf(dfFeb.format(Float.parseFloat(documentosPower7.getString("V02187"))));
                PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
                celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
                tabla.addCell(celdaFeb);
                
                DecimalFormat dfTot = new DecimalFormat("###,###,###.00");
                sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosPower7.getString("V01187"))+
                                                  Float.parseFloat(documentosPower7.getString("V02187"));
                sumaT = sumaT + sumaHorizontal;
                PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                tabla.addCell(cell);
                sumaHorizontal = 0;
        }    
        PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.00");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
        PdfPCell cellFebr = new PdfPCell(new Phrase(dfFeb.format(sumaFeb),fuente));
        cellFebr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellFebr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFebr);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.00");
        PdfPCell cellT = new PdfPCell(new Phrase(dfT.format(sumaT),fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        return tabla;
   }
   public static PdfPTable createTableEFMV(int mes,ResultSet documentosPower7,String sucursal,int ano) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(9);
        cuerpo.setSize(8);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(6);
        tabla.setWidths(new float[]{0.017f,0.1f,0.1f,0.1f,0.1f,0.1f});
        
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("VENTA EN PESOS AÑO "+ano+" - "+sucursal+"",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(6);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Ventas $ Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Ventas $ Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Ventas $ Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);
        
        PdfPCell cellTot = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellTot.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTot.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTot);
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumT = 0;
         while(documentosPower7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosPower7.getString("FAM187"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosPower7.getString("DES187"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);


            DecimalFormat dfEne = new DecimalFormat("###,###,###.00");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosPower7.getString("V01187"));
            cadenaEne = String.valueOf(dfEne.format(Float.parseFloat(documentosPower7.getString("V01187"))));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosPower7.getString("V02187"));
            cadenaFeb = String.valueOf(dfFeb.format(Float.parseFloat(documentosPower7.getString("V02187"))));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb); 

            DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosPower7.getString("V03187"));
            cadenaMar = String.valueOf(dfMar.format(Float.parseFloat(documentosPower7.getString("V03187"))));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);
            
            DecimalFormat dfTot = new DecimalFormat("###,###,###.00");
            sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosPower7.getString("V01187"))+
                                              Float.parseFloat(documentosPower7.getString("V02187"))+
                                              Float.parseFloat(documentosPower7.getString("V03187"));
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            sumT = sumT + sumaHorizontal;
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
        }
        PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.00");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
        PdfPCell cellFebr = new PdfPCell(new Phrase(dfFeb.format(sumaFeb),fuente));
        cellFebr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellFebr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFebr);
        
        DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
        PdfPCell cellMarz = new PdfPCell(new Phrase(dfMar.format(sumaMar),fuente));
        cellMarz.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMarz.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMarz);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.00");
        PdfPCell cellT = new PdfPCell(new Phrase(dfT.format(sumT),fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        return tabla;
   }
   public static PdfPTable createTableEFMAV(int mes,ResultSet documentosPower7,String sucursal,int ano) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(9);
        cuerpo.setSize(8);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(7);
        tabla.setWidths(new float[]{0.02f,0.1f,0.1f,0.1f,0.1f,0.1f,0.1f});
        
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("VENTA EN PESOS AÑO "+ano+" - "+sucursal+"",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(7);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Ventas $ Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Ventas $ Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Ventas $ Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);

        PdfPCell cellAbr = new PdfPCell(new Paragraph("Ventas $ Abril",fuente));
        cellAbr.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAbr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbr);
        
        PdfPCell cellTot = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellTot.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTot.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTot);
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumaAbr = 0,sumT = 0;
        
        while(documentosPower7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosPower7.getString("FAM187"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosPower7.getString("DES187"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);


            DecimalFormat dfEne = new DecimalFormat("###,###,###.00");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosPower7.getString("V01187"));
            cadenaEne = String.valueOf(dfEne.format(Float.parseFloat(documentosPower7.getString("V01187"))));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosPower7.getString("V02187"));
            cadenaFeb = String.valueOf(dfFeb.format(Float.parseFloat(documentosPower7.getString("V02187"))));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb); 

            DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosPower7.getString("V03187"));
            cadenaMar = String.valueOf(dfMar.format(Float.parseFloat(documentosPower7.getString("V03187"))));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);

            DecimalFormat dfAbr = new DecimalFormat("###,###,###.00");
            String cadenaAbr = "";
            sumaAbr = sumaAbr + Float.parseFloat(documentosPower7.getString("V04187"));
            cadenaAbr = String.valueOf(dfAbr.format(Float.parseFloat(documentosPower7.getString("V04187"))));
            PdfPCell celdaAbr = new PdfPCell(new Phrase(cadenaAbr,cuerpo));
            celdaAbr.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAbr);
            
            DecimalFormat dfTot = new DecimalFormat("###,###,###.00");
            sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosPower7.getString("V01187"))+
                                              Float.parseFloat(documentosPower7.getString("V02187"))+
                                              Float.parseFloat(documentosPower7.getString("V03187"))+
                                              Float.parseFloat(documentosPower7.getString("V04187"));
            sumT = sumT = sumaHorizontal;
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
        }
        PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.00");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
        PdfPCell cellFebr = new PdfPCell(new Phrase(dfFeb.format(sumaFeb),fuente));
        cellFebr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellFebr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFebr);
        
        DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
        PdfPCell cellMarz = new PdfPCell(new Phrase(dfMar.format(sumaMar),fuente));
        cellMarz.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMarz.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMarz);
        
        DecimalFormat dfAbri = new DecimalFormat("###,###,###.00");
        PdfPCell cellAbri = new PdfPCell(new Phrase(dfAbri.format(sumaAbr),fuente));
        cellAbri.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAbri.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbri);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.00");
        PdfPCell cellT = new PdfPCell(new Phrase(dfT.format(sumT),fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        return tabla;
   }
   public static PdfPTable createTableEFMAMV(int mes,ResultSet documentosPower7,String sucursal,int ano) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(9);
        cuerpo.setSize(8);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(8);
        tabla.setWidths(new float[]{0.023f,0.1f,0.1f,0.1f,0.1f,0.1f,0.1f,0.1f});
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("VENTA EN PESOS AÑO "+ano+" - "+sucursal+"",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(8);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Ventas $ Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Ventas $ Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Ventas $ Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);

        PdfPCell cellAbr = new PdfPCell(new Paragraph("Ventas $ Abril",fuente));
        cellAbr.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAbr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbr);

        PdfPCell cellMayo = new PdfPCell(new Paragraph("Ventas $ Mayo",fuente));
        cellMayo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMayo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMayo);
        
        PdfPCell cellTot = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellTot.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTot.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTot);
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumaAbr = 0,sumaMay = 0,sumT = 0;
        
        while(documentosPower7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosPower7.getString("FAM187"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosPower7.getString("DES187"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);


            DecimalFormat dfEne = new DecimalFormat("###,###,###.00");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosPower7.getString("V01187"));
            cadenaEne = String.valueOf(dfEne.format(Float.parseFloat(documentosPower7.getString("V01187"))));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosPower7.getString("V02187"));
            cadenaFeb = String.valueOf(dfFeb.format(Float.parseFloat(documentosPower7.getString("V02187"))));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb); 

            DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosPower7.getString("V03187"));
            cadenaMar = String.valueOf(dfMar.format(Float.parseFloat(documentosPower7.getString("V03187"))));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);

            DecimalFormat dfAbr = new DecimalFormat("###,###,###.00");
            String cadenaAbr = "";
            sumaAbr = sumaAbr + Float.parseFloat(documentosPower7.getString("V04187"));
            cadenaAbr = String.valueOf(dfAbr.format(Float.parseFloat(documentosPower7.getString("V04187"))));
            PdfPCell celdaAbr = new PdfPCell(new Phrase(cadenaAbr,cuerpo));
            celdaAbr.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAbr);

            DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
            String cadenaMayo = "";
            sumaMay = sumaMay + Float.parseFloat(documentosPower7.getString("V05187"));
            cadenaMayo = String.valueOf(dfMayo.format(Float.parseFloat(documentosPower7.getString("V05187"))));
            PdfPCell celdaMayo = new PdfPCell(new Phrase(cadenaMayo,cuerpo));
            celdaMayo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMayo);
            
            DecimalFormat dfTot = new DecimalFormat("###,###,###.00");
            sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosPower7.getString("V01187"))+
                                              Float.parseFloat(documentosPower7.getString("V02187"))+
                                              Float.parseFloat(documentosPower7.getString("V03187"))+
                                              Float.parseFloat(documentosPower7.getString("V04187"))+
                                              Float.parseFloat(documentosPower7.getString("V05187"));
            sumT = sumT + sumaHorizontal;
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
        }
        PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.00");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
        PdfPCell cellFebr = new PdfPCell(new Phrase(dfFeb.format(sumaFeb),fuente));
        cellFebr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellFebr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFebr);
        
        DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
        PdfPCell cellMarz = new PdfPCell(new Phrase(dfMar.format(sumaMar),fuente));
        cellMarz.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMarz.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMarz);
        
        DecimalFormat dfAbri = new DecimalFormat("###,###,###.00");
        PdfPCell cellAbri = new PdfPCell(new Phrase(dfAbri.format(sumaAbr),fuente));
        cellAbri.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAbri.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbri);
        
        DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
        PdfPCell cellMay = new PdfPCell(new Phrase(dfMayo.format(sumaMay),fuente));
        cellMay.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMay.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMay);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.00");
        PdfPCell cellT = new PdfPCell(new Phrase(dfT.format(sumT),fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        return tabla;
   }
   public static PdfPTable createTableEFMAMJV(int mes,ResultSet documentosPower7,String sucursal,int ano) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(9);
        cuerpo.setSize(8);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(9);
        tabla.setWidths(new float[]{0.028f,0.11f,0.1f,0.1f,0.1f,0.1f,0.1f,0.1f,0.1f});
        
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("VENTA EN PESOS AÑO "+ano+" - "+sucursal+"",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(9);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Ventas $ Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Ventas $ Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Ventas $ Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);

        PdfPCell cellAbr = new PdfPCell(new Paragraph("Ventas $ Abril",fuente));
        cellAbr.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAbr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbr);

        PdfPCell cellMayo = new PdfPCell(new Paragraph("Ventas $ Mayo",fuente));
        cellMayo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMayo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMayo);

        PdfPCell cellJun = new PdfPCell(new Paragraph("Ventas $ Junio",fuente));
        cellJun.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJun.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJun);
        
        PdfPCell cellTot = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellTot.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTot.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTot);
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumaAbr = 0,sumaMay = 0,sumaJun = 0,sumT = 0;
        while(documentosPower7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosPower7.getString("FAM187"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosPower7.getString("DES187"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);


            DecimalFormat dfEne = new DecimalFormat("###,###,###.00");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosPower7.getString("V01187"));
            cadenaEne = String.valueOf(dfEne.format(Float.parseFloat(documentosPower7.getString("V01187"))));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosPower7.getString("V02187"));
            cadenaFeb = String.valueOf(dfFeb.format(Float.parseFloat(documentosPower7.getString("V02187"))));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb); 

            DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosPower7.getString("V03187"));
            cadenaMar = String.valueOf(dfMar.format(Float.parseFloat(documentosPower7.getString("V03187"))));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);

            DecimalFormat dfAbr = new DecimalFormat("###,###,###.00");
            String cadenaAbr = "";
            sumaAbr = sumaAbr + Float.parseFloat(documentosPower7.getString("V04187"));
            cadenaAbr = String.valueOf(dfAbr.format(Float.parseFloat(documentosPower7.getString("V04187"))));
            PdfPCell celdaAbr = new PdfPCell(new Phrase(cadenaAbr,cuerpo));
            celdaAbr.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAbr);

            DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
            String cadenaMayo = "";
            sumaMay = sumaMay + Float.parseFloat(documentosPower7.getString("V05187"));
            cadenaMayo = String.valueOf(dfMayo.format(Float.parseFloat(documentosPower7.getString("V05187"))));
            PdfPCell celdaMayo = new PdfPCell(new Phrase(cadenaMayo,cuerpo));
            celdaMayo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMayo);

            DecimalFormat dfJun = new DecimalFormat("###,###,###.00");
            String cadenaJun = "";
            sumaJun = sumaJun + Float.parseFloat(documentosPower7.getString("V06187"));
            cadenaJun = String.valueOf(dfJun.format(Float.parseFloat(documentosPower7.getString("V06187"))));
            PdfPCell celdaJun = new PdfPCell(new Phrase(cadenaJun,cuerpo));
            celdaJun.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJun);
            
            DecimalFormat dfTot = new DecimalFormat("###,###,###.00");
            sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosPower7.getString("V01187"))+
                                              Float.parseFloat(documentosPower7.getString("V02187"))+
                                              Float.parseFloat(documentosPower7.getString("V03187"))+
                                              Float.parseFloat(documentosPower7.getString("V04187"))+
                                              Float.parseFloat(documentosPower7.getString("V05187"))+
                                              Float.parseFloat(documentosPower7.getString("V06187"));
            sumT = sumT + sumaHorizontal;
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
        }
        PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.00");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
        PdfPCell cellFebr = new PdfPCell(new Phrase(dfFeb.format(sumaFeb),fuente));
        cellFebr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellFebr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFebr);
        
        DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
        PdfPCell cellMarz = new PdfPCell(new Phrase(dfMar.format(sumaMar),fuente));
        cellMarz.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMarz.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMarz);
        
        DecimalFormat dfAbri = new DecimalFormat("###,###,###.00");
        PdfPCell cellAbri = new PdfPCell(new Phrase(dfAbri.format(sumaAbr),fuente));
        cellAbri.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAbri.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbri);
        
        DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
        PdfPCell cellMay = new PdfPCell(new Phrase(dfMayo.format(sumaMay),fuente));
        cellMay.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMay.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMay);
        
        DecimalFormat dfJunio = new DecimalFormat("###,###,###.00");
        PdfPCell cellJunio = new PdfPCell(new Phrase(dfJunio.format(sumaJun),fuente));
        cellJunio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJunio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJunio);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.00");
        PdfPCell cellT = new PdfPCell(new Phrase(dfT.format(sumT),fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        return tabla;
   }
   public static PdfPTable createTableEFMAMJJV(int mes,ResultSet documentosPower7,String sucursal,int ano) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(9);
        cuerpo.setSize(8);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(10);
        tabla.setWidths(new float[]{0.033f,0.13f,0.1f,0.11f,0.1f,0.1f,0.1f,0.1f,0.1f,0.12f});
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("VENTA EN PESOS AÑO "+ano+" - "+sucursal+"",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(10);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Ventas $ Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Ventas $ Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Ventas $ Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);

        PdfPCell cellAbr = new PdfPCell(new Paragraph("Ventas $ Abril",fuente));
        cellAbr.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAbr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbr);

        PdfPCell cellMayo = new PdfPCell(new Paragraph("Ventas $ Mayo",fuente));
        cellMayo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMayo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMayo);

        PdfPCell cellJun = new PdfPCell(new Paragraph("Ventas $ Junio",fuente));
        cellJun.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJun.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJun);

        PdfPCell cellJul = new PdfPCell(new Paragraph("Ventas $ Julio",fuente));
        cellJul.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJul.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJul);
        
        PdfPCell cellTot = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellTot.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTot.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTot);
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumaAbr = 0,sumaMay = 0,sumaJun = 0,sumaJul = 0,sumT = 0;
        
        while(documentosPower7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosPower7.getString("FAM187"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosPower7.getString("DES187"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);


            DecimalFormat dfEne = new DecimalFormat("###,###,###.00");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosPower7.getString("V01187"));
            cadenaEne = String.valueOf(dfEne.format(Float.parseFloat(documentosPower7.getString("V01187"))));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosPower7.getString("V02187"));
            cadenaFeb = String.valueOf(dfFeb.format(Float.parseFloat(documentosPower7.getString("V02187"))));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb); 

            DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosPower7.getString("V03187"));
            cadenaMar = String.valueOf(dfMar.format(Float.parseFloat(documentosPower7.getString("V03187"))));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);

            DecimalFormat dfAbr = new DecimalFormat("###,###,###.00");
            String cadenaAbr = "";
            sumaAbr = sumaAbr + Float.parseFloat(documentosPower7.getString("V04187"));
            cadenaAbr = String.valueOf(dfAbr.format(Float.parseFloat(documentosPower7.getString("V04187"))));
            PdfPCell celdaAbr = new PdfPCell(new Phrase(cadenaAbr,cuerpo));
            celdaAbr.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAbr);

            DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
            String cadenaMayo = "";
            sumaMay = sumaMay + Float.parseFloat(documentosPower7.getString("V05187"));
            cadenaMayo = String.valueOf(dfMayo.format(Float.parseFloat(documentosPower7.getString("V05187"))));
            PdfPCell celdaMayo = new PdfPCell(new Phrase(cadenaMayo,cuerpo));
            celdaMayo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMayo);

            DecimalFormat dfJun = new DecimalFormat("###,###,###.00");
            String cadenaJun = "";
            sumaJun = sumaJun + Float.parseFloat(documentosPower7.getString("V06187"));
            cadenaJun = String.valueOf(dfJun.format(Float.parseFloat(documentosPower7.getString("V06187"))));
            PdfPCell celdaJun = new PdfPCell(new Phrase(cadenaJun,cuerpo));
            celdaJun.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJun);

            DecimalFormat dfJul = new DecimalFormat("###,###,###.00");
            String cadenaJul = "";
            sumaJul = sumaJul + Float.parseFloat(documentosPower7.getString("V07187"));
            cadenaJul = String.valueOf(dfJul.format(Float.parseFloat(documentosPower7.getString("V07187"))));
            PdfPCell celdaJul = new PdfPCell(new Phrase(cadenaJul,cuerpo));
            celdaJul.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJul);
            
            DecimalFormat dfTot = new DecimalFormat("###,###,###.00");
            sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosPower7.getString("V01187"))+
                                              Float.parseFloat(documentosPower7.getString("V02187"))+
                                              Float.parseFloat(documentosPower7.getString("V03187"))+
                                              Float.parseFloat(documentosPower7.getString("V04187"))+
                                              Float.parseFloat(documentosPower7.getString("V05187"))+
                                              Float.parseFloat(documentosPower7.getString("V06187"))+
                                              Float.parseFloat(documentosPower7.getString("V07187"));
            sumT = sumT +sumaHorizontal;
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
        }
        PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.00");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
        PdfPCell cellFebr = new PdfPCell(new Phrase(dfFeb.format(sumaFeb),fuente));
        cellFebr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellFebr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFebr);
        
        DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
        PdfPCell cellMarz = new PdfPCell(new Phrase(dfMar.format(sumaMar),fuente));
        cellMarz.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMarz.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMarz);
        
        DecimalFormat dfAbri = new DecimalFormat("###,###,###.00");
        PdfPCell cellAbri = new PdfPCell(new Phrase(dfAbri.format(sumaAbr),fuente));
        cellAbri.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAbri.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbri);
        
        DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
        PdfPCell cellMay = new PdfPCell(new Phrase(dfMayo.format(sumaMay),fuente));
        cellMay.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMay.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMay);
        
        DecimalFormat dfJunio = new DecimalFormat("###,###,###.00");
        PdfPCell cellJunio = new PdfPCell(new Phrase(dfJunio.format(sumaJun),fuente));
        cellJunio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJunio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJunio);
        
        DecimalFormat dfJulio = new DecimalFormat("###,###,###.00");
        PdfPCell cellJulio = new PdfPCell(new Phrase(dfJulio.format(sumaJul),fuente));
        cellJulio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJulio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJulio);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.00");
        PdfPCell cellT = new PdfPCell(new Phrase(dfT.format(sumT),fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        return tabla;
   }
   public static PdfPTable createTableEFMAMJJAV(int mes,ResultSet documentosPower7,String sucursal,int ano) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(8);
        cuerpo.setSize(7);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(11);
        tabla.setWidths(new float[]{0.31f,1.2f,1,1,1,1,1,1,1,1,1});
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("VENTA EN PESOS AÑO "+ano+" - "+sucursal+"",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(11);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Ventas $ Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Ventas $ Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Ventas $ Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);

        PdfPCell cellAbr = new PdfPCell(new Paragraph("Ventas $ Abril",fuente));
        cellAbr.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAbr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbr);

        PdfPCell cellMayo = new PdfPCell(new Paragraph("Ventas $ Mayo",fuente));
        cellMayo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMayo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMayo);

        PdfPCell cellJun = new PdfPCell(new Paragraph("Ventas $ Junio",fuente));
        cellJun.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJun.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJun);

        PdfPCell cellJul = new PdfPCell(new Paragraph("Ventas $ Julio",fuente));
        cellJul.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJul.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJul);

        PdfPCell cellAgo = new PdfPCell(new Paragraph("Ventas $ Agosto",fuente));
        cellAgo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAgo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAgo);
        
        PdfPCell cellT = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumaAbr = 0,sumaMay = 0,sumaJun = 0,sumaJul = 0,sumaAgos = 0,sumT = 0;
        while(documentosPower7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosPower7.getString("FAM187"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosPower7.getString("DES187"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);

            DecimalFormat dfEne = new DecimalFormat("###,###,###.00");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosPower7.getString("V01187"));
            cadenaEne = String.valueOf(dfEne.format(Float.parseFloat(documentosPower7.getString("V01187"))));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosPower7.getString("V02187"));
            cadenaFeb = String.valueOf(dfFeb.format(Float.parseFloat(documentosPower7.getString("V02187"))));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb); 

            DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosPower7.getString("V03187"));
            cadenaMar = String.valueOf(dfMar.format(Float.parseFloat(documentosPower7.getString("V03187"))));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);

            DecimalFormat dfAbr = new DecimalFormat("###,###,###.00");
            String cadenaAbr = "";
            sumaAbr = sumaAbr + Float.parseFloat(documentosPower7.getString("V04187"));
            cadenaAbr = String.valueOf(dfAbr.format(Float.parseFloat(documentosPower7.getString("V04187"))));
            PdfPCell celdaAbr = new PdfPCell(new Phrase(cadenaAbr,cuerpo));
            celdaAbr.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAbr);

            DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
            String cadenaMayo = "";
            sumaMay = sumaMay + Float.parseFloat(documentosPower7.getString("V05187"));
            cadenaMayo = String.valueOf(dfMayo.format(Float.parseFloat(documentosPower7.getString("V05187"))));
            PdfPCell celdaMayo = new PdfPCell(new Phrase(cadenaMayo,cuerpo));
            celdaMayo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMayo);

            DecimalFormat dfJun = new DecimalFormat("###,###,###.00");
            String cadenaJun = "";
            sumaJun = sumaJun + Float.parseFloat(documentosPower7.getString("V06187"));
            cadenaJun = String.valueOf(dfJun.format(Float.parseFloat(documentosPower7.getString("V06187"))));
            PdfPCell celdaJun = new PdfPCell(new Phrase(cadenaJun,cuerpo));
            celdaJun.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJun);

            DecimalFormat dfJul = new DecimalFormat("###,###,###.00");
            String cadenaJul = "";
            sumaJul = sumaJul + Float.parseFloat(documentosPower7.getString("V07187"));
            cadenaJul = String.valueOf(dfJul.format(Float.parseFloat(documentosPower7.getString("V07187"))));
            PdfPCell celdaJul = new PdfPCell(new Phrase(cadenaJul,cuerpo));
            celdaJul.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJul);

            DecimalFormat dfAgo = new DecimalFormat("###,###,###.00");
            String cadenaAgo = "";
            sumaAgos = sumaAgos + Float.parseFloat(documentosPower7.getString("V08187"));
            cadenaAgo = String.valueOf(dfAgo.format(Float.parseFloat(documentosPower7.getString("V08187"))));
            PdfPCell celdaAgo = new PdfPCell(new Phrase(cadenaAgo,cuerpo));
            celdaAgo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAgo);
            
            DecimalFormat dfTot = new DecimalFormat("###,###,###.00");
            sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosPower7.getString("V01187")) + 
                                              Float.parseFloat(documentosPower7.getString("V02187")) +
                                              Float.parseFloat(documentosPower7.getString("V03187")) + 
                                              Float.parseFloat(documentosPower7.getString("V04187")) +
                                              Float.parseFloat(documentosPower7.getString("V05187")) + 
                                              Float.parseFloat(documentosPower7.getString("V06187")) +
                                              Float.parseFloat(documentosPower7.getString("V07187")) + 
                                              Float.parseFloat(documentosPower7.getString("V08187"));
            sumT = sumT +sumaHorizontal;
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
            
        }
        PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.00");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
        PdfPCell cellFebr = new PdfPCell(new Phrase(dfFeb.format(sumaFeb),fuente));
        cellFebr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellFebr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFebr);
        
        DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
        PdfPCell cellMarz = new PdfPCell(new Phrase(dfMar.format(sumaMar),fuente));
        cellMarz.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMarz.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMarz);
        
        DecimalFormat dfAbri = new DecimalFormat("###,###,###.00");
        PdfPCell cellAbri = new PdfPCell(new Phrase(dfAbri.format(sumaAbr),fuente));
        cellAbri.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAbri.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbri);
        
        DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
        PdfPCell cellMay = new PdfPCell(new Phrase(dfMayo.format(sumaMay),fuente));
        cellMay.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMay.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMay);
        
        DecimalFormat dfJunio = new DecimalFormat("###,###,###.00");
        PdfPCell cellJunio = new PdfPCell(new Phrase(dfJunio.format(sumaJun),fuente));
        cellJunio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJunio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJunio);
        
        DecimalFormat dfJulio = new DecimalFormat("###,###,###.00");
        PdfPCell cellJulio = new PdfPCell(new Phrase(dfJulio.format(sumaJul),fuente));
        cellJulio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJulio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJulio);
        
        DecimalFormat dfAgos = new DecimalFormat("###,###,###.00");
        PdfPCell cellAgos = new PdfPCell(new Phrase(dfAgos.format(sumaAgos),fuente));
        cellAgos.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAgos.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAgos);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.00");
        PdfPCell cellTo = new PdfPCell(new Phrase(dfT.format(sumT),fuente));
        cellTo.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellTo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTo);
        return tabla;
   }
   public static PdfPTable createTableEFMAMJJASV(int mes,ResultSet documentosPower7,String sucursal,int ano) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(7);
        cuerpo.setSize(6);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(12);
        tabla.setWidths(new float[]{0.5f,2,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.8f,1.5f});
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("VENTA EN PESOS AÑO "+ano+" - "+sucursal+"",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(12);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Ventas $ Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Ventas $ Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Ventas $ Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);

        PdfPCell cellAbr = new PdfPCell(new Paragraph("Ventas $ Abril",fuente));
        cellAbr.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAbr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbr);

        PdfPCell cellMayo = new PdfPCell(new Paragraph("Ventas $ Mayo",fuente));
        cellMayo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMayo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMayo);

        PdfPCell cellJun = new PdfPCell(new Paragraph("Ventas $ Junio",fuente));
        cellJun.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJun.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJun);

        PdfPCell cellJul = new PdfPCell(new Paragraph("Ventas $ Julio",fuente));
        cellJul.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJul.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJul);

        PdfPCell cellAgo = new PdfPCell(new Paragraph("Ventas $ Agosto",fuente));
        cellAgo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAgo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAgo);

        PdfPCell cellSep = new PdfPCell(new Paragraph("Ventas $ Septiembre",fuente));
        cellSep.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellSep.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellSep);
        
        PdfPCell cellT = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumaAbr = 0,sumaMay = 0,sumaJun = 0,sumaJul = 0,sumaAgos = 0,sumaSep = 0,sumT = 0;

        while(documentosPower7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosPower7.getString("FAM187"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosPower7.getString("DES187"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);

            DecimalFormat dfEne = new DecimalFormat("###,###,###.00");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosPower7.getString("V01187"));
            cadenaEne = String.valueOf(dfEne.format(Float.parseFloat(documentosPower7.getString("V01187"))));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosPower7.getString("V02187"));
            cadenaFeb = String.valueOf(dfFeb.format(Float.parseFloat(documentosPower7.getString("V02187"))));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb); 

            DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosPower7.getString("V03187"));
            cadenaMar = String.valueOf(dfMar.format(Float.parseFloat(documentosPower7.getString("V03187"))));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);

            DecimalFormat dfAbr = new DecimalFormat("###,###,###.00");
            String cadenaAbr = "";
            sumaAbr = sumaAbr + Float.parseFloat(documentosPower7.getString("V04187"));
            cadenaAbr = String.valueOf(dfAbr.format(Float.parseFloat(documentosPower7.getString("V04187"))));
            PdfPCell celdaAbr = new PdfPCell(new Phrase(cadenaAbr,cuerpo));
            celdaAbr.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAbr);

            DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
            String cadenaMayo = "";
            sumaMay = sumaMay + Float.parseFloat(documentosPower7.getString("V05187"));
            cadenaMayo = String.valueOf(dfMayo.format(Float.parseFloat(documentosPower7.getString("V05187"))));
            PdfPCell celdaMayo = new PdfPCell(new Phrase(cadenaMayo,cuerpo));
            celdaMayo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMayo);

            DecimalFormat dfJun = new DecimalFormat("###,###,###.00");
            String cadenaJun = "";
            sumaJun = sumaJun + Float.parseFloat(documentosPower7.getString("V06187"));
            cadenaJun = String.valueOf(dfJun.format(Float.parseFloat(documentosPower7.getString("V06187"))));
            PdfPCell celdaJun = new PdfPCell(new Phrase(cadenaJun,cuerpo));
            celdaJun.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJun);

            DecimalFormat dfJul = new DecimalFormat("###,###,###.00");
            String cadenaJul = "";
            sumaJul = sumaJul + Float.parseFloat(documentosPower7.getString("V07187"));
            cadenaJul = String.valueOf(dfJul.format(Float.parseFloat(documentosPower7.getString("V07187"))));
            PdfPCell celdaJul = new PdfPCell(new Phrase(cadenaJul,cuerpo));
            celdaJul.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJul);

            DecimalFormat dfAgo = new DecimalFormat("###,###,###.00");
            String cadenaAgo = "";
            sumaAgos = sumaAgos + Float.parseFloat(documentosPower7.getString("V08187"));
            cadenaAgo = String.valueOf(dfAgo.format(Float.parseFloat(documentosPower7.getString("V08187"))));
            PdfPCell celdaAgo = new PdfPCell(new Phrase(cadenaAgo,cuerpo));
            celdaAgo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAgo);

            DecimalFormat dfSep = new DecimalFormat("###,###,###.00");
            String cadenaSep = "";
            sumaSep = sumaSep + Float.parseFloat(documentosPower7.getString("V09187"));
            cadenaSep = String.valueOf(dfSep.format(Float.parseFloat(documentosPower7.getString("V09187"))));
            PdfPCell celdaSep = new PdfPCell(new Phrase(cadenaSep,cuerpo));
            celdaSep.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaSep);
            
            DecimalFormat dfTot = new DecimalFormat("###,###,###.00");
            sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosPower7.getString("V01187")) + 
                                              Float.parseFloat(documentosPower7.getString("V02187")) +
                                              Float.parseFloat(documentosPower7.getString("V03187")) + 
                                              Float.parseFloat(documentosPower7.getString("V04187")) +
                                              Float.parseFloat(documentosPower7.getString("V05187")) + 
                                              Float.parseFloat(documentosPower7.getString("V06187")) +
                                              Float.parseFloat(documentosPower7.getString("V07187")) + 
                                              Float.parseFloat(documentosPower7.getString("V08187")) +
                                              Float.parseFloat(documentosPower7.getString("V09187"));
            sumT = sumT + sumaHorizontal;
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
        }
        PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.00");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
        PdfPCell cellFebr = new PdfPCell(new Phrase(dfFeb.format(sumaFeb),fuente));
        cellFebr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellFebr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFebr);
        
        DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
        PdfPCell cellMarz = new PdfPCell(new Phrase(dfMar.format(sumaMar),fuente));
        cellMarz.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMarz.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMarz);
        
        DecimalFormat dfAbri = new DecimalFormat("###,###,###.00");
        PdfPCell cellAbri = new PdfPCell(new Phrase(dfAbri.format(sumaAbr),fuente));
        cellAbri.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAbri.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbri);
        
        DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
        PdfPCell cellMay = new PdfPCell(new Phrase(dfMayo.format(sumaMay),fuente));
        cellMay.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMay.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMay);
        
        DecimalFormat dfJunio = new DecimalFormat("###,###,###.00");
        PdfPCell cellJunio = new PdfPCell(new Phrase(dfJunio.format(sumaJun),fuente));
        cellJunio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJunio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJunio);
        
        DecimalFormat dfJulio = new DecimalFormat("###,###,###.00");
        PdfPCell cellJulio = new PdfPCell(new Phrase(dfJulio.format(sumaJul),fuente));
        cellJulio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJulio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJulio);
        
        DecimalFormat dfAgos = new DecimalFormat("###,###,###.00");
        PdfPCell cellAgos = new PdfPCell(new Phrase(dfAgos.format(sumaAgos),fuente));
        cellAgos.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAgos.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAgos);
        
        DecimalFormat dfSep = new DecimalFormat("###,###,###.00");
        PdfPCell cellSept = new PdfPCell(new Phrase(dfSep.format(sumaSep),fuente));
        cellSept.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellSept.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellSept);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.00");
        PdfPCell cellTo = new PdfPCell(new Phrase(dfT.format(sumT),fuente));
        cellTo.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellTo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTo);
        return tabla;
   }
   public static PdfPTable createTableEFMAMJJASOV(int mes,ResultSet documentosPower7,String sucursal,int ano) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(7);
        cuerpo.setSize(6);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(13);
        
        tabla.setWidths(new float[]{0.5f,1.8f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.8f,1.5f,1.5f});
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("VENTA EN PESOS AÑO "+ano+" - "+sucursal+"",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(13);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Ventas $ Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Ventas $ Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Ventas $ Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);

        PdfPCell cellAbr = new PdfPCell(new Paragraph("Ventas $ Abril",fuente));
        cellAbr.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAbr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbr);

        PdfPCell cellMayo = new PdfPCell(new Paragraph("Ventas $ Mayo",fuente));
        cellMayo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMayo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMayo);

        PdfPCell cellJun = new PdfPCell(new Paragraph("Ventas $ Junio",fuente));
        cellJun.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJun.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJun);

        PdfPCell cellJul = new PdfPCell(new Paragraph("Ventas $ Julio",fuente));
        cellJul.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJul.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJul);

        PdfPCell cellAgo = new PdfPCell(new Paragraph("Ventas $ Agosto",fuente));
        cellAgo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAgo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAgo);

        PdfPCell cellSep = new PdfPCell(new Paragraph("Ventas $ Septiembre",fuente));
        cellSep.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellSep.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellSep);

        PdfPCell cellOct = new PdfPCell(new Paragraph("Ventas $ Octubre",fuente));
        cellOct.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellOct.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellOct);
        
        PdfPCell cellT = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumaAbr = 0,sumaMay = 0,sumaJun = 0,sumaJul = 0,sumaAgos = 0,sumaSep = 0,sumaOct = 0,sumT = 0;
        
        while(documentosPower7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosPower7.getString("FAM187"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosPower7.getString("DES187"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);

            DecimalFormat dfEne = new DecimalFormat("###,###,###.0");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosPower7.getString("V01187"));
            cadenaEne = String.valueOf(dfEne.format(Float.parseFloat(documentosPower7.getString("V01187"))));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###,###,###.0");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosPower7.getString("V02187"));
            cadenaFeb = String.valueOf(dfFeb.format(Float.parseFloat(documentosPower7.getString("V02187"))));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb); 

            DecimalFormat dfMar = new DecimalFormat("###,###,###.0");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosPower7.getString("V03187"));
            cadenaMar = String.valueOf(dfMar.format(Float.parseFloat(documentosPower7.getString("V03187"))));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);

            DecimalFormat dfAbr = new DecimalFormat("###,###,###.0");
            String cadenaAbr = "";
            sumaAbr = sumaAbr + Float.parseFloat(documentosPower7.getString("V04187"));
            cadenaAbr = String.valueOf(dfAbr.format(Float.parseFloat(documentosPower7.getString("V04187"))));
            PdfPCell celdaAbr = new PdfPCell(new Phrase(cadenaAbr,cuerpo));
            celdaAbr.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAbr);

            DecimalFormat dfMayo = new DecimalFormat("###,###,###.0");
            String cadenaMayo = "";
            sumaMay = sumaMay + Float.parseFloat(documentosPower7.getString("V05187"));
            cadenaMayo = String.valueOf(dfMayo.format(Float.parseFloat(documentosPower7.getString("V05187"))));
            PdfPCell celdaMayo = new PdfPCell(new Phrase(cadenaMayo,cuerpo));
            celdaMayo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMayo);

            DecimalFormat dfJun = new DecimalFormat("###,###,###.0");
            String cadenaJun = "";
            sumaJun = sumaJun + Float.parseFloat(documentosPower7.getString("V06187"));
            cadenaJun = String.valueOf(dfJun.format(Float.parseFloat(documentosPower7.getString("V06187"))));
            PdfPCell celdaJun = new PdfPCell(new Phrase(cadenaJun,cuerpo));
            celdaJun.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJun);

            DecimalFormat dfJul = new DecimalFormat("###,###,###.0");
            String cadenaJul = "";
            sumaJul = sumaJul + Float.parseFloat(documentosPower7.getString("V07187"));
            cadenaJul = String.valueOf(dfJul.format(Float.parseFloat(documentosPower7.getString("V07187"))));
            PdfPCell celdaJul = new PdfPCell(new Phrase(cadenaJul,cuerpo));
            celdaJul.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJul);

            DecimalFormat dfAgo = new DecimalFormat("###,###,###.0");
            String cadenaAgo = "";
            sumaAgos = sumaAgos + Float.parseFloat(documentosPower7.getString("V08187"));
            cadenaAgo = String.valueOf(dfAgo.format(Float.parseFloat(documentosPower7.getString("V08187"))));
            PdfPCell celdaAgo = new PdfPCell(new Phrase(cadenaAgo,cuerpo));
            celdaAgo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAgo);

            DecimalFormat dfSep = new DecimalFormat("###,###,###.0");
            String cadenaSep = "";
            sumaSep = sumaSep + Float.parseFloat(documentosPower7.getString("V09187"));
            cadenaSep = String.valueOf(dfSep.format(Float.parseFloat(documentosPower7.getString("V09187"))));
            PdfPCell celdaSep = new PdfPCell(new Phrase(cadenaSep,cuerpo));
            celdaSep.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaSep);

            DecimalFormat dfOct = new DecimalFormat("###,###,###.0");
            String cadenaOct = "";
            sumaOct = sumaOct + Float.parseFloat(documentosPower7.getString("V10187"));
            cadenaOct = String.valueOf(dfOct.format(Float.parseFloat(documentosPower7.getString("V10187"))));
            PdfPCell celdaOct = new PdfPCell(new Phrase(cadenaOct,cuerpo));
            celdaOct.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaOct);
            
            DecimalFormat dfTot = new DecimalFormat("###,###,###.0");
            sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosPower7.getString("V01187")) + 
                                              Float.parseFloat(documentosPower7.getString("V02187")) +
                                              Float.parseFloat(documentosPower7.getString("V03187")) + 
                                              Float.parseFloat(documentosPower7.getString("V04187")) +
                                              Float.parseFloat(documentosPower7.getString("V05187")) + 
                                              Float.parseFloat(documentosPower7.getString("V06187")) +
                                              Float.parseFloat(documentosPower7.getString("V07187")) + 
                                              Float.parseFloat(documentosPower7.getString("V08187")) +
                                              Float.parseFloat(documentosPower7.getString("V09187")) +
                                              Float.parseFloat(documentosPower7.getString("V10187"));
            sumT = sumT + sumaHorizontal;
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
        } 
        PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.0");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfFeb = new DecimalFormat("###,###,###.0");
        PdfPCell cellFebr = new PdfPCell(new Phrase(dfFeb.format(sumaFeb),fuente));
        cellFebr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellFebr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFebr);
        
        DecimalFormat dfMar = new DecimalFormat("###,###,###.0");
        PdfPCell cellMarz = new PdfPCell(new Phrase(dfMar.format(sumaMar),fuente));
        cellMarz.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMarz.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMarz);
        
        DecimalFormat dfAbri = new DecimalFormat("###,###,###.0");
        PdfPCell cellAbri = new PdfPCell(new Phrase(dfAbri.format(sumaAbr),fuente));
        cellAbri.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAbri.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbri);
        
        DecimalFormat dfMayo = new DecimalFormat("###,###,###.0");
        PdfPCell cellMay = new PdfPCell(new Phrase(dfMayo.format(sumaMay),fuente));
        cellMay.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMay.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMay);
        
        DecimalFormat dfJunio = new DecimalFormat("###,###,###.0");
        PdfPCell cellJunio = new PdfPCell(new Phrase(dfJunio.format(sumaJun),fuente));
        cellJunio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJunio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJunio);
        
        DecimalFormat dfJulio = new DecimalFormat("###,###,###.0");
        PdfPCell cellJulio = new PdfPCell(new Phrase(dfJulio.format(sumaJul),fuente));
        cellJulio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJulio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJulio);
        
        DecimalFormat dfAgos = new DecimalFormat("###,###,###.0");
        PdfPCell cellAgos = new PdfPCell(new Phrase(dfAgos.format(sumaAgos),fuente));
        cellAgos.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAgos.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAgos);
        
        DecimalFormat dfSep = new DecimalFormat("###,###,###.0");
        PdfPCell cellSept = new PdfPCell(new Phrase(dfSep.format(sumaSep),fuente));
        cellSept.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellSept.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellSept);
        
        DecimalFormat dfOct = new DecimalFormat("###,###,###.0");
        PdfPCell cellOctu = new PdfPCell(new Phrase(dfOct.format(sumaOct),fuente));
        cellOctu.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellOctu.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellOctu);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.0");
        PdfPCell cellTo = new PdfPCell(new Phrase(dfT.format(sumT),fuente));
        cellTo.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellTo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTo);
        return tabla;
   }
   public static PdfPTable createTableEFMAMJJASONV(int mes,ResultSet documentosPower7,String sucursal,int ano) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(6);
        cuerpo.setSize(5);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(14);
        tabla.setWidths(new float[]{0.45f,1.5f,1.2f,1.3f,1.2f,1.2f,1.1f,1.2f,1.2f,1.3f,1.2f,1.2f,1.5f,1.5f});
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("VENTA EN PESOS AÑO "+ano+" - "+sucursal+"",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(14);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Ventas $ Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Ventas $ Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Ventas $ Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);

        PdfPCell cellAbr = new PdfPCell(new Paragraph("Ventas $ Abril",fuente));
        cellAbr.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAbr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbr);

        PdfPCell cellMayo = new PdfPCell(new Paragraph("Ventas $ Mayo",fuente));
        cellMayo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMayo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMayo);

        PdfPCell cellJun = new PdfPCell(new Paragraph("Ventas $ Junio",fuente));
        cellJun.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJun.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJun);

        PdfPCell cellJul = new PdfPCell(new Paragraph("Ventas $ Julio",fuente));
        cellJul.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJul.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJul);

        PdfPCell cellAgo = new PdfPCell(new Paragraph("Ventas $ Agosto",fuente));
        cellAgo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAgo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAgo);

        PdfPCell cellSep = new PdfPCell(new Paragraph("Ventas $ Septiembre",fuente));
        cellSep.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellSep.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellSep);

        PdfPCell cellOct = new PdfPCell(new Paragraph("Ventas $ Octubre",fuente));
        cellOct.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellOct.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellOct);

        PdfPCell cellNov = new PdfPCell(new Paragraph("Ventas $ Noviembre",fuente));
        cellNov.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellNov.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellNov);
        
        PdfPCell cellT = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumaAbr = 0,sumaMay = 0,sumaJun = 0,sumaJul = 0,sumaAgos = 0,sumaSep = 0,sumaOct = 0,sumaNov = 0,sumT = 0;
        
        while(documentosPower7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosPower7.getString("FAM187"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosPower7.getString("DES187"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);

            DecimalFormat dfEne = new DecimalFormat("###,###,###.00");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosPower7.getString("V01187"));
            cadenaEne = String.valueOf(dfEne.format(Float.parseFloat(documentosPower7.getString("V01187"))));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosPower7.getString("V02187"));
            cadenaFeb = String.valueOf(dfFeb.format(Float.parseFloat(documentosPower7.getString("V02187"))));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb); 

            DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosPower7.getString("V03187"));
            cadenaMar = String.valueOf(dfMar.format(Float.parseFloat(documentosPower7.getString("V03187"))));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);

            DecimalFormat dfAbr = new DecimalFormat("###,###,###.00");
            String cadenaAbr = "";
            sumaAbr = sumaAbr + Float.parseFloat(documentosPower7.getString("V04187"));
            cadenaAbr = String.valueOf(dfAbr.format(Float.parseFloat(documentosPower7.getString("V04187"))));
            PdfPCell celdaAbr = new PdfPCell(new Phrase(cadenaAbr,cuerpo));
            celdaAbr.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAbr);

            DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
            String cadenaMayo = "";
            sumaMay = sumaMay + Float.parseFloat(documentosPower7.getString("V05187"));
            cadenaMayo = String.valueOf(dfMayo.format(Float.parseFloat(documentosPower7.getString("V05187"))));
            PdfPCell celdaMayo = new PdfPCell(new Phrase(cadenaMayo,cuerpo));
            celdaMayo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMayo);

            DecimalFormat dfJun = new DecimalFormat("###,###,###.00");
            String cadenaJun = "";
            sumaJun = sumaJun + Float.parseFloat(documentosPower7.getString("V06187"));
            cadenaJun = String.valueOf(dfJun.format(Float.parseFloat(documentosPower7.getString("V06187"))));
            PdfPCell celdaJun = new PdfPCell(new Phrase(cadenaJun,cuerpo));
            celdaJun.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJun);

            DecimalFormat dfJul = new DecimalFormat("###,###,###.00");
            String cadenaJul = "";
            sumaJul = sumaJul + Float.parseFloat(documentosPower7.getString("V07187"));
            cadenaJul = String.valueOf(dfJul.format(Float.parseFloat(documentosPower7.getString("V07187"))));
            PdfPCell celdaJul = new PdfPCell(new Phrase(cadenaJul,cuerpo));
            celdaJul.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJul);

            DecimalFormat dfAgo = new DecimalFormat("###,###,###.00");
            String cadenaAgo = "";
            sumaAgos = sumaAgos + Float.parseFloat(documentosPower7.getString("V08187"));
            cadenaAgo = String.valueOf(dfAgo.format(Float.parseFloat(documentosPower7.getString("V08187"))));
            PdfPCell celdaAgo = new PdfPCell(new Phrase(cadenaAgo,cuerpo));
            celdaAgo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAgo);

            DecimalFormat dfSep = new DecimalFormat("###,###,###.00");
            String cadenaSep = "";
            sumaSep = sumaSep + Float.parseFloat(documentosPower7.getString("V09187"));
            cadenaSep = String.valueOf(dfSep.format(Float.parseFloat(documentosPower7.getString("V09187"))));
            PdfPCell celdaSep = new PdfPCell(new Phrase(cadenaSep,cuerpo));
            celdaSep.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaSep);

            DecimalFormat dfOct = new DecimalFormat("###,###,###.00");
            String cadenaOct = "";
            sumaOct = sumaOct + Float.parseFloat(documentosPower7.getString("V10187"));
            cadenaOct = String.valueOf(dfOct.format(Float.parseFloat(documentosPower7.getString("V10187"))));
            PdfPCell celdaOct = new PdfPCell(new Phrase(cadenaOct,cuerpo));
            celdaOct.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaOct);

            DecimalFormat dfNov = new DecimalFormat("###,###,###.00");
            String cadenaNov = "";
            sumaNov = sumaNov + Float.parseFloat(documentosPower7.getString("V11187"));
            cadenaNov = String.valueOf(dfNov.format(Float.parseFloat(documentosPower7.getString("V11187"))));
            PdfPCell celdaNov = new PdfPCell(new Phrase(cadenaNov,cuerpo));
            celdaNov.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaNov);
            
            DecimalFormat dfTot = new DecimalFormat("###,###,###.00");
            sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosPower7.getString("V01187")) + 
                                              Float.parseFloat(documentosPower7.getString("V02187")) +
                                              Float.parseFloat(documentosPower7.getString("V03187")) + 
                                              Float.parseFloat(documentosPower7.getString("V04187")) +
                                              Float.parseFloat(documentosPower7.getString("V05187")) + 
                                              Float.parseFloat(documentosPower7.getString("V06187")) +
                                              Float.parseFloat(documentosPower7.getString("V07187")) + 
                                              Float.parseFloat(documentosPower7.getString("V08187")) +
                                              Float.parseFloat(documentosPower7.getString("V09187")) +
                                              Float.parseFloat(documentosPower7.getString("V10187")) +
                                              Float.parseFloat(documentosPower7.getString("V11187"));
            sumT = sumT + sumaHorizontal;
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
            
        }
        PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.00");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
        PdfPCell cellFebr = new PdfPCell(new Phrase(dfFeb.format(sumaFeb),fuente));
        cellFebr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellFebr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFebr);
        
        DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
        PdfPCell cellMarz = new PdfPCell(new Phrase(dfMar.format(sumaMar),fuente));
        cellMarz.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMarz.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMarz);
        
        DecimalFormat dfAbri = new DecimalFormat("###,###,###.00");
        PdfPCell cellAbri = new PdfPCell(new Phrase(dfAbri.format(sumaAbr),fuente));
        cellAbri.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAbri.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbri);
        
        DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
        PdfPCell cellMay = new PdfPCell(new Phrase(dfMayo.format(sumaMay),fuente));
        cellMay.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMay.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMay);
        
        DecimalFormat dfJunio = new DecimalFormat("###,###,###.00");
        PdfPCell cellJunio = new PdfPCell(new Phrase(dfJunio.format(sumaJun),fuente));
        cellJunio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJunio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJunio);
        
        DecimalFormat dfJulio = new DecimalFormat("###,###,###.00");
        PdfPCell cellJulio = new PdfPCell(new Phrase(dfJulio.format(sumaJul),fuente));
        cellJulio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJulio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJulio);
        
        DecimalFormat dfAgos = new DecimalFormat("###,###,###.00");
        PdfPCell cellAgos = new PdfPCell(new Phrase(dfAgos.format(sumaAgos),fuente));
        cellAgos.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAgos.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAgos);
        
        DecimalFormat dfSep = new DecimalFormat("###,###,###.00");
        PdfPCell cellSept = new PdfPCell(new Phrase(dfSep.format(sumaSep),fuente));
        cellSept.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellSept.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellSept);
        
        DecimalFormat dfOct = new DecimalFormat("###,###,###.00");
        PdfPCell cellOctu = new PdfPCell(new Phrase(dfOct.format(sumaOct),fuente));
        cellOctu.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellOctu.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellOctu);
        
        DecimalFormat dfNov = new DecimalFormat("###,###,###.00");
        PdfPCell cellNovi = new PdfPCell(new Phrase(dfNov.format(sumaNov),fuente));
        cellNovi.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellNovi.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellNovi);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.00");
        PdfPCell cellTo = new PdfPCell(new Phrase(dfT.format(sumT),fuente));
        cellTo.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellTo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTo);
        return tabla;
   }
   public static PdfPTable createTableEFMAMJJASONDV(int mes,ResultSet documentosPower7,String sucursal,int ano) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(6);
        cuerpo.setSize(5);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(15);
        tabla.setWidths(new float[]{0.5f,1.8f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.8f,1.5f,1.5f,1.5f,1.8f});
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("VENTA EN PESOS AÑO "+ano+" - "+sucursal+"",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(15);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Ventas $ Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Ventas $ Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Ventas $ Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);

        PdfPCell cellAbr = new PdfPCell(new Paragraph("Ventas $ Abril",fuente));
        cellAbr.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAbr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbr);

        PdfPCell cellMayo = new PdfPCell(new Paragraph("Ventas $ Mayo",fuente));
        cellMayo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMayo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMayo);

        PdfPCell cellJun = new PdfPCell(new Paragraph("Ventas $ Junio",fuente));
        cellJun.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJun.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJun);

        PdfPCell cellJul = new PdfPCell(new Paragraph("Ventas $ Julio",fuente));
        cellJul.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJul.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJul);

        PdfPCell cellAgo = new PdfPCell(new Paragraph("Ventas $ Agosto",fuente));
        cellAgo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAgo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAgo);

        PdfPCell cellSep = new PdfPCell(new Paragraph("Ventas $ Septiembre",fuente));
        cellSep.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellSep.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellSep);

        PdfPCell cellOct = new PdfPCell(new Paragraph("Ventas $ Octubre",fuente));
        cellOct.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellOct.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellOct);

        PdfPCell cellNov = new PdfPCell(new Paragraph("Ventas $ Noviembre",fuente));
        cellNov.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellNov.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellNov);

        PdfPCell cellDic = new PdfPCell(new Paragraph("Ventas $ Diciembre",fuente));
        cellDic.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDic.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDic);
        
        PdfPCell cellT = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumaAbr = 0,sumaMay = 0,sumaJun = 0,sumaJul = 0,sumaAgos = 0,sumaSep = 0,sumaOct = 0,sumaNov = 0,sumaDic = 0,sumT = 0;

         while(documentosPower7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosPower7.getString("FAM187"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosPower7.getString("DES187"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);

            DecimalFormat dfEne = new DecimalFormat("###,###,###.0");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosPower7.getString("V01187"));
            cadenaEne = String.valueOf(dfEne.format(Float.parseFloat(documentosPower7.getString("V01187"))));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###,###,###.0");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosPower7.getString("V02187"));
            cadenaFeb = String.valueOf(dfFeb.format(Float.parseFloat(documentosPower7.getString("V02187"))));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb); 

            DecimalFormat dfMar = new DecimalFormat("###,###,###.0");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosPower7.getString("V03187"));
            cadenaMar = String.valueOf(dfMar.format(Float.parseFloat(documentosPower7.getString("V03187"))));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);

            DecimalFormat dfAbr = new DecimalFormat("###,###,###.0");
            String cadenaAbr = "";
            sumaAbr = sumaAbr + Float.parseFloat(documentosPower7.getString("V04187"));
            cadenaAbr = String.valueOf(dfAbr.format(Float.parseFloat(documentosPower7.getString("V04187"))));
            PdfPCell celdaAbr = new PdfPCell(new Phrase(cadenaAbr,cuerpo));
            celdaAbr.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAbr);

            DecimalFormat dfMayo = new DecimalFormat("###,###,###.0");
            String cadenaMayo = "";
            sumaMay = sumaMay + Float.parseFloat(documentosPower7.getString("V05187"));
            cadenaMayo = String.valueOf(dfMayo.format(Float.parseFloat(documentosPower7.getString("V05187"))));
            PdfPCell celdaMayo = new PdfPCell(new Phrase(cadenaMayo,cuerpo));
            celdaMayo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMayo);

            DecimalFormat dfJun = new DecimalFormat("###,###,###.0");
            String cadenaJun = "";
            sumaJun = sumaJun + Float.parseFloat(documentosPower7.getString("V06187"));
            cadenaJun = String.valueOf(dfJun.format(Float.parseFloat(documentosPower7.getString("V06187"))));
            PdfPCell celdaJun = new PdfPCell(new Phrase(cadenaJun,cuerpo));
            celdaJun.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJun);

            DecimalFormat dfJul = new DecimalFormat("###,###,###.0");
            String cadenaJul = "";
            sumaJul = sumaJul + Float.parseFloat(documentosPower7.getString("V07187"));
            cadenaJul = String.valueOf(dfJul.format(Float.parseFloat(documentosPower7.getString("V07187"))));
            PdfPCell celdaJul = new PdfPCell(new Phrase(cadenaJul,cuerpo));
            celdaJul.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJul);

            DecimalFormat dfAgo = new DecimalFormat("###,###,###.0");
            String cadenaAgo = "";
            sumaAgos = sumaAgos + Float.parseFloat(documentosPower7.getString("V08187"));
            cadenaAgo = String.valueOf(dfAgo.format(Float.parseFloat(documentosPower7.getString("V08187"))));
            PdfPCell celdaAgo = new PdfPCell(new Phrase(cadenaAgo,cuerpo));
            celdaAgo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAgo);

            DecimalFormat dfSep = new DecimalFormat("###,###,###.0");
            String cadenaSep = "";
            sumaSep = sumaSep + Float.parseFloat(documentosPower7.getString("V09187"));
            cadenaSep = String.valueOf(dfSep.format(Float.parseFloat(documentosPower7.getString("V09187"))));
            PdfPCell celdaSep = new PdfPCell(new Phrase(cadenaSep,cuerpo));
            celdaSep.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaSep);

            DecimalFormat dfOct = new DecimalFormat("###,###,###.0");
            String cadenaOct = "";
            sumaOct = sumaOct + Float.parseFloat(documentosPower7.getString("V10187"));
            cadenaOct = String.valueOf(dfOct.format(Float.parseFloat(documentosPower7.getString("V10187"))));
            PdfPCell celdaOct = new PdfPCell(new Phrase(cadenaOct,cuerpo));
            celdaOct.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaOct);

            DecimalFormat dfNov = new DecimalFormat("###,###,###.0");
            String cadenaNov = "";
            sumaNov = sumaNov + Float.parseFloat(documentosPower7.getString("V11187"));
            cadenaNov = String.valueOf(dfNov.format(Float.parseFloat(documentosPower7.getString("V11187"))));
            PdfPCell celdaNov = new PdfPCell(new Phrase(cadenaNov,cuerpo));
            celdaNov.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaNov);

            DecimalFormat dfDic = new DecimalFormat("###,###,###.0");
            String cadenaDic = "";
            sumaDic = sumaDic + Float.parseFloat(documentosPower7.getString("V12187"));
            cadenaDic = String.valueOf(dfDic.format(Float.parseFloat(documentosPower7.getString("V12187"))));
            PdfPCell celdaDic = new PdfPCell(new Phrase(cadenaDic,cuerpo));
            celdaDic.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaDic);
            
            DecimalFormat dfTot = new DecimalFormat("###,###,###.0");
            sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosPower7.getString("V01187")) + 
                                              Float.parseFloat(documentosPower7.getString("V02187")) +
                                              Float.parseFloat(documentosPower7.getString("V03187")) + 
                                              Float.parseFloat(documentosPower7.getString("V04187")) +
                                              Float.parseFloat(documentosPower7.getString("V05187")) + 
                                              Float.parseFloat(documentosPower7.getString("V06187")) +
                                              Float.parseFloat(documentosPower7.getString("V07187")) + 
                                              Float.parseFloat(documentosPower7.getString("V08187")) +
                                              Float.parseFloat(documentosPower7.getString("V09187")) +
                                              Float.parseFloat(documentosPower7.getString("V10187")) +
                                              Float.parseFloat(documentosPower7.getString("V11187")) +
                                              Float.parseFloat(documentosPower7.getString("V12187"));
            sumT = sumT + sumaHorizontal;
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
        }
         PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.0");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfFeb = new DecimalFormat("###,###,###.0");
        PdfPCell cellFebr = new PdfPCell(new Phrase(dfFeb.format(sumaFeb),fuente));
        cellFebr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellFebr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFebr);
        
        DecimalFormat dfMar = new DecimalFormat("###,###,###.0");
        PdfPCell cellMarz = new PdfPCell(new Phrase(dfMar.format(sumaMar),fuente));
        cellMarz.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMarz.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMarz);
        
        DecimalFormat dfAbri = new DecimalFormat("###,###,###.0");
        PdfPCell cellAbri = new PdfPCell(new Phrase(dfAbri.format(sumaAbr),fuente));
        cellAbri.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAbri.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbri);
        
        DecimalFormat dfMayo = new DecimalFormat("###,###,###.0");
        PdfPCell cellMay = new PdfPCell(new Phrase(dfMayo.format(sumaMay),fuente));
        cellMay.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMay.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMay);
        
        DecimalFormat dfJunio = new DecimalFormat("###,###,###.0");
        PdfPCell cellJunio = new PdfPCell(new Phrase(dfJunio.format(sumaJun),fuente));
        cellJunio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJunio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJunio);
        
        DecimalFormat dfJulio = new DecimalFormat("###,###,###.0");
        PdfPCell cellJulio = new PdfPCell(new Phrase(dfJulio.format(sumaJul),fuente));
        cellJulio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJulio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJulio);
        
        DecimalFormat dfAgos = new DecimalFormat("###,###,###.0");
        PdfPCell cellAgos = new PdfPCell(new Phrase(dfAgos.format(sumaAgos),fuente));
        cellAgos.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAgos.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAgos);
        
        DecimalFormat dfSep = new DecimalFormat("###,###,###.0");
        PdfPCell cellSept = new PdfPCell(new Phrase(dfSep.format(sumaSep),fuente));
        cellSept.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellSept.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellSept);
        
        DecimalFormat dfOct = new DecimalFormat("###,###,###.0");
        PdfPCell cellOctu = new PdfPCell(new Phrase(dfOct.format(sumaOct),fuente));
        cellOctu.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellOctu.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellOctu);
        
        DecimalFormat dfNov = new DecimalFormat("###,###,###.0");
        PdfPCell cellNovi = new PdfPCell(new Phrase(dfNov.format(sumaNov),fuente));
        cellNovi.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellNovi.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellNovi);
        
        DecimalFormat dfDic = new DecimalFormat("###,###,###.0");
        PdfPCell cellDici = new PdfPCell(new Phrase(dfDic.format(sumaDic),fuente));
        cellDici.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellDici.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDici);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.0");
        PdfPCell cellTo = new PdfPCell(new Phrase(dfT.format(sumT),fuente));
        cellTo.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellTo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTo);
        return tabla;
   }
   /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   
   public static PdfPTable createTableEneroK(int mes,ResultSet documentosKilos7,String sucursal, int ano) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(9);
        cuerpo.setSize(8);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(4);
        tabla.setWidths(new float[]{0.01f,0.1f,0.1f,0.1f});
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("KILOS AÑO "+ano+" - "+sucursal+"",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(4);
        tabla.addCell(cellTitulo);
        
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Kilos Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);
        
        PdfPCell cellTot = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellTot.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTot.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTot);

        int j=1;
        float sumaEnero = 0,sumaHorizontal = 0,sumaT = 0;        
        while(documentosKilos7.next()){
                PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosKilos7.getString("FAM187"),cuerpo));
                celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
                tabla.addCell(celdaFAM);

                PdfPCell celdaDES = new PdfPCell(new Phrase(documentosKilos7.getString("DES187"),cuerpo));
                celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
                tabla.addCell(celdaDES);


                DecimalFormat df = new DecimalFormat("###,###,###.00");
                String cadena = "";
                sumaEnero = sumaEnero + Float.parseFloat(documentosKilos7.getString("K01187"));
                
                cadena = String.valueOf(df.format(Float.parseFloat(documentosKilos7.getString("K01187"))));
                PdfPCell celda = new PdfPCell(new Phrase(cadena,cuerpo));
                celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                tabla.addCell(celda);
                
                DecimalFormat dfTot = new DecimalFormat("###,###,###.00");
                sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosKilos7.getString("K01187"));
                sumaT = sumaT + sumaHorizontal;
                PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                tabla.addCell(cell);
                sumaHorizontal = 0;
        }
        PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.00");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.00");
        PdfPCell cellT = new PdfPCell(new Phrase(dfT.format(sumaT),fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        
        return tabla;
   }
   public static PdfPTable createTableEFK(int mes,ResultSet documentosKilos7,String sucursal,int ano) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(9);
        cuerpo.setSize(8);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(5);
        tabla.setWidths(new float[]{0.015f,0.1f,0.1f,0.1f,0.1f});
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("KILOS AÑO "+ano+" - "+sucursal+"",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(5);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Kilos Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Kilos Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);
        
        PdfPCell cellTot = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellTot.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTot.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTot);
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaT = 0;
        while(documentosKilos7.next()){
                PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosKilos7.getString("FAM187"),cuerpo));
                celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
                tabla.addCell(celdaFAM);

                PdfPCell celdaDES = new PdfPCell(new Phrase(documentosKilos7.getString("DES187"),cuerpo));
                celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
                tabla.addCell(celdaDES);


                DecimalFormat dfEne = new DecimalFormat("###,###,###.00");
                String cadenaEne = "";
                cadenaEne = String.valueOf(dfEne.format(Float.parseFloat(documentosKilos7.getString("K01187"))));
                sumaEnero = sumaEnero + Float.parseFloat(documentosKilos7.getString("K01187"));
                PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
                celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
                tabla.addCell(celdaEne);

                DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
                String cadenaFeb = "";
                cadenaFeb = String.valueOf(dfFeb.format(Float.parseFloat(documentosKilos7.getString("K02187"))));
                sumaFeb = sumaFeb + Float.parseFloat(documentosKilos7.getString("K02187"));
                PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
                celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
                tabla.addCell(celdaFeb);
                
                DecimalFormat dfTot = new DecimalFormat("###,###,###.00");
                sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosKilos7.getString("K01187"))+
                                                  Float.parseFloat(documentosKilos7.getString("K02187"));
                sumaT = sumaT + sumaHorizontal;
                PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                tabla.addCell(cell);
                sumaHorizontal = 0;
        }
        PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.00");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
        PdfPCell cellFebr = new PdfPCell(new Phrase(dfFeb.format(sumaFeb),fuente));
        cellFebr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellFebr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFebr);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.00");
        PdfPCell cellT = new PdfPCell(new Phrase(dfT.format(sumaT),fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        return tabla;
   }
   public static PdfPTable createTableEFMK(int mes,ResultSet documentosKilos7,String sucursal,int ano) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(9);
        cuerpo.setSize(8);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(6);
        tabla.setWidths(new float[]{0.017f,0.1f,0.1f,0.1f,0.1f,0.1f});
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("KILOS AÑO "+ano+" - "+sucursal+"",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(6);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Kilos Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Kilos Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Kilos Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);
        
        PdfPCell cellTot = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellTot.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTot.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTot);
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumT = 0;
        
         while(documentosKilos7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosKilos7.getString("FAM187"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosKilos7.getString("DES187"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);


            DecimalFormat dfEne = new DecimalFormat("###,###,###.00");
            String cadenaEne = "";
            cadenaEne = String.valueOf(dfEne.format(Float.parseFloat(documentosKilos7.getString("K01187"))));
            sumaEnero = sumaEnero + Float.parseFloat(documentosKilos7.getString("K01187"));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
            String cadenaFeb = "";
            cadenaFeb = String.valueOf(dfFeb.format(Float.parseFloat(documentosKilos7.getString("K02187"))));
            sumaFeb = sumaFeb + Float.parseFloat(documentosKilos7.getString("K02187"));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb); 

            DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
            String cadenaMar = "";
            cadenaMar = String.valueOf(dfMar.format(Float.parseFloat(documentosKilos7.getString("K03187"))));
            sumaMar = sumaMar + Float.parseFloat(documentosKilos7.getString("K03187"));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);
            
            DecimalFormat dfTot = new DecimalFormat("###,###,###.00");
            sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosKilos7.getString("K01187"))+
                                              Float.parseFloat(documentosKilos7.getString("K02187"))+
                                              Float.parseFloat(documentosKilos7.getString("K03187"));
            sumT = sumT + sumaHorizontal;
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
        }
        PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.00");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
        PdfPCell cellFebr = new PdfPCell(new Phrase(dfFeb.format(sumaFeb),fuente));
        cellFebr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellFebr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFebr);
        
        DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
        PdfPCell cellMarz = new PdfPCell(new Phrase(dfMar.format(sumaMar),fuente));
        cellMarz.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMarz.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMarz);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.00");
        PdfPCell cellT = new PdfPCell(new Phrase(dfT.format(sumT),fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        return tabla;
   }
   public static PdfPTable createTableEFMAK(int mes,ResultSet documentosKilos7,String sucursal,int ano) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(9);
        cuerpo.setSize(8);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(7);
        tabla.setWidths(new float[]{0.02f,0.1f,0.1f,0.1f,0.1f,0.1f,0.1f});
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("KILOS AÑO "+ano+" - "+sucursal+"",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(7);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Kilos Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Kilos Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Kilos Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);

        PdfPCell cellAbr = new PdfPCell(new Paragraph("Kilos Abril",fuente));
        cellAbr.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAbr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbr);
        
        PdfPCell cellTot = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellTot.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTot.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTot);
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumaAbr = 0,sumT = 0;

        while(documentosKilos7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosKilos7.getString("FAM187"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosKilos7.getString("DES187"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);


            DecimalFormat dfEne = new DecimalFormat("###,###,###.00");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosKilos7.getString("K01187"));
            cadenaEne = String.valueOf(dfEne.format(Float.parseFloat(documentosKilos7.getString("K01187"))));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosKilos7.getString("K02187"));
            cadenaFeb = String.valueOf(dfFeb.format(Float.parseFloat(documentosKilos7.getString("K02187"))));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb); 

            DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosKilos7.getString("K03187"));
            cadenaMar = String.valueOf(dfMar.format(Float.parseFloat(documentosKilos7.getString("K03187"))));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);

            DecimalFormat dfAbr = new DecimalFormat("###,###,###.00");
            String cadenaAbr = "";
            sumaAbr = sumaAbr + Float.parseFloat(documentosKilos7.getString("K04187"));
            cadenaAbr = String.valueOf(dfAbr.format(Float.parseFloat(documentosKilos7.getString("K04187"))));
            PdfPCell celdaAbr = new PdfPCell(new Phrase(cadenaAbr,cuerpo));
            celdaAbr.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAbr);
            
            DecimalFormat dfTot = new DecimalFormat("###,###,###.00");
            sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosKilos7.getString("K01187"))+
                                              Float.parseFloat(documentosKilos7.getString("K02187"))+
                                              Float.parseFloat(documentosKilos7.getString("K03187"))+
                                              Float.parseFloat(documentosKilos7.getString("K04187"));
            sumT = sumT + sumaHorizontal;
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
        }
        PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.00");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
        PdfPCell cellFebr = new PdfPCell(new Phrase(dfFeb.format(sumaFeb),fuente));
        cellFebr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellFebr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFebr);
        
        DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
        PdfPCell cellMarz = new PdfPCell(new Phrase(dfMar.format(sumaMar),fuente));
        cellMarz.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMarz.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMarz);
        
        DecimalFormat dfAbri = new DecimalFormat("###,###,###.00");
        PdfPCell cellAbri = new PdfPCell(new Phrase(dfAbri.format(sumaAbr),fuente));
        cellAbri.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAbri.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbri);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.00");
        PdfPCell cellT = new PdfPCell(new Phrase(dfT.format(sumT),fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        return tabla;
   }
   public static PdfPTable createTableEFMAMK(int mes,ResultSet documentosKilos7,String sucursal,int ano) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(9);
        cuerpo.setSize(8);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(8);
        tabla.setWidths(new float[]{0.023f,0.1f,0.1f,0.1f,0.1f,0.1f,0.1f,0.1f});
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("KILOS AÑO "+ano+" - "+sucursal+"",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(8);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Kilos Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Kilos Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Kilos Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);

        PdfPCell cellAbr = new PdfPCell(new Paragraph("Kilos Abril",fuente));
        cellAbr.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAbr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbr);

        PdfPCell cellMayo = new PdfPCell(new Paragraph("Kilos Mayo",fuente));
        cellMayo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMayo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMayo);
        
        PdfPCell cellTot = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellTot.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTot.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTot);
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumaAbr = 0,sumaMay = 0,sumT = 0;

        while(documentosKilos7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosKilos7.getString("FAM187"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosKilos7.getString("DES187"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);


            DecimalFormat dfEne = new DecimalFormat("###,###,###.00");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosKilos7.getString("K01187"));
            cadenaEne = String.valueOf(dfEne.format(Float.parseFloat(documentosKilos7.getString("K01187"))));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosKilos7.getString("K02187"));
            cadenaFeb = String.valueOf(dfFeb.format(Float.parseFloat(documentosKilos7.getString("K02187"))));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb); 

            DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosKilos7.getString("K03187"));
            cadenaMar = String.valueOf(dfMar.format(Float.parseFloat(documentosKilos7.getString("K03187"))));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);

            DecimalFormat dfAbr = new DecimalFormat("###,###,###.00");
            String cadenaAbr = "";
            sumaAbr = sumaAbr + Float.parseFloat(documentosKilos7.getString("K04187"));
            cadenaAbr = String.valueOf(dfAbr.format(Float.parseFloat(documentosKilos7.getString("K04187"))));
            PdfPCell celdaAbr = new PdfPCell(new Phrase(cadenaAbr,cuerpo));
            celdaAbr.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAbr);

            DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
            String cadenaMayo = "";
            sumaMay = sumaMay + Float.parseFloat(documentosKilos7.getString("K05187"));
            cadenaMayo = String.valueOf(dfMayo.format(Float.parseFloat(documentosKilos7.getString("K05187"))));
            PdfPCell celdaMayo = new PdfPCell(new Phrase(cadenaMayo,cuerpo));
            celdaMayo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMayo);
            
            DecimalFormat dfTot = new DecimalFormat("###,###,###.00");
            sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosKilos7.getString("K01187"))+
                                              Float.parseFloat(documentosKilos7.getString("K02187"))+
                                              Float.parseFloat(documentosKilos7.getString("K03187"))+
                                              Float.parseFloat(documentosKilos7.getString("K04187"))+
                                              Float.parseFloat(documentosKilos7.getString("K05187"));
            sumT = sumT + sumaHorizontal;                                 
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
        }
        PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.00");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
        PdfPCell cellFebr = new PdfPCell(new Phrase(dfFeb.format(sumaFeb),fuente));
        cellFebr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellFebr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFebr);
        
        DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
        PdfPCell cellMarz = new PdfPCell(new Phrase(dfMar.format(sumaMar),fuente));
        cellMarz.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMarz.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMarz);
        
        DecimalFormat dfAbri = new DecimalFormat("###,###,###.00");
        PdfPCell cellAbri = new PdfPCell(new Phrase(dfAbri.format(sumaAbr),fuente));
        cellAbri.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAbri.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbri);
        
        DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
        PdfPCell cellMay = new PdfPCell(new Phrase(dfMayo.format(sumaMay),fuente));
        cellMay.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMay.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMay);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.00");
        PdfPCell cellT = new PdfPCell(new Phrase(dfT.format(sumT),fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        return tabla;
   }
   public static PdfPTable createTableEFMAMJK(int mes,ResultSet documentosKilos7,String sucursal,int ano) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(9);
        cuerpo.setSize(8);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(9);
        tabla.setWidths(new float[]{0.028f,0.11f,0.1f,0.1f,0.1f,0.1f,0.1f,0.1f,0.1f});
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("KILOS AÑO "+ano+" - "+sucursal+"",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(9);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Kilos Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Kilos Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Kilos Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);

        PdfPCell cellAbr = new PdfPCell(new Paragraph("Kilos Abril",fuente));
        cellAbr.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAbr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbr);

        PdfPCell cellMayo = new PdfPCell(new Paragraph("Kilos Mayo",fuente));
        cellMayo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMayo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMayo);

        PdfPCell cellJun = new PdfPCell(new Paragraph("Kilos Junio",fuente));
        cellJun.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJun.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJun);
        
        PdfPCell cellTot = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellTot.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTot.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTot);
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumaAbr = 0,sumaMay = 0,sumaJun = 0,sumT = 0;

        while(documentosKilos7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosKilos7.getString("FAM187"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosKilos7.getString("DES187"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);


            DecimalFormat dfEne = new DecimalFormat("###,###,###.00");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosKilos7.getString("K01187"));
            cadenaEne = String.valueOf(dfEne.format(Float.parseFloat(documentosKilos7.getString("K01187"))));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosKilos7.getString("K02187"));
            cadenaFeb = String.valueOf(dfFeb.format(Float.parseFloat(documentosKilos7.getString("K02187"))));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb); 

            DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosKilos7.getString("K03187"));
            cadenaMar = String.valueOf(dfMar.format(Float.parseFloat(documentosKilos7.getString("K03187"))));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);

            DecimalFormat dfAbr = new DecimalFormat("###,###,###.00");
            String cadenaAbr = "";
            sumaAbr = sumaAbr + Float.parseFloat(documentosKilos7.getString("K04187"));
            cadenaAbr = String.valueOf(dfAbr.format(Float.parseFloat(documentosKilos7.getString("K04187"))));
            PdfPCell celdaAbr = new PdfPCell(new Phrase(cadenaAbr,cuerpo));
            celdaAbr.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAbr);

            DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
            String cadenaMayo = "";
            sumaMay = sumaMay + Float.parseFloat(documentosKilos7.getString("K05187"));
            cadenaMayo = String.valueOf(dfMayo.format(Float.parseFloat(documentosKilos7.getString("K05187"))));
            PdfPCell celdaMayo = new PdfPCell(new Phrase(cadenaMayo,cuerpo));
            celdaMayo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMayo);

            DecimalFormat dfJun = new DecimalFormat("###,###,###.00");
            String cadenaJun = "";
            sumaJun = sumaJun + Float.parseFloat(documentosKilos7.getString("K06187"));
            cadenaJun = String.valueOf(dfJun.format(Float.parseFloat(documentosKilos7.getString("K06187"))));
            PdfPCell celdaJun = new PdfPCell(new Phrase(cadenaJun,cuerpo));
            celdaJun.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJun);
            
            DecimalFormat dfTot = new DecimalFormat("###,###,###.00");
            sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosKilos7.getString("K01187"))+
                                              Float.parseFloat(documentosKilos7.getString("K02187"))+
                                              Float.parseFloat(documentosKilos7.getString("K03187"))+
                                              Float.parseFloat(documentosKilos7.getString("K04187"))+
                                              Float.parseFloat(documentosKilos7.getString("K05187"))+
                                              Float.parseFloat(documentosKilos7.getString("K06187"));
            sumT = sumT + sumaHorizontal;
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
        }
        PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.00");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
        PdfPCell cellFebr = new PdfPCell(new Phrase(dfFeb.format(sumaFeb),fuente));
        cellFebr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellFebr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFebr);
        
        DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
        PdfPCell cellMarz = new PdfPCell(new Phrase(dfMar.format(sumaMar),fuente));
        cellMarz.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMarz.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMarz);
        
        DecimalFormat dfAbri = new DecimalFormat("###,###,###.00");
        PdfPCell cellAbri = new PdfPCell(new Phrase(dfAbri.format(sumaAbr),fuente));
        cellAbri.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAbri.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbri);
        
        DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
        PdfPCell cellMay = new PdfPCell(new Phrase(dfMayo.format(sumaMay),fuente));
        cellMay.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMay.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMay);
        
        DecimalFormat dfJunio = new DecimalFormat("###,###,###.00");
        PdfPCell cellJunio = new PdfPCell(new Phrase(dfJunio.format(sumaJun),fuente));
        cellJunio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJunio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJunio);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.00");
        PdfPCell cellT = new PdfPCell(new Phrase(dfT.format(sumT),fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        return tabla;
   }
   public static PdfPTable createTableEFMAMJJK(int mes,ResultSet documentosKilos7,String sucursal,int ano) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(9);
        cuerpo.setSize(8);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(10);
        tabla.setWidths(new float[]{0.033f,0.13f,0.1f,0.11f,0.1f,0.1f,0.1f,0.1f,0.1f,0.12f});
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("KILOS AÑO "+ano+" - "+sucursal+"",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(10);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Kilos Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Kilos Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Kilos Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);

        PdfPCell cellAbr = new PdfPCell(new Paragraph("Kilos Abril",fuente));
        cellAbr.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAbr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbr);

        PdfPCell cellMayo = new PdfPCell(new Paragraph("Kilos Mayo",fuente));
        cellMayo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMayo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMayo);

        PdfPCell cellJun = new PdfPCell(new Paragraph("Kilos Junio",fuente));
        cellJun.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJun.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJun);

        PdfPCell cellJul = new PdfPCell(new Paragraph("Kilos Julio",fuente));
        cellJul.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJul.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJul);
        
        PdfPCell cellTot = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellTot.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTot.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTot);
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumaAbr = 0,sumaMay = 0,sumaJun = 0,sumaJul = 0,sumT = 0;

        while(documentosKilos7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosKilos7.getString("FAM187"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosKilos7.getString("DES187"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);


            DecimalFormat dfEne = new DecimalFormat("###,###,###.00");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosKilos7.getString("K01187"));
            cadenaEne = String.valueOf(dfEne.format(Float.parseFloat(documentosKilos7.getString("K01187"))));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosKilos7.getString("K02187"));
            cadenaFeb = String.valueOf(dfFeb.format(Float.parseFloat(documentosKilos7.getString("K02187"))));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb); 

            DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosKilos7.getString("K03187"));
            cadenaMar = String.valueOf(dfMar.format(Float.parseFloat(documentosKilos7.getString("K03187"))));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);

            DecimalFormat dfAbr = new DecimalFormat("###,###,###.00");
            String cadenaAbr = "";
            sumaAbr = sumaAbr + Float.parseFloat(documentosKilos7.getString("K04187"));
            cadenaAbr = String.valueOf(dfAbr.format(Float.parseFloat(documentosKilos7.getString("K04187"))));
            PdfPCell celdaAbr = new PdfPCell(new Phrase(cadenaAbr,cuerpo));
            celdaAbr.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAbr);

            DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
            String cadenaMayo = "";
            sumaMay = sumaMay + Float.parseFloat(documentosKilos7.getString("K05187"));
            cadenaMayo = String.valueOf(dfMayo.format(Float.parseFloat(documentosKilos7.getString("K05187"))));
            PdfPCell celdaMayo = new PdfPCell(new Phrase(cadenaMayo,cuerpo));
            celdaMayo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMayo);

            DecimalFormat dfJun = new DecimalFormat("###,###,###.00");
            String cadenaJun = "";
            sumaJun = sumaJun + Float.parseFloat(documentosKilos7.getString("K06187"));
            cadenaJun = String.valueOf(dfJun.format(Float.parseFloat(documentosKilos7.getString("K06187"))));
            PdfPCell celdaJun = new PdfPCell(new Phrase(cadenaJun,cuerpo));
            celdaJun.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJun);

            DecimalFormat dfJul = new DecimalFormat("###,###,###.00");
            String cadenaJul = "";
            sumaJul = sumaJul + Float.parseFloat(documentosKilos7.getString("K07187"));
            cadenaJul = String.valueOf(dfJul.format(Float.parseFloat(documentosKilos7.getString("K07187"))));
            PdfPCell celdaJul = new PdfPCell(new Phrase(cadenaJul,cuerpo));
            celdaJul.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJul);
            
            DecimalFormat dfTot = new DecimalFormat("###,###,###.00");
            sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosKilos7.getString("K01187"))+
                                              Float.parseFloat(documentosKilos7.getString("K02187"))+
                                              Float.parseFloat(documentosKilos7.getString("K03187"))+
                                              Float.parseFloat(documentosKilos7.getString("K04187"))+
                                              Float.parseFloat(documentosKilos7.getString("K05187"))+
                                              Float.parseFloat(documentosKilos7.getString("K06187"))+
                                              Float.parseFloat(documentosKilos7.getString("K07187"));
            sumT = sumT + sumaHorizontal;
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
        }
        PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.00");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
        PdfPCell cellFebr = new PdfPCell(new Phrase(dfFeb.format(sumaFeb),fuente));
        cellFebr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellFebr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFebr);
        
        DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
        PdfPCell cellMarz = new PdfPCell(new Phrase(dfMar.format(sumaMar),fuente));
        cellMarz.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMarz.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMarz);
        
        DecimalFormat dfAbri = new DecimalFormat("###,###,###.00");
        PdfPCell cellAbri = new PdfPCell(new Phrase(dfAbri.format(sumaAbr),fuente));
        cellAbri.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAbri.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbri);
        
        DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
        PdfPCell cellMay = new PdfPCell(new Phrase(dfMayo.format(sumaMay),fuente));
        cellMay.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMay.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMay);
        
        DecimalFormat dfJunio = new DecimalFormat("###,###,###.00");
        PdfPCell cellJunio = new PdfPCell(new Phrase(dfJunio.format(sumaJun),fuente));
        cellJunio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJunio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJunio);
        
        DecimalFormat dfJulio = new DecimalFormat("###,###,###.00");
        PdfPCell cellJulio = new PdfPCell(new Phrase(dfJulio.format(sumaJul),fuente));
        cellJulio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJulio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJulio);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.00");
        PdfPCell cellT = new PdfPCell(new Phrase(dfT.format(sumT),fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        return tabla;
   }
   public static PdfPTable createTableEFMAMJJAK(int mes,ResultSet documentosKilos7,String sucursal,int ano) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(8);
        cuerpo.setSize(7);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(11);
        tabla.setWidths(new float[]{0.31f,1.2f,1,1,1,1,1,1,1,1,1});
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("KILOS AÑO "+ano+" - "+sucursal+"",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(11);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Kilos Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Kilos Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Kilos Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);

        PdfPCell cellAbr = new PdfPCell(new Paragraph("Kilos Abril",fuente));
        cellAbr.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAbr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbr);

        PdfPCell cellMayo = new PdfPCell(new Paragraph("Kilos Mayo",fuente));
        cellMayo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMayo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMayo);

        PdfPCell cellJun = new PdfPCell(new Paragraph("Kilos Junio",fuente));
        cellJun.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJun.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJun);

        PdfPCell cellJul = new PdfPCell(new Paragraph("Kilos Julio",fuente));
        cellJul.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJul.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJul);

        PdfPCell cellAgo = new PdfPCell(new Paragraph("Kilos Agosto",fuente));
        cellAgo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAgo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAgo);
        
        PdfPCell cellT = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumaAbr = 0,sumaMay = 0,sumaJun = 0,sumaJul = 0,sumaAgos = 0,sumT = 0;
        
        while(documentosKilos7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosKilos7.getString("FAM187"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosKilos7.getString("DES187"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);

            DecimalFormat dfEne = new DecimalFormat("###,###,###.00");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosKilos7.getString("K01187"));
            cadenaEne = String.valueOf(dfEne.format(Float.parseFloat(documentosKilos7.getString("K01187"))));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosKilos7.getString("K02187"));
            cadenaFeb = String.valueOf(dfFeb.format(Float.parseFloat(documentosKilos7.getString("K02187"))));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb); 

            DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosKilos7.getString("K03187"));
            cadenaMar = String.valueOf(dfMar.format(Float.parseFloat(documentosKilos7.getString("K03187"))));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);

            DecimalFormat dfAbr = new DecimalFormat("###,###,###.00");
            String cadenaAbr = "";
            sumaAbr = sumaAbr + Float.parseFloat(documentosKilos7.getString("K04187"));
            cadenaAbr = String.valueOf(dfAbr.format(Float.parseFloat(documentosKilos7.getString("K04187"))));
            PdfPCell celdaAbr = new PdfPCell(new Phrase(cadenaAbr,cuerpo));
            celdaAbr.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAbr);

            DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
            String cadenaMayo = "";
            sumaMay = sumaMay + Float.parseFloat(documentosKilos7.getString("K05187"));
            cadenaMayo = String.valueOf(dfMayo.format(Float.parseFloat(documentosKilos7.getString("K05187"))));
            PdfPCell celdaMayo = new PdfPCell(new Phrase(cadenaMayo,cuerpo));
            celdaMayo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMayo);

            DecimalFormat dfJun = new DecimalFormat("###,###,###.00");
            String cadenaJun = "";
            sumaJun = sumaJun + Float.parseFloat(documentosKilos7.getString("K06187"));
            cadenaJun = String.valueOf(dfJun.format(Float.parseFloat(documentosKilos7.getString("K06187"))));
            PdfPCell celdaJun = new PdfPCell(new Phrase(cadenaJun,cuerpo));
            celdaJun.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJun);

            DecimalFormat dfJul = new DecimalFormat("###,###,###.00");
            String cadenaJul = "";
            sumaJul = sumaJul + Float.parseFloat(documentosKilos7.getString("K07187"));
            cadenaJul = String.valueOf(dfJul.format(Float.parseFloat(documentosKilos7.getString("K07187"))));
            PdfPCell celdaJul = new PdfPCell(new Phrase(cadenaJul,cuerpo));
            celdaJul.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJul);
            
            DecimalFormat dfAgo = new DecimalFormat("###,###,###.00");
            String cadenaAgo = "";
            sumaAgos = sumaAgos + Float.parseFloat(documentosKilos7.getString("K08187"));
            cadenaAgo = String.valueOf(dfAgo.format(Float.parseFloat(documentosKilos7.getString("K08187"))));
            PdfPCell celdaAgo = new PdfPCell(new Phrase(cadenaAgo,cuerpo));
            celdaAgo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAgo);
            
            DecimalFormat dfTot = new DecimalFormat("###,###,###.00");
            sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosKilos7.getString("K01187")) + 
                                              Float.parseFloat(documentosKilos7.getString("K02187")) +
                                              Float.parseFloat(documentosKilos7.getString("K03187")) + 
                                              Float.parseFloat(documentosKilos7.getString("K04187")) +
                                              Float.parseFloat(documentosKilos7.getString("K05187")) + 
                                              Float.parseFloat(documentosKilos7.getString("K06187")) +
                                              Float.parseFloat(documentosKilos7.getString("K07187")) + 
                                              Float.parseFloat(documentosKilos7.getString("K08187"));
            sumT = sumT + sumaHorizontal;
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
        }
        PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.00");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
        PdfPCell cellFebr = new PdfPCell(new Phrase(dfFeb.format(sumaFeb),fuente));
        cellFebr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellFebr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFebr);
        
        DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
        PdfPCell cellMarz = new PdfPCell(new Phrase(dfMar.format(sumaMar),fuente));
        cellMarz.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMarz.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMarz);
        
        DecimalFormat dfAbri = new DecimalFormat("###,###,###.00");
        PdfPCell cellAbri = new PdfPCell(new Phrase(dfAbri.format(sumaAbr),fuente));
        cellAbri.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAbri.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbri);
        
        DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
        PdfPCell cellMay = new PdfPCell(new Phrase(dfMayo.format(sumaMay),fuente));
        cellMay.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMay.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMay);
        
        DecimalFormat dfJunio = new DecimalFormat("###,###,###.00");
        PdfPCell cellJunio = new PdfPCell(new Phrase(dfJunio.format(sumaJun),fuente));
        cellJunio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJunio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJunio);
        
        DecimalFormat dfJulio = new DecimalFormat("###,###,###.00");
        PdfPCell cellJulio = new PdfPCell(new Phrase(dfJulio.format(sumaJul),fuente));
        cellJulio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJulio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJulio);
        
        DecimalFormat dfAgos = new DecimalFormat("###,###,###.00");
        PdfPCell cellAgos = new PdfPCell(new Phrase(dfAgos.format(sumaAgos),fuente));
        cellAgos.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAgos.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAgos);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.00");
        PdfPCell cellTo = new PdfPCell(new Phrase(dfT.format(sumT),fuente));
        cellTo.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellTo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTo);
        return tabla;
   }
   public static PdfPTable createTableEFMAMJJASK(int mes,ResultSet documentosKilos7,String sucursal,int ano) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(7);
        cuerpo.setSize(6);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(12);
        tabla.setWidths(new float[]{0.5f,2,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.8f,1.5f});
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("KILOS AÑO "+ano+" - "+sucursal+"",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(12);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Kilos Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Kilos Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Kilos Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);

        PdfPCell cellAbr = new PdfPCell(new Paragraph("Kilos Abril",fuente));
        cellAbr.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAbr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbr);

        PdfPCell cellMayo = new PdfPCell(new Paragraph("Kilos Mayo",fuente));
        cellMayo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMayo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMayo);

        PdfPCell cellJun = new PdfPCell(new Paragraph("Kilos Junio",fuente));
        cellJun.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJun.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJun);

        PdfPCell cellJul = new PdfPCell(new Paragraph("Kilos Julio",fuente));
        cellJul.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJul.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJul);

        PdfPCell cellAgo = new PdfPCell(new Paragraph("Kilos Agosto",fuente));
        cellAgo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAgo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAgo);

        PdfPCell cellSep = new PdfPCell(new Paragraph("Kilos Septiembre",fuente));
        cellSep.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellSep.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellSep);
        
        PdfPCell cellT = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumaAbr = 0,sumaMay = 0,sumaJun = 0,sumaJul = 0,sumaAgos = 0,sumaSep = 0,sumT = 0;

        while(documentosKilos7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosKilos7.getString("FAM187"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosKilos7.getString("DES187"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);

            DecimalFormat dfEne = new DecimalFormat("###,###,###.00");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosKilos7.getString("K01187"));
            cadenaEne = String.valueOf(dfEne.format(Float.parseFloat(documentosKilos7.getString("K01187"))));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosKilos7.getString("K02187"));
            cadenaFeb = String.valueOf(dfFeb.format(Float.parseFloat(documentosKilos7.getString("K02187"))));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb); 

            DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosKilos7.getString("K03187"));
            cadenaMar = String.valueOf(dfMar.format(Float.parseFloat(documentosKilos7.getString("K03187"))));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);

            DecimalFormat dfAbr = new DecimalFormat("###,###,###.00");
            String cadenaAbr = "";
            sumaAbr = sumaAbr + Float.parseFloat(documentosKilos7.getString("K04187"));
            cadenaAbr = String.valueOf(dfAbr.format(Float.parseFloat(documentosKilos7.getString("K04187"))));
            PdfPCell celdaAbr = new PdfPCell(new Phrase(cadenaAbr,cuerpo));
            celdaAbr.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAbr);

            DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
            String cadenaMayo = "";
            sumaMay = sumaMay + Float.parseFloat(documentosKilos7.getString("K05187"));
            cadenaMayo = String.valueOf(dfMayo.format(Float.parseFloat(documentosKilos7.getString("K05187"))));
            PdfPCell celdaMayo = new PdfPCell(new Phrase(cadenaMayo,cuerpo));
            celdaMayo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMayo);

            DecimalFormat dfJun = new DecimalFormat("###,###,###.00");
            String cadenaJun = "";
            sumaJun = sumaJun + Float.parseFloat(documentosKilos7.getString("K06187"));
            cadenaJun = String.valueOf(dfJun.format(Float.parseFloat(documentosKilos7.getString("K06187"))));
            PdfPCell celdaJun = new PdfPCell(new Phrase(cadenaJun,cuerpo));
            celdaJun.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJun);

            DecimalFormat dfJul = new DecimalFormat("###,###,###.00");
            String cadenaJul = "";
            sumaJul = sumaJul + Float.parseFloat(documentosKilos7.getString("K07187"));
            cadenaJul = String.valueOf(dfJul.format(Float.parseFloat(documentosKilos7.getString("K07187"))));
            PdfPCell celdaJul = new PdfPCell(new Phrase(cadenaJul,cuerpo));
            celdaJul.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJul);

            DecimalFormat dfAgo = new DecimalFormat("###,###,###.00");
            String cadenaAgo = "";
            sumaAgos = sumaAgos + Float.parseFloat(documentosKilos7.getString("K08187"));
            cadenaAgo = String.valueOf(dfAgo.format(Float.parseFloat(documentosKilos7.getString("K08187"))));
            PdfPCell celdaAgo = new PdfPCell(new Phrase(cadenaAgo,cuerpo));
            celdaAgo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAgo);

            DecimalFormat dfSep = new DecimalFormat("###,###,###.00");
            String cadenaSep = "";
            sumaSep = sumaSep + Float.parseFloat(documentosKilos7.getString("K09187"));
            cadenaSep = String.valueOf(dfSep.format(Float.parseFloat(documentosKilos7.getString("K09187"))));
            PdfPCell celdaSep = new PdfPCell(new Phrase(cadenaSep,cuerpo));
            celdaSep.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaSep);
            
            DecimalFormat dfTot = new DecimalFormat("###,###,###.00");
            sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosKilos7.getString("K01187")) + 
                                              Float.parseFloat(documentosKilos7.getString("K02187")) +
                                              Float.parseFloat(documentosKilos7.getString("K03187")) + 
                                              Float.parseFloat(documentosKilos7.getString("K04187")) +
                                              Float.parseFloat(documentosKilos7.getString("K05187")) + 
                                              Float.parseFloat(documentosKilos7.getString("K06187")) +
                                              Float.parseFloat(documentosKilos7.getString("K07187")) + 
                                              Float.parseFloat(documentosKilos7.getString("K08187")) +
                                              Float.parseFloat(documentosKilos7.getString("K09187"));
            sumT = sumT + sumaHorizontal;
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
        }
        PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.00");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
        PdfPCell cellFebr = new PdfPCell(new Phrase(dfFeb.format(sumaFeb),fuente));
        cellFebr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellFebr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFebr);
        
        DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
        PdfPCell cellMarz = new PdfPCell(new Phrase(dfMar.format(sumaMar),fuente));
        cellMarz.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMarz.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMarz);
        
        DecimalFormat dfAbri = new DecimalFormat("###,###,###.00");
        PdfPCell cellAbri = new PdfPCell(new Phrase(dfAbri.format(sumaAbr),fuente));
        cellAbri.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAbri.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbri);
        
        DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
        PdfPCell cellMay = new PdfPCell(new Phrase(dfMayo.format(sumaMay),fuente));
        cellMay.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMay.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMay);
        
        DecimalFormat dfJunio = new DecimalFormat("###,###,###.00");
        PdfPCell cellJunio = new PdfPCell(new Phrase(dfJunio.format(sumaJun),fuente));
        cellJunio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJunio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJunio);
        
        DecimalFormat dfJulio = new DecimalFormat("###,###,###.00");
        PdfPCell cellJulio = new PdfPCell(new Phrase(dfJulio.format(sumaJul),fuente));
        cellJulio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJulio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJulio);
        
        DecimalFormat dfAgos = new DecimalFormat("###,###,###.00");
        PdfPCell cellAgos = new PdfPCell(new Phrase(dfAgos.format(sumaAgos),fuente));
        cellAgos.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAgos.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAgos);
        
        DecimalFormat dfSep = new DecimalFormat("###,###,###.00");
        PdfPCell cellSept = new PdfPCell(new Phrase(dfSep.format(sumaSep),fuente));
        cellSept.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellSept.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellSept);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.00");
        PdfPCell cellTo = new PdfPCell(new Phrase(dfT.format(sumT),fuente));
        cellTo.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellTo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTo);
        return tabla;
   }
   public static PdfPTable createTableEFMAMJJASOK(int mes,ResultSet documentosKilos7,String sucursal,int ano) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(7);
        cuerpo.setSize(6);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(13);
        tabla.setWidths(new float[]{0.5f,1.8f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.8f,1.2f,1.5f});
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("KILOS AÑO "+ano+" - "+sucursal+"",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(13);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Kilos Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Kilos Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Kilos Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);

        PdfPCell cellAbr = new PdfPCell(new Paragraph("Kilos Abril",fuente));
        cellAbr.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAbr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbr);

        PdfPCell cellMayo = new PdfPCell(new Paragraph("Kilos Mayo",fuente));
        cellMayo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMayo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMayo);

        PdfPCell cellJun = new PdfPCell(new Paragraph("Kilos Junio",fuente));
        cellJun.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJun.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJun);

        PdfPCell cellJul = new PdfPCell(new Paragraph("Kilos Julio",fuente));
        cellJul.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJul.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJul);

        PdfPCell cellAgo = new PdfPCell(new Paragraph("Kilos Agosto",fuente));
        cellAgo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAgo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAgo);

        PdfPCell cellSep = new PdfPCell(new Paragraph("Kilos Septiembre",fuente));
        cellSep.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellSep.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellSep);

        PdfPCell cellOct = new PdfPCell(new Paragraph("Kilos Octubre",fuente));
        cellOct.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellOct.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellOct);
        
        PdfPCell cellT = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumaAbr = 0,sumaMay = 0,sumaJun = 0,sumaJul = 0,sumaAgos = 0,sumaSep = 0,sumaOct = 0,sumT = 0;

        while(documentosKilos7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosKilos7.getString("FAM187"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosKilos7.getString("DES187"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);

            DecimalFormat dfEne = new DecimalFormat("###,###,###.00");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosKilos7.getString("K01187"));
            cadenaEne = String.valueOf(dfEne.format(Float.parseFloat(documentosKilos7.getString("K01187"))));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosKilos7.getString("K02187"));
            cadenaFeb = String.valueOf(dfFeb.format(Float.parseFloat(documentosKilos7.getString("K02187"))));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb); 

            DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosKilos7.getString("K03187"));
            cadenaMar = String.valueOf(dfMar.format(Float.parseFloat(documentosKilos7.getString("K03187"))));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);

            DecimalFormat dfAbr = new DecimalFormat("###,###,###.00");
            String cadenaAbr = "";
            sumaAbr = sumaAbr + Float.parseFloat(documentosKilos7.getString("K04187"));
            cadenaAbr = String.valueOf(dfAbr.format(Float.parseFloat(documentosKilos7.getString("K04187"))));
            PdfPCell celdaAbr = new PdfPCell(new Phrase(cadenaAbr,cuerpo));
            celdaAbr.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAbr);

            DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
            String cadenaMayo = "";
            sumaMay = sumaMay + Float.parseFloat(documentosKilos7.getString("K05187"));
            cadenaMayo = String.valueOf(dfMayo.format(Float.parseFloat(documentosKilos7.getString("K05187"))));
            PdfPCell celdaMayo = new PdfPCell(new Phrase(cadenaMayo,cuerpo));
            celdaMayo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMayo);

            DecimalFormat dfJun = new DecimalFormat("###,###,###.00");
            String cadenaJun = "";
            sumaJun = sumaJun + Float.parseFloat(documentosKilos7.getString("K06187"));
            cadenaJun = String.valueOf(dfJun.format(Float.parseFloat(documentosKilos7.getString("K06187"))));
            PdfPCell celdaJun = new PdfPCell(new Phrase(cadenaJun,cuerpo));
            celdaJun.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJun);

            DecimalFormat dfJul = new DecimalFormat("###,###,###.00");
            String cadenaJul = "";
            sumaJul = sumaJul + Float.parseFloat(documentosKilos7.getString("K07187"));
            cadenaJul = String.valueOf(dfJul.format(Float.parseFloat(documentosKilos7.getString("K07187"))));
            PdfPCell celdaJul = new PdfPCell(new Phrase(cadenaJul,cuerpo));
            celdaJul.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJul);

            DecimalFormat dfAgo = new DecimalFormat("###,###,###.00");
            String cadenaAgo = "";
            sumaAgos = sumaAgos + Float.parseFloat(documentosKilos7.getString("K08187"));
            cadenaAgo = String.valueOf(dfAgo.format(Float.parseFloat(documentosKilos7.getString("K08187"))));
            PdfPCell celdaAgo = new PdfPCell(new Phrase(cadenaAgo,cuerpo));
            celdaAgo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAgo);

            DecimalFormat dfSep = new DecimalFormat("###,###,###.00");
            String cadenaSep = "";
            sumaSep = sumaSep + Float.parseFloat(documentosKilos7.getString("K09187"));
            cadenaSep = String.valueOf(dfSep.format(Float.parseFloat(documentosKilos7.getString("K09187"))));
            PdfPCell celdaSep = new PdfPCell(new Phrase(cadenaSep,cuerpo));
            celdaSep.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaSep);

            DecimalFormat dfOct = new DecimalFormat("###,###,###.00");
            String cadenaOct = "";
            sumaOct = sumaOct + Float.parseFloat(documentosKilos7.getString("K10187"));
            cadenaOct = String.valueOf(dfOct.format(Float.parseFloat(documentosKilos7.getString("K10187"))));
            PdfPCell celdaOct = new PdfPCell(new Phrase(cadenaOct,cuerpo));
            celdaOct.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaOct);
            
            DecimalFormat dfTot = new DecimalFormat("###,###,###.00");
            sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosKilos7.getString("K01187")) + 
                                              Float.parseFloat(documentosKilos7.getString("K02187")) +
                                              Float.parseFloat(documentosKilos7.getString("K03187")) + 
                                              Float.parseFloat(documentosKilos7.getString("K04187")) +
                                              Float.parseFloat(documentosKilos7.getString("K05187")) + 
                                              Float.parseFloat(documentosKilos7.getString("K06187")) +
                                              Float.parseFloat(documentosKilos7.getString("K07187")) + 
                                              Float.parseFloat(documentosKilos7.getString("K08187")) +
                                              Float.parseFloat(documentosKilos7.getString("K09187")) +
                                              Float.parseFloat(documentosKilos7.getString("K10187"));
            sumT = sumT + sumaHorizontal;
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
        }
        PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.00");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
        PdfPCell cellFebr = new PdfPCell(new Phrase(dfFeb.format(sumaFeb),fuente));
        cellFebr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellFebr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFebr);
        
        DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
        PdfPCell cellMarz = new PdfPCell(new Phrase(dfMar.format(sumaMar),fuente));
        cellMarz.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMarz.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMarz);
        
        DecimalFormat dfAbri = new DecimalFormat("###,###,###.00");
        PdfPCell cellAbri = new PdfPCell(new Phrase(dfAbri.format(sumaAbr),fuente));
        cellAbri.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAbri.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbri);
        
        DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
        PdfPCell cellMay = new PdfPCell(new Phrase(dfMayo.format(sumaMay),fuente));
        cellMay.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMay.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMay);
        
        DecimalFormat dfJunio = new DecimalFormat("###,###,###.00");
        PdfPCell cellJunio = new PdfPCell(new Phrase(dfJunio.format(sumaJun),fuente));
        cellJunio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJunio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJunio);
        
        DecimalFormat dfJulio = new DecimalFormat("###,###,###.00");
        PdfPCell cellJulio = new PdfPCell(new Phrase(dfJulio.format(sumaJul),fuente));
        cellJulio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJulio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJulio);
        
        DecimalFormat dfAgos = new DecimalFormat("###,###,###.00");
        PdfPCell cellAgos = new PdfPCell(new Phrase(dfAgos.format(sumaAgos),fuente));
        cellAgos.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAgos.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAgos);
        
        DecimalFormat dfSep = new DecimalFormat("###,###,###.00");
        PdfPCell cellSept = new PdfPCell(new Phrase(dfSep.format(sumaSep),fuente));
        cellSept.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellSept.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellSept);
        
        DecimalFormat dfOct = new DecimalFormat("###,###,###.00");
        PdfPCell cellOctu = new PdfPCell(new Phrase(dfOct.format(sumaOct),fuente));
        cellOctu.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellOctu.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellOctu);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.00");
        PdfPCell cellTo = new PdfPCell(new Phrase(dfT.format(sumT),fuente));
        cellTo.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellTo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTo);
        return tabla;
   }
   public static PdfPTable createTableEFMAMJJASONK(int mes,ResultSet documentosKilos7,String sucursal,int ano) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(6);
        cuerpo.setSize(5);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(14);
        tabla.setWidths(new float[]{0.45f,1.5f,1.2f,1.3f,1.2f,1.2f,1.1f,1.2f,1.2f,1.3f,1.2f,1.2f,1.5f,1.5f});
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("KILOS AÑO "+ano+" - "+sucursal+"",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(14);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Kilos Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Kilos Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Kilos Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);

        PdfPCell cellAbr = new PdfPCell(new Paragraph("Kilos Abril",fuente));
        cellAbr.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAbr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbr);

        PdfPCell cellMayo = new PdfPCell(new Paragraph("Kilos Mayo",fuente));
        cellMayo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMayo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMayo);

        PdfPCell cellJun = new PdfPCell(new Paragraph("Kilos Junio",fuente));
        cellJun.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJun.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJun);

        PdfPCell cellJul = new PdfPCell(new Paragraph("Kilos Julio",fuente));
        cellJul.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJul.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJul);

        PdfPCell cellAgo = new PdfPCell(new Paragraph("Kilos Agosto",fuente));
        cellAgo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAgo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAgo);

        PdfPCell cellSep = new PdfPCell(new Paragraph("Kilos Septiembre",fuente));
        cellSep.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellSep.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellSep);

        PdfPCell cellOct = new PdfPCell(new Paragraph("Kilos Octubre",fuente));
        cellOct.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellOct.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellOct);

        PdfPCell cellNov = new PdfPCell(new Paragraph("Kilos Noviembre",fuente));
        cellNov.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellNov.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellNov);
        
        PdfPCell cellT = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumaAbr = 0,sumaMay = 0,sumaJun = 0,sumaJul = 0,sumaAgos = 0,sumaSep = 0,sumaOct = 0,sumaNov = 0,sumT = 0;

        while(documentosKilos7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosKilos7.getString("FAM187"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosKilos7.getString("DES187"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);

            DecimalFormat dfEne = new DecimalFormat("###,###,###.00");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosKilos7.getString("K01187"));
            cadenaEne = String.valueOf(dfEne.format(Float.parseFloat(documentosKilos7.getString("K01187"))));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosKilos7.getString("K02187"));
            cadenaFeb = String.valueOf(dfFeb.format(Float.parseFloat(documentosKilos7.getString("K02187"))));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb); 

            DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosKilos7.getString("K03187"));
            cadenaMar = String.valueOf(dfMar.format(Float.parseFloat(documentosKilos7.getString("K03187"))));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);

            DecimalFormat dfAbr = new DecimalFormat("###,###,###.00");
            String cadenaAbr = "";
            sumaAbr = sumaAbr + Float.parseFloat(documentosKilos7.getString("K04187"));
            cadenaAbr = String.valueOf(dfAbr.format(Float.parseFloat(documentosKilos7.getString("K04187"))));
            PdfPCell celdaAbr = new PdfPCell(new Phrase(cadenaAbr,cuerpo));
            celdaAbr.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAbr);

            DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
            String cadenaMayo = "";
            sumaMay = sumaMay + Float.parseFloat(documentosKilos7.getString("K05187"));
            cadenaMayo = String.valueOf(dfMayo.format(Float.parseFloat(documentosKilos7.getString("K05187"))));
            PdfPCell celdaMayo = new PdfPCell(new Phrase(cadenaMayo,cuerpo));
            celdaMayo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMayo);

            DecimalFormat dfJun = new DecimalFormat("###,###,###.00");
            String cadenaJun = "";
            sumaJun = sumaJun + Float.parseFloat(documentosKilos7.getString("K06187"));
            cadenaJun = String.valueOf(dfJun.format(Float.parseFloat(documentosKilos7.getString("K06187"))));
            PdfPCell celdaJun = new PdfPCell(new Phrase(cadenaJun,cuerpo));
            celdaJun.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJun);

            DecimalFormat dfJul = new DecimalFormat("###,###,###.00");
            String cadenaJul = "";
            sumaJul = sumaJul + Float.parseFloat(documentosKilos7.getString("K07187"));
            cadenaJul = String.valueOf(dfJul.format(Float.parseFloat(documentosKilos7.getString("K07187"))));
            PdfPCell celdaJul = new PdfPCell(new Phrase(cadenaJul,cuerpo));
            celdaJul.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJul);

            DecimalFormat dfAgo = new DecimalFormat("###,###,###.00");
            String cadenaAgo = "";
            sumaAgos = sumaAgos + Float.parseFloat(documentosKilos7.getString("K08187"));
            cadenaAgo = String.valueOf(dfAgo.format(Float.parseFloat(documentosKilos7.getString("K08187"))));
            PdfPCell celdaAgo = new PdfPCell(new Phrase(cadenaAgo,cuerpo));
            celdaAgo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAgo);

            DecimalFormat dfSep = new DecimalFormat("###,###,###.00");
            String cadenaSep = "";
            sumaSep = sumaSep + Float.parseFloat(documentosKilos7.getString("K09187"));
            cadenaSep = String.valueOf(dfSep.format(Float.parseFloat(documentosKilos7.getString("K09187"))));
            PdfPCell celdaSep = new PdfPCell(new Phrase(cadenaSep,cuerpo));
            celdaSep.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaSep);

            DecimalFormat dfOct = new DecimalFormat("###,###,###.00");
            String cadenaOct = "";
            sumaOct = sumaOct + Float.parseFloat(documentosKilos7.getString("K10187"));
            cadenaOct = String.valueOf(dfOct.format(Float.parseFloat(documentosKilos7.getString("K10187"))));
            PdfPCell celdaOct = new PdfPCell(new Phrase(cadenaOct,cuerpo));
            celdaOct.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaOct);

            DecimalFormat dfNov = new DecimalFormat("###,###,###.00");
            String cadenaNov = "";
            sumaNov = sumaNov + Float.parseFloat(documentosKilos7.getString("K11187"));
            cadenaNov = String.valueOf(dfNov.format(Float.parseFloat(documentosKilos7.getString("K11187"))));
            PdfPCell celdaNov = new PdfPCell(new Phrase(cadenaNov,cuerpo));
            celdaNov.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaNov);
            
            DecimalFormat dfTot = new DecimalFormat("###,###,###.00");
            sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosKilos7.getString("K01187")) + 
                                              Float.parseFloat(documentosKilos7.getString("K02187")) +
                                              Float.parseFloat(documentosKilos7.getString("K03187")) + 
                                              Float.parseFloat(documentosKilos7.getString("K04187")) +
                                              Float.parseFloat(documentosKilos7.getString("K05187")) + 
                                              Float.parseFloat(documentosKilos7.getString("K06187")) +
                                              Float.parseFloat(documentosKilos7.getString("K07187")) + 
                                              Float.parseFloat(documentosKilos7.getString("K08187")) +
                                              Float.parseFloat(documentosKilos7.getString("K09187")) +
                                              Float.parseFloat(documentosKilos7.getString("K10187")) +
                                              Float.parseFloat(documentosKilos7.getString("K11187"));
            sumT = sumT + sumaHorizontal;
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
        }
        PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.00");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
        PdfPCell cellFebr = new PdfPCell(new Phrase(dfFeb.format(sumaFeb),fuente));
        cellFebr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellFebr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFebr);
        
        DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
        PdfPCell cellMarz = new PdfPCell(new Phrase(dfMar.format(sumaMar),fuente));
        cellMarz.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMarz.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMarz);
        
        DecimalFormat dfAbri = new DecimalFormat("###,###,###.00");
        PdfPCell cellAbri = new PdfPCell(new Phrase(dfAbri.format(sumaAbr),fuente));
        cellAbri.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAbri.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbri);
        
        DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
        PdfPCell cellMay = new PdfPCell(new Phrase(dfMayo.format(sumaMay),fuente));
        cellMay.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMay.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMay);
        
        DecimalFormat dfJunio = new DecimalFormat("###,###,###.00");
        PdfPCell cellJunio = new PdfPCell(new Phrase(dfJunio.format(sumaJun),fuente));
        cellJunio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJunio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJunio);
        
        DecimalFormat dfJulio = new DecimalFormat("###,###,###.00");
        PdfPCell cellJulio = new PdfPCell(new Phrase(dfJulio.format(sumaJul),fuente));
        cellJulio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJulio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJulio);
        
        DecimalFormat dfAgos = new DecimalFormat("###,###,###.00");
        PdfPCell cellAgos = new PdfPCell(new Phrase(dfAgos.format(sumaAgos),fuente));
        cellAgos.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAgos.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAgos);
        
        DecimalFormat dfSep = new DecimalFormat("###,###,###.00");
        PdfPCell cellSept = new PdfPCell(new Phrase(dfSep.format(sumaSep),fuente));
        cellSept.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellSept.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellSept);
        
        DecimalFormat dfOct = new DecimalFormat("###,###,###.00");
        PdfPCell cellOctu = new PdfPCell(new Phrase(dfOct.format(sumaOct),fuente));
        cellOctu.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellOctu.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellOctu);
        
        DecimalFormat dfNov = new DecimalFormat("###,###,###.00");
        PdfPCell cellNovi = new PdfPCell(new Phrase(dfNov.format(sumaNov),fuente));
        cellNovi.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellNovi.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellNovi);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.00");
        PdfPCell cellTo = new PdfPCell(new Phrase(dfT.format(sumT),fuente));
        cellTo.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellTo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTo);
        return tabla;
   }
   public static PdfPTable createTableEFMAMJJASONDK(int mes,ResultSet documentosKilos7,String sucursal,int ano) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(6);
        cuerpo.setSize(5);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(15);
        tabla.setWidths(new float[]{0.5f,1.8f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.8f,1.5f,1.5f,1.5f,1.8f});
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("KILOS AÑO "+ano+" - "+sucursal+"",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(15);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Kilos Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Kilos Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Kilos Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);

        PdfPCell cellAbr = new PdfPCell(new Paragraph("Kilos Abril",fuente));
        cellAbr.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAbr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbr);

        PdfPCell cellMayo = new PdfPCell(new Paragraph("Kilos Mayo",fuente));
        cellMayo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMayo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMayo);

        PdfPCell cellJun = new PdfPCell(new Paragraph("Kilos Junio",fuente));
        cellJun.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJun.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJun);

        PdfPCell cellJul = new PdfPCell(new Paragraph("Kilos Julio",fuente));
        cellJul.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJul.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJul);

        PdfPCell cellAgo = new PdfPCell(new Paragraph("Kilos Agosto",fuente));
        cellAgo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAgo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAgo);

        PdfPCell cellSep = new PdfPCell(new Paragraph("Kilos Septiembre",fuente));
        cellSep.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellSep.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellSep);

        PdfPCell cellOct = new PdfPCell(new Paragraph("Kilos Octubre",fuente));
        cellOct.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellOct.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellOct);

        PdfPCell cellNov = new PdfPCell(new Paragraph("Kilos Noviembre",fuente));
        cellNov.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellNov.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellNov);

        PdfPCell cellDic = new PdfPCell(new Paragraph("Kilos Diciembre",fuente));
        cellDic.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDic.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDic);
        
        PdfPCell cellT = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumaAbr = 0,sumaMay = 0,sumaJun = 0,sumaJul = 0,sumaAgos = 0,sumaSep = 0,sumaOct = 0,sumaNov = 0,sumaDic = 0,sumT = 0;

         while(documentosKilos7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosKilos7.getString("FAM187"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosKilos7.getString("DES187"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);

            DecimalFormat dfEne = new DecimalFormat("###,###,###.00");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosKilos7.getString("K01187"));
            cadenaEne = String.valueOf(dfEne.format(Float.parseFloat(documentosKilos7.getString("K01187"))));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosKilos7.getString("K02187"));
            cadenaFeb = String.valueOf(dfFeb.format(Float.parseFloat(documentosKilos7.getString("K02187"))));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb); 

            DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosKilos7.getString("K03187"));
            cadenaMar = String.valueOf(dfMar.format(Float.parseFloat(documentosKilos7.getString("K03187"))));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);

            DecimalFormat dfAbr = new DecimalFormat("###,###,###.00");
            String cadenaAbr = "";
            sumaAbr = sumaAbr + Float.parseFloat(documentosKilos7.getString("K04187"));
            cadenaAbr = String.valueOf(dfAbr.format(Float.parseFloat(documentosKilos7.getString("K04187"))));
            PdfPCell celdaAbr = new PdfPCell(new Phrase(cadenaAbr,cuerpo));
            celdaAbr.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAbr);

            DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
            String cadenaMayo = "";
            sumaMay = sumaMay + Float.parseFloat(documentosKilos7.getString("K05187"));
            cadenaMayo = String.valueOf(dfMayo.format(Float.parseFloat(documentosKilos7.getString("K05187"))));
            PdfPCell celdaMayo = new PdfPCell(new Phrase(cadenaMayo,cuerpo));
            celdaMayo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMayo);

            DecimalFormat dfJun = new DecimalFormat("###,###,###.00");
            String cadenaJun = "";
            sumaJun = sumaJun + Float.parseFloat(documentosKilos7.getString("K06187"));
            cadenaJun = String.valueOf(dfJun.format(Float.parseFloat(documentosKilos7.getString("K06187"))));
            PdfPCell celdaJun = new PdfPCell(new Phrase(cadenaJun,cuerpo));
            celdaJun.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJun);

            DecimalFormat dfJul = new DecimalFormat("###,###,###.00");
            String cadenaJul = "";
            sumaJul = sumaJul + Float.parseFloat(documentosKilos7.getString("K07187"));
            cadenaJul = String.valueOf(dfJul.format(Float.parseFloat(documentosKilos7.getString("K07187"))));
            PdfPCell celdaJul = new PdfPCell(new Phrase(cadenaJul,cuerpo));
            celdaJul.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJul);

            DecimalFormat dfAgo = new DecimalFormat("###,###,###.00");
            String cadenaAgo = "";
            sumaAgos = sumaAgos + Float.parseFloat(documentosKilos7.getString("K08187"));
            cadenaAgo = String.valueOf(dfAgo.format(Float.parseFloat(documentosKilos7.getString("K08187"))));
            PdfPCell celdaAgo = new PdfPCell(new Phrase(cadenaAgo,cuerpo));
            celdaAgo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAgo);

            DecimalFormat dfSep = new DecimalFormat("###,###,###.00");
            String cadenaSep = "";
            sumaSep = sumaSep + Float.parseFloat(documentosKilos7.getString("K09187"));
            cadenaSep = String.valueOf(dfSep.format(Float.parseFloat(documentosKilos7.getString("K09187"))));
            PdfPCell celdaSep = new PdfPCell(new Phrase(cadenaSep,cuerpo));
            celdaSep.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaSep);

            DecimalFormat dfOct = new DecimalFormat("###,###,###.00");
            String cadenaOct = "";
            sumaOct = sumaOct + Float.parseFloat(documentosKilos7.getString("K10187"));
            cadenaOct = String.valueOf(dfOct.format(Float.parseFloat(documentosKilos7.getString("K10187"))));
            PdfPCell celdaOct = new PdfPCell(new Phrase(cadenaOct,cuerpo));
            celdaOct.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaOct);

            DecimalFormat dfNov = new DecimalFormat("###,###,###.00");
            String cadenaNov = "";
            sumaNov = sumaNov + Float.parseFloat(documentosKilos7.getString("K11187"));
            cadenaNov = String.valueOf(dfNov.format(Float.parseFloat(documentosKilos7.getString("K11187"))));
            PdfPCell celdaNov = new PdfPCell(new Phrase(cadenaNov,cuerpo));
            celdaNov.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaNov);

            DecimalFormat dfDic = new DecimalFormat("###,###,###.00");
            String cadenaDic = "";
            sumaDic = sumaDic + Float.parseFloat(documentosKilos7.getString("K12187"));
            cadenaDic = String.valueOf(dfDic.format(Float.parseFloat(documentosKilos7.getString("K12187"))));
            PdfPCell celdaDic = new PdfPCell(new Phrase(cadenaDic,cuerpo));
            celdaDic.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaDic);
            
            DecimalFormat dfTot = new DecimalFormat("###,###,###.00");
            sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosKilos7.getString("K01187")) + 
                                              Float.parseFloat(documentosKilos7.getString("K02187")) +
                                              Float.parseFloat(documentosKilos7.getString("K03187")) + 
                                              Float.parseFloat(documentosKilos7.getString("K04187")) +
                                              Float.parseFloat(documentosKilos7.getString("K05187")) + 
                                              Float.parseFloat(documentosKilos7.getString("K06187")) +
                                              Float.parseFloat(documentosKilos7.getString("K07187")) + 
                                              Float.parseFloat(documentosKilos7.getString("K08187")) +
                                              Float.parseFloat(documentosKilos7.getString("K09187")) +
                                              Float.parseFloat(documentosKilos7.getString("K10187")) +
                                              Float.parseFloat(documentosKilos7.getString("K11187")) +
                                              Float.parseFloat(documentosKilos7.getString("K12187"));
            sumT = sumT +sumaHorizontal;
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
        }    
        PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.00");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
        PdfPCell cellFebr = new PdfPCell(new Phrase(dfFeb.format(sumaFeb),fuente));
        cellFebr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellFebr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFebr);
        
        DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
        PdfPCell cellMarz = new PdfPCell(new Phrase(dfMar.format(sumaMar),fuente));
        cellMarz.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMarz.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMarz);
        
        DecimalFormat dfAbri = new DecimalFormat("###,###,###.00");
        PdfPCell cellAbri = new PdfPCell(new Phrase(dfAbri.format(sumaAbr),fuente));
        cellAbri.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAbri.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbri);
        
        DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
        PdfPCell cellMay = new PdfPCell(new Phrase(dfMayo.format(sumaMay),fuente));
        cellMay.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMay.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMay);
        
        DecimalFormat dfJunio = new DecimalFormat("###,###,###.00");
        PdfPCell cellJunio = new PdfPCell(new Phrase(dfJunio.format(sumaJun),fuente));
        cellJunio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJunio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJunio);
        
        DecimalFormat dfJulio = new DecimalFormat("###,###,###.00");
        PdfPCell cellJulio = new PdfPCell(new Phrase(dfJulio.format(sumaJul),fuente));
        cellJulio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJulio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJulio);
        
        DecimalFormat dfAgos = new DecimalFormat("###,###,###.00");
        PdfPCell cellAgos = new PdfPCell(new Phrase(dfAgos.format(sumaAgos),fuente));
        cellAgos.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAgos.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAgos);
        
        DecimalFormat dfSep = new DecimalFormat("###,###,###.00");
        PdfPCell cellSept = new PdfPCell(new Phrase(dfSep.format(sumaSep),fuente));
        cellSept.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellSept.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellSept);
        
        DecimalFormat dfOct = new DecimalFormat("###,###,###.00");
        PdfPCell cellOctu = new PdfPCell(new Phrase(dfOct.format(sumaOct),fuente));
        cellOctu.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellOctu.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellOctu);
        
        DecimalFormat dfNov = new DecimalFormat("###,###,###.00");
        PdfPCell cellNovi = new PdfPCell(new Phrase(dfNov.format(sumaNov),fuente));
        cellNovi.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellNovi.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellNovi);
        
        DecimalFormat dfDic = new DecimalFormat("###,###,###.00");
        PdfPCell cellDici = new PdfPCell(new Phrase(dfDic.format(sumaDic),fuente));
        cellDici.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellDici.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDici);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.00");
        PdfPCell cellTo = new PdfPCell(new Phrase(dfT.format(sumT),fuente));
        cellTo.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellTo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTo);
        return tabla;
   }
    //////////////////////////////////////////////////////////////////////
   
   
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        try{
            int mes = 0,sucursal = 0;
            String sucursalS = null,sucursalS1 = null;
            int Ano = Integer.parseInt(jTextField2.getText());
            if(jComboBox1.getSelectedItem().equals("Enero"))
                mes = 1;
            else if(jComboBox1.getSelectedItem().equals("Febrero"))
                mes = 2;
            else if(jComboBox1.getSelectedItem().equals("Marzo"))
                mes = 3;
            else if(jComboBox1.getSelectedItem().equals("Abril"))
                mes = 4;
            else if(jComboBox1.getSelectedItem().equals("Mayo"))
                mes = 5;
            else if(jComboBox1.getSelectedItem().equals("Junio"))
                mes = 6;
            else if(jComboBox1.getSelectedItem().equals("Julio"))
                mes = 7;
            else if(jComboBox1.getSelectedItem().equals("Agosto"))
                mes = 8;
            else if(jComboBox1.getSelectedItem().equals("Septiembre"))
                mes = 9;
            else if(jComboBox1.getSelectedItem().equals("Octubre"))
                mes = 10;
            else if(jComboBox1.getSelectedItem().equals("Noviembre"))
                mes = 11;
            else if(jComboBox1.getSelectedItem().equals("Diciembre"))
                mes = 12;
            
            
            if(jComboBox2.getSelectedItem().equals("01 - Santiago")){
                sucursal = 1;
                sucursalS1 = "01 - Santiago";
                sucursalS = "SUCURSAL SANTIAGO";
            }
            else if(jComboBox2.getSelectedItem().equals("02 - Temuco")){
                sucursal = 2;
                sucursalS1 = "02 - Temuco";
                sucursalS = "SUCURSAL TEMUCO";
            }
            else if(jComboBox2.getSelectedItem().equals("03 - Puerto Montt")){
                sucursal = 3;
                sucursalS1 = "03 - Puerto Montt";
                sucursalS = "SUCURSAL PUERTO MONTT";
            }
            else if(jComboBox2.getSelectedItem().equals("04 - Viña")){
                sucursal = 4;
                sucursalS1 = "04 - Viña";
                sucursalS = "SUCURSAL VIÑA";
            }
            else if(jComboBox2.getSelectedItem().equals("05 - Antofagasta")){
                sucursal = 5;
                sucursalS1 = "05 - Antofagasta";
                sucursalS = "SUCURSAL ANTOFAGASTA";
            }
            else if(jComboBox2.getSelectedItem().equals("06 - Hualpén")){
                sucursal = 6;
                sucursalS1 = "06 - Hualpén";
                sucursalS = "SUCURSAL HUALPÉN";
            }
            else if(jComboBox2.getSelectedItem().equals("07 - Copiapó")){
                sucursal = 7;
                sucursalS1 = "07 - Copiapó";
                sucursalS = "SUCURSAL COPIAPÓ";
            }
            else if(jComboBox2.getSelectedItem().equals("08 - San Pedro")){
                sucursal = 8;
                sucursalS1 = "08 - San Pedro";
                sucursalS = "SUCURSAL SAN PEDRO";
            }
            else if(jComboBox2.getSelectedItem().equals("10 - Rancagua")){
                sucursal = 10;
                sucursalS1 = "10 - Rancagua";
                sucursalS = "SUCURSAL RANCAGUA";
            }
           
            
            
            calendarioPDF obtenerFechas = new calendarioPDF();
            Calendar fecha = Calendar.getInstance();
            int anoA = fecha.get(Calendar.YEAR);
            int mesA = fecha.get(Calendar.MONTH);
            int diaA = fecha.get(Calendar.DAY_OF_MONTH);
            int hora = fecha.get(Calendar.HOUR_OF_DAY);
            int minuto = fecha.get(Calendar.MINUTE);
            int segundo = fecha.get(Calendar.SECOND);
            int anoTec = Integer.parseInt(jTextField2.getText());
            mesA = mesA + 1;
            int anoActual = obtenerFechas.anoActual();
            System.out.println(mesA);
            System.out.println(anoActual);
            if((mes < 1)||(mes >12))
                System.out.println("Error en el mes");
                //valida año (cambiar por variable cargada con año actual).
            else if(Ano > anoActual)
                System.out.println("Error en el Año");
                //procesa si no hay errores.
            else{
                int fechaInicialPower = obtenerFechas.fechaMinimaPower(Integer.toString(mes), jTextField2.getText());
                int fechaFinalPower = obtenerFechas.fechaMaximaPower(Integer.toString(mes), jTextField2.getText());
                //procesa
                sql consultaPower = new sql();
                sql consultaPower2 = new sql();
                sqlKilos consultaKilos = new sqlKilos();
                sqlKilos consultaKilos2 = new sqlKilos();
                
                Document documento = new Document(PageSize.LETTER.rotate());
                
               
                documento.setMargins(-70, -70, 20, 20);
                
                ResultSet documentosPower7 = consultaPower.documentosPower(fechaInicialPower, fechaFinalPower,mes,Ano,sucursal);
                ResultSet documentosKilos = consultaKilos.documentosKilos(fechaInicialPower, fechaFinalPower, mes, Ano,sucursal);
                ResultSet documentosPower72 = consultaPower2.documentosPower(fechaInicialPower, fechaFinalPower, mes, Ano,sucursal);   
                ResultSet documentosKilos2 = consultaKilos2.documentosKilos(fechaInicialPower, fechaFinalPower, mes, Ano,sucursal);
                
                String path = "C:\\\\reportesSucursal\\\\"+anoA+"_"+mesA+"_"+diaA+"_"+hora+"_"+minuto+"_"+segundo+".pdf";
                String nombre = ""+anoA+"_"+mesA+"_"+diaA+"_"+hora+"_"+minuto+"_"+segundo+".pdf";
                FileOutputStream ficheroPdf; 
                try 
                {
                    File fichero = new File("C:/reportesSucursal");
                    if(fichero.exists()){
                        ficheroPdf = new FileOutputStream(path);
                        PdfWriter.getInstance(documento, ficheroPdf).setInitialLeading(400);
                    }
                    else{
                        fichero.mkdir();
                        ficheroPdf = new FileOutputStream(path);
                        PdfWriter.getInstance(documento, ficheroPdf).setInitialLeading(400);
                    }
                   
                    
                }
                catch (Exception ex) 
                {
                    System.out.println(ex.toString());
                }
                try{
                     if(mes == 01 && jComboBox2.getSelectedItem().equals(sucursalS1)){
                            documento.open();
                            PdfPTable tabla = createTableEneroV(mes,documentosPower7,sucursalS,anoTec);
                            documento.add(tabla);
                            tabla = createTableEneroK(mes,documentosKilos,sucursalS,anoTec);                           
                            tabla.setSpacingBefore(30);
                            tabla.setSpacingAfter(30);  
                            documento.add(tabla);
                            tabla = createMiniTable(diaA, mesA, anoA);
                            tabla.setSpacingBefore(30);
                            tabla.setSpacingAfter(30);
                            documento.add(tabla);
                            tabla = createTableEneroD(mes, documentosPower72,documentosKilos2,sucursalS,anoTec);
                            tabla.setSpacingBefore(50);
                            tabla.setSpacingAfter(50);
                            documento.add(tabla);
                            
                            documento.close();
                            JOptionPane.showMessageDialog(null,"Se creó el pdf");
                            //envioCorreo.envio(path, nombre);
                            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+path);
                     }
                     else{
                         if(mes == 02 && jComboBox2.getSelectedItem().equals(sucursalS1)){
                            documento.open();
                            PdfPTable tabla = createTableEFV(mes,documentosPower7,sucursalS,anoTec);
                            documento.add(tabla);
                            tabla = createTableEFK(mes,documentosKilos,sucursalS,anoTec);                           
                            tabla.setSpacingBefore(30);
                            tabla.setSpacingAfter(30);  
                            documento.add(tabla); 
                            tabla = createMiniTable(diaA, mesA, anoA);
                            tabla.setSpacingBefore(30);
                            tabla.setSpacingAfter(30);
                            documento.add(tabla);
                            tabla = createTableFD(mes, documentosPower72,documentosKilos2,sucursalS,anoTec);
                            tabla.setSpacingBefore(50);
                            tabla.setSpacingAfter(50);
                            documento.add(tabla);
                            documento.close();
                            JOptionPane.showMessageDialog(null,"Se creó el pdf");
                            //envioCorreo.envio(path, nombre);
                            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+path);
                         }   
                         else{
                             if(mes == 03 && jComboBox2.getSelectedItem().equals(sucursalS1)){
                                documento.open();
                                PdfPTable tabla = createTableEFMV(mes,documentosPower7,sucursalS,anoTec);
                                documento.add(tabla);
                                tabla = createTableEFMK(mes,documentosKilos,sucursalS,anoTec);                           
                                tabla.setSpacingBefore(30);
                                tabla.setSpacingAfter(30);  
                                documento.add(tabla);
                                tabla = createMiniTable(diaA, mesA, anoA);
                                tabla.setSpacingBefore(30);
                                tabla.setSpacingAfter(30);
                                documento.add(tabla);
                                tabla = createTableMD(mes, documentosPower72,documentosKilos2,sucursalS,anoTec);
                                tabla.setSpacingBefore(50);
                                tabla.setSpacingAfter(50);
                                documento.add(tabla);
                                documento.close();
                                JOptionPane.showMessageDialog(null,"Se creó el pdf");
                                //envioCorreo.envio(path, nombre);
                                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+path);
                             }
                             else{
                                 if(mes == 04 && jComboBox2.getSelectedItem().equals(sucursalS1)){
                                    documento.open();
                                    PdfPTable tabla = createTableEFMAV(mes,documentosPower7,sucursalS,anoTec);
                                    documento.add(tabla);
                                    tabla = createTableEFMAK(mes,documentosKilos,sucursalS,anoTec);                           
                                    tabla.setSpacingBefore(30);
                                    tabla.setSpacingAfter(30);  
                                    documento.add(tabla);
                                    tabla = createMiniTable(diaA, mesA, anoA);
                                    tabla.setSpacingBefore(30);
                                    tabla.setSpacingAfter(30);
                                    documento.add(tabla);
                                    tabla = createTableAD(mes, documentosPower72,documentosKilos2,sucursalS,anoTec);
                                    tabla.setSpacingBefore(50);
                                    tabla.setSpacingAfter(50);
                                    documento.add(tabla);
                                    documento.close();
                                    JOptionPane.showMessageDialog(null,"Se creó el pdf");
                                    //envioCorreo.envio(path, nombre);
                                    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+path);
                                 }
                                 else{
                                     if(mes == 05 && jComboBox2.getSelectedItem().equals(sucursalS1)){
                                        documento.open();
                                        PdfPTable tabla = createTableEFMAMV(mes,documentosPower7,sucursalS,anoTec);
                                        documento.add(tabla);
                                        tabla = createTableEFMAMK(mes,documentosKilos,sucursalS,anoTec);                           
                                        tabla.setSpacingBefore(30);
                                        tabla.setSpacingAfter(30);  
                                        documento.add(tabla);
                                        tabla = createMiniTable(diaA, mesA, anoA);
                                        tabla.setSpacingBefore(30);
                                        tabla.setSpacingAfter(30);
                                        documento.add(tabla);
                                        tabla = createTableMayD(mes, documentosPower72,documentosKilos2,sucursalS,anoTec);
                                        tabla.setSpacingBefore(50);
                                        tabla.setSpacingAfter(50);
                                        documento.add(tabla);
                                        documento.close();
                                        JOptionPane.showMessageDialog(null,"Se creó el pdf");
                                        //envioCorreo.envio(path, nombre);
                                        Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+path);
                                     }
                                     else{
                                         if(mes == 06 && jComboBox2.getSelectedItem().equals(sucursalS1)){
                                            documento.open();
                                            PdfPTable tabla = createTableEFMAMJV(mes,documentosPower7,sucursalS,anoTec);
                                            documento.add(tabla);
                                            tabla = createTableEFMAMJK(mes,documentosKilos,sucursalS,anoTec);                           
                                            tabla.setSpacingBefore(30);
                                            tabla.setSpacingAfter(30);  
                                            documento.add(tabla);
                                            tabla = createMiniTable(diaA, mesA, anoA);
                                            tabla.setSpacingBefore(30);
                                            tabla.setSpacingAfter(30);
                                            documento.add(tabla);
                                            tabla = createTableJD(mes, documentosPower72,documentosKilos2,sucursalS,anoTec);
                                            tabla.setSpacingBefore(50);
                                            tabla.setSpacingAfter(50);
                                            documento.add(tabla);
                                            documento.close();
                                            JOptionPane.showMessageDialog(null,"Se creó el pdf");
                                            //envioCorreo.envio(path, nombre);
                                            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+path);
                                         }
                                         else{
                                             if(mes == 07 && jComboBox2.getSelectedItem().equals(sucursalS1)){
                                                documento.open();
                                                PdfPTable tabla = createTableEFMAMJJV(mes,documentosPower7,sucursalS,anoTec);
                                                documento.add(tabla);
                                                tabla = createTableEFMAMJJK(mes,documentosKilos,sucursalS,anoTec);                           
                                                tabla.setSpacingBefore(30);
                                                tabla.setSpacingAfter(30);  
                                                documento.add(tabla);
                                                tabla = createMiniTable(diaA, mesA, anoA);
                                                tabla.setSpacingBefore(30);
                                                tabla.setSpacingAfter(30);
                                                documento.add(tabla);    
                                                tabla = createTableJulD(mes, documentosPower72,documentosKilos2,sucursalS,anoTec);
                                                tabla.setSpacingBefore(50);
                                                tabla.setSpacingAfter(50);
                                                documento.add(tabla);
                                                documento.close();
                                                JOptionPane.showMessageDialog(null,"Se creó el pdf");
                                                //envioCorreo.envio(path, nombre);
                                                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+path);
                                             }
                                             else{
                                                 if(mes == 8 && jComboBox2.getSelectedItem().equals(sucursalS1)){
                                                    documento.open();
                                                    PdfPTable tabla = createTableEFMAMJJAV(mes,documentosPower7,sucursalS,anoTec);
                                                    documento.add(tabla);
                                                    tabla = createTableEFMAMJJAK(mes,documentosKilos,sucursalS,anoTec);                           
                                                    tabla.setSpacingBefore(30);
                                                    tabla.setSpacingAfter(30);  
                                                    documento.add(tabla);
                                                    tabla = createMiniTable(diaA, mesA, anoA);
                                                    tabla.setSpacingBefore(30);
                                                    tabla.setSpacingAfter(30);
                                                    documento.add(tabla);
                                                    tabla = createTableAgoD(mes, documentosPower72,documentosKilos2,sucursalS,anoTec);
                                                    tabla.setSpacingBefore(50);
                                                    tabla.setSpacingAfter(50);
                                                    documento.add(tabla);
                                                    documento.close();
                                                    JOptionPane.showMessageDialog(null,"Se creó el pdf");
                                                    //envioCorreo.envio(path, nombre);
                                                    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+path);
                                                 }
                                                 else{
                                                     if(mes == 9 && jComboBox2.getSelectedItem().equals(sucursalS1)){
                                                        documento.open();
                                                        PdfPTable tabla = createTableEFMAMJJASV(mes,documentosPower7,sucursalS,anoTec);
                                                        documento.add(tabla);
                                                        tabla = createTableEFMAMJJASK(mes,documentosKilos,sucursalS,anoTec);                           
                                                        tabla.setSpacingBefore(30);
                                                        tabla.setSpacingAfter(30);  
                                                        documento.add(tabla);
                                                        tabla = createMiniTable(diaA, mesA, anoA);
                                                        tabla.setSpacingBefore(50);
                                                        tabla.setSpacingAfter(50);
                                                        documento.add(tabla);
                                                        tabla = createTableSD(mes, documentosPower72,documentosKilos2,sucursalS,anoTec);
                                                        tabla.setSpacingBefore(50);
                                                        tabla.setSpacingAfter(50);
                                                        documento.add(tabla);
                                                        documento.close();
                                                        JOptionPane.showMessageDialog(null,"Se creó el pdf");
                                                        //envioCorreo.envio(path, nombre);
                                                        Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+path);
                                                     }
                                                     else{
                                                         if(mes == 10 && jComboBox2.getSelectedItem().equals(sucursalS1)){
                                                            documento.setMargins(-90, -90, 25, 20);
                                                            documento.open();
                                                            PdfPTable tabla = createTableEFMAMJJASOV(mes,documentosPower7,sucursalS,anoTec);
                                                            documento.add(tabla);
                                                            tabla = createTableEFMAMJJASOK(mes,documentosKilos,sucursalS,anoTec);                           
                                                            tabla.setSpacingBefore(30);
                                                            tabla.setSpacingAfter(30);  
                                                            documento.add(tabla); 
                                                            tabla = createMiniTable(diaA, mesA, anoA);
                                                            tabla.setSpacingBefore(50);
                                                            tabla.setSpacingAfter(50);
                                                            documento.add(tabla);
                                                            tabla = createTableOD(mes, documentosPower72,documentosKilos2,sucursalS,anoTec);
                                                            tabla.setSpacingBefore(50);
                                                            tabla.setSpacingAfter(50);
                                                            documento.add(tabla);
                                                            documento.close();
                                                            JOptionPane.showMessageDialog(null,"Se creó el pdf");
                                                            //envioCorreo.envio(path, nombre);
                                                            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+path);
                                                         }
                                                         else{
                                                             if(mes == 11 && jComboBox2.getSelectedItem().equals(sucursalS1)){
                                                                documento.setMargins(-97, -97, 25, 20); 
                                                                documento.open();
                                                                PdfPTable tabla = createTableEFMAMJJASONV(mes,documentosPower7,sucursalS,anoTec);
                                                                documento.add(tabla);
                                                                tabla = createTableEFMAMJJASONK(mes,documentosKilos,sucursalS,anoTec);                           
                                                                tabla.setSpacingBefore(40);
                                                                tabla.setSpacingAfter(40);  
                                                                documento.add(tabla);
                                                                tabla = createMiniTable(diaA, mesA, anoA);
                                                                tabla.setSpacingBefore(60);
                                                                tabla.setSpacingAfter(60);
                                                                documento.add(tabla);
                                                                tabla = createTableND(mes, documentosPower72,documentosKilos2,sucursalS,anoTec);
                                                                tabla.setSpacingBefore(50);
                                                                tabla.setSpacingAfter(50);
                                                                documento.add(tabla);
                                                                documento.close();
                                                                JOptionPane.showMessageDialog(null,"Se creó el pdf");
                                                                //envioCorreo.envio(path, nombre);
                                                                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+path);
                                                             }
                                                             else{
                                                                 if(mes == 12 && jComboBox2.getSelectedItem().equals(sucursalS1)){
                                                                    documento.setMargins(-97, -97, 25, 20); 
                                                                    documento.open();
                                                                    PdfPTable tabla = createTableEFMAMJJASONDV(mes,documentosPower7,sucursalS,anoTec);
                                                                    documento.add(tabla);
                                                                    tabla = createTableEFMAMJJASONDK(mes,documentosKilos,sucursalS,anoTec);                           
                                                                    tabla.setSpacingBefore(40);
                                                                    tabla.setSpacingAfter(40);  
                                                                    documento.add(tabla);
                                                                    tabla = createMiniTable(diaA, mesA, anoA);
                                                                    tabla.setSpacingBefore(60);
                                                                    tabla.setSpacingAfter(60);
                                                                    documento.add(tabla);
                                                                    tabla = createTableDD(mes, documentosPower72,documentosKilos2,sucursalS,anoTec);
                                                                    tabla.setSpacingBefore(50);
                                                                    tabla.setSpacingAfter(50);
                                                                    documento.add(tabla);
                                                                    documento.close();
                                                                    JOptionPane.showMessageDialog(null,"Se creó el pdf");
                                                                    //envioCorreo.envio(path, nombre);
                                                                    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+path);
                                                                 }
                                                             }
                                                         }
                                                     }
                                                         
                                                 }
                                             }
                                         }
                                     }
                                 }
                             }
                         }
                           
                     }
                
            
                }catch(Exception ex){
                    System.out.println(ex.toString());
                }

            } 
        }
        catch(Exception e){
            System.out.println("Error");
        }
        
    }//GEN-LAST:event_jButton1ActionPerformed

    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private JDialog aboutBox;

    

   

    
}
