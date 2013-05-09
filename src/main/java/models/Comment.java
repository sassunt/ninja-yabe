package models;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import com.avaje.ebean.annotation.CreatedTimestamp;

@Entity
public class Comment {
 
	@Id
	public Long id;
	
    public final String author;
    
    @CreatedTimestamp
    public Timestamp postedAt;
     
    @Lob
    public final String content;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    public final Post post;
    
    public Comment(Post post, String author, String content) {
        this.post = post;
        this.author = author;
        this.content = content;
	}

}
