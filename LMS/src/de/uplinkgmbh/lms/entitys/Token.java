package de.uplinkgmbh.lms.entitys;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import de.uplinkgmbh.lms.utils.LMSToken;
import de.uplinkgmbh.lms.utils.Tokenaizer;

@Entity
@NamedQueries( {
    @NamedQuery( 
           name="DeleteTokenByTime",
           query=          
        	"DELETE " +
   			"FROM Token " +
   			"WHERE creationdate < :date" )
} )

public class Token {

	@Id
	private long uuid;
	private Date creationdate = new Date( System.currentTimeMillis() );
	
	public long getUuid() {
		return uuid;
	}
	public void setUuid(long uuid) {
		this.uuid = uuid;
	}
	public Date getCreationdate() {
		return creationdate;
	}
	protected void setCreationdate() {}
	
	public void setToken( String token ){
		this.uuid = Tokenaizer.restoreLMSToken( token.getBytes() ).uuid;	
	}
	
	public void setToken( LMSToken token ){
		this.uuid = token.uuid;
	}
}
