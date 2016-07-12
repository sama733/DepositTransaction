package serverSide;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class MainJSonParser {

    private final static String JSON_PATH = "core.json";
    public static JSONObject jsonObject;
    public static List<Deposit> deposits = new ArrayList();
    public static String outLogPath;

    //parse kardane json
    public JSONObject jSonReader() {

        JSONParser jsonParser = new JSONParser();
        try {
            Object inputObject = jsonParser.parse(new FileReader(JSON_PATH));
            jsonObject = (JSONObject) inputObject;
            return jsonObject;

        } catch (ParseException e) {
            System.out.println("can not parse file");
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            System.out.println("directory is not correct");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //set Deposit Values
    public List<Deposit> getDeposits() {

        JSONArray depositList = (JSONArray) jsonObject.get("deposits");
        Iterator iterateDeposits = depositList.iterator();
        while (iterateDeposits.hasNext()) {
            Deposit depositValues = new Deposit();
            JSONObject deposit = (JSONObject) iterateDeposits.next();
            depositValues.setCustomerId(((String) deposit.get("id")));
            depositValues.setCustomerName(String.valueOf(deposit.get("customer")));
            depositValues.setInitialBalance(new BigDecimal(String.valueOf(deposit.get("initialBalance"))));
            depositValues.setUpperBound(new BigDecimal(String.valueOf(deposit.get("upperBound"))));
            deposits.add(depositValues);
        }
        return deposits;
    }

    public void getOutLogPathAndPort() {
        MainServer mainServer = new MainServer();
        outLogPath = (String) jsonObject.get("outLog");
        mainServer.setOutLog(outLogPath);
        Long port = (Long) jsonObject.get("port");
        mainServer.setPort(port.intValue());
    }
}





