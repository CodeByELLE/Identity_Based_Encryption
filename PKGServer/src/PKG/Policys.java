package PKG;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Policys {
	
	Map<Integer,Date> DatePolicysIdtoDate =  new HashMap<Integer,Date>();
	Map<Date,Integer> DatePolicysDatetoID =  new HashMap<Date,Integer>();
	private int maxID=0;
	
	public Policys() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String date1 = "17/11/2018";
		String date2 = "27/12/2018";
		String date3 = "17/01/2019";
		String date4 = "27/03/2019";
		
		try {
			this.maxID+=1;
			this.DatePolicysIdtoDate.put(this.maxID,simpleDateFormat.parse(date1));
			this.DatePolicysDatetoID.put(simpleDateFormat.parse(date1),this.maxID);
			
			this.maxID+=1;
			this.DatePolicysIdtoDate.put(this.maxID,simpleDateFormat.parse(date2));
			this.DatePolicysDatetoID.put(simpleDateFormat.parse(date2),this.maxID);
			
			this.maxID+=1;
			this.DatePolicysIdtoDate.put(this.maxID,simpleDateFormat.parse(date3));
			this.DatePolicysDatetoID.put(simpleDateFormat.parse(date3),this.maxID);
			
			this.maxID+=1;
			this.DatePolicysIdtoDate.put(4,simpleDateFormat.parse(date4));
			this.DatePolicysDatetoID.put(simpleDateFormat.parse(date4),this.maxID);

			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * @param Integer ID  check if the policy exist and if it's note expired */
	public Date ChekPolicyID(Integer ID) {
		if(this.DatePolicysIdtoDate.containsKey(ID)) {// the policy exist
			Date tmpDate  =  this.DatePolicysIdtoDate.get(ID);
			
			Integer tmpID = this.ChekPolicyDate(tmpDate); // check the date
			
			if(tmpID == ID) {// the date is well checked if we got the same ID from the method called previously
				return tmpDate;
			}else if(tmpID == -1){// expired date
				return null;
			}else {// Unexpected Case
				System.err.println("Policys.ChekPolicyID()");
				System.err.println("Unexpected returned value from Policys.ChekPolicyDate");
				System.err.println(tmpID);
				return null;
			}
		}
		
		return null;// inexisting ID or expired Date
	}
	
	/**
	 * @param Integer ID of a policy
	 * simply check if there is a policy with that ID
	 **/
	public boolean ChekPolicyIDExistence(Integer ID) {
		return this.DatePolicysIdtoDate.containsKey(ID);
	}
	
	/**
	 * @param Date check if the date policy already exist and if not expired if it dosn't exist 
	 * and not expired it will return ID else it return -1 for expired policy
	 **/
	public Integer ChekPolicyDate(Date PolicyDate){
		// if it is not expired 
		Date d = Calendar.getInstance().getTime();
		if(d.compareTo(PolicyDate)<0) {//not expired
			//check if it already exist
			if(this.DatePolicysDatetoID.containsKey(PolicyDate)) {
				return this.DatePolicysDatetoID.get(PolicyDate);
			}else {
				System.out.println("new Policy");
				this.maxID+=1;
				this.DatePolicysIdtoDate.put(this.maxID,PolicyDate);
				this.DatePolicysDatetoID.put(PolicyDate,this.maxID);
				return this.maxID;
			}
		}else {//expired
			return -1;  
		}
		// if it already exist 
	}
	
	

}
