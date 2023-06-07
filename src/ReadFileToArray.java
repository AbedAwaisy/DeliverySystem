import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
public class ReadFileToArray {
    public static List<List<Object>> readRequestsFromFile(String filePath) {
        List<List<Object>> requestList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip the header line
                }

                String[] columns = line.split("\t");

                String id = columns[0];
                String serviceType = columns[1];
                String serviceArea = columns[2];
                double distance = Double.parseDouble(columns[3]);
                double time = Double.parseDouble(columns[4]);
                int arrival = Integer.parseInt(columns[5]);

                List<Object> requestData = new ArrayList<>();
                Request request = new Request(id, serviceType, serviceArea, distance);
                requestData.add(request);
                requestData.add(arrival);
                requestData.add(time);
                requestList.add(requestData);
            }

            // Sort the list based on arrival time
            requestList.sort(new Comparator<List<Object>>() {
                @Override
                public int compare(List<Object> requestData1, List<Object> requestData2) {
                    int arrival1 = (int) requestData1.get(1);
                    int arrival2 = (int) requestData2.get(1);
                    return Integer.compare(arrival1, arrival2);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return requestList;
    }
}
