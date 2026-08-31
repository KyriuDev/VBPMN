package fr.inria.convecs.optimus.util;

import fr.inria.convecs.optimus.constants.Constant;

public class CadpHelper
{
	private CadpHelper()
	{

	}

	public static boolean versionIsGreaterThan(final String versionToCheck,
											   final String minimumVersion)
	{
		final String versionToCheckYearStr = versionToCheck.substring(1, 5);
		final String versionToCheckLetter = versionToCheck.substring(5);
		final String minimumVersionYearStr = minimumVersion.substring(1, 5);
		final String minimumVersionLetter = minimumVersion.substring(5);

		if (!Utils.isAnInt(versionToCheckYearStr)
				|| !Utils.isAnInt(minimumVersionYearStr)
				|| versionToCheckLetter.length() != 1
				|| minimumVersionLetter.length() != 1)
		{
			throw new RuntimeException(String.format(
					"The versions to check do not have the expected format \"_<year><version_letter>\" (version to check " +
							"is \"%s\" and minimum version is \"%s\").",
					versionToCheck,
					minimumVersion
			));
		}

		final char versionToCheckChar = versionToCheckLetter.charAt(0);
		final char minimumVersionChar = minimumVersionLetter.charAt(0);

		if (versionToCheckChar < Constant.ASCII_FIRST_LOWERCASE_LETTER
				|| versionToCheckChar > Constant.ASCII_LAST_LOWERCASE_LETTER
				|| minimumVersionChar < Constant.ASCII_FIRST_LOWERCASE_LETTER
				|| minimumVersionChar > Constant.ASCII_LAST_LOWERCASE_LETTER)
		{
			throw new RuntimeException(String.format(
					"The CADP version code should be a lowercase alphabetical character! (version to check is \"%s\" and " +
							"minimum version is \"%s\").",
					versionToCheckLetter,
					minimumVersionLetter
			));
		}

		final int versionToCheckYear = Integer.parseInt(versionToCheckYearStr);
		final int minimumVersionYear = Integer.parseInt(minimumVersionYearStr);

		if (versionToCheckYear == minimumVersionYear)
		{
			return versionToCheckChar >= minimumVersionChar;
		}
		else
		{
			return versionToCheckYear > minimumVersionYear;
		}
	}
}
