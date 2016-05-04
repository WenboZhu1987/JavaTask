package javaTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class Task {

	public static void main(String[] args) {
		try {

			String femaleNamesTxt = "resources/femalenames.txt";
			String malenamesTxt = "resources/malenames.txt";
			String csvFile = "resources/name_email_sample.csv";

			// create a genderList to be written in the csv file later
			List<String> genderList = new ArrayList();
			genderList.add("gender");// first add the title

			// store the femaleNames to an ArrayList
			String femaleName = null;
			List<String> femaleNameList = new ArrayList();
			BufferedReader bff = new BufferedReader(new FileReader(
					femaleNamesTxt));
			while ((femaleName = bff.readLine()) != null) {
				femaleNameList.add(femaleName);
			}
			bff.close();

			// store the maleNames to an ArrayList
			String maleName = null;
			List<String> maleNameList = new ArrayList();
			BufferedReader bfm = new BufferedReader(
					new FileReader(malenamesTxt));
			while ((maleName = bfm.readLine()) != null) {
				maleNameList.add(maleName);
			}
			bfm.close();

			//read .csv file
			BufferedReader bfcsv = new BufferedReader(new FileReader(csvFile));
			bfcsv.readLine();// skip the title
			String line = null;
			int maleNum = 0;
			int femaleNum = 0;
			while ((line = bfcsv.readLine()) != null) {
				String item[] = line.split(",");// split CSV file by ","
				String firstName = item[0];// get first name

				// to check if the first name is for male
				boolean isMale = false;
				if (!"".equals(firstName)) {
					for (String ml : maleNameList) {
						if (ml.equalsIgnoreCase(firstName)) {
							genderList.add("male");
							isMale = true;
							maleNum++;
							break;
						}
					}
					if (isMale == false) {// if isMale equals true, do not need to cheek with female name
						// to check if the first name is for female
						for (String fl : femaleNameList) {
							if (fl.equalsIgnoreCase(firstName)) {
								genderList.add("female");
								femaleNum++;
								break;
							}
						}
					}
				}
			}
			bfcsv.close();
			// add gender cloumn in the csv file
			addCloumn(genderList, csvFile);

			
			int totolNum = genderList.size();
			NumberFormat numberFormat = NumberFormat.getInstance();
			numberFormat.setMaximumFractionDigits(2);
			String maleResult = numberFormat.format((float) maleNum
					/ (float) totolNum * 100);
			String femaleResult = numberFormat.format((float) femaleNum
					/ (float) totolNum * 100);
			System.out.println("The number of matched males is " + maleNum
					+ ". The percentages of hits is " + maleResult + "%");
			System.out.println("The number of matched females is " + femaleNum
					+ ". The percentages of hits is " + femaleResult + "%");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	/**
	 * Add gender in the last column of the csv file
	 * 
	 * @param gList
	 * @param filePath
	 * @throws IOException
	 */
	public static void addCloumn(List<String> gList, String filePath)
			throws IOException {
		BufferedReader bufReader = new BufferedReader(new FileReader(filePath));
		String lineStr = "";
		int rowNumber = 0;
		StringBuffer nContent = new StringBuffer();
		while ((lineStr = bufReader.readLine()) != null) {
			String item[] = lineStr.split(",");
			String firstName = item[0];
			if (!"".equals(firstName)) {
				String addValue = "";
				if (rowNumber < gList.size()) {
					addValue = gList.get(rowNumber);
				}
				if (lineStr.endsWith(",")) {
					nContent.append(lineStr).append("\"" + addValue + "\"");
				} else {
					nContent.append(lineStr).append(",\"" + addValue + "\"");
				}
				rowNumber++;
				nContent.append("\r\n\r\n");
			}
		}
		bufReader.close();

		FileOutputStream fileOs = new FileOutputStream(new File(filePath),
				false);
		fileOs.write(nContent.toString().getBytes());
		fileOs.close();
	}

}
