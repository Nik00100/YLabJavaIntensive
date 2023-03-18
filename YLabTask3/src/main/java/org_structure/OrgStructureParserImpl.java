package org_structure;

import com.opencsv.*;
import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvValidationException;
import java.io.*;
import java.util.*;

public class OrgStructureParserImpl implements OrgStructureParser {
    String[] columnMapping;
    Map<Long, List<Employee>> subordinaries;

    public OrgStructureParserImpl(String[] columnMapping) {
        this.columnMapping = columnMapping;
        this.subordinaries = new HashMap<>();
    }

    @Override
    public Employee parseStructure(File csvFile) throws IOException, CsvValidationException {
        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(csvFile))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build()) {

            csvReader.readNext();

            ColumnPositionMappingStrategy strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean csvToBean = new CsvToBeanBuilder(csvReader).
                    withMappingStrategy(strategy).
                    build();

            List<Employee> employees = (List<Employee>) csvToBean.parse();
            Employee boss = getBoss(employees);
            fillSubordinaries(employees, boss);
            return boss;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Employee getBoss(List<Employee> employees) {
        Employee boss = employees.stream().filter(employee -> employee.getBossId() == null).findFirst().get();
        return boss;
    }

    // метод заполняет подчиненными непосредственных руководителей
    private void fillSubordinaries(List<Employee> employees, Employee boss) {
        for (Employee employee : employees) {
            if (!employee.equals(boss)) {
                long bossId = employee.getBossId();
                List<Employee> subordinary= subordinaries.getOrDefault(bossId, new ArrayList<>());
                subordinary.add(employee);
                subordinaries.put(bossId, subordinary);
            }
        }

        for (Employee employee: employees) {
            if (subordinaries.containsKey(employee.getId())) {
                for (Employee emp : subordinaries.get(employee.getId())) {
                    employee.getSubordinate().add(emp);
                }
            }
        }
    }

    // Дополнительный метод - определяет из всего списка сотрудников, подчиненных
    // непосредственного руководителя с индексом boss_id
    private void setSubordinatesToEmployee(List<Employee> employees, Employee employee, long boss_id) {
        for (Employee emp : employees) {
            if (emp.getBossId() != null && emp.getBossId().longValue() == boss_id)
                employee.getSubordinate().add(emp);
        }
    }

}
