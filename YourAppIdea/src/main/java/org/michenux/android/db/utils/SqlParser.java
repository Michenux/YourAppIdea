package org.michenux.android.db.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.res.AssetManager;

public class SqlParser {

	/**
	 * @param sqlFile
	 * @param assetManager
	 * @return
	 * @throws IOException
	 */
	public static List<String> parseSqlFile(String sqlFile, AssetManager assetManager) throws IOException {
		List<String> sqlIns = null ;
		InputStream is = assetManager.open(sqlFile);
		try {
			sqlIns = parseSqlFile(is);
		}
		finally {
			is.close();
		}
		return sqlIns;
	}
	
	/**
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static List<String> parseSqlFile(InputStream is) throws IOException {
		String script = removeComments(is);
		return splitSqlScript(script, ';');
	}

	/**
	 * @param is
	 * @return
	 * @throws IOException
	 */
	private static String removeComments(InputStream is) throws IOException {

		StringBuilder sql = new StringBuilder();

		InputStreamReader isReader = new InputStreamReader(is);
		try {
			BufferedReader buffReader = new BufferedReader(isReader);
			try {
				String line;
				String multiLineComment = null;
				while ((line = buffReader.readLine()) != null) {
					line = line.trim();

					// Check for start of multi-line comment
					if (multiLineComment == null) {
						// Check for first multi-line comment type
						if (line.startsWith("/*")) {
							if (!line.endsWith("}")) {
								multiLineComment = "/*";
							}
							// Check for second multi-line comment type
						} else if (line.startsWith("{")) {
							if (!line.endsWith("}")) {
								multiLineComment = "{";
							}
							// Append line if line is not empty or a single line
							// comment
						} else if (!line.startsWith("--") && !line.equals("")) {
							sql.append(line);
						} // Check for matching end comment
					} else if (multiLineComment.equals("/*")) {
						if (line.endsWith("*/")) {
							multiLineComment = null;
						}
						// Check for matching end comment
					} else if (multiLineComment.equals("{")) {
						if (line.endsWith("}")) {
							multiLineComment = null;
						}
					}

				}
			} finally {
				buffReader.close();
			}

		} finally {
			isReader.close();
		}

		return sql.toString();
	}

	/**
	 * Split an SQL script into separate statements delimited with the provided
	 * delimiter character. Each individual statement will be added to the
	 * provided <code>List</code>.
	 * 
	 * @param script
	 *            the SQL script
	 * @param delim
	 *            character delimiting each statement - typically a ';'
	 *            character
	 * @param statements
	 *            the List that will contain the individual statements
	 */
	private static List<String> splitSqlScript(String script, char delim) {
		List<String> statements = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		boolean inLiteral = false;
		char[] content = script.toCharArray();
		for (int i = 0; i < script.length(); i++) {
			if (content[i] == '\'') {
				inLiteral = !inLiteral;
			}
			if (content[i] == delim && !inLiteral) {
				if (sb.length() > 0) {
					statements.add(sb.toString().trim());
					sb = new StringBuilder();
				}
			} else {
				sb.append(content[i]);
			}
		}
		if (sb.length() > 0) {
			statements.add(sb.toString().trim());
		}
		return statements;
	}

}
