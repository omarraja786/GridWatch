/**
 * This class is used to get the average values across different time periods for different data variables
 */

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

class GraphData {

    // raw data lists for each variable from the csv file, there could be more variables, there could be less, this could be made more flexible
    ArrayList<Integer> idList = new ArrayList<>();
    ArrayList<String> timestampList = new ArrayList<>();
    ArrayList<Double> demandList = new ArrayList<>();
    ArrayList<Double> frequencyList = new ArrayList<>();
    ArrayList<Double> coalList = new ArrayList<>();
    ArrayList<Double> nuclearList = new ArrayList<>();
    ArrayList<Double> ccgtList = new ArrayList<>();
    ArrayList<Double> windList = new ArrayList<>();
    ArrayList<Double> frenchList = new ArrayList<>();
    ArrayList<Double> dutchList = new ArrayList<>();
    ArrayList<Double> irishList = new ArrayList<>();
    ArrayList<Double> ewList = new ArrayList<>();
    ArrayList<Double> pumpedList = new ArrayList<>();
    ArrayList<Double> hydroList = new ArrayList<>();
    ArrayList<Double> oilList = new ArrayList<>();
    ArrayList<Double> ocgtList = new ArrayList<>();
    ArrayList<Double> otherList = new ArrayList<>();
    ArrayList<Double> solarList = new ArrayList<>();

    ArrayList<String> yearList = new ArrayList<>();
    ArrayList<String> seasonList = new ArrayList<>();
    Map<String, ArrayList<Integer>> yearMap = new HashMap<>();

    // fill each list with data, has to return something for it to work, no idea why but oh well
    GraphData() {
        String line;
        Scanner input;
        String getFileText;

        // code stolen and modified from our readData class
        try {
            getFileText = FilledFrame.enterFileName.getText();
            File fileReader = new File(getFileText + ".csv");
            input = new Scanner(fileReader);

            input.nextLine(); // first line of the csv file is the data names, it must be excluded!

            // actually putting data into each raw data list now
            int position = 0;
            while (input.hasNextLine()) {
                line = input.nextLine();
                String[] splitted = line.replaceAll(" ", "").split(",");
                idList.add(position, Integer.parseInt(splitted[0]));
                timestampList.add(position, splitted[1]);
                demandList.add(position, Double.parseDouble(splitted[2]));
                frequencyList.add(position, Double.parseDouble(splitted[3]));
                coalList.add(position, Double.parseDouble(splitted[4]));
                nuclearList.add(position, Double.parseDouble(splitted[5]));
                ccgtList.add(position, Double.parseDouble(splitted[6]));
                windList.add(position, Double.parseDouble(splitted[7]));
                frenchList.add(position, Double.parseDouble(splitted[8]));
                dutchList.add(position, Double.parseDouble((splitted[9])));
                irishList.add(position, Double.parseDouble(splitted[10]));
                ewList.add(position, Double.parseDouble((splitted[11])));
                pumpedList.add(position, Double.parseDouble(splitted[12]));
                hydroList.add(position, Double.parseDouble(splitted[13]));
                oilList.add(position, Double.parseDouble((splitted[14])));
                oilList.add(position, Double.parseDouble(splitted[14]));
                ocgtList.add(position, Double.parseDouble(splitted[15]));
                otherList.add(position, Double.parseDouble(splitted[16]));
                solarList.add(position, Double.parseDouble(splitted[17]));
                position++;
            }

            input.close();

            yearList = getYear();
            seasonList.add(0, "winter");
            seasonList.add(1, "spring");
            seasonList.add(2, "summer");
            seasonList.add(3, "autumn");

        } catch (FileNotFoundException ex) {
            String s = "File not found \n Please enter the correct file name and ensure it is in the same directory as the JAR file";

            JOptionPane.showMessageDialog(null, s, "File not found", JOptionPane.INFORMATION_MESSAGE);
            ex.printStackTrace();
        }
    }

