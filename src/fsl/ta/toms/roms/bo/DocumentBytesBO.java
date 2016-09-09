package fsl.ta.toms.roms.bo;

import java.io.Serializable;
import java.util.List;

public class DocumentBytesBO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5748801416868615809L;

	List <byte[]> docBytes;

	/**
	 * @return the docBytes
	 */
	public List<byte[]> getDocBytes() {
		return docBytes;
	}

	/**
	 * @param docBytes the docBytes to set
	 */
	public void setDocBytes(List<byte[]> docBytes) {
		this.docBytes = docBytes;
	}

	
	
}
