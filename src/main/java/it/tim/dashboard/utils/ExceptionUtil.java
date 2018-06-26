package it.tim.dashboard.utils;

public class ExceptionUtil {

	   /**
	    * Returns a string containing the Exception with the first
	    * 3 elements of the stack trace
	    *
	    * @param ex the exception to print
	    * @return a string containing the Exception with the first
	    * 3 elements of the stack trace
	    */
	   public static  String getStackTrace(Throwable ex){

	    StringBuilder buff = new StringBuilder();
	    buff.append(ex.toString());
	    StackTraceElement[] exEl = ex.getStackTrace();

	    if(exEl == null || exEl.length==0){
	    }
	    else{
	    	
	    	for(int i=0; i<exEl.length; i++) {
	    		buff.append(" -> " + exEl[i].toString());
	    		if(i==3) {
	    			break;
	    		}
	    	}
	    }

	    return buff.toString();
	    
	   }

	   

	 }///:~
	    
	    