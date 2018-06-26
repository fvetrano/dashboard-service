package it.tim.dashboard.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FormatterUtils {

	private static DecimalFormat df = new DecimalFormat("#0.00", new DecimalFormatSymbols(Locale.ITALIAN));

	private static DecimalFormat signeddf = new DecimalFormat("+#0.00;-#0.00",
			new DecimalFormatSymbols(Locale.ITALIAN));

	private static final double BYTE_TO_GBYTE_CONVERSION_THRESHOLD = 1024.0 * 1024.0 * 1024.0;

	public static String formatCurrency(double number) {
		String resp = "" + number;

		resp = df.format(number);

		return resp;

	}

	public static String formatCurrency(int number) {
		String resp = "" + number;
		resp = df.format(number);

		return resp;
	}

	public static String formatSignedCurrency(double number) {
		String resp = "" + number;

		resp = signeddf.format(number);

		return resp;

	}

	public static String formatBasketValue(double val, String basketType) {

		if (basketType.equalsIgnoreCase("VOCE")) {
			return formatDuration(val);
		} else if (basketType.equalsIgnoreCase("DATI")) {
			return formatVolumeDati(val);
		} else {

			return "" + (long) val;
		}
	}

	public static String formatDuration(double seconds) {

		StringBuilder buff = new StringBuilder();
		if (seconds > 0) {

			long min = ((long) seconds) / 60;
			// long sec = ((Double.valueOf(seconds).longValue()) % 60);

			buff.append(min);
		} else
			buff.append(seconds);
		return buff.toString();

	}

	public static String formatVolumeDati(double dQuantity) {
		StringBuilder buff = new StringBuilder();
		dQuantity = dQuantity / BYTE_TO_GBYTE_CONVERSION_THRESHOLD;

		buff.append(String.format("%.2f", dQuantity));

		return buff.toString().replaceAll(",", ".");

	}

	public static String formatVolumeDati(String vol) {
		StringBuilder buff = new StringBuilder();
		if (vol != null && vol.length() > 0) {
			double dQuantity = 0;
			try {
				dQuantity = Double.parseDouble(vol);
			} catch (Exception ex) {
				return "0 Kb";
			}

			dQuantity = dQuantity / BYTE_TO_GBYTE_CONVERSION_THRESHOLD;

			buff.append(String.format("%.2f", dQuantity));
		} else {
			buff.append(String.format("%.2f", vol));
		}

		return buff.toString();

	}

	public static String getDayUntilToday(String firstDate, String format) {

		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String diffDate = "";
		try {
			Date dateOne = formatter.parse(firstDate);

			Date dateTwo = new Date();
			Calendar c1 = Calendar.getInstance();

			Calendar c2 = Calendar.getInstance();

			c1.setTime(dateOne);

			c2.setTime(dateTwo);

			long days = (c1.getTime().getTime() - c2.getTime().getTime()) / (24 * 3600 * 1000);

			// if (c1.getTime().after(c2.getTime()))
			// diffDate = "-";
			diffDate = diffDate + days;
			System.out.println("Differenza giorni [" + diffDate + "]");

		} catch (java.text.ParseException e) {
			System.out.println("Errore nella parserizzazione della data [" + e.getMessage() + "]");
		}

		return diffDate;

	}

	public static String getLastDate(String firstDate, String secondDate, String format) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Date dateOne = formatter.parse(firstDate);

		if (secondDate != null && secondDate.length() > 0) {
			Date dateTwo = formatter.parse(secondDate);

			if (dateTwo.after(dateOne))
				dateOne = dateTwo;
		}
		return formatter.format(dateOne);

	}

	public static boolean isStringNullOrEmpty(String inputString) throws Exception {
		if (inputString != null && inputString.length() > 0)
			return false;
		else
			return true;
	}

	public static void main(String[] args) {
		String v1 = formatCurrency(-12);
		System.out.println("v1 = [" + v1 + "]");

		System.out.println("signed v1 = [" + formatSignedCurrency(-12) + "]");
		System.out.println("signed zero = [" + formatSignedCurrency(0.0) + "]");

		String v2 = formatCurrency(0);
		System.out.println("v2 = [" + v2 + "]");

		String v3 = formatCurrency(10.3);
		System.out.println("v3 = [" + v3 + "]");

		String v4 = formatCurrency(12.0);
		System.out.println("v4 = [" + v4 + "]");

		// String dur = formatDuration(777, "");
		// System.out.println("dur = [" + dur + "]");

		/*
		 * System.out.println("dati 110532 = [" + formatVolumeDati("110592", "")
		 * + "]"); System.out.println("dati 7077888 = [" +
		 * formatVolumeDati("7077888", "") + "]");
		 * System.out.println("dati 452984832 = [" +
		 * formatVolumeDati("452984832", "") + "]");
		 * System.out.println("dati 28991029248 = [" +
		 * formatVolumeDati("28991029248", "") + "]");
		 * System.out.println("dati 69991000248 = [" +
		 * formatVolumeDati("29998529248", "") + "]");
		 * System.out.println("dati 1855425871872 = [" +
		 * formatVolumeDati("1855425871872", "") + "]");
		 */
	}

}
