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
		output = input.replace("Ae", "�");
		output = output.replace("ae", "�");
		output = output.replace("Ue", "�");
		output = output.replace("ue", "�");
		output = output.replace("Oe", "�");
		output = output.replace("oe", "�");
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
