package de.uplinkgmbh.lms.business;

public abstract class FolderList {

	public static String generate( long size, int shownSize, int position, String target, String listname ){
		
		double sS = shownSize;
		double pages = size/sS;
		
		pages = Math.ceil( pages );
		if( pages < 2 ) return "";
		int back = 1;
		int forward = 1;
		if( position > 1 ) back = position-1;
		else back = 1;
		if( position < pages ) forward = position+1;
		else forward = position;
		
		String res = "<div class=\"folder\" ><a class=\"folder_arrows\" href=\""+target+listname+"_pos="+back+"\" >&lt;&lt;</a>\n";
		
		for( int i=1; i <= (int)pages; i++ ){
			if( i == position ) res = res.concat( "&nbsp;["+i+"]\n" );
			else res = res.concat( "&nbsp;<a href=\""+target+listname+"_pos="+i+"\" >"+i+"</a>\n" );
		}
		
		res = res.concat( "&nbsp;<a class=\"folder_arrows\" href=\""+target+listname+"_pos="+forward+"\" >&gt;&gt;</a></div>\n" );
		
		return res;
	}
}
