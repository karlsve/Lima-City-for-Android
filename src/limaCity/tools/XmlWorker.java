package limaCity.tools;

import android.text.Html;

public class XmlWorker 
{
	public static String htmlToText(String input)
	{
		String output = Html.fromHtml(input).toString();
		return output;
	}
	
	public static String prepareNodeName(String input)
	{
		String output = "";
		output = input.replace("Ae", "Ä");
		output = output.replace("ae", "ä");
		output = output.replace("Ue", "Ü");
		output = output.replace("ue", "ü");
		output = output.replace("Oe", "Ö");
		output = output.replace("oe", "ö");
		output = output.replace("_", " ");
		return output;
	}

	public static String firstCharToUpperCase(String input) {
		String output = "";
		if(input.length() != 0)
		{
			char[] chars = input.toCharArray();
			chars[0] = Character.toUpperCase(chars[0]);
			for(char current : chars)
			{
				output += Character.toString(current);
			}
		}
		else
		{
			output = input;
		}
		return output;
	}
}
