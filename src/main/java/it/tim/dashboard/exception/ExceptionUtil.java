package it.tim.dashboard.exception;


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

		    StringBuffer buff = new StringBuffer();
		    buff.append(ex.toString());
		    StackTraceElement[] exEl = ex.getStackTrace();

		    if(exEl == null || exEl.length==0){
		    }
		    else if(exEl.length>4){
		        buff.append(" -> " + exEl[0].toString());
		        buff.append(" -> " + exEl[1].toString());
		        buff.append(" -> " + exEl[2].toString());
		        buff.append(" -> " + exEl[3].toString());
		        buff.append(" -> " + exEl[4].toString());
		     }
		    else if(exEl.length>3){
		        buff.append(" -> " + exEl[0].toString());
		        buff.append(" -> " + exEl[1].toString());
		        buff.append(" -> " + exEl[2].toString());
		        buff.append(" -> " + exEl[3].toString());
		     }
		    else if(exEl.length>2){
		       buff.append(" -> " + exEl[0].toString());
		       buff.append(" -> " + exEl[1].toString());
		       buff.append(" -> " + exEl[2].toString());
		    }
		    else if(exEl.length>1){
		      buff.append(" -> " + exEl[0].toString());
		      buff.append(" -> " + exEl[1].toString());
		    }
		    else{
		      buff.append(" -> " + exEl[0].toString());
		    }

		    return buff.toString();
		    
		   }

		   /*
		   static public String stack2string(Exception e) {
		     try {
		       java.io.StringWriter sw = new java.io.StringWriter();
		       java.io.PrintWriter pw = new java.io.PrintWriter(sw);
		       e.printStackTrace(pw);
		       return sw.toString();
		     }
		     catch(Exception e2) {
		       return "";
		     }
		   }
		    */


		 }///:~
		    


