package Reports;

import DBConnect.ConnectSQL;

import java.io.File;


import java.util.Map;
import java.util.HashMap;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

public class FirstReport {

    public void create(String path,String pattern) throws JRException {
        // Параметры
        Map<String, Object> parameters;
        parameters = new HashMap<String, Object>();

        JasperDesign jasperDesign;
        JasperReport jasperReport;
        JasperPrint jasperPrint;

        File reportPattern = new File(pattern);

        jasperDesign = JRXmlLoader.load(reportPattern);
        jasperReport = JasperCompileManager.compileReport(jasperDesign);
        jasperPrint = JasperFillManager.fillReport(jasperReport,
                parameters,
                ConnectSQL.getConnection());
        path += "myreport.docx";
        File dest = new File(path);
        JRDocxExporter exporter = new JRDocxExporter();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_FILE,dest);
        exporter.exportReport();
    }
}
