package edu.jsu.mcis;

import java.io.*;
import java.util.*;
import com.opencsv.*;
import static javafx.scene.input.KeyCode.T;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Converter {
    
    /*
    
        Consider the following CSV data:
        
        "ID","Total","Assignment 1","Assignment 2","Exam 1"
        "111278","611","146","128","337"
        "111352","867","227","228","412"
        "111373","461","96","90","275"
        "111305","835","220","217","398"
        "111399","898","226","229","443"
        "111160","454","77","125","252"
        "111276","579","130","111","338"
        "111241","973","236","237","500"
        
        The corresponding JSON data would be similar to the following (tabs and
        other whitespace have been added for clarity).  Note the curly braces,
        square brackets, and double-quotes!  These indicate which values should
        be encoded as strings, and which values should be encoded as integers!
        
        {
            "colHeaders":["ID","Total","Assignment 1","Assignment 2","Exam 1"],
            "rowHeaders":["111278","111352","111373","111305","111399","111160",
            "111276","111241"],
            "data":[[611,146,128,337],
                    [867,227,228,412],
                    [461,96,90,275],
                    [835,220,217,398],
                    [898,226,229,443],
                    [454,77,125,252],
                    [579,130,111,338],
                    [973,236,237,500]
            ]
        }
    
        Your task for this program is to complete the two conversion methods in
        this class, "csvToJson()" and "jsonToCsv()", so that the CSV data shown
        above can be converted to JSON format, and vice-versa.  Both methods
        should return the converted data as strings, but the strings do not need
        to include the newlines and whitespace shown in the examples; again,
        this whitespace has been added only for clarity.
    
        NOTE: YOU SHOULD NOT WRITE ANY CODE WHICH MANUALLY COMPOSES THE OUTPUT
        STRINGS!!!  Leave ALL string conversion to the two data conversion
        libraries we have discussed, OpenCSV and json-simple.  See the "Data
        Exchange" lecture notes for more details, including example code.
    
    */
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        
        String results = "";
        
        try {
            
            CSVReader reader = new CSVReader(new StringReader(csvString));
            List<String[]> full = reader.readAll();
            Iterator<String[]> iterator = full.iterator();
            
            
    //My Code
            
            //Create Json Objects
            JSONArray rowArray = new JSONArray();
            JSONArray colArray = new JSONArray();
            JSONArray dataArray = new JSONArray();
            JSONObject json = new JSONObject();
            
            //Set col headers as first line in the array
            for(String header : iterator.next()) colArray.add(header);
            
            //Set the data and row headers
            while (iterator.hasNext()){
                String[] line = iterator.next();
               List<Integer> tmpDataLine = new ArrayList<Integer>();
                for(String data : line){
                    //Add data to proper array
                    if(data.equals(line[0])) rowArray.add(data);
                    else tmpDataLine.add((Integer.parseInt(data)));
                }
                dataArray.add(tmpDataLine);
            }
            
            json.put("rowHeaders",rowArray);
            json.put("colHeaders", colArray);
            json.put("data",dataArray);
            
            results = JSONValue.toJSONString(json);
                                  
    //End My Code
        }        
        catch(Exception e) { return e.toString(); }
        
        return results.trim();
        
    }
    
    public static String jsonToCsv(String jsonString) {
        
        String results = "";
        
        try {
            StringWriter writer = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(writer, ',', '"', '\n');
            
    //My Code
        
            //Create Parser
            JSONParser p = new JSONParser();
            //Object to parse from
            JSONObject json = (JSONObject) p.parse(jsonString);
            
            //Create String JSONArrays
            JSONArray colJSON = (JSONArray) json.get("colHeaders");
            JSONArray rowJSON = (JSONArray) json.get("rowHeaders");
            JSONArray dataJSON = (JSONArray) json.get("data");
            
            //Create String Arrays
            String[] colArray = new String[colJSON.size()];
            String[] rowArray = new String[rowJSON.size()];
            String[] dataArray = new String[dataJSON.size()];
            
            //Set index int
            int index;
            int count;
            
            //Populate CSV First Row
            index = 0;
            for(Object col : colJSON){
                colArray[index++] = (String) col;
            }
            
            //Write CSV First Line
            csvWriter.writeNext(colArray);
            
            
            //Populate CSV Main data
            index = 0;
            
            //Fill DataArray and HeadersArray
            for(int i = 0; i < rowJSON.size(); i++){
                dataArray[i] = dataJSON.get(i).toString();
                rowArray[i] = rowJSON.get(i).toString();
            }
            
            //Create CSV Array Objects
            for(String data : dataArray){
                JSONArray dataParseLine = (JSONArray)p.parse(data);
                String[] tmpLine = new String[dataParseLine.size()+1];
                
                tmpLine[0] = rowArray[index++];
                for(int i = 1; i<dataParseLine.size()+1; i++){
                    tmpLine[i] = dataParseLine.get(i-1).toString();
                }
                csvWriter.writeNext(tmpLine);
            }
            
            //Write to CSV
            results = writer.toString();
            
    
    //End My Code
           
        }
        
        catch(Exception e) { return e.toString(); }
        
        return results.trim();
        
    }
    
     

}