    // returns an ArrayList containing each year that is in the csv file, for example the result from the original csv file would be ["2016", "2017"].
    private ArrayList<String> getYear() {
        ArrayList<String> myYear = new ArrayList<>();
        myYear.add(timestampList.get(0).substring(0, 4));
        for (int i = 1; i < timestampList.size(); i++) {
            String tempYear = timestampList.get(i).substring(0, 4);
            if (!myYear.get(myYear.size() - 1).equals(tempYear)) {
                myYear.add(tempYear);
            }
        }
        return myYear;
    }

    // takes an ArrayList of positions in the csv file for a specific year, and a string for season checking.
    // returns an ArrayList of positions in the csv file for a specific season.
    ArrayList<Integer> getSeasonArray(ArrayList<Integer> yearPositions, String season) {
        ArrayList<Integer> mySeasonArray = new ArrayList<>();
        for (int i = yearPositions.size() - 1; i >= 0; i--) {
            String tempMonth = timestampList.get(i).substring(5, 7);
            switch (season) {
                case "winter":
                    if (Arrays.asList("01", "02", "12").contains(tempMonth)) {
                        mySeasonArray.add(0, i);
                    }
                    break;
                case "spring":
                    if (Arrays.asList("03", "04", "05").contains(tempMonth)) {
                        mySeasonArray.add(0, i);
                    }
                    break;
                case "summer":
                    if (Arrays.asList("06", "07", "08").contains(tempMonth)) {
                        mySeasonArray.add(0, i);
                    }
                    break;
                default:
                    mySeasonArray.add(0, i);
                    break;
            }
        }
        return mySeasonArray;
    }

    // takes a string of a year, returns an ArrayList of positions in the csv file for that specific year.
    ArrayList<Integer> getYearArray(String year) {
        ArrayList<Integer> myYearArray = new ArrayList<>();
        for (int i = timestampList.size() - 1; i >= 0; i--) {
            if ((timestampList.get(i).substring(0, 4)).equals(year)) {
                myYearArray.add(0, i);
            }
        }
        return myYearArray;
    }


    // takes the raw data list for a specific variable and a list of positions for a certain time period (year or season), gives the average value of the variables in that time period.
    double getAverage(ArrayList<Double> arrayList, ArrayList<Integer> positionsList) {
        ArrayList<Double> newArrayList = new ArrayList<>();
        int count = 0;
        for (int position : positionsList) {
            if (arrayList.get(position) > -1000000) { // some values in the csv file seem to be anomalies, this helps reduce the effect of them.
                newArrayList.add(count, arrayList.get(position));
                count++;
            }
        }
        double sum = 0.0;
        for (double value : newArrayList) {
            sum += value;
        }
        return (sum / count);
    }
}

// provides easy access to average values for a season or year.
class GraphAverages {

    double averageVariableYear;
    double averageVariableSeason;

    // takes GraphData object so it is only created once in the program, an ArrayList of variables, String of required year and String of required season.
    // creates average value for the specific variable for the specific time period.
    GraphAverages(GraphData myData, ArrayList<Double> myVariable, String thisYear, String thisSeason) {
        Map<String, ArrayList<Integer>> myYearMap = myData.yearMap; // maps are used to prevent recalculation of previously calculated values and ArrayLists
        if (!myYearMap.containsKey(thisYear)) {
            ArrayList<Integer> yearPositions = myData.getYearArray(thisYear);
            myYearMap.put(thisYear, yearPositions);
            myData.yearMap = myYearMap;
        }
        ArrayList<Integer> seasonArray = myData.getSeasonArray(myYearMap.get(thisYear), thisSeason);
        if (seasonArray.isEmpty()) {
            averageVariableSeason = 0; // any season in a year which has not got values yet should be given values of 0.
        } else {
            averageVariableSeason = myData.getAverage(myVariable, seasonArray);
        }
        averageVariableYear = myData.getAverage(myVariable, myYearMap.get(thisYear));
    }
}
