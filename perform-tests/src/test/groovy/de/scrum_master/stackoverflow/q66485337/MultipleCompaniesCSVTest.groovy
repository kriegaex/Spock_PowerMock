package de.scrum_master.stackoverflow.q66485337

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class MultipleCompaniesCSVTest extends Specification {
  @Shared
  def functionalUtils = new FunctionalUtils()

  @Unroll
  def "company #company, row #row"() {
    expect:
    csvFile.startsWith(company)
    firstName.contains("-$company-")
    lastName.contains("-$company-")
    firstName.endsWith("-$id")
    lastName.endsWith("-$id")

    where:
    [company, csvFile, row] << getRecordsForCompanies("A", "B", "C", "D")
    // id = row[0]; firstName = row[1]; lastName = row[2]
    // Since Spock 2.0-M3 you can simplify to:
     (id, firstName, lastName) = row
  }

  private List<List> getRecordsForCompanies(String... companies) {
    def records = []
    companies.each { company ->
      def csvFile = "${company}.csv"
      functionalUtils.getRecordsFromCsv(csvFile).each { row ->
        records.add([company, csvFile, row])
      }
    }
    records
  }

  static class FunctionalUtils {
    def getRecordsFromCsv(String csvFile) {
      def company = csvFile.replaceFirst("[.]csv\$", "")
      // Emulate company-specific CSV file, creating some records
      (1..3).collect { id -> [id, "John-$company-$id", "Doe-$company-$id"] }
    }
  }
}
