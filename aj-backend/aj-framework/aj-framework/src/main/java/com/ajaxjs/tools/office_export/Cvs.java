package com.ajaxjs.tools.office_export;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;

public class Cvs {
    public static <T> void writeCsv(List<List<T>> csv, char separator, OutputStream output) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8))) {
            for (List<T> row : csv) {
                for (Iterator<T> iter = row.iterator(); iter.hasNext(); ) {
                    String field = String.valueOf(iter.next()).replace("\"", "\"\"");
                    if (field.indexOf(separator) > -1 || field.indexOf('"') > -1)
                        field = '"' + field + '"';

                    writer.append(field);

                    if (iter.hasNext())
                        writer.append(separator);
                }

                writer.newLine();
            }

            writer.flush();
        }
    }

    public static void doGet(List<List<Object>> csv, HttpServletRequest request, HttpServletResponse response) {
        String filename = request.getPathInfo().substring(1);
        response.setHeader("content-type", "text/csv");
        response.setHeader("content-disposition", "attachment;filename=\"" + filename + "\"");

        try {
            writeCsv(csv, ';', response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
