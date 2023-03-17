package org_structure;

import com.opencsv.exceptions.CsvValidationException;
import java.io.*;

public interface OrgStructureParser {
    Employee parseStructure(File csvFile) throws IOException, CsvValidationException;
}
