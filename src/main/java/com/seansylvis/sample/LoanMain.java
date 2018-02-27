package com.seansylvis.sample;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Application entry-point for {@link LoanProcessor}, which reads in input files and outputs results.
 *
 * @author Sean Sylvis (ssylvis@gmail.com)
 */
public class LoanMain {

  // input files
  private static final String BANKS = "banks.csv";
  private static final String COVENANTS = "covenants.csv";
  private static final String FACILITIES = "facilities.csv";
  private static final String LOANS = "loans.csv";

  // output files
  private static final String ASSIGNMENTS = "assignments.csv";
  private static final String YIELDS = "yields.csv";

  private final List<Facility> facilities = new ArrayList<>();
  private final LoanProcessor loanProcessor = new LoanProcessor();
  private final List<Loan> loans = new ArrayList<>();

  public LoanMain(String inputDirectory) throws IOException {
    // parse banking records
    Map<Integer, Bank> banksMap = new HashMap<>();
    try (FileReader fileReader = new FileReader(new File(inputDirectory, BANKS))) {
      CSVParser csvParser = createParser(fileReader, "id", "name");
      for (CSVRecord record : csvParser) {
        int bankId = Integer.parseInt(record.get("id"));
        String name = record.get("name");
        banksMap.put(bankId, new Bank(bankId, name));
      }
    }

    // parse facility records
    Map<Integer, Facility> facilitiesMap = new TreeMap<>();
    try (FileReader fileReader = new FileReader(new File(inputDirectory, FACILITIES))) {
      CSVParser csvParser = createParser(fileReader, "amount", "interest_rate", "id", "bank_id");
      for (CSVRecord record : csvParser) {
        int amountCents = (int) Double.parseDouble(record.get("amount"));
        int bankId = Integer.parseInt(record.get("bank_id"));
        int facilityId = Integer.parseInt(record.get("id"));
        float interestRate = Float.parseFloat(record.get("interest_rate"));
        Facility facility = new Facility(facilityId, banksMap.get(bankId), interestRate,amountCents);

        facilitiesMap.put(facilityId, facility);
      }
    }
    // add sorted
    facilities.addAll(facilitiesMap.values());

    // parse covenants records
    try (FileReader fileReader = new FileReader(new File(inputDirectory, COVENANTS))) {
      CSVParser csvParser = createParser(fileReader, "facility_id", "max_default_likelihood", "bank_id",
          "banned_state");
      for (CSVRecord record : csvParser) {
        List<Covenant> covenants = createCovenants(record);

        if (isRecordSet(record, "facility_id")) {
          int facilityId = Integer.parseInt(record.get("facility_id"));
          Facility facility = facilitiesMap.get(facilityId);
          for (Covenant covenant : covenants) {
            facility.addCovenant(covenant);
          }
        } else {
          int bankId = Integer.parseInt(record.get("bank_id"));
          Bank bank = banksMap.get(bankId);
          for (Covenant covenant : covenants) {
            bank.addCovenant(covenant);
          }
        }
      }
    }

    // parse loan records to be processed (read into memory for convenience)
    try (FileReader fileReader = new FileReader(new File(inputDirectory, LOANS))) {
      CSVParser csvParser = createParser(fileReader, "interest_rate", "amount", "id", "default_likelihood", "state");
      for (CSVRecord record : csvParser) {
        int amountCents = Integer.parseInt(record.get("amount"));
        int loanId = Integer.parseInt(record.get("id"));
        float defaultLikelihood = Float.parseFloat(record.get("default_likelihood"));
        float interestRate = Float.parseFloat(record.get("interest_rate"));
        String state = record.get("state");
        loans.add(new Loan(loanId, amountCents, defaultLikelihood, interestRate, state));
      }
    }

    // add all facilities to loan processor
    for (Facility facility : facilitiesMap.values()) {
      loanProcessor.addFacility(facility);
    }
  }

  /**
   * Streams over input loans and outputs results.
   */
  public void run() throws IOException {
    try (FileWriter fileWriter = new FileWriter(ASSIGNMENTS)) {
      CSVPrinter csvPrinter = new CSVPrinter(fileWriter, createFormat("loan_id", "facility_id"));

      for (Loan loan : loans) {
        Facility facility = loanProcessor.process(loan);
        if (facility == null) {
          csvPrinter.printRecord(loan.getLoanId(), "");
        } else {
          csvPrinter.printRecord(loan.getLoanId(), facility.getFacilityId());
        }
      }
    }

    try (FileWriter fileWriter = new FileWriter(YIELDS)) {
      CSVPrinter csvPrinter = new CSVPrinter(fileWriter, createFormat("facility_id", "expected_yield"));

      for (Facility facility : facilities) {
        List<Loan> fundedLoans = loanProcessor.getFundedLoans(facility);
        int expectedYield = facility.computeExpectedYield(fundedLoans);
        csvPrinter.printRecord(facility.getFacilityId(), expectedYield);
      }
    }
  }

  private CSVFormat createFormat(String... header) {
    return CSVFormat.DEFAULT.withHeader(header);
  }

  private CSVParser createParser(FileReader fileReader, String... header) throws IOException {
    return createFormat(header).withSkipHeaderRecord().parse(fileReader);
  }

  private List<Covenant> createCovenants(CSVRecord record) {
    List<Covenant> covenants = new ArrayList<>();
    if (isRecordSet(record, "max_default_likelihood")) {
      float maxDefaultLikelihood = Float.parseFloat(record.get("max_default_likelihood"));
      covenants.add(new DefaultLikelihoodCovenant(maxDefaultLikelihood));
    }
    if (isRecordSet(record, "banned_state")) {
      String bannedState = record.get("banned_state");
      covenants.add(new BannedStateCovenant(bannedState));
    }
    return covenants;
  }

  private boolean isRecordSet(CSVRecord record, String name) {
    return record.isSet(name) && !record.get(name).isEmpty();
  }

  public static void main(String[] args) throws Exception {
    new LoanMain(args[0]).run();
  }
}
