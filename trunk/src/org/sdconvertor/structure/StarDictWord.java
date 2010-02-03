package org.sdconvertor.structure;

/**
 * Created:	January 31, 2010
 * License:	GPLv3
 * @author	I-Fan Chen
 * @version	1
 *
 */
public class StarDictWord{
	String name;
	int offset, size;

	public StarDictWord(String content, int offset, int size){
		setName(content);
		setOffset(offset);
		setSize(size);
	}

///////////////////////////////////////////////////////////////////////////////
// Getters and Setters
	
	public String getName(){return name;}
	public void setName(String content){this.name = content;}
	
	public int getOffset(){return offset;}
	public void setOffset(int offset){this.offset = offset;}
	
	public int getSize(){return size;}
	public void setSize(int size){this.size = size;}

}