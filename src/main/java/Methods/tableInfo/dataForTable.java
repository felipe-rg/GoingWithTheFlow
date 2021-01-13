package Methods.tableInfo;

import Client.Patient;
import Methods.dateFormat;

import java.util.ArrayList;
/*      Data for table has an object of known dimensions in which data is stored
*       This data can be fed into the table easily so long as the required columns correlate
*       Getnumber simply returns the size of the array so that numbers of patients can be known quickly*/
public interface dataForTable{
    String getNumber();
    Object[][] getData();

}
