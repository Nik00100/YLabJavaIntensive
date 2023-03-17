package org_structure;

import com.opencsv.exceptions.CsvValidationException;
import java.io.*;

public class OrgStructureParserTest {
    public static void main(String[] args) throws IOException, CsvValidationException {
        String[] columnMapping = {"id", "bossId", "name", "position"};
        OrgStructureParser orgStructureParser = new OrgStructureParserImpl(columnMapping);
        System.out.println(orgStructureParser.parseStructure(new File("src/main/resources/organization.csv")));
    }
}